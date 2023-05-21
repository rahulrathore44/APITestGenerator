package com.open.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.Yaml;

import com.open.api.codegen.EndPointVisitor;
import com.open.api.codegen.visitors.RestAssuredVisitor;
import com.open.api.cofig.VisitorConfiguration;
import com.open.api.models.Endpoint;
import com.open.api.models.HttpMethod;
import com.open.api.models.body.IBody;
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
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.ParseOptions;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws FileNotFoundException {
		// String file = "C:\\Data\\log\\OpenApi\\casesvc.json";
		String file = "C:\\Data\\log\\OpenApi\\petstore.yaml";
		// String file = "C:\\Data\\log\\OpenApi\\oas.yml";

		// readWithSnakeYml(file);
		readWithSwaggerParser(file);

	}

	static void readWithSnakeYml(String file) throws FileNotFoundException {
		InputStream input = new FileInputStream(new File(file));
		Yaml yaml = new Yaml();
		Map<String, Object> obj = yaml.load(input);
		Map<String, Object> paths = (Map<String, Object>) obj.get("paths");
		System.out.println(paths);

	}

	static void readWithSwaggerParser(String file) {
		OpenAPIV3Parser apiParser = new OpenAPIV3Parser();
		ParseOptions parseOptions = new ParseOptions();
		parseOptions.setResolve(true); // implicit
		parseOptions.setResolveFully(true);

		OpenAPI openApi = apiParser.read(file, null, parseOptions);
		// OpenAPI openApi = result.getOpenAPI();
		Paths paths = openApi.getPaths();
		System.out.println("Paths " + paths);

		List<Endpoint> endpoints = new ArrayList<Endpoint>();

		paths.forEach((key, value) -> {
			endpoints.addAll(getEndPoint(key, value));

		});

		PathItem item = paths.get("/pet");
		System.out.println(endpoints);

		if (item.getPost().getRequestBody().getContent().get("application/json").getSchema() instanceof ArraySchema) {

		}

		VisitorConfiguration config = new VisitorConfiguration.Builder().setDelimiter("_")
				.setAbsolutePath("C:\\Data\\log\\restsharp").setPackage("com.api.automation").build();
		EndPointVisitor endPointVisitor = new RestAssuredVisitor(config);

		endpoints.forEach((endpoint) -> {
			endpoint.accept(endPointVisitor);
		});

	}

	static List<Endpoint> getEndPoint(String path, PathItem value) {
		List<Endpoint> endpoints = new ArrayList<Endpoint>();

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

	static List<Parameter> getParameter(Operation operation) {

		List<Parameter> parameters = new ArrayList<Parameter>();

		if (operation.getParameters() != null) {
			operation.getParameters().forEach((param) -> {
				if (param.getIn().equalsIgnoreCase("query")) {
					parameters.add(
							new QueryParameter(param.getDescription(), param.getName(), getType(param.getSchema())));
				} else if (param.getIn().equalsIgnoreCase("path")) {
					parameters.add(
							new PathParameter(param.getDescription(), param.getName(), getType(param.getSchema())));
				} else if (param.getIn().equalsIgnoreCase("header")) {
					parameters.add(
							new HeaderParameter(param.getDescription(), param.getName(), getType(param.getSchema())));
				}

			});
		}
		return parameters.isEmpty() ? null : parameters;

	}

	static String getType(Schema schema) {
		return schema != null ? schema.getType() : null;
	}

	static List<IBody> getRequestBody(Operation operation) {
		List<IBody> requestBody = new ArrayList<IBody>();

		if (operation.getRequestBody() != null) {
			RequestBody body = operation.getRequestBody();
			requestBody.addAll(extractedBody(body.getContent(), body.getDescription()));

		}

		return requestBody.isEmpty() ? null : requestBody;
	}

	private static List<IBody> extractedBody(Content content, String description) {
		List<IBody> body = new ArrayList<IBody>();
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

	static String get$ref(MediaType media) {
		return media.getSchema() != null ? media.getSchema().get$ref() : null;
	}

	static Map<String, List<IBody>> getResponseBody(Operation operation) {
		Map<String, List<IBody>> responseBody = new HashMap<String, List<IBody>>();
		if (operation.getResponses() != null) {
			ApiResponses body = operation.getResponses();
			body.forEach((key, value) -> {
				responseBody.put(key, extractedBody(value.getContent(), value.getDescription()));
			});
		}
		return responseBody.isEmpty() ? null : responseBody;
	}

	static Endpoint buildEndPoint(String path, HttpMethod method, Operation operation) {

		return operation != null
				? new Endpoint(path, operation.getDescription(), method, getParameter(operation),
						getRequestBody(operation), getResponseBody(operation))
				: null;
	}
}
