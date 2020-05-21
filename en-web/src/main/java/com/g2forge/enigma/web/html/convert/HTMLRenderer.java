package com.g2forge.enigma.web.html.convert;

import java.util.ArrayList;
import java.util.List;

import com.g2forge.alexandria.java.close.ICloseable;
import com.g2forge.alexandria.java.core.error.NotYetImplementedError;
import com.g2forge.alexandria.java.core.helpers.HCollection;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.text.TextUpdate;
import com.g2forge.alexandria.java.text.escape.WebEscapeType;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;
import com.g2forge.enigma.backend.convert.IExplicitRenderable;
import com.g2forge.enigma.backend.convert.IRendering;
import com.g2forge.enigma.backend.text.model.modifier.ITextModifier;
import com.g2forge.enigma.web.css.convert.CSSRenderer;
import com.g2forge.enigma.web.css.convert.ICSSRenderContext;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class HTMLRenderer extends CSSRenderer {
	@Data
	@Builder(toBuilder = true)
	@RequiredArgsConstructor
	public static class HTMLContentModifier implements ITextModifier {
		protected final String tag;

		protected final boolean inline;

		@Override
		public List<? extends List<? extends TextUpdate<?>>> computeUpdates(List<CharSequence> list) {
			final int size = list.size();
			if (list.isEmpty()) return HCollection.<List<? extends TextUpdate<?>>>asList(HCollection.asList(new TextUpdate<>(0, 0, IFunction1.create("/>"))));

			final List<List<TextUpdate<?>>> retVal = new ArrayList<>(size);
			for (int i = 0; i < size; i++) {
				if ((i == 0) || (i == size - 1)) {
					final List<TextUpdate<?>> updates = new ArrayList<>();
					if (i == 0) updates.add(new TextUpdate<>(0, 0, IFunction1.create(">" + (isInline() ? "" : "\n"))));
					if (i == size - 1) updates.add(new TextUpdate<>(HCollection.getLast(list).length(), 0, IFunction1.create("</" + getTag() + ">")));
					retVal.add(updates);
				} else retVal.add(null);
			}
			return retVal;
		}

		@Override
		public boolean isRequireSomething() {
			return true;
		}

		@Override
		public ITextModifier merge(Iterable<? extends ITextModifier> modifiers) {
			throw new NotYetImplementedError();
		}

		@Override
		public String toString() {
			return getClass().getSimpleName();
		}
	}

	protected class HTMLRenderContext extends CSSRenderContext implements IHTMLRenderContext {
		@Override
		protected IHTMLRenderContext getThis() {
			return this;
		}

		@Override
		public ICloseable openTag(String tag, boolean inline) {
			return getBuilder().open(new HTMLContentModifier(tag, inline));
		}
	}

	protected static class HTMLRendering extends CSSRendering {
		@Override
		protected void extend(TypeSwitch1.FunctionBuilder<Object, IExplicitRenderable<? super ICSSRenderContext>> builder) {
			super.extend(builder);
			builder.add(CharSequence.class, e -> context -> context.append(WebEscapeType.XML.getEscaper().escape(e.toString())));
			builder.add(IExplicitHTMLRenderable.class, e -> c -> e.render((IHTMLRenderContext) c));
			builder.add(IReflectiveHTMLElement.class, e -> c -> new ReflectiveExplicitHTMLElement(e).render((IHTMLRenderContext) c));
		}
	}

	@Getter(lazy = true, value = AccessLevel.PROTECTED)
	private static final IRendering<Object, ICSSRenderContext, IExplicitRenderable<? super ICSSRenderContext>> renderingStatic = new HTMLRendering();

	@Override
	protected IHTMLRenderContext createContext() {
		return new HTMLRenderContext();
	}

	@Override
	protected IRendering<? super Object, ? extends ICSSRenderContext, ? extends IExplicitRenderable<? super ICSSRenderContext>> getRendering() {
		return getRenderingStatic();
	}
}
