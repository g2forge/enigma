package com.g2forge.enigma.diagram.plantuml.convert;

import java.util.Collection;
import java.util.stream.Collectors;

import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.convert.textual.ATextualRenderer;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLComponent;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLComponentDiagram;
import com.g2forge.enigma.diagram.plantuml.model.component.PUMLLink;
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
