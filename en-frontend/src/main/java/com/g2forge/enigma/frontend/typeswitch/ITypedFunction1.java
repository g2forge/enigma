package com.g2forge.enigma.frontend.typeswitch;

import com.g2forge.alexandria.java.function.IFunction1;

public interface ITypedFunction1<I, O> extends IFunction1<I, O> {
	public Class<? super I> getInputType();
}
