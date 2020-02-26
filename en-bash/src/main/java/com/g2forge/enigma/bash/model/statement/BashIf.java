package com.g2forge.enigma.bash.model.statement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashIf implements IBashExecutable {
	protected final Object condition;

	protected final IBashBlock thenStatement;

	protected final IBashBlock elseStatement;

	public BashIf(Object condition, IBashBlock thenStatement) {
		this(condition, thenStatement, null);
	}
}
