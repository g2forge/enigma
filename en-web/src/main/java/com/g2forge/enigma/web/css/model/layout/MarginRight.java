package com.g2forge.enigma.web.css.model.layout;

import com.g2forge.enigma.web.css.model.ICSSRecord;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MarginRight implements ICSSRecord {
	protected final Margin margin;
}
