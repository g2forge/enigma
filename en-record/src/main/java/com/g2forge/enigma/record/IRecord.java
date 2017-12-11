package com.g2forge.enigma.record;

import java.util.Collection;

import com.g2forge.enigma.record.core.IType;

public interface IRecord<LN, LT> extends IType<LN, LT> {
	public Collection<IProperty<LN, LT>> getProperties();
}
