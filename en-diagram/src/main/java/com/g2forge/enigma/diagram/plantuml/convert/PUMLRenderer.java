package com.g2forge.enigma.diagram.plantuml.convert;

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
import com.g2forge.enigma.diagram.plantuml.convert.PUMLRelationship.PUMLRelationshipBuilder;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLComponent;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLComponentDiagram;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLLink;
import com.g2forge.enigma.diagram.plantuml.model.klass.IPUMLStereotype;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClass;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLClassDiagram;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLNamedStereotype;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLRelation;
import com.g2forge.enigma.diagram.plantuml.model.klass.PUMLSpotStereotype;
import com.g2forge.enigma.diagram.plantuml.model.sequence.IPUMLEvent;
import com.g2forge.enigma.diagram.plantuml.model.sequence.PUMLMessage;
import com.g2forge.enigma.diagram.plantuml.model.sequence.PUMLParticipant;
import com.g2forge.enigma.diagram.plantuml.model.sequence.PUMLSequenceDiagram;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

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
		protected static String mangle(final String name) {
			return name.replaceAll("[^a-zA-Z0-9_]", "_");
		}

		protected static boolean isQuoteRequired(final String name) {
			return !name.matches("[a-zA-Z_][0-9a-zA-Z_]*");
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
			builder.add(PUMLNamedStereotype.class, e -> c -> c.append(e.getName()));
			builder.add(PUMLSpotStereotype.class, e -> c -> c.append('(').append(e.getLetter()).append(',').append(e.getColor()).append(')'));
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
}
