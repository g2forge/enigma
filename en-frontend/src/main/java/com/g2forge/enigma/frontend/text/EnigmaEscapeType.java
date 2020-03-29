package com.g2forge.enigma.frontend.text;

import com.g2forge.alexandria.java.text.escape.IEscapeType;
import com.g2forge.alexandria.java.text.escape.IEscaper;
import com.g2forge.alexandria.java.text.escape.SingleCharacterEscaper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EnigmaEscapeType implements IEscapeType {
	String(new SingleCharacterEscaper("\\", null, "\b\n\r\f\"\\\u201c\u201d\t\'", "bnrf\"\\\u201c\u201dt'", 8));

	protected final IEscaper escaper;
}
