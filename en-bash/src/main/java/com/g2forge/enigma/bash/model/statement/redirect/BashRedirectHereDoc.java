package com.g2forge.enigma.bash.model.statement.redirect;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashRedirectHereDoc implements IBashRedirect {
	protected final int handle;
	
	protected final String delimiter;

	protected final Object document;

	protected final boolean expand;

	protected final boolean stripTabs;

	public BashRedirectHereDoc(Object document) {
		this(HBashHandle.UNSPECIFIED, "EOF", document, true, false);
	}
}
