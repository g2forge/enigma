package com.g2forge.enigma.bash.model.expression;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashExpansion implements IBashExpression {
	protected final String name;
}
