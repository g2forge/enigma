package com.g2forge.enigma.frontend.typeswitch;

import com.g2forge.alexandria.java.function.IFunction2;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
class TypedFunction2<I0, I1, O> implements ITypedFunction2<I0, I1, O> {
	@Getter
	protected final Class<? super I0> input0Type;

	@Getter
	protected final Class<? super I1> input1Type;

	protected final IFunction2<? super I0, ? super I1, ? extends O> function;

	@Override
	public <_I0, _I1> O apply(_I0 input0, _I1 input1) {
		@SuppressWarnings("unchecked")
		final TypedFunction2<? super _I0, ? super _I1, ? extends O> retVal = (TypedFunction2<? super _I0, ? super _I1, ? extends O>) this;
		return retVal.function.apply(input0, input1);
	}

	@Override
	public <_I0, _I1> boolean isApplicable(_I0 input0, _I1 input1) {
		if (!getInput0Type().isInstance(input0)) return false;
		if (!getInput1Type().isInstance(input1)) return false;
		return true;
	}
}
