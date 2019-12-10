package com.g2forge.enigma.bash.model.statement;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashOperation implements IBashExecutable {
	public enum Operator {
		And,
		Or;

		public BashOperation.BashOperationBuilder builder() {
			return BashOperation.builder().operator(this);
		}
	}

	protected final Operator operator;

	@Singular
	protected final List<IBashExecutable> operands;
}
