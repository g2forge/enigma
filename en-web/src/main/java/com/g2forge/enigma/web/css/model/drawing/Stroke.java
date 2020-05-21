package com.g2forge.enigma.web.css.model.drawing;

import com.g2forge.enigma.web.css.model.ICSSRecord;
import com.g2forge.enigma.web.css.model.color.IColor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Stroke implements ICSSRecord {
	protected final IColor color;
}
