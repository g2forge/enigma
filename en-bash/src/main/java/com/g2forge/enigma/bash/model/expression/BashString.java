package com.g2forge.enigma.bash.model.expression;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashString {
	@Singular
	protected final List<Object> elements;

	public BashString(Object... elements) {
		this(HCollection.asList(elements));
	}
}
