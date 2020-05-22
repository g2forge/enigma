package com.g2forge.enigma.web.css.model.layout;

import com.g2forge.enigma.web.css.model.ICSSRecord;
import com.g2forge.enigma.web.css.model.distance.IDistance;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class MaxWidth implements ICSSRecord {
	protected final IDistance distance;
}
