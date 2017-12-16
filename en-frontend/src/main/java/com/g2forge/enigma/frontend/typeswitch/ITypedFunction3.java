package com.g2forge.enigma.frontend.typeswitch;

public interface ITypedFunction3<I0, I1, I2, O> {
	public <_I0, _I1, _I2> O apply(_I0 input0, _I1 input1, _I2 input2);

	public Class<? super I0> getInput0Type();

	public Class<? super I1> getInput1Type();

	public Class<? super I2> getInput2Type();

	public <_I0, _I1, _I2> boolean isApplicable(_I0 input0, _I1 input1, _I2 input2);
}
