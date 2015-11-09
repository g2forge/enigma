package com.g2forge.alexandria.java.io;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.LinkedList;

public class FileHelpers {
	public static void delete(Path path) throws IOException {
		final LinkedList<Path> onexit = new LinkedList<>();
		Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
			protected FileVisitResult deleteAndContinue(Path path) throws IOException {
				try {
					Files.deleteIfExists(path);
				} catch (IOException exception) {
					onexit.addFirst(path);
				}
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path directory, IOException exception) throws IOException {
				if (exception == null) return deleteAndContinue(directory);
				else throw exception;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attributes) throws IOException {
				return deleteAndContinue(file);
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exception) throws IOException {
				return deleteAndContinue(file);
			}
		});
		onexit.forEach(toDelete -> toDelete.toFile().deleteOnExit());
	}

	public static void gc() {
		gc(3, 100);
	}

	public static void gc(int repeat, int pause) {
		for (int i = 0; i < repeat; i++) {
			if (i > 0) synchronized (FileHelpers.class) {
				try {
					FileHelpers.class.wait(pause);
				} catch (InterruptedException exception) {}
			}
			System.gc();
		}
	}
}
