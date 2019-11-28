package com.g2forge.enigma.backend.bash.model.expression;

import com.g2forge.enigma.backend.bash.model.BashCommand;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashCommandSubstitution implements IBashExpression {
	protected final BashCommand command;
}
