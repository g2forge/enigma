package com.g2forge.enigma.diagram.plantuml.convert;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.convert.textual.ATextualRenderer;
import com.g2forge.enigma.diagram.plantuml.model.IPUMLDiagram;
import com.g2forge.enigma.diagram.plantuml.model.PUMLContent;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLComponent;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLComponentDiagram;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLLink;
import com.g2forge.enigma.diagram.plantuml.model.klass.IPUMLStereotype;
import com.g2forge.enigma.diagram.plantuml.model.klass.NamedPUMLStereotype;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClass;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClassDiagram;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLRelation;
import com.g2forge.enigma.diagram.plantuml.model.klass.SpotPUMLStereotype;
import com.g2forge.enigma.diagram.plantuml.model.sequence.IPUMLEvent;
import com.g2forge.enigma.diagram.plantuml.model.sequence.PUMLMessage;
import com.g2forge.enigma.diagram.plantuml.model.sequence.PUMLParticipant;
import com.g2forge.enigma.diagram.plantuml.model.sequence.PUMLSequenceDiagram;
import com.g2forge.enigma.diagram.plantuml.model.style.IPUMLColor;
import com.g2forge.enigma.diagram.plantuml.model.style.PUMLControl;
import com.g2forge.enigma.diagram.plantuml.model.style.StringPUMLColor;
import com.g2forge.enigma.diagram.plantuml.model.style.TransparentPUMLColor;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.sourceforge.plantuml.FileFormat;
import net.sourceforge.plantuml.FileFormatOption;
import net.sourceforge.plantuml.SourceStringReader;

@Getter
@RequiredArgsConstructor
public class PUMLRenderer extends ATextualRenderer<Object, IPUMLRenderContext> {
	protected class PUMLRenderContext extends ARenderContext implements IPUMLRenderContext {
		@Override
		protected IPUMLRenderContext getThis() {
			return this;
		}
	}

	protected static class PUMLRendering extends ARenderer.ARendering<Object, IPUMLRenderContext, IExplicitRenderable<? super IPUMLRenderContext>> {
		protected static boolean isQuoteRequired(final String name) {
			return !name.matches("[a-zA-Z_][0-9a-zA-Z_]*");
		}

		protected static String mangle(final String name) {
			return name.replaceAll("[^a-zA-Z0-9_]", "_");
		}

		@Override
		protected void extend(TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super IPUMLRenderContext>> builder) {
			builder.add(IExplicitPUMLRenderable.class, e -> c -> e.render(c));
			ITextAppender.addToBuilder(builder, new ITextAppender.IExplicitFactory<IPUMLRenderContext, IExplicitRenderable<? super IPUMLRenderContext>>() {
				@Override
				public <T> IFunction1<? super T, ? extends IExplicitRenderable<? super IPUMLRenderContext>> create(IConsumer2<? super IPUMLRenderContext, ? super T> consumer) {
					return e -> c -> consumer.accept(c, e);
				}
			});

			builder.add(StringPUMLColor.class, e -> c -> c.append(e.getColor()));
			builder.add(TransparentPUMLColor.class, e -> c -> c.append("transparent"));
			builder.add(PUMLDiagram.class, e -> c -> {
				final Collection<?> entities = e.getEntities();
				final Collection<?> relationships = e.getRelationships();
				final boolean hasEntities = entities != null && !entities.isEmpty();
				final boolean hasRelationships = relationships != null && !relationships.isEmpty();

				if (hasEntities) for (Object entity : entities) {
					c.render(entity, e.getEntityType());
					c.newline();
				}
				if (hasEntities && hasRelationships) c.newline();
				if (hasRelationships) for (Object relationship : relationships) {
					c.render(relationship, e.getRelationshipType());
					c.newline();
				}
			});
			builder.add(PUMLRelationship.class, e -> c -> {
				c.append(mangle(e.getLeft())).append(' ');
				if (e.getLeftLabel() != null) c.append(" \"").append(e.getLeftLabel()).append('"');

				if (e.isBack()) {
					c.append(e.getArrow().getBack());
					if (e.isTwo()) c.append(e.getLine().getCharacter());
				} else c.append(e.getLine().getCharacter());

				if ((e.getModifiers() != null) && !e.getModifiers().isEmpty()) {
					boolean first = true;
					c.append('[');
					for (String modifier : e.getModifiers()) {
						if (first) first = false;
						else c.append(',');
						c.append(modifier);
					}
					c.append(']');
				}

				if (!e.isBack()) {
					if (e.isTwo()) c.append(e.getLine().getCharacter());
					c.append(e.getArrow().getForward());
				} else c.append(e.getLine().getCharacter());

				if (e.getRightLabel() != null) c.append(" \"").append(e.getRightLabel()).append('"');
				c.append(' ').append(mangle(e.getRight()));

				if (e.getLabel() != null) {
					final String trimmed = e.getLabel().trim();
					if (!trimmed.isEmpty()) c.append(" : ").append(trimmed);
				}
			});
			builder.add(PUMLControl.class, e -> c -> {
				if (e.getDpi() != null) c.append("skinparam dpi ").append(e.getDpi()).newline();
				c.append("skinparam shadowing ").append(e.isShadowing()).newline();
				if (e.getBackground() != null) c.append("skinparam backgroundcolor ").render(e.getBackground(), IPUMLColor.class).newline();
				if (e.getPx() != null) c.append("skinparam scale ").append(e.getPx()).append('x').append(e.getPx()).newline();
			});
			builder.add(PUMLContent.class, e -> c -> {
				c.append("@startuml").newline().newline();
				if (e.getControl() != null) c.render(e.getControl(), PUMLControl.class).newline();
				c.render(e.getDiagram(), IPUMLDiagram.class).newline().newline();
				c.append("@enduml");
			});

			builder.add(PUMLComponentDiagram.class, e -> c -> c.render(new PUMLDiagram<>(e.getComponents(), e.getLinks(), PUMLComponent.class, PUMLLink.class), PUMLDiagram.class));
			builder.add(PUMLComponent.class, e -> c -> {
				final String name = e.getName();
				c.append('[').append(name).append(']');
				final String mangled = mangle(name);
				if (!name.equals(mangled)) c.append(" as ").append(mangled);
			});
			builder.add(PUMLLink.class, e -> c -> {
				final PUMLRelationship.PUMLRelationshipBuilder b = PUMLRelationship.builder(e.getLeft(), e.getRight()).two(e.isVertical());
				if ((e.getModifiers() != null) && !e.getModifiers().isEmpty()) b.modifiers(e.getModifiers().stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toList()));
				c.render(b.build(), PUMLRelationship.class);
			});

			builder.add(PUMLSequenceDiagram.class, e -> c -> c.render(new PUMLDiagram<>(e.getParticipants(), e.getEvents(), PUMLParticipant.class, IPUMLEvent.class), PUMLDiagram.class));
			builder.add(PUMLParticipant.class, e -> c -> {
				final String name = e.getName();
				c.append("participant ").append(name);
				final String mangled = mangle(name);
				if (!name.equals(mangled)) c.append(" as ").append(mangled);
			});
			builder.add(PUMLMessage.class, e -> c -> c.render(PUMLRelationship.builder(e.getSource(), e.getDestination()).label(e.getLabel()).build(), PUMLRelationship.class));

			builder.add(PUMLClassDiagram.class, e -> c -> c.render(new PUMLDiagram<>(e.getUclasses(), e.getRelations(), PUMLClass.class, PUMLRelation.class), PUMLDiagram.class));
			builder.add(PUMLClass.class, e -> c -> {
				switch (e.getMetaType()) {
					case Class:
					case Enum:
						c.append(e.getMetaType().name().toLowerCase()).append(' ');
						break;
					default:
						throw new EnumException(PUMLClass.MetaType.class, e.getMetaType());
				}

				final String name = e.getName();
				final boolean quote = isQuoteRequired(name);
				if (quote) c.append('\"');
				c.append(name);
				if (quote) c.append('\"');

				final List<IPUMLStereotype> stereotypes = e.getStereotypes();
				if ((stereotypes != null) && !stereotypes.isEmpty()) {
					c.append(" <<");
					for (IPUMLStereotype stereotype : stereotypes) {
						c.append(' ').render(stereotype, IPUMLStereotype.class);
					}
					c.append(" >>");
				}

				final List<String> members = e.getMembers();
				if ((members != null) && !members.isEmpty()) {
					c.append(" {").newline();
					try (final ICloseable indent = c.indent()) {
						for (String member : members) {
							c.append(member).newline();
						}
					}
					c.append('}');
				}
			});
			builder.add(NamedPUMLStereotype.class, e -> c -> c.append(e.getName()));
			builder.add(SpotPUMLStereotype.class, e -> c -> c.append('(').append(e.getLetter()).append(',').render(e.getColor(), IPUMLColor.class).append(')'));
			builder.add(PUMLRelation.class, e -> c -> {
				final PUMLRelationship.PUMLRelationshipBuilder b = PUMLRelationship.builder(e.getLeft(), e.getRight());
				b.leftLabel(e.getLeftLabel()).rightLabel(e.getRightLabel());
				b.back(e.isBack()).two(e.isVertical());
				switch (e.getType()) {
					case Arrow:
						b.arrow(PUMLRelationship.ArrowStyle.Normal);
						break;
					case Extension:
						b.arrow(PUMLRelationship.ArrowStyle.Extension);
						break;
					case Composition:
						b.arrow(PUMLRelationship.ArrowStyle.Composition);
						break;
					case Aggregation:
						b.arrow(PUMLRelationship.ArrowStyle.Aggregation);
						break;
					default:
						throw new EnumException(PUMLRelation.Type.class, e.getType());
				}

				if (e.getLabel() != null) {
					final String suffix;
					switch (e.getArrow()) {
						case Left:
							suffix = " <";
							break;
						case Right:
							suffix = " >";
							break;
						default:
							throw new EnumException(PUMLRelation.Direction.class, e.getArrow());
					}
					b.label(e.getLabel() + suffix);
				}

				c.render(b.build(), PUMLRelationship.class);
			});
		}
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final IRendering<Object, IPUMLRenderContext, IExplicitRenderable<? super IPUMLRenderContext>> renderingStatic = new PUMLRendering();

	@Override
	protected PUMLRenderContext createContext() {
		return new PUMLRenderContext();
	}

	@Override
	protected IRendering<? super Object, ? extends IPUMLRenderContext, ? extends IExplicitRenderable<? super IPUMLRenderContext>> getRendering() {
		return getRenderingStatic();
	}

	/**
	 * Render some plant UML content.
	 * 
	 * @param content The content to render.
	 * @param path The path to the file to put the content in, which should inclde the filename but not a file extension. For example
	 *            {@code Paths.get("/tmp/diagram")}.
	 * @param format The format to render the content in. For example {@code FileFormat.PNG}.
	 * @return The actual file path. For example {@code Paths.get("/tmp/diagram.png")}.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Path toFile(PUMLContent content, Path path, FileFormat format) throws IOException, FileNotFoundException {
		final String filename = path.getFileName().toString() + format.getFileSuffix();
		final Path retVal = path.getParent() != null ? path.getParent().resolve(filename) : Paths.get(filename);

		final String string = render(content);
		final SourceStringReader reader = new SourceStringReader(string);
		try (final OutputStream os = new BufferedOutputStream(new FileOutputStream(retVal.toFile()))) {
			reader.generateImage(os, new FileFormatOption(format));
		}
		return retVal;
	}

	/**
	 * Wrap a diagram in standard plant UML control, and render it to a PNG. See {@link #toFile(PUMLContent, Path, FileFormat)} to understand the {@code path}
	 * argument and return value.
	 * 
	 * @param diagram The diagram to render.
	 * @param path The path to the PNG file, where the filename should not have the PNG extension.
	 * @return The path to the resulting PNG.
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	public Path toPNG(final IPUMLDiagram diagram, final Path path) throws IOException, FileNotFoundException {
		final PUMLContent.PUMLContentBuilder builder = PUMLContent.builder().diagram(diagram);
		builder.control(PUMLControl.builder().shadowing(false).dpi(600).background(TransparentPUMLColor.create()).build());
		return toFile(builder.build(), path, FileFormat.PNG);
	}
}
