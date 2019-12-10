package com.g2forge.enigma.bash.model.expression;

import com.g2forge.enigma.bash.model.statement.IBashExecutable;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashCommandSubstitution implements IBashExpression {
	protected final IBashExecutable executable;
}
