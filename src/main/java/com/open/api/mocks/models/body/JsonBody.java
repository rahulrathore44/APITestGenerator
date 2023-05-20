package com.open.api.mocks.models.body;

public class JsonBody implements IBody {

	private final String $ref;
	private final String description;

	public JsonBody(String $ref, String description) {
		this.$ref = $ref;
		this.description = description;
	}

	@Override
	public String getType() {
		return "application/json";
	}

	@Override
	public String get$ref() {
		return $ref;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String toString() {
		return String.format("%s{type=%s description=%s ref=%s}", this.getClass().getSimpleName(), this.getType(),
				this.description, this.$ref);
	}

}
