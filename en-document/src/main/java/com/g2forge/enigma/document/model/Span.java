package com.g2forge.enigma.document.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Singular;

@Data
@Builder
@AllArgsConstructor
public class Span implements ISpan {
	@Singular
	protected final List<ISpan> contents;
}
