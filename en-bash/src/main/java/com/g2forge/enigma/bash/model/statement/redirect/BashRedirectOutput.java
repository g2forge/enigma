package com.g2forge.enigma.bash.model.statement.redirect;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashRedirectOutput implements IBashRedirect {
	@Default
	protected final int handle = HBashHandle.UNSPECIFIED;

	protected final Object target;

	@Default
	protected final boolean append = false;

	@Default
	protected final boolean clobber = true;

	public BashRedirectOutput(int handle, Object target) {
		this(handle, target, false, true);
	}

	public BashRedirectOutput(Object target) {
		this(HBashHandle.UNSPECIFIED, target);
	}
}
