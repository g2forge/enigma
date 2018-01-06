package com.g2forge.enigma.document.sandbox.doc.md;

import java.util.Arrays;
import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MDBlock implements IMDRenderable {
	protected Collection<IMDRenderable> elements;

	public MDBlock(IMDRenderable... elements) {
		this(Arrays.asList(elements));
	}

	@Override
	public String toMD() {
		final StringBuilder retVal = new StringBuilder();
		for (IMDRenderable element : elements) {
			retVal.append(element.toMD());
		}
		return retVal.toString();
	}
}
