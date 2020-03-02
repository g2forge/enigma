package com.g2forge.enigma.bash.model.statement;

import java.util.List;

import com.g2forge.enigma.backend.model.IOperator;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashOperation implements IBashExecutable {
	@Getter(AccessLevel.PROTECTED)
	@RequiredArgsConstructor
	public enum Operator implements IOperator {
		Not("! ", 1),
		Parentheses(null, 1) {
			@Override
			public String getPostfix() {
				return ")";
			}

			@Override
			public String getPrefix() {
				return "(";
			}
		},
		And(" && ", 0),
		Or(" || ", 0),
		Pipe(" | ", 0),
		Sequence("; ", 0);

		protected final String symbol;

		protected final int numArguments;

		@Override
		public BashOperation.BashOperationBuilder builder() {
			return BashOperation.builder().operator(this);
		}

		@Override
		public String getInfix() {
			return getSymbol();
		}

		@Override
		public String getPostfix() {
			return null;
		}

		@Override
		public String getPrefix() {
			return getNumArguments() == 1 ? getSymbol() : null;
		}

		@Override
		public boolean isValidNumArguments(int numArguments) {
			if (getNumArguments() == 0) return numArguments > 0;
			else return getNumArguments() == numArguments;
		}
	}

	protected final Operator operator;

	@Singular
	protected final List<IBashExecutable> operands;
}
