package com.g2forge.enigma.bash.model.statement;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashAssignment implements IBashStatement {
	protected final String name;

	protected final Object expression;
}
