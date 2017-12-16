package com.g2forge.enigma.frontend.typeswitch;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class TypedFunction1<I, O> implements ITypedFunction1<I, O> {
	@Getter
	protected final Class<? super I> inputType;

	protected final IFunction1<? super I, ? extends O> function;

	@Override
	public O apply(I t) {
		return function.apply(t);
	}

	protected <_I> TypedFunction1<? super _I, ? extends O> cast(_I input) {
		if (!isApplicable(input)) return null;
		@SuppressWarnings("unchecked")
		final TypedFunction1<? super _I, ? extends O> retVal = (TypedFunction1<? super _I, ? extends O>) this;
		return retVal;
	}

	protected <_I> boolean isApplicable(_I input) {
		return getInputType().isInstance(input);
	}
}
