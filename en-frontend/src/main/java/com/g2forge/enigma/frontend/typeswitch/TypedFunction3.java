package com.g2forge.enigma.frontend.typeswitch;

import com.g2forge.alexandria.java.function.IFunction3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
class TypedFunction3<I0, I1, I2, O> implements ITypedFunction3<I0, I1, I2, O> {
	@Getter
	protected final Class<? super I0> input0Type;

	@Getter
	protected final Class<? super I1> input1Type;

	@Getter
	protected final Class<? super I2> input2Type;

	protected final IFunction3<? super I0, ? super I1, ? super I2, ? extends O> function;

	@Override
	public <_I0, _I1, _I2> O apply(_I0 input0, _I1 input1, _I2 input2) {
		@SuppressWarnings("unchecked")
		final TypedFunction3<? super _I0, ? super _I1, ? super _I2, ? extends O> retVal = (TypedFunction3<? super _I0, ? super _I1, ? super _I2, ? extends O>) this;
		return retVal.function.apply(input0, input1, input2);
	}

	@Override
	public <_I0, _I1, _I2> boolean isApplicable(_I0 input0, _I1 input1, _I2 input2) {
		if (!getInput0Type().isInstance(input0)) return false;
		if (!getInput1Type().isInstance(input1)) return false;
		if (!getInput2Type().isInstance(input2)) return false;
		return true;
	}
}
