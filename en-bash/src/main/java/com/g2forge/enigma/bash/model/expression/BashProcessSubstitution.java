package com.g2forge.enigma.bash.model.expression;

import com.g2forge.enigma.bash.model.statement.IBashExecutable;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashProcessSubstitution implements IBashExpression {
	public enum Direction {
		Input,
		Output;

		public BashProcessSubstitution create(IBashExecutable executable) {
			return new BashProcessSubstitution(executable, this);
		}
	}

	protected final IBashExecutable executable;

	protected final Direction direction;
}
