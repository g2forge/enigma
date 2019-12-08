package com.g2forge.enigma.backend.model.expression;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TextConcatenation implements ITextExpression {
	@Singular
	protected final List<Object> elements;
}
