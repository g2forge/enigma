package com.g2forge.enigma.backend.model.expression;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TextCharSequence implements ITextExpression {
	protected final CharSequence value;
}
