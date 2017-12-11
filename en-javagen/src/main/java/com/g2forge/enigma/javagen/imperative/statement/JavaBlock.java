package com.g2forge.enigma.javagen.imperative.statement;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class JavaBlock implements IJavaStatement {
	protected static final String TEMPLATE = "{<if(statements)>\n\t<statements;separator=\"\\n\">\n<endif>}";

	@Singular
	protected final Collection<IJavaStatement> statements;
}
