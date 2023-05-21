package com.open.api.models.body;

public class XmlBody extends Body {

	public XmlBody(String $ref, String description) {
		super($ref, description);
	}

	@Override
	public String getType() {
		return "application/xml";
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
