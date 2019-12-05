package com.g2forge.enigma.backend.bash.model;

import com.g2forge.enigma.backend.bash.convert.IBashRenderable;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashScript implements IBashRenderable {
	protected final IBashBlock body;
}
