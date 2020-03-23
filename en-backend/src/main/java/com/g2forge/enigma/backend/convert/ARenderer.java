package com.g2forge.enigma.backend.convert;

import java.lang.reflect.Type;

import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.type.function.TypeSwitch1;

import lombok.AccessLevel;
import lombok.Getter;

public abstract class ARenderer<R, C extends IRenderContext<R, ? super C>> implements IRenderer<R> {
	protected static abstract class ARendering<R, C extends IRenderContext<R, ? super C>, E extends IExplicitRenderable<? super C>> implements IRendering<R, C, E> {
		@Getter(lazy = true, value = AccessLevel.PROTECTED)
		private final IFunction1<R, E> toExplicit = computeToExplicit();

		protected IFunction1<R, E> computeToExplicit() {
			return new TypeSwitch1.FunctionBuilder<R, E>().with(this::extend).build();
		}

		protected abstract void extend(TypeSwitch1.FunctionBuilder<R, E> builder);

		@Override
		public E toExplicit(R object, Type type) {
			return getToExplicit().apply(object);
		}
	}

	protected abstract IRendering<R, C, ? extends IExplicitRenderable<? super C>> getRendering();

	protected abstract String build(final C context);

	protected abstract C createContext();

	@Override
	public String render(R renderable) {
		final C context = createContext();
		context.render(renderable, null);
		return build(context);
	}
}
