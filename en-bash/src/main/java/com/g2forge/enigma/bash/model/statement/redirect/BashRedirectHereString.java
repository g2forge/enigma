package com.g2forge.enigma.bash.model.statement.redirect;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashRedirectHereString implements IBashRedirect {
	protected final int handle;

	protected final Object string;

	public BashRedirectHereString(Object string) {
		this(HBashHandle.UNSPECIFIED, string);
	}
}
