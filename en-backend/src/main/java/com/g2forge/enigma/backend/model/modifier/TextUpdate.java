package com.g2forge.enigma.backend.model.modifier;

import com.g2forge.alexandria.java.function.IFunction1;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class TextUpdate {
	protected final int offset;

	protected final int length;

	protected final IFunction1<? super CharSequence, ? extends Object> function;
}