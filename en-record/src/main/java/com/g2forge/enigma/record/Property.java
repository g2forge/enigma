package com.g2forge.enigma.record;

import com.g2forge.enigma.record.core.IName;
import com.g2forge.enigma.record.core.IType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Property<LN, LT> implements IProperty<LN, LT> {
	protected final IName<LN> name;

	protected final IType<LN, LT> type;
}
