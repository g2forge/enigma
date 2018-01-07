package com.g2forge.enigma.document.sandbox.doc.md;

import java.util.Arrays;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MDList implements IMDRenderable {
	protected Collection<IMDRenderable> elements;

	public MDList(IMDRenderable... elements) {
		this(Arrays.asList(elements));
	}

	@Override
	public String toMD() {
		final StringBuilder retVal = new StringBuilder();
		for (IMDRenderable element : getElements()) {
			retVal.append("* ").append(element.toMD());
			retVal.append('\n');
		}
		return retVal.toString();
	}
}
