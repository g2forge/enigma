package com.g2forge.enigma.bash.model;

import com.g2forge.enigma.bash.convert.IBashRenderable;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashScript implements IBashRenderable {
	protected final IBashBlock body;
}
