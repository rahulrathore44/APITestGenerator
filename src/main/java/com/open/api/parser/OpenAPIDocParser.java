package com.open.api.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.open.api.codegen.EndPoint;
import com.open.api.models.EndpointImpl;
import com.open.api.models.HttpMethod;
import com.open.api.models.body.Body;
import com.open.api.models.body.JsonBody;
import com.open.api.models.body.UrlEncodedBody;
import com.open.api.models.body.XmlBody;
import com.open.api.models.param.HeaderParameter;
import com.open.api.models.param.Parameter;
import com.open.api.models.param.PathParameter;
import com.open.api.models.param.QueryParameter;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.Paths;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;

public class OpenAPIDocParser {

	private final String filePath;

	public OpenAPIDocParser(String filePath) {
		this.filePath = filePath;
	}

	public List<EndPoint> getEndPoints() {

		List<EndPoint> endpoints = new ArrayList<EndPoint>();
		Paths paths = getPaths();

		paths.forEach((key, value) -> {
			endpoints.addAll(getEndPoint(key, value));
		});

		return endpoints;

	}

	private Paths getPaths() {

		OpenAPIV3Parser apiParser = new OpenAPIV3Parser();
		ParseOptions parseOptions = new ParseOptions();
		parseOptions.setResolve(true); // implicit
		parseOptions.setResolveFully(true);

		OpenAPI openApi = apiParser.read(filePath, null, parseOptions);
		return openApi.getPaths();
	}

	private List<EndPoint> getEndPoint(String path, PathItem value) {

		List<EndPoint> endpoints = new ArrayList<EndPoint>();

		endpoints.add(buildEndPoint(path, HttpMethod.get, value.getGet()));
		endpoints.add(buildEndPoint(path, HttpMethod.put, value.getPut()));
		endpoints.add(buildEndPoint(path, HttpMethod.post, value.getPost()));
		endpoints.add(buildEndPoint(path, HttpMethod.delete, value.getDelete()));
		endpoints.add(buildEndPoint(path, HttpMethod.options, value.getOptions()));
		endpoints.add(buildEndPoint(path, HttpMethod.head, value.getHead()));
		endpoints.add(buildEndPoint(path, HttpMethod.patch, value.getPatch()));
		endpoints.add(buildEndPoint(path, HttpMethod.trace, value.getTrace()));

		return endpoints.stream().filter((endPoint) -> {
			return endPoint != null;
		}).collect(Collectors.toList());

	}

	private EndpointImpl buildEndPoint(String path, HttpMethod method, Operation operation) {

		return operation != null
				? new EndpointImpl(path, operation.getDescription(), method, getParameter(operation),
						getRequestBody(operation), getResponseBody(operation))
				: null;
	}

	private List<Parameter> getParameter(Operation operation) {

		List<Parameter> parameters = new ArrayList<Parameter>();

		if (operation.getParameters() != null) {
			operation.getParameters().forEach((param) -> {
				switch (param.getIn()) {

				case "query":
					parameters.add(
							new QueryParameter(param.getDescription(), param.getName(), getType(param.getSchema())));
					break;
				case "path":
					parameters.add(
							new PathParameter(param.getDescription(), param.getName(), getType(param.getSchema())));
					break;
				case "header":
					parameters.add(
							new HeaderParameter(param.getDescription(), param.getName(), getType(param.getSchema())));

				default:
					break;
				}
			});
		}
		return parameters.isEmpty() ? null : parameters;

	}

	private String getType(Schema schema) {
		return schema != null ? schema.getType() : null;
	}

	private List<Body> getRequestBody(Operation operation) {
		List<Body> requestBody = new ArrayList<Body>();

		if (operation.getRequestBody() != null) {
			RequestBody body = operation.getRequestBody();
			requestBody.addAll(extractedBody(body.getContent(), body.getDescription()));

		}

		return requestBody.isEmpty() ? null : requestBody;
	}

	private List<Body> extractedBody(Content content, String description) {

		List<Body> body = new ArrayList<Body>();
		if (content != null) {
			content.forEach((key, value) -> {
				if (key.equalsIgnoreCase("application/json")) {
					body.add(new JsonBody(get$ref(value), description));
				} else if (key.equalsIgnoreCase("application/xml")) {
					body.add(new XmlBody(get$ref(value), description));
				} else if (key.equalsIgnoreCase("application/x-www-form-urlencoded")) {
					body.add(new UrlEncodedBody(get$ref(value), description));
				}
			});
		}
		return body;
	}

	private String get$ref(MediaType media) {
		return media.getSchema() != null ? media.getSchema().get$ref() : null;
	}

	private Map<String, List<Body>> getResponseBody(Operation operation) {

		Map<String, List<Body>> responseBody = new HashMap<String, List<Body>>();
		if (operation.getResponses() != null) {

			ApiResponses body = operation.getResponses();
			body.forEach((key, value) -> {
				responseBody.put(key, extractedBody(value.getContent(), value.getDescription()));
			});
		}
		return responseBody.isEmpty() ? null : responseBody;
	}
}
