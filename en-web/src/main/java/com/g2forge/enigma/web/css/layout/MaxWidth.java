package com.g2forge.enigma.web.css.layout;

import com.g2forge.enigma.web.css.ICSSRecord;
import com.g2forge.enigma.web.css.distance.IDistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MaxWidth implements ICSSRecord {
	protected final IDistance distance;
}
