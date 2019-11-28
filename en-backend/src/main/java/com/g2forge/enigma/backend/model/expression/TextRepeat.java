package com.g2forge.enigma.backend.model.expression;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TextRepeat implements ITextExpression {
	protected final ITextExpression expression;
	
	protected final int repeat;
}
