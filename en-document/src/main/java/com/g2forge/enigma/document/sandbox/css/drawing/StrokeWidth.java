package com.g2forge.enigma.document.sandbox.css.drawing;

import com.g2forge.enigma.document.sandbox.css.ICSSRecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StrokeWidth implements ICSSRecord {
	protected final int width;
}
