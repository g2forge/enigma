package com.g2forge.enigma.bash.model.statement.redirect;

import com.g2forge.alexandria.java.core.marker.Helpers;

import lombok.experimental.UtilityClass;

@UtilityClass
@Helpers
public class HBashHandle {
	public static final int BOTH = -2;

	public static final int UNSPECIFIED = -1;

	public static final int STDIN = 0;

	public static final int STDOUT = 1;

	public static final int STDERR = 2;
}
