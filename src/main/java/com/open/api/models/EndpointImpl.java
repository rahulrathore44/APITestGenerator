package com.open.api.models;

import java.util.List;
import java.util.Map;

import com.open.api.codegen.EndPoint;
import com.open.api.codegen.EndPointVisitor;
import com.open.api.models.body.IBody;
import com.open.api.models.param.Parameter;

public class EndpointImpl implements EndPoint {

	private final String contextPath;
	private final HttpMethod method;
	private final List<Parameter> parameter;
	private final List<IBody> requestBody;
	private final Map<String, List<IBody>> responses;
	private final String description;

	public EndpointImpl(String contextPath, String description, HttpMethod method, List<Parameter> parameter,
			List<IBody> requestBody, Map<String, List<IBody>> responses) {
		this.contextPath = contextPath;
		this.description = description;
		this.method = method;
		this.parameter = parameter;
		this.requestBody = requestBody;
		this.responses = responses;

	}

	public String getDescription() {
		return description;
	}

	public String getContextPath() {
		return contextPath;
	}

	public HttpMethod getOperation() {
		return method;
	}

	public List<Parameter> getParameter() {
		return parameter;
	}

	public List<IBody> getRequestBody() {
		return requestBody;
	}

	public Map<String, List<IBody>> getResponses() {
		return responses;
	}

	@Override
	public void accept(EndPointVisitor endPointVisitor) {
		endPointVisitor.visit(this);
	}

	@Override
	public String toString() {
		return String.format("%s[path=%s description=%s method=%s requestBody=%s responseBody=%s]",
				this.getClass().getSimpleName(), this.contextPath, this.description, this.method.name(),
				this.requestBody, this.responses);
	}

}
