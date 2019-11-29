package com.g2forge.enigma.backend.convert;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.text.CharSubSequence;
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
import com.g2forge.enigma.backend.model.modifier.TextNestedModified;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified.Element;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified.Modifier;
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
				final List<List<TextUpdate>> updates = modifier.computeUpdates(HCollection.asList(new CharSubSequence(b, priorOffset)));
				if (updates != null) {
					final List<TextUpdate> stringUpdates = HCollection.getOne(updates);
					if (!stringUpdates.isEmpty()) {
						final int unchanged = stringUpdates.get(0).getOffset();
						final String string = b.substring(priorOffset + unchanged, b.length());
						b.delete(priorOffset + unchanged, b.length());
						((TextRenderContext) c).modify(string, stringUpdates, unchanged);
					}
				}
			});
			builder.add(TextNestedModified.class, e -> c -> {
				final StringBuilder b = c.getBuilder();

				final Map<Element, List<ITextModifier>> closureMap = e.getClosureMap();
				// Render each expression, storing the starting offset where it went in the builder
				final List<Integer> elementOffsets = new ArrayList<>();
				for (TextNestedModified.Element element : e.getElements()) {
					elementOffsets.add(b.length());
					c.render(element.getExpression(), ITextExpression.class);

					final List<ITextModifier> closures = closureMap.get(element);
					// Whenever we close a modifier, perform the modifications
					if (closures != null) for (ITextModifier modifier : closures) {
						// Look back over the elements for those to which the modifier applies, and coalesce runs

						// A modify() can span multiple elements, so update the element offsets (resultlength - length = expansion)

						// If the modify is at the end, we can copy/delete the text
						// If the modify is in the middle, we need a child renderer to properly render the updates
					}
				}
			});
		}).build();

		/**
		 * @param string
		 * @param updates
		 * @param unchanged
		 * @return A list of the lengths of the updates.
		 */
		protected List<Integer> modify(final String string, final List<TextUpdate> updates, int unchanged) {
			final StringBuilder builder = getBuilder();

			final List<Integer> retVal = new ArrayList<>();
			int stringOffset = 0;
			for (TextUpdate update : updates) {
				final int updateOffset = update.getOffset() - unchanged;
				if (updateOffset > stringOffset) builder.append(string.substring(stringOffset, updateOffset));
				final int prelength = builder.length();
				render(update.getFunction().apply(string.substring(updateOffset, updateOffset + update.getLength())), ITextExpression.class);
				retVal.add(builder.length() - prelength);
				stringOffset = updateOffset + update.getLength();
			}
			if (stringOffset < string.length()) builder.append(string.substring(stringOffset, string.length()));
			return retVal;
		}

		@Getter
		protected final StringBuilder builder = new StringBuilder();

		@Override
		public String build() {
			return getBuilder().toString();
		}

		@Override
		public ITextRenderContext render(Object object, Type type) {
			toExplicit.apply(object).render(this);
			return this;
		}
	}

	@Override
	public String render(ITextRenderable renderable) {
		final TextRenderContext context = new TextRenderContext();
		context.render(renderable, null);
		return context.build();
	}
}
