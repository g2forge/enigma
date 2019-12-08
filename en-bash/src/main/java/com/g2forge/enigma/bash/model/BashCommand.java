package com.g2forge.enigma.bash.model;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashCommand implements IBashStatement {
	@Singular
	protected final List<Object> tokens;

	public BashCommand(Object... tokens) {
		this(HCollection.asList(tokens));
	}
}
