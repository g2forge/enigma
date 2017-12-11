package com.g2forge.enigma.javagen.model;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Record {
	protected String name;

	protected Collection<Record> parents;

	protected Collection<Property> properties;
}
