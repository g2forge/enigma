package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.g2forge.alexandria.java.close.AGuaranteeClose;

import lombok.Getter;

public class TempDirectory extends AGuaranteeClose {
	@Getter
	protected final Path path;

	protected final boolean autodelete;

	public TempDirectory() {
		this(null, null, true);
	}

	public TempDirectory(Path parent, String prefix, boolean autodelete) {
		super(autodelete);
		try {
			if (parent != null) Files.createDirectories(parent);
			final String actualPrefix = prefix == null ? getClass().getSimpleName() : prefix;
			path = parent == null ? Files.createTempDirectory(actualPrefix) : Files.createTempDirectory(parent, actualPrefix);
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
		this.autodelete = autodelete;
	}

	@Override
	protected void closeInternal() {
		try {
			if (path != null && Files.exists(path)) {
				FileHelpers.gc();
				FileHelpers.delete(path);
			}
		} catch (IOException exception) {
			throw new RuntimeIOException(exception);
		}
	}
}
