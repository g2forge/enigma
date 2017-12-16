package com.g2forge.enigma.frontend.typeswitch;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
class TypedFunction1<I, O> implements ITypedFunction1<I, O> {
	@Getter
	protected final Class<? super I> inputType;

	protected final IFunction1<? super I, ? extends O> function;

	@Override
	public <_I> O apply(_I input) {
		@SuppressWarnings("unchecked")
		final TypedFunction1<? super _I, ? extends O> retVal = (TypedFunction1<? super _I, ? extends O>) this;
		return retVal.function.apply(input);
	}

	public <_I> boolean isApplicable(_I input) {
		return getInputType().isInstance(input);
	}
}
