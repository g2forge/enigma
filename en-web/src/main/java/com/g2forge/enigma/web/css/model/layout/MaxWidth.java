package com.g2forge.enigma.web.css.model.layout;

import com.g2forge.enigma.web.css.model.ICSSRecord;
import com.g2forge.enigma.web.css.model.distance.IDistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MaxWidth implements ICSSRecord {
	protected final IDistance distance;
}
