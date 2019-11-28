package com.g2forge.enigma.backend.bash.convert;

import java.lang.reflect.Type;
import java.util.Stack;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.bash.convert.textmodifiers.BashTokenModifier;
import com.g2forge.enigma.backend.bash.model.BashBlock;
import com.g2forge.enigma.backend.bash.model.BashCommand;
import com.g2forge.enigma.backend.bash.model.BashScript;
import com.g2forge.enigma.backend.bash.model.IBashBlock;
import com.g2forge.enigma.backend.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.backend.bash.model.expression.BashString;
import com.g2forge.enigma.backend.convert.common.ARenderer;
import com.g2forge.enigma.backend.model.expression.ITextExpression;
import com.g2forge.enigma.backend.model.expression.TextCharSequence;
import com.g2forge.enigma.backend.model.expression.TextConcatenation;
import com.g2forge.enigma.backend.model.expression.TextNewline;
import com.g2forge.enigma.backend.model.expression.TextObject;
import com.g2forge.enigma.backend.model.modifier.ITextModifier;
import com.g2forge.enigma.backend.model.modifier.IndentTextModifier;
import com.g2forge.enigma.backend.model.modifier.TextModified;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Note(type = NoteType.TODO, value = "Add a method which renders a list of strings for a one-liner")
public class BashRenderer extends ARenderer<IBashRenderable, BashRenderer.BashRenderContext> {
	public static class BashRenderContext implements IBashRenderContext, IBuilder<ITextExpression> {
		@Getter
		@RequiredArgsConstructor
		protected static class Frame {
			protected final ITextModifier modifier;

			protected final TextConcatenation.TextConcatenationBuilder builder = TextConcatenation.builder();
		}

		protected static final IFunction1<Object, IExplicitBashRenderable> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitBashRenderable>().with(builder -> {
			ITextAppender.addToBuilder(builder, new ITextAppender.IExplicitFactory<IBashRenderContext, IExplicitBashRenderable>() {
				@Override
				public <T> IFunction1<? super T, ? extends IExplicitBashRenderable> create(IConsumer2<? super IBashRenderContext, ? super T> consumer) {
					return e -> c -> consumer.accept(c, e);
				}
			});

			builder.add(BashScript.class, e -> c -> c.append("#!/bin/bash").newline().render(e.getBody(), IBashBlock.class));
			builder.add(BashBlock.class, e -> c -> e.getContents().forEach(x -> c.render(x, IBashBlock.class)));
			builder.add(BashCommand.class, e -> c -> {
				boolean first = true;
				for (Object object : e.getTokens()) {
					if (first) first = false;
					else c.append(" ");
					try (final ICloseable token = c.token()) {
						c.render(object, null);
					}
				}
			});
			builder.add(BashCommandSubstitution.class, e -> c -> c.append("$(").render(e.getCommand(), BashCommand.class).append(")"));
			builder.add(BashString.class, e -> c -> {
				try (final ICloseable token = c.token()) {
					e.getElements().forEach(x -> c.render(x, null));
				}
			});
		}).build();

		@Getter(AccessLevel.PROTECTED)
		protected final Stack<Frame> stack = new Stack<>();

		public BashRenderContext() {
			getStack().push(new Frame(null));
		}

		@Override
		public IBashRenderContext append(boolean bool) {
			throw new NotYetImplementedError();
		}

		@Override
		public IBashRenderContext append(byte number) {
			throw new NotYetImplementedError();
		}

		@Override
		public IBashRenderContext append(char character) {
			throw new NotYetImplementedError();
		}

		@Override
		public IBashRenderContext append(CharSequence characters) {
			getBuilder().element(new TextCharSequence(characters));
			return this;
		}

		@Override
		public IBashRenderContext append(double number) {
			throw new NotYetImplementedError();
		}

		@Override
		public IBashRenderContext append(float number) {
			throw new NotYetImplementedError();
		}

		@Override
		public IBashRenderContext append(int number) {
			throw new NotYetImplementedError();
		}

		@Override
		public IBashRenderContext append(long number) {
			throw new NotYetImplementedError();
		}

		@Override
		public IBashRenderContext append(Object object) {
			getBuilder().element(new TextObject(object));
			return this;
		}

		@Override
		public IBashRenderContext append(short number) {
			throw new NotYetImplementedError();
		}

		@Override
		public ITextExpression build() {
			while (getStack().size() > 1) {
				pop();
			}
			return getBuilder().build();
		}

		protected TextConcatenation.TextConcatenationBuilder getBuilder() {
			return getStack().peek().getBuilder();
		}

		@Override
		public ICloseable indent() {
			return open(new Frame(new IndentTextModifier(new TextCharSequence("\t"))));
		}

		@Override
		public IBashRenderContext newline() {
			getBuilder().element(TextNewline.create());
			return this;
		}

		protected ICloseable open(final Frame frame) {
			getStack().push(frame);
			return () -> {
				if (getStack().peek() != frame) throw new IllegalStateException();
				pop();
			};
		}

		protected void pop() {
			final Stack<Frame> stack = getStack();
			final Frame frame = stack.pop();
			final ITextExpression expression = frame.getBuilder().build();
			final ITextExpression modified = (frame.getModifier() == null) ? expression : new TextModified(expression, frame.getModifier());
			stack.peek().getBuilder().element(modified);
		}

		@Override
		public IBashRenderContext render(Object object, Type type) {
			toExplicit.apply(object).render(this);
			return this;
		}

		@Override
		public ICloseable token() {
			return open(new Frame(BashTokenModifier.create()));
		}
	}

	@Override
	protected BashRenderContext createContext() {
		return new BashRenderContext();
	}
}
