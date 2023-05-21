package com.open.api.models.param;

public class QueryParameter implements Parameter {

	private final String description;
	private final String name;
	private final String schema;

	public QueryParameter(String description, String name, String schema) {
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
		return "query";
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
