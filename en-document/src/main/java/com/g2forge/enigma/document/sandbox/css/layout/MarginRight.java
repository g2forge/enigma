package com.g2forge.enigma.document.sandbox.css.layout;

import com.g2forge.enigma.document.sandbox.css.ICSSRecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MarginRight implements ICSSRecord {
	protected final Margin margin;
}
