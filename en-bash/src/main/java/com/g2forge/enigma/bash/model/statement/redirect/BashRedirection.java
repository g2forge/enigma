package com.g2forge.enigma.bash.model.statement.redirect;

import java.util.List;

import com.g2forge.enigma.bash.model.statement.IBashExecutable;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Singular;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class BashRedirection implements IBashExecutable {
	protected final IBashExecutable executable;

	@Singular
	protected final List<IBashRedirect> redirects;
}
