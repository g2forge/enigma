package com.g2forge.enigma.web.css.model.layout;

import com.g2forge.enigma.web.css.model.ICSSRecord;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MarginLeft implements ICSSRecord {
	protected final Margin margin;
}
