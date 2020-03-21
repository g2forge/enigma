package com.g2forge.enigma.bash.model;

import com.g2forge.enigma.bash.model.statement.IBashBlock;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashScript {
	protected final IBashBlock body;
}
