package com.g2forge.enigma.document.sandbox.css.drawing;

import com.g2forge.enigma.document.sandbox.css.ICSSRecord;
import com.g2forge.enigma.document.sandbox.css.color.IColor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Fill implements ICSSRecord {
	protected final IColor color;
}
