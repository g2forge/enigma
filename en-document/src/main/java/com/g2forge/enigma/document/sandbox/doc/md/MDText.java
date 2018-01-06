package com.g2forge.enigma.document.sandbox.doc.md;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MDText implements IMDRenderable {
	protected String text;

	@Override
	public String toMD() {
		return text;
	}
}
