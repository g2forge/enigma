package com.g2forge.enigma.bash.model.statement;

import java.util.List;

import com.g2forge.alexandria.java.core.helpers.HCollection;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashBlock implements IBashBlock {
	@Singular
	protected final List<IBashBlock> contents;

	public BashBlock(IBashBlock... contents) {
		this(HCollection.asList(contents));
	}
}
