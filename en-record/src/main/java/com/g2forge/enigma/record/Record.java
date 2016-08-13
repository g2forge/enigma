package com.g2forge.enigma.record;

import java.util.Collection;

import com.g2forge.enigma.record.core.IType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Record<LN, LT> implements IRecord<LN, LT> {
	protected Collection<IProperty<LN, LT>> properties;

	@Getter(AccessLevel.PROTECTED)
	@Setter(AccessLevel.PROTECTED)
	@Delegate
	protected IType<LN, LT> type;
}
