package com.g2forge.enigma.web.css.model.drawing;

import com.g2forge.enigma.web.css.model.ICSSRecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StrokeWidth implements ICSSRecord {
	protected final int width;
}
