package com.g2forge.enigma.record;

import java.util.Collection;

import com.g2forge.enigma.record.core.IType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.Delegate;

@Data
@Builder
@AllArgsConstructor
public class Record<LN, LT> implements IRecord<LN, LT> {
	protected final Collection<IProperty<LN, LT>> properties;

	@Getter(AccessLevel.PROTECTED)
	@Delegate
	protected final IType<LN, LT> type;
}
