package com.g2forge.enigma.frontend.typeswitch;

public interface ITypedFunction2<I0, I1, O> {
	public <_I0, _I1> O apply(_I0 input0, _I1 input1);

	public Class<? super I0> getInput0Type();

	public Class<? super I1> getInput1Type();

	public <_I0, _I1> boolean isApplicable(_I0 input0, _I1 input1);
}
