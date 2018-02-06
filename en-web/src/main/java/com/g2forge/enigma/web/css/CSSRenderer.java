package com.g2forge.enigma.web.css;

import java.lang.reflect.Type;

import com.g2forge.alexandria.generic.type.java.structure.JavaScope;
import com.g2forge.alexandria.java.core.helpers.HCollector;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.CamelCase;
import com.g2forge.alexandria.java.text.SnakeCase;
import com.g2forge.alexandria.java.typeswitch.TypeSwitch1;
import com.g2forge.alexandria.reflection.object.HReflection;
import com.g2forge.alexandria.reflection.object.IJavaFieldReflection;
import com.g2forge.enigma.web.css.color.Color;
import com.g2forge.enigma.web.css.distance.Distance;

import lombok.Data;

public class CSSRenderer {
	@Data
	protected static class CSSRenderContext implements ICSSRenderContext {
		protected static CSSRenderer css = new CSSRenderer();

		protected static final IFunction1<Object, IExplicitCSSRenderable> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitCSSRenderable>().with(builder -> {
			builder.add(Block.class, e -> c -> {
				boolean first = true;
				for (ICSSStyle style : e.getStyles()) {
					if (!first) c.getBuilder().append("; ");
					c.toExplicit(style, ICSSStyle.class).render(c);
					first = false;
				}
			});
			builder.add(IExplicitCSSRenderable.class, IFunction1.identity());
			builder.add(ICSSRecord.class, RecordExplicitCSSRenderable::new);
			builder.add(Enum.class, e -> {
				if (e instanceof ICSSStyle) return new EnumExplicitCSSStyle(e);
				else return new EnumExplicitCSSRenderable(e);
			});

			builder.add(Color.class, e -> c -> c.getBuilder().append("rgb(").append(e.getR()).append(',').append(e.getG()).append(',').append(e.getB()).append(')'));
			builder.add(Distance.class, e -> c -> c.getBuilder().append(e.getAmount()).append(e.getUnit().name().toLowerCase()));

			builder.add(String.class, e -> context -> context.getBuilder().append(e));
			builder.add(Boolean.class, e -> context -> context.getBuilder().append(e));
			builder.add(Integer.class, e -> context -> context.getBuilder().append(e));
			builder.add(Long.class, e -> context -> context.getBuilder().append(e));
			builder.add(Float.class, e -> context -> context.getBuilder().append(e));
			builder.add(Double.class, e -> context -> context.getBuilder().append(e));
		}).build();

		protected final StringBuilder builder;

		protected final String newline;

		@Override
		public IExplicitCSSRenderable toExplicit(final Object element, Type type) {
			return toExplicit.apply(element);
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
			context.getBuilder().append(property).append(": ").append(value);
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
			context.getBuilder().append(value);
		}
	}

	@Data
	protected static class RecordExplicitCSSRenderable implements IExplicitCSSRenderable {
		protected final ICSSRecord record;

		@Override
		public void render(ICSSRenderContext context) {
			final StringBuilder builder = context.getBuilder();
			final String property = SnakeCase.DASH.toString(CamelCase.create().fromString(record.getClass().getSimpleName()));
			builder.append(property).append(": ");

			final IJavaFieldReflection<ICSSRecord, ?> field = HReflection.toReflection(record).getFields(JavaScope.Inherited, null).collect(HCollector.toOne());
			final Object value = field.getAccessor(record).get0();
			context.toExplicit(value, field.getType().getFieldType().getJavaType()).render(context);
		}
	}

	public String render(ICSSRenderable element) {
		final StringBuilder retVal = new StringBuilder();
		final CSSRenderContext context = new CSSRenderContext(retVal, "\n");
		context.toExplicit(element, null).render(context);
		return retVal.toString();
	}

}
