package com.g2forge.enigma.document.sandbox.wikitext;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Block implements IBlock {
	protected static final String TEMPLATE = "<contents:{c|<c><\\n>}>";

	@Singular
	protected final List<IBlock> contents;
}
