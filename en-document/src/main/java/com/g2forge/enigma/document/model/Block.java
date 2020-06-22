package com.g2forge.enigma.document.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Block implements IBlock {
	public enum Type {
		Document,
		Block,
		Paragraph,
		ListItem;
	}

	protected final Type type;

	@Singular
	protected final List<IBlock> contents;
}
