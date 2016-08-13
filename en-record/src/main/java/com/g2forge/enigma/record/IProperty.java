package com.g2forge.enigma.record;

import com.g2forge.enigma.record.core.IName;
import com.g2forge.enigma.record.core.IType;

public interface IProperty<LN, LT> {
	public IName<LN> getName();

	public IType<LN, LT> getType();
}
