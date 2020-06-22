package com.g2forge.enigma.web.css.convert;

import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.reflect.JavaScope;
import com.g2forge.alexandria.java.text.casing.CamelCase;
import com.g2forge.alexandria.java.text.casing.SnakeCase;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.alexandria.reflection.object.HReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.convert.textual.ATextualRenderer;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;
import com.g2forge.enigma.web.css.model.Block;
import com.g2forge.enigma.web.css.model.ICSSRecord;
import com.g2forge.enigma.web.css.model.ICSSStyle;
import com.g2forge.enigma.web.css.model.color.Color;
import com.g2forge.enigma.web.css.model.distance.Distance;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CSSRenderer extends ATextualRenderer<Object, ICSSRenderContext> {
	protected class CSSRenderContext extends ARenderContext implements ICSSRenderContext {
		protected CSSRenderContext(TextNestedModified.TextNestedModifiedBuilder builder) {
			super(builder);
		}

		@Override
		protected ICSSRenderContext getThis() {
			return this;
		}
	}

	protected static class CSSRendering extends ARenderer.ARendering<Object, ICSSRenderContext, IExplicitRenderable<? super ICSSRenderContext>> {
		@Override
		protected void extend(TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super ICSSRenderContext>> builder) {
			builder.add(IExplicitCSSRenderable.class, e -> c -> e.render(c));
			ITextAppender.addToBuilder(builder, new ITextAppender.IExplicitFactory<ICSSRenderContext, IExplicitRenderable<? super ICSSRenderContext>>() {
				@Override
				public <T> IFunction1<? super T, ? extends IExplicitRenderable<? super ICSSRenderContext>> create(IConsumer2<? super ICSSRenderContext, ? super T> consumer) {
					return e -> c -> consumer.accept(c, e);
				}
			});

			builder.add(Block.class, e -> c -> {
				boolean first = true;
				for (ICSSStyle style : e.getStyles()) {
					if (!first) c.append("; ");
					c.render(style, ICSSStyle.class);
					first = false;
				}
			});
			builder.add(IExplicitCSSRenderable.class, IFunction1.identity());
			builder.add(ICSSRecord.class, RecordExplicitCSSRenderable::new);
			builder.add(Enum.class, e -> {
				if (e instanceof ICSSStyle) return new EnumExplicitCSSStyle(e);
				else return new EnumExplicitCSSRenderable(e);
			});

			builder.add(Color.class, e -> c -> c.append("rgb(").append(e.getR()).append(',').append(e.getG()).append(',').append(e.getB()).append(')'));
			builder.add(Distance.class, e -> c -> c.append(e.getAmount()).append(e.getUnit().name().toLowerCase()));
		}
	}

	protected static class EnumExplicitCSSRenderable implements IExplicitCSSRenderable {
		protected final Enum<? extends ICSSRenderable> element;

		@SuppressWarnings("unchecked")
		public EnumExplicitCSSRenderable(Enum<?> element) {
			if (!(element instanceof ICSSRenderable)) throw new IllegalArgumentException(String.format("Enumeration element \"%1$s\" of \"%2$s\" is not a CSS renderable!", element, element.getClass()));
			this.element = (Enum<? extends ICSSRenderable>) element;
		}

		@Override
		public void render(ICSSRenderContext context) {
			final String value = SnakeCase.DASH.toString(CamelCase.create().fromString(element.name()));
			context.append(value);
		}
	}

	protected static class EnumExplicitCSSStyle implements IExplicitCSSRenderable {
		protected final Enum<? extends ICSSStyle> element;

		@SuppressWarnings("unchecked")
		public EnumExplicitCSSStyle(Enum<?> element) {
			if (!(element instanceof ICSSStyle)) throw new IllegalArgumentException(String.format("Enumeration element \"%1$s\" of \"%2$s\" is not a CSS renderable!", element, element.getClass()));
			this.element = (Enum<? extends ICSSStyle>) element;
		}

		@Override
		public void render(ICSSRenderContext context) {
			final String property = SnakeCase.DASH.toString(CamelCase.create().fromString(element.getClass().getSimpleName()));
			final String value = SnakeCase.DASH.toString(CamelCase.create().fromString(element.name()));
			context.append(property).append(": ").append(value);
		}
	}

	@Data
	protected static class RecordExplicitCSSRenderable implements IExplicitCSSRenderable {
		protected final ICSSRecord record;

		@Override
		public void render(ICSSRenderContext context) {
			final String property = SnakeCase.DASH.toString(CamelCase.create().fromString(record.getClass().getSimpleName()));
			context.append(property).append(": ");

			final IJavaFieldReflection<ICSSRecord, ?> field = HReflection.toReflection(record).getFields(JavaScope.Inherited, null).collect(HCollector.toOne());
			final Object value = field.getAccessor(record).get0();
			context.render(value, field.getType().getFieldType().getJavaType());
		}
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final IRendering<Object, ICSSRenderContext, IExplicitRenderable<? super ICSSRenderContext>> renderingStatic = new CSSRendering();

	@Override
	protected ICSSRenderContext createContext(TextNestedModified.TextNestedModifiedBuilder builder) {
		return new CSSRenderContext(builder);
	}

	@Override
	protected IRendering<? super Object, ? extends ICSSRenderContext, ? extends IExplicitRenderable<? super ICSSRenderContext>> getRendering() {
		return getRenderingStatic();
	}
}
