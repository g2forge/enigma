package com.g2forge.enigma.bash.model.statement;

import com.g2forge.alexandria.java.core.marker.ISingleton;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashBlank implements IBashStatement, ISingleton {
	protected static final BashBlank INSTANCE = new BashBlank();

	public static BashBlank create() {
		return INSTANCE;
	}
}
