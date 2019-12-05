package com.g2forge.enigma.backend.bash.convert;

import java.lang.reflect.Type;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.close.ICloseable;
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
import com.g2forge.enigma.backend.model.expression.TextBoolean;
import com.g2forge.enigma.backend.model.expression.TextByte;
import com.g2forge.enigma.backend.model.expression.TextCharSequence;
import com.g2forge.enigma.backend.model.expression.TextCharacter;
import com.g2forge.enigma.backend.model.expression.TextDouble;
import com.g2forge.enigma.backend.model.expression.TextFloat;
import com.g2forge.enigma.backend.model.expression.TextInteger;
import com.g2forge.enigma.backend.model.expression.TextLong;
import com.g2forge.enigma.backend.model.expression.TextNewline;
import com.g2forge.enigma.backend.model.expression.TextObject;
import com.g2forge.enigma.backend.model.expression.TextShort;
import com.g2forge.enigma.backend.model.modifier.IndentTextModifier;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified;

import lombok.AccessLevel;
import lombok.Getter;

@Note(type = NoteType.TODO, value = "Add a method which renders a list of strings for a one-liner")
public class BashRenderer extends ARenderer<IBashRenderable, BashRenderer.BashRenderContext> {
	public static class BashRenderContext implements IBashRenderContext, IBuilder<ITextExpression> {
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
				try (final ICloseable raw = c.raw()) {
					try (final ICloseable token = c.token()) {
						e.getElements().forEach(x -> c.render(x, null));
					}
				}
			});
		}).build();

		@Getter(AccessLevel.PROTECTED)
		protected final TextNestedModified.TextNestedModifiedBuilder builder = TextNestedModified.builder();

		@Override
		public IBashRenderContext append(boolean bool) {
			getBuilder().expression(new TextBoolean(bool));
			return this;
		}

		@Override
		public IBashRenderContext append(byte number) {
			getBuilder().expression(new TextByte(number));
			return this;
		}

		@Override
		public IBashRenderContext append(char character) {
			getBuilder().expression(new TextCharacter(character));
			return this;
		}

		@Override
		public IBashRenderContext append(CharSequence characters) {
			getBuilder().expression(new TextCharSequence(characters));
			return this;
		}

		@Override
		public IBashRenderContext append(double number) {
			getBuilder().expression(new TextDouble(number));
			return this;
		}

		@Override
		public IBashRenderContext append(float number) {
			getBuilder().expression(new TextFloat(number));
			return this;
		}

		@Override
		public IBashRenderContext append(int number) {
			getBuilder().expression(new TextInteger(number));
			return this;
		}

		@Override
		public IBashRenderContext append(long number) {
			getBuilder().expression(new TextLong(number));
			return this;
		}

		@Override
		public IBashRenderContext append(Object object) {
			getBuilder().expression(new TextObject(object));
			return this;
		}

		@Override
		public IBashRenderContext append(short number) {
			getBuilder().expression(new TextShort(number));
			return this;
		}

		@Override
		public ITextExpression build() {
			return getBuilder().build();
		}

		@Override
		public TextNestedModified.IModifierHandle indent() {
			return getBuilder().open(new IndentTextModifier(new TextCharSequence("\t")));
		}

		@Override
		public IBashRenderContext newline() {
			getBuilder().expression(TextNewline.create());
			return this;
		}

		@Override
		public TextNestedModified.IModifierHandle raw() {
			return getBuilder().getRoot().reactivate();
		}

		@Override
		public IBashRenderContext render(Object object, Type type) {
			toExplicit.apply(object).render(this);
			return this;
		}

		@Override
		public TextNestedModified.IModifierHandle token() {
			return getBuilder().open(BashTokenModifier.create());
		}
	}

	@Override
	protected BashRenderContext createContext() {
		return new BashRenderContext();
	}
}
