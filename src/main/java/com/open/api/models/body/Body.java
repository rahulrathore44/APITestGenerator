package com.open.api.models.body;

public abstract class Body {

	protected final String $ref;
	protected final String description;

	public Body(String $ref, String description) {
		this.$ref = $ref;
		this.description = description;
	}
	
	abstract public String getType();

	abstract public String get$ref();

	abstract public String getDescription();

}
