package com.g2forge.enigma.document.sandbox.css.layout;

import com.g2forge.enigma.document.sandbox.css.ICSSRecord;
import com.g2forge.enigma.document.sandbox.css.distance.IDistance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MaxWidth implements ICSSRecord {
	protected final IDistance distance;
}
