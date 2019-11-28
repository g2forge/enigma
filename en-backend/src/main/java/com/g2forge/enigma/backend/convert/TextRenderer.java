package com.g2forge.enigma.backend.convert;

import java.lang.reflect.Type;
import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.convert.common.IRenderer;
import com.g2forge.enigma.backend.model.ITextRenderable;
import com.g2forge.enigma.backend.model.expression.ITextExpression;
import com.g2forge.enigma.backend.model.expression.TextCharSequence;
import com.g2forge.enigma.backend.model.expression.TextConcatenation;
import com.g2forge.enigma.backend.model.expression.TextNewline;
import com.g2forge.enigma.backend.model.expression.TextObject;
import com.g2forge.enigma.backend.model.expression.TextRepeat;
import com.g2forge.enigma.backend.model.modifier.ITextModifier;
import com.g2forge.enigma.backend.model.modifier.TextModified;
import com.g2forge.enigma.backend.model.modifier.TextUpdate;

import lombok.Getter;

public class TextRenderer implements IRenderer<ITextRenderable> {
	protected static class TextRenderContext implements ITextRenderContext, IBuilder<String> {
		protected static final IFunction1<Object, IExplicitTextRenderable> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitTextRenderable>().with(builder -> {
			builder.add(TextCharSequence.class, e -> c -> c.getBuilder().append(e.getValue()));
			builder.add(TextObject.class, e -> c -> c.getBuilder().append(e.getValue()));
			builder.add(TextNewline.class, e -> c -> c.getBuilder().append("\n"));
			builder.add(TextConcatenation.class, e -> c -> e.getElements().forEach(x -> c.render(x, ITextExpression.class)));
			builder.add(TextRepeat.class, e -> c -> {
				for (int i = 0; i < e.getRepeat(); i++) {
					c.render(e.getExpression(), ITextExpression.class);
				}
			});
			builder.add(TextModified.class, e -> c -> {
				final StringBuilder b = c.getBuilder();
				final int priorOffset = b.length();
				c.render(e.getExpression(), ITextExpression.class);

				final ITextModifier modifier = e.getModifier();
				final String string = b.substring(priorOffset, b.length());
				final List<List<TextUpdate>> updates = modifier.computeUpdates(HCollection.asList(string));
				if (updates != null) {
					final List<TextUpdate> stringUpdates = HCollection.getOne(updates);
					if (!stringUpdates.isEmpty()) {
						b.delete(priorOffset, b.length());
						int stringOffset = 0;
						for (TextUpdate update : stringUpdates) {
							final int updateOffset = update.getOffset();
							if (updateOffset > stringOffset) b.append(string.substring(stringOffset, updateOffset));
							c.render(update.getFunction().apply(string.substring(updateOffset, updateOffset + update.getLength())), ITextExpression.class);
							stringOffset = updateOffset + update.getLength();
						}
						if (stringOffset < string.length()) b.append(string.substring(stringOffset, string.length()));
					}
				}
			});
		}).build();

		@Getter
		protected final StringBuilder builder = new StringBuilder();

		@Override
		public ITextRenderContext render(Object object, Type type) {
			toExplicit.apply(object).render(this);
			return this;
		}

		@Override
		public String build() {
			return getBuilder().toString();
		}
	}

	@Override
	public String render(ITextRenderable renderable) {
		final TextRenderContext context = new TextRenderContext();
		context.render(renderable, null);
		return context.build();
	}
}
