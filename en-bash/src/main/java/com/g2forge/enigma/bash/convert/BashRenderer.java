package com.g2forge.enigma.bash.convert;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.builder.IBuilder;
import com.g2forge.alexandria.java.nestedstate.StackGlobalState;
import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.alexandria.java.text.quote.QuoteControl;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ATextualRenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.text.model.IOperator;
import com.g2forge.enigma.backend.text.model.expression.ITextExpression;
import com.g2forge.enigma.backend.text.model.expression.TextNewline;
import com.g2forge.enigma.backend.text.model.modifier.IndentTextModifier;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified.IModifierHandle;
import com.g2forge.enigma.bash.convert.textmodifiers.BashQuoteModifier;
import com.g2forge.enigma.bash.convert.textmodifiers.BashTokenModifier;
import com.g2forge.enigma.bash.model.BashScript;
import com.g2forge.enigma.bash.model.expression.BashCommandSubstitution;
import com.g2forge.enigma.bash.model.expression.BashExpansion;
import com.g2forge.enigma.bash.model.expression.BashProcessSubstitution;
import com.g2forge.enigma.bash.model.expression.BashString;
import com.g2forge.enigma.bash.model.statement.BashAssignment;
import com.g2forge.enigma.bash.model.statement.BashBlank;
import com.g2forge.enigma.bash.model.statement.BashBlock;
import com.g2forge.enigma.bash.model.statement.BashCommand;
import com.g2forge.enigma.bash.model.statement.BashIf;
import com.g2forge.enigma.bash.model.statement.BashOperation;
import com.g2forge.enigma.bash.model.statement.IBashBlock;
import com.g2forge.enigma.bash.model.statement.IBashExecutable;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectHandle;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectHereDoc;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectHereString;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectIO;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectInput;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirectOutput;
import com.g2forge.enigma.bash.model.statement.redirect.BashRedirection;
import com.g2forge.enigma.bash.model.statement.redirect.HBashHandle;
import com.g2forge.enigma.bash.model.statement.redirect.IBashRedirect;
import com.g2forge.enigma.bash.model.test.BashTest;
import com.g2forge.enigma.bash.model.test.BashTestOperation;
import com.g2forge.enigma.bash.model.test.IBashTestExpression;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class BashRenderer extends ATextualRenderer<Object, BashRenderer.BashRenderContext> {
	protected static class BashRenderContext implements IBashRenderContext, IBuilder<ITextExpression> {
		protected static final IFunction1<Object, ? extends IExplicitRenderable<? super IBashRenderContext>> toExplicit = new TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super IBashRenderContext>>().with(builder -> {
			builder.add(IExplicitBashRenderable.class, e -> c -> e.render(c));
			ITextAppender.addToBuilder(builder, new ITextAppender.IExplicitFactory<IBashRenderContext, IExplicitRenderable<? super IBashRenderContext>>() {
				@Override
				public <T> IFunction1<? super T, ? extends IExplicitRenderable<? super IBashRenderContext>> create(IConsumer2<? super IBashRenderContext, ? super T> consumer) {
					return e -> c -> {
						try (final ICloseable token = c.token(BashQuoteType.BashDoubleExpand, QuoteControl.IfNeeded)) {
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
				try (final ICloseable line = c.line()) {
					IOperator.render(e.getOperator(), c, e.getOperands(), IBashExecutable.class);
				}
				if (c.isBlockMode()) c.newline();
			});
			builder.add(BashRedirection.class, e -> c -> {
				try (final ICloseable line = c.line()) {
					c.render(e.getExecutable(), IBashExecutable.class);
					for (IBashRedirect redirect : e.getRedirects()) {
						c.append(" ").render(redirect, IBashRedirect.class);
					}
				}
				if (c.isBlockMode()) c.newline();
			});

			builder.add(BashRedirectInput.class, e -> c -> {
				if (e.getHandle() != HBashHandle.UNSPECIFIED) {
					if (e.getHandle() < 0) throw new IllegalArgumentException();
					else c.append(e.getHandle());
				}
				c.append("<").render(e.getTarget(), null);
			});
			builder.add(BashRedirectOutput.class, e -> c -> {
				if (e.getHandle() != HBashHandle.UNSPECIFIED) {
					if (e.getHandle() == HBashHandle.BOTH) c.append("&");
					else if (e.getHandle() < 0) throw new IllegalArgumentException();
					else c.append(e.getHandle());
				}
				c.append(e.isAppend() ? ">>" : ">");
				if (!e.isClobber()) c.append("|");
				c.render(e.getTarget(), null);
			});
			builder.add(BashRedirectIO.class, e -> c -> {
				if (e.getHandle() != HBashHandle.UNSPECIFIED) {
					if (e.getHandle() < 0) throw new IllegalArgumentException();
					else c.append(e.getHandle());
				}
				c.append("<>").render(e.getTarget(), null);
			});
			builder.add(BashRedirectHandle.class, e -> c -> {
				try (final ICloseable token = c.token(null, QuoteControl.Never)) {
					c.append("&").append(e.getHandle());
					if (BashRedirectHandle.Operation.Move.equals(e.getOperation())) c.append("-");
				}
			});
			builder.add(BashRedirectHereString.class, e -> c -> {
				if (e.getHandle() != HBashHandle.UNSPECIFIED) {
					if (e.getHandle() < 0) throw new IllegalArgumentException();
					else c.append(e.getHandle());
				}
				c.append("<<< ").render(e.getString(), null);
			});
			builder.add(BashRedirectHereDoc.class, e -> c -> {
				if (e.getHandle() != HBashHandle.UNSPECIFIED) {
					if (e.getHandle() < 0) throw new IllegalArgumentException();
					else c.append(e.getHandle());
				}
				c.append("<<");
				if (e.isStripTabs()) c.append("-");
				try (final ICloseable block = c.block()) {
					try (final ICloseable token = e.isExpand() ? c.token(null, QuoteControl.Never) : c.quote()) {
						c.render(e.getDelimiter(), null);
					}
					c.newline().append(e.getDocument()).newline().render(e.getDelimiter(), null);
				}
				if (c.isBlockMode()) c.newline();
			});

			builder.add(BashScript.class, e -> c -> c.append("#!/bin/bash").newline().render(e.getBody(), IBashBlock.class));
			builder.add(BashBlock.class, e -> c -> e.getContents().forEach(x -> c.render(x, IBashBlock.class)));
			builder.add(BashAssignment.class, e -> c -> {
				c.append(e.getName()).append("=").render(e.getExpression(), Object.class);
				if (c.isBlockMode()) c.newline();
			});
			builder.add(BashBlank.class, e -> c -> c.newline());
			builder.add(BashIf.class, e -> c -> {
				c.append("if ").render(e.getCondition(), null).append("; then");
				if (c.isBlockMode()) {
					c.newline();
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
				} else {
					c.append(' ').render(e.getThenStatement(), IBashBlock.class);
					if (e.getElseStatement() != null) c.append("; else ").render(e.getElseStatement(), IBashBlock.class);
					c.append("; fi");
				}
			});

			builder.add(BashCommandSubstitution.class, e -> c -> {
				try (final ICloseable token = c.token(BashQuoteType.BashDoubleExpand, QuoteControl.IfNeeded)) {
					c.append("$(");
					try (final ICloseable raw = c.raw(); final ICloseable line = c.line()) {
						c.render(e.getExecutable(), IBashExecutable.class);
					}
					c.append(")");
				}
			});
			builder.add(BashProcessSubstitution.class, e -> c -> {
				try (final ICloseable token = c.token(null, QuoteControl.Never)) {
					switch (e.getDirection()) {
						case Input:
							c.append("<");
							break;
						case Output:
							c.append(">");
							break;
						default:
							throw new EnumException(BashProcessSubstitution.Direction.class, e.getDirection());
					}
					c.append("(");
					try (final ICloseable line = c.line()) {
						c.render(e.getExecutable(), IBashExecutable.class);
					}
					c.append(")");
				}
			});
			builder.add(BashString.class, e -> c -> {
				try (final ICloseable raw = c.raw()) {
					try (final ICloseable token = c.token(e.getQuoteType(), e.getQuoteControl())) {
						e.getElements().forEach(x -> c.render(x, null));
					}
				}
			});
			builder.add(BashExpansion.class, e -> c -> {
				try (final ICloseable token = c.token(BashQuoteType.BashDoubleExpand, QuoteControl.IfNeeded)) {
					c.append("${");
					try (final ICloseable expansion = c.raw()) {
						c.append(e.getName());
					}
					c.append('}');
				}
			});

			builder.add(BashTest.class, e -> c -> c.append("[[ ").render(e.getExpression(), IBashTestExpression.class).append(" ]]"));
			builder.add(BashTestOperation.class, e -> c -> IOperator.render(e.getOperator(), c, e.getOperands(), IBashTestExpression.class));
		}).build();

		@Getter(AccessLevel.PROTECTED)
		protected final TextNestedModified.TextNestedModifiedBuilder builder = TextNestedModified.builder();

		@Getter(AccessLevel.PROTECTED)
		protected final StackGlobalState<Mode> state;

		public BashRenderContext(Mode mode) {
			this.state = new StackGlobalState<Mode>(mode);
		}

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
		public ICloseable block() {
			return getState().open(Mode.Block);
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
		public ICloseable quote() {
			final ICloseable state = getState().open(Mode.Token);
			final IModifierHandle modifier = getBuilder().open(new BashQuoteModifier(BashQuoteType.BashDoubleExpand));
			return () -> {
				modifier.close();
				state.close();
			};
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
		public ICloseable token(BashQuoteType quoteType, QuoteControl quoteControl) {
			if (getState().get().equals(Mode.Token)) return () -> {};
			final ICloseable state = getState().open(Mode.Token);
			if ((quoteControl == null) || QuoteControl.Never.equals(quoteControl)) return state;
			final IModifierHandle modifier = getBuilder().open(new BashTokenModifier(quoteType, quoteControl));
			return () -> {
				modifier.close();
				state.close();
			};
		}
	}

	public enum Mode {
		Block,
		Line,
		Token;
	}

	public static List<String> toTokens(BashCommand command) {
		final BashRenderer renderer = new BashRenderer(Mode.Token);
		final List<String> retVal = new ArrayList<>();
		for (Object token : command.getTokens()) {
			retVal.add(renderer.render(token));
		}
		return retVal;
	}

	protected final Mode mode;

	public BashRenderer() {
		this(Mode.Block);
	}

	@Override
	protected BashRenderContext createContext() {
		return new BashRenderContext(getMode());
	}
}
