package com.g2forge.enigma.bash.convert;

import java.lang.reflect.Type;

import com.g2forge.alexandria.annotations.note.Note;
import com.g2forge.alexandria.annotations.note.NoteType;
import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.nestedstate.StackGlobalState;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.common.ARenderer;
import com.g2forge.enigma.backend.model.expression.ITextExpression;
import com.g2forge.enigma.backend.model.expression.TextNewline;
import com.g2forge.enigma.backend.model.modifier.IndentTextModifier;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified;
import com.g2forge.enigma.backend.model.modifier.TextNestedModified.IModifierHandle;
import com.g2forge.enigma.bash.convert.textmodifiers.BashTokenModifier;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.bash.model.expression.BashExpansion;
import com.g2forge.enigma.bash.model.expression.BashString;
import com.g2forge.enigma.bash.model.statement.BashAssignment;
import com.g2forge.enigma.bash.model.statement.BashBlank;
import com.g2forge.enigma.bash.model.statement.BashBlock;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashIf;
import com.g2forge.enigma.bash.model.statement.BashOperation;
import com.g2forge.enigma.bash.model.statement.IBashBlock;
import com.g2forge.enigma.bash.model.statement.IBashExecutable;

import lombok.AccessLevel;
import lombok.Getter;

@Note(type = NoteType.TODO, value = "Add a method which renders a list of strings for a one-liner")
public class BashRenderer extends ARenderer<IBashRenderable, BashRenderer.BashRenderContext> {
	public static class BashRenderContext implements IBashRenderContext, IBuilder<ITextExpression> {
		protected enum Mode {
			Block,
			Line,
			Token;
		}

		protected static final IFunction1<Object, IExplicitBashRenderable> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitBashRenderable>().with(builder -> {
			ITextAppender.addToBuilder(builder, new ITextAppender.IExplicitFactory<IBashRenderContext, IExplicitBashRenderable>() {
				@Override
				public <T> IFunction1<? super T, ? extends IExplicitBashRenderable> create(IConsumer2<? super IBashRenderContext, ? super T> consumer) {
					return e -> c -> {
						try (final ICloseable token = c.token()) {
							consumer.accept(c, e);
						}
					};
				}
			});

			builder.add(BashCommand.class, e -> c -> {
				boolean first = true;
				for (Object object : e.getTokens()) {
					if (first) first = false;
					else c.append(" ");
					c.render(object, null);
				}
				if (c.isBlockMode()) c.newline();
			});
			builder.add(BashOperation.class, e -> c -> {
				final String operator;
				switch (e.getOperator()) {
					case And:
						operator = " && ";
						break;
					case Or:
						operator = " || ";
						break;
					default:
						throw new EnumException(BashOperation.Operator.class, e.getOperator());
				}
				boolean first = true;
				for (IBashExecutable operand : e.getOperands()) {
					if (first) first = false;
					else c.append(operator);
					c.render(operand, IBashExecutable.class);
				}
			});

			builder.add(BashScript.class, e -> c -> c.append("#!/bin/bash").newline().render(e.getBody(), IBashBlock.class));
			builder.add(BashBlock.class, e -> c -> e.getContents().forEach(x -> c.render(x, IBashBlock.class)));
			builder.add(BashAssignment.class, e -> c -> c.append(e.getName()).append("=").render(e.getExpression(), Object.class).newline());
			builder.add(BashBlank.class, e -> c -> c.newline());
			builder.add(BashIf.class, e -> c -> {
				c.append("if ").render(e.getCondition(), null).append("; then").newline();
				try (final ICloseable indent = c.indent()) {
					c.render(e.getThenStatement(), IBashBlock.class);
				}
				if (e.getElseStatement() != null) {
					c.append("else").newline();
					try (final ICloseable indent = c.indent()) {
						c.render(e.getElseStatement(), IBashBlock.class);
					}
				}
				c.append("fi").newline();
			});

			builder.add(BashCommandSubstitution.class, e -> c -> {
				try (final ICloseable token = c.token()) {
					c.append("$(");
					try (final ICloseable line = c.line()) {
						c.render(e.getExecutable(), IBashExecutable.class);
					}
					c.append(")");
				}
			});
			builder.add(BashString.class, e -> c -> {
				try (final ICloseable raw = c.raw()) {
					try (final ICloseable token = c.token()) {
						e.getElements().forEach(x -> c.render(x, null));
					}
				}
			});
			builder.add(BashExpansion.class, e -> c -> {
				try (final ICloseable token = c.token()) {
					c.append("${");
					try (final ICloseable expansion = c.raw()) {
						c.append(e.getName());
					}
					c.append("}");
				}
			});
		}).build();

		@Getter(AccessLevel.PROTECTED)
		protected final TextNestedModified.TextNestedModifiedBuilder builder = TextNestedModified.builder();

		@Getter(AccessLevel.PROTECTED)
		protected final StackGlobalState<Mode> state = new StackGlobalState<Mode>(Mode.Block);

		@Override
		public IBashRenderContext append(boolean bool) {
			getBuilder().expression(bool);
			return this;
		}

		@Override
		public IBashRenderContext append(byte number) {
			getBuilder().expression(number);
			return this;
		}

		@Override
		public IBashRenderContext append(char character) {
			getBuilder().expression(character);
			return this;
		}

		@Override
		public IBashRenderContext append(CharSequence characters) {
			getBuilder().expression(characters);
			return this;
		}

		@Override
		public IBashRenderContext append(double number) {
			getBuilder().expression(number);
			return this;
		}

		@Override
		public IBashRenderContext append(float number) {
			getBuilder().expression(number);
			return this;
		}

		@Override
		public IBashRenderContext append(int number) {
			getBuilder().expression(number);
			return this;
		}

		@Override
		public IBashRenderContext append(long number) {
			getBuilder().expression(number);
			return this;
		}

		@Override
		public IBashRenderContext append(Object object) {
			getBuilder().expression(object);
			return this;
		}

		@Override
		public IBashRenderContext append(short number) {
			getBuilder().expression(number);
			return this;
		}

		@Override
		public ITextExpression build() {
			return getBuilder().build();
		}

		@Override
		public ICloseable indent() {
			switch (getState().get()) {
				case Token:
					throw new IllegalStateException();
				case Line:
					return () -> {};
				case Block:
					return getBuilder().open(new IndentTextModifier(true, "\t"));
				default:
					throw new EnumException(Mode.class, getState().get());
			}
		}

		@Override
		public boolean isBlockMode() {
			switch (getState().get()) {
				case Token:
				case Line:
					return false;
				case Block:
					return true;
				default:
					throw new EnumException(Mode.class, getState().get());
			}
		}

		@Override
		public ICloseable line() {
			return getState().open(Mode.Line);
		}

		@Override
		public IBashRenderContext newline() {
			switch (getState().get()) {
				case Token:
					throw new IllegalStateException();
				case Line:
					getBuilder().expression(";");
				case Block:
					getBuilder().expression(TextNewline.create());
					break;
				default:
					throw new EnumException(Mode.class, getState().get());
			}
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
		public ICloseable token() {
			if (getState().get().equals(Mode.Token)) { return () -> {}; }
			final ICloseable state = getState().open(Mode.Token);
			final IModifierHandle modifier = getBuilder().open(BashTokenModifier.create());
			return () -> {
				modifier.close();
				state.close();
			};
		}
	}

	@Override
	protected BashRenderContext createContext() {
		return new BashRenderContext();
	}
}
