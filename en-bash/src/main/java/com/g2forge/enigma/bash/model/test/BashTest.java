package com.g2forge.enigma.bash.model.test;

import com.g2forge.enigma.bash.model.expression.IBashExpression;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashTest implements IBashExpression {
	protected final IBashTestExpression expression;
}
