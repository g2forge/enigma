package com.g2forge.enigma.bash.convert;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.enums.EnumException;
import com.g2forge.alexandria.java.function.IConsumer2;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.nestedstate.StackGlobalState;
import com.g2forge.alexandria.java.text.quote.BashQuoteType;
import com.g2forge.alexandria.java.text.quote.QuoteControl;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.ITextAppender;
import com.g2forge.enigma.backend.convert.ARenderer;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.convert.text.ControlQuoteTextModifier;
import com.g2forge.enigma.backend.convert.text.QuoteTextModifier;
import com.g2forge.enigma.backend.convert.textual.ATextualRenderer;
import com.g2forge.enigma.backend.text.model.IOperator;
import com.g2forge.enigma.backend.text.model.modifier.TextNestedModified;
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
public class BashRenderer extends ATextualRenderer<Object, IBashRenderContext> {
	protected class BashRenderContext extends ARenderContext implements IBashRenderContext {
		@Getter(AccessLevel.PROTECTED)
		protected final StackGlobalState<Mode> state;

		public BashRenderContext(TextNestedModified.TextNestedModifiedBuilder builder, Mode mode) {
			super(builder);
			this.state = new StackGlobalState<Mode>(mode);
		}

		@Override
		public ICloseable block() {
			return getState().open(Mode.Block);
		}

		@Override
		protected IBashRenderContext getThis() {
			return this;
		}

		@Override
		public ICloseable indent() {
			switch (getState().get()) {
				case Token:
					throw new IllegalStateException();
				case Line:
					return () -> {};
				case Block:
					return super.indent();
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
					return this;
				case Block:
					return super.newline();
				default:
					throw new EnumException(Mode.class, getState().get());
			}
		}

		@Override
		public ICloseable quote() {
			final ICloseable state = getState().open(Mode.Token);
			final ICloseable modifier = getBuilder().open(new QuoteTextModifier(BashQuoteType.BashDoubleExpand));
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
		public ICloseable token(BashQuoteType quoteType, QuoteControl quoteControl) {
			if (getState().get().equals(Mode.Token)) return () -> {};
			final ICloseable state = getState().open(Mode.Token);
			if ((quoteControl == null) || QuoteControl.Never.equals(quoteControl)) return state;
			final ICloseable modifier = getBuilder().open(new ControlQuoteTextModifier(quoteType, quoteControl));
			return () -> {
				modifier.close();
				state.close();
			};
		}
	}

	protected static class BashRendering extends ARenderer.ARendering<Object, IBashRenderContext, IExplicitRenderable<? super IBashRenderContext>> {
		@Override
		protected void extend(TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super IBashRenderContext>> builder) {
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
				try (final ICloseable line = c.line()) {
					boolean first = true;
					for (Object object : e.getTokens()) {
						if (first) first = false;
						else c.append(" ");
						c.render(object, null);
					}
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
		}
	}

	public enum Mode {
		Block,
		Line,
		Token;
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final IRendering<Object, IBashRenderContext, IExplicitRenderable<? super IBashRenderContext>> renderingStatic = new BashRendering();

	public static List<String> toTokens(BashCommand command) {
		return toTokens(BashRenderer::new, command);
	}

	public static List<String> toTokens(IFunction1<? super Mode, ? extends BashRenderer> constructor, BashCommand command) {
		final BashRenderer renderer = constructor.apply(Mode.Token);
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
	protected IBashRenderContext createContext(TextNestedModified.TextNestedModifiedBuilder builder) {
		return new BashRenderContext(builder, getMode());
	}

	@Override
	protected IRendering<? super Object, ? extends IBashRenderContext, ? extends IExplicitRenderable<? super IBashRenderContext>> getRendering() {
		return getRenderingStatic();
	}
}
