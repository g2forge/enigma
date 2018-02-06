package com.g2forge.enigma.web.css.drawing;

import com.g2forge.enigma.web.css.ICSSRecord;
import com.g2forge.enigma.web.css.color.IColor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Stroke implements ICSSRecord {
	protected final IColor color;
}
