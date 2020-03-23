package com.g2forge.enigma.backend.convert.textual;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.text.convert.TextRenderer;
import com.g2forge.enigma.backend.text.model.expression.ITextExpression;
import com.g2forge.enigma.backend.text.model.expression.TextNewline;
import com.g2forge.enigma.backend.text.model.modifier.IndentTextModifier;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class ATextualRenderer<R, C extends ITextualRenderContext<R, ? super C>> extends ARenderer<R, C> {
	protected abstract class ARenderContext implements ITextualRenderContext<R, C> {
		@Getter(AccessLevel.PROTECTED)
		protected final TextNestedModified.TextNestedModifiedBuilder builder = TextNestedModified.builder();

		@Override
		public C append(boolean bool) {
			getBuilder().expression(bool);
			return getThis();
		}

		@Override
		public C append(byte number) {
			getBuilder().expression(number);
			return getThis();
		}

		@Override
		public C append(char character) {
			getBuilder().expression(character);
			return getThis();
		}

		@Override
		public C append(CharSequence characters) {
			getBuilder().expression(characters);
			return getThis();
		}

		@Override
		public C append(double number) {
			getBuilder().expression(number);
			return getThis();
		}

		@Override
		public C append(float number) {
			getBuilder().expression(number);
			return getThis();
		}

		@Override
		public C append(int number) {
			getBuilder().expression(number);
			return getThis();
		}

		@Override
		public C append(long number) {
			getBuilder().expression(number);
			return getThis();
		}

		@Override
		public C append(Object object) {
			getBuilder().expression(object);
			return getThis();
		}

		@Override
		public C append(short number) {
			getBuilder().expression(number);
			return getThis();
		}

		@Override
		public ITextExpression build() {
			return getBuilder().build();
		}

		protected abstract C getThis();

		@Override
		public ICloseable indent() {
			return getBuilder().open(new IndentTextModifier(true, "\t"));
		}

		@Override
		public C newline() {
			getBuilder().expression(TextNewline.create());
			return getThis();
		}

		@Override
		public C render(R object, Type type) {
			getRendering().toExplicit(object, type).render(getThis());
			return getThis();
		}
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final TextRenderer textRenderer = new TextRenderer();

	@Override
	protected String build(final C context) {
		final ITextExpression text = context.build();
		return getTextRenderer().render(text);
	}
}
