package com.g2forge.enigma.record;

import com.g2forge.enigma.record.core.IName;
import com.g2forge.enigma.record.core.IType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Property<LN, LT> implements IProperty<LN, LT> {
	protected IName<LN> name;

	protected IType<LN, LT> type;
}
