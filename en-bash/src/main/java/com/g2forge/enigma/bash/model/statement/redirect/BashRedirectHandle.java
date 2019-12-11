package com.g2forge.enigma.bash.model.statement.redirect;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashRedirectHandle {
	public enum Operation {
		Duplicate,
		Move;

		public BashRedirectHandle create(int handle) {
			return new BashRedirectHandle(handle, this);
		}
	}

	protected final int handle;

	protected final Operation operation;
}