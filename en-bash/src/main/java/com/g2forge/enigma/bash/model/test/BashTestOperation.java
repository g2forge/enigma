package com.g2forge.enigma.bash.model.test;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
public class BashTestOperation implements IBashTestExpression {
	@Getter(AccessLevel.PROTECTED)
	@RequiredArgsConstructor
	public enum Operator {
		Not("!", 1),
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
		LogicalAnd("&&", 0),
		LogicalOr("||", 0),
		NumericEqual("-eq", 2),
		NumericNotEqual("-ne", 2),
		NumericLessThan("-lt", 2),
		NumericLessThanOrEqual("-le", 2),
		NumericGreaterThan("-gt", 2),
		NumericGreaterThanOrEqual("-ge", 2);

		protected final String symbol;

		protected final int numArguments;

		public BashTestOperation.BashTestOperationBuilder createOperation() {
			return BashTestOperation.builder().operator(this);
		}

		public String getInfix() {
			return getSymbol();
		}

		public String getPostfix() {
			return null;
		}

		public String getPrefix() {
			return getNumArguments() == 1 ? getSymbol() : null;
		}

		public boolean isValidNumArguments(int numArguments) {
			if (getNumArguments() == 0) return numArguments > 0;
			else return getNumArguments() == numArguments;
		}
	}

	protected final Operator operator;

	@Singular
	protected final List<?> operands;

	public BashTestOperation(Operator operator, List<?> operands) {
		if (!operator.isValidNumArguments(operands.size())) throw new IllegalArgumentException(String.format("Invalid number of arguments for %1$s, received %2$d: %3$s", operator, operands.size(), operands));
		this.operator = operator;
		this.operands = operands;
	}

	public BashTestOperation(Operator operator, Object... operands) {
		this(operator, HCollection.asList(operands));
	}
}
