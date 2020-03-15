package com.g2forge.enigma.backend.convert;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.g2forge.alexandria.adt.range.IRange;
import com.g2forge.alexandria.adt.range.IntegerRange;
import com.g2forge.alexandria.java.core.error.UnreachableCodeError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.text.CharSubSequence;
import com.g2forge.alexandria.java.text.TextUpdate;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.convert.common.IRenderer;
import com.g2forge.enigma.backend.model.expression.TextConcatenation;
import com.g2forge.enigma.backend.model.expression.TextNewline;
import com.g2forge.enigma.backend.model.expression.TextRepeat;
import com.g2forge.enigma.backend.model.modifier.ITextModifier;
import com.g2forge.enigma.backend.model.modifier.TextModified;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified.Element;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified.Modifier;

import lombok.Getter;

public class TextRenderer implements IRenderer<Object> {
	protected static class TextRenderContext implements ITextRenderContext, IBuilder<String> {
		protected static final IFunction1<Object, IExplicitTextRenderable> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitTextRenderable>().with(builder -> {
			builder.add(Object.class, e -> c -> c.getBuilder().append(e));
			builder.add(CharSequence.class, e -> c -> c.getBuilder().append(e));
			builder.add(String.class, e -> c -> c.getBuilder().append(e));
			builder.add(Boolean.class, e -> c -> c.getBuilder().append(e.booleanValue()));
			builder.add(Character.class, e -> c -> c.getBuilder().append(e.charValue()));
			builder.add(Byte.class, e -> c -> c.getBuilder().append(e.byteValue()));
			builder.add(Short.class, e -> c -> c.getBuilder().append(e.shortValue()));
			builder.add(Integer.class, e -> c -> c.getBuilder().append(e.intValue()));
			builder.add(Long.class, e -> c -> c.getBuilder().append(e.longValue()));
			builder.add(Float.class, e -> c -> c.getBuilder().append(e.floatValue()));
			builder.add(Double.class, e -> c -> c.getBuilder().append(e.doubleValue()));

			builder.add(TextNewline.class, e -> c -> c.getBuilder().append("\n"));
			builder.add(TextConcatenation.class, e -> c -> e.getElements().forEach(x -> c.render(x, Object.class)));
			builder.add(TextRepeat.class, e -> c -> {
				for (int i = 0; i < e.getRepeat(); i++) {
					c.render(e.getExpression(), Object.class);
				}
			});
			builder.add(TextModified.class, e -> c -> {
				final StringBuilder b = c.getBuilder();
				final int priorOffset = b.length();
				c.render(e.getExpression(), Object.class);

				final ITextModifier modifier = e.getModifier();
				final List<? extends List<? extends TextUpdate<?>>> updates = modifier.computeUpdates(HCollection.asList(new CharSubSequence(b, priorOffset)));
				if (updates != null) {
					final List<? extends TextUpdate<?>> stringUpdates = HCollection.getOne(updates);
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

				final Map<Modifier, Set<Element>> applicableMap = e.getApplicableMap();
				final Map<Element, List<Modifier>> closureMap = e.getClosureMap();
				// Render each expression, storing the starting offset where it went in the builder
				final List<Integer> elementOffsets = new ArrayList<>();
				for (TextNestedModified.Element element : e.getElements()) {
					elementOffsets.add(b.length());
					c.render(element.getExpression(), Object.class);

					final List<Modifier> closures = closureMap.get(element);
					// Whenever we close a modifier, perform the modifications
					if (closures != null) for (Modifier modifier : closures) {
						// Look back over the elements for those to which the modifier applies, and coalesce runs
						final List<IRange<Integer>> ranges = IRange.coalesce(applicableMap.get(modifier).stream().map(x -> {
							int index = -1;
							for (int i = 0; i < e.getElements().size(); i++) {
								if (e.getElements().get(i) == x) {
									index = i;
									break;
								}
							}
							if (index == -1) throw new UnreachableCodeError();
							return new IntegerRange(elementOffsets.get(index), x == element ? b.length() : elementOffsets.get(index + 1));
						}).collect(Collectors.toList()), IntegerRange::new);

						// Compute the updates
						final List<CharSequence> list = ranges.stream().map(r -> new CharSubSequence(b, r.getMin(), r.getMax() - r.getMin())).collect(Collectors.toList());
						final List<? extends List<? extends TextUpdate<?>>> allUpdates = modifier.getModifier().computeUpdates(list);
						if (allUpdates != null) {
							// Apply the updates
							final int nRanges = ranges.size();
							// Make sure we got as many update lists as we sent in ranges
							if (allUpdates.size() != nRanges) throw new RuntimeException();
							int cumulative = 0; // Cumulative offset update from previous ranges
							for (int i = 0; i < nRanges; i++) {
								// Apply updates for each range (if any)
								final List<? extends TextUpdate<?>> rangeUpdates = allUpdates.get(i);
								if (rangeUpdates == null || rangeUpdates.isEmpty()) continue;
								final IRange<Integer> range = ranges.get(i);

								// Perform the update using a child renderer
								final int unchanged = rangeUpdates.get(0).getOffset();
								final String string = b.substring(range.getMin() + cumulative + unchanged, range.getMax() + cumulative);
								final TextRenderContext child = new TextRenderContext();
								final List<Integer> updateLengths = child.modify(string, rangeUpdates, unchanged);
								b.replace(range.getMin() + cumulative + unchanged, range.getMax() + cumulative, child.build());

								// Update the element offsets (resultlength - length = expansion)
								for (int j = 0; j < rangeUpdates.size(); j++) {
									final TextUpdate<?> textUpdate = rangeUpdates.get(j);
									final int offset = textUpdate.getOffset() + range.getMin() + cumulative;
									final int index = Collections.binarySearch(elementOffsets, offset);
									final int updateFrom = index >= 0 ? index + 1 : -(index + 1);
									for (int k = updateFrom; k < elementOffsets.size(); k++) {
										elementOffsets.set(k, elementOffsets.get(k) + updateLengths.get(j));
									}
								}
								cumulative += updateLengths.stream().collect(Collectors.summingInt(Integer::intValue));
							}
						}
					}
				}
			});
		}).build();

		@Getter
		protected final StringBuilder builder = new StringBuilder();

		@Override
		public String build() {
			return getBuilder().toString();
		}

		/**
		 * @param string
		 * @param updates
		 * @param unchanged
		 * @return A list of the lengths of the updates.
		 */
		protected List<Integer> modify(final String string, final List<? extends TextUpdate<?>> updates, int unchanged) {
			final StringBuilder builder = getBuilder();

			final List<Integer> retVal = new ArrayList<>();
			int stringOffset = 0;
			for (TextUpdate<?> update : updates) {
				final int updateOffset = update.getOffset() - unchanged;
				if (updateOffset > stringOffset) builder.append(string.substring(stringOffset, updateOffset));
				final int prelength = builder.length();
				render(update.getFunction().apply(string.substring(updateOffset, updateOffset + update.getLength())), Object.class);
				retVal.add(builder.length() - prelength);
				stringOffset = updateOffset + update.getLength();
			}
			if (stringOffset < string.length()) builder.append(string.substring(stringOffset, string.length()));
			return retVal;
		}

		@Override
		public ITextRenderContext render(Object object, Type type) {
			toExplicit.apply(object).render(this);
			return this;
		}
	}

	@Override
	public String render(Object renderable) {
		final TextRenderContext context = new TextRenderContext();
		context.render(renderable, null);
		return context.build();
	}
}
