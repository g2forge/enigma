package com.g2forge.enigma.frontend.antlr;

import java.io.IOException;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.Parser;

import com.g2forge.alexandria.java.core.resource.Resource;
import com.g2forge.alexandria.java.function.IFunction1;
import com.g2forge.alexandria.java.function.IThrowFunction1;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter(AccessLevel.PROTECTED)
public abstract class AStandardANTLRLoader<K, P extends Parser, E, VI, VO> extends AANTLRLoader<K, P, E, VI, VO> {
	public static IThrowFunction1<? super String, ? extends CharStream, IOException> createTestCreateCharStream(Class<?> type, String suffix) {
		return key -> CharStreams.fromStream(new Resource(type, key + suffix).getResourceAsStream(true));
	}

	protected final IThrowFunction1<? super K, ? extends CharStream, IOException> createCharStream;

	protected final IFunction1<? super K, ? extends VI> createDefaultResult;

	protected CharStream createCharStream(K key) throws IOException {
		return getCreateCharStream().apply(key);
	}

	protected VI createDefaultResult(K key) {
		if (getCreateDefaultResult() == null) return null;
		return getCreateDefaultResult().apply(key);
	}
}
