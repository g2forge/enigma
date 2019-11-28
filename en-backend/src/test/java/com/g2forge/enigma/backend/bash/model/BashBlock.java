package com.g2forge.enigma.backend.bash.model;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashBlock implements IBashBlock {
	@Singular
	protected final List<IBashBlock> contents;
}
