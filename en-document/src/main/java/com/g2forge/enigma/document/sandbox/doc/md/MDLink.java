package com.g2forge.enigma.document.sandbox.doc.md;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MDLink implements IMDRenderable {
	protected String title;

	protected String url;

	@Override
	public String toMD() {
		return String.format("[%1$s](%2$s)", getTitle(), getUrl());
	}
}
