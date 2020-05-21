package com.g2forge.enigma.web.css.model.drawing;

import com.g2forge.enigma.web.css.model.ICSSRecord;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class StrokeWidth implements ICSSRecord {
	protected final int width;
}
