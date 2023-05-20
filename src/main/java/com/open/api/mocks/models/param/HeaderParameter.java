package com.open.api.mocks.models.param;

public class HeaderParameter implements Parameter {

	private final String description;
	private final String name;
	private final String schema;

	public HeaderParameter(String description, String name, String schema) {
		this.description = description;
		this.name = name;
		this.schema = schema;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public String getIn() {
		return "header";
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getSchema() {
		return schema;
	}

}
