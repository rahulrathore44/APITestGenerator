package com.open.api.codegen.visitors;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.open.api.codegen.EndPoint;
import com.open.api.codegen.EndPointVisitor;
import com.open.api.codegen.HttpMethodProcessor;
import com.open.api.codegen.Processor;
import com.open.api.cofig.VisitorConfiguration;
import com.open.api.models.EndpointImpl;
import com.open.api.models.HttpMethod;
import com.open.api.models.body.IBody;
import com.open.api.models.param.HeaderParameter;
import com.open.api.models.param.Parameter;
import com.open.api.models.param.PathParameter;
import com.open.api.models.param.QueryParameter;

public class RestAssuredVisitor implements EndPointVisitor, Processor, HttpMethodProcessor {

	private StringBuilder content;
	private String className = "";
	private VisitorConfiguration configuration;
	private String absolutePath = "";

	private static final Logger oLog = LoggerFactory.getLogger(RestAssuredVisitor.class);

	public RestAssuredVisitor(VisitorConfiguration configuration) {
		this.configuration = configuration;
		content = new StringBuilder();
	}

	@Override
	public void visit(EndpointImpl endpoint) {

		preProcessor(endpoint);

		switch (endpoint.getOperation()) {

		case get:
			processGet(endpoint);
			break;

		case put:
			processPut(endpoint);
			break;

		case post:
			processPost(endpoint);
			break;

		case delete:
			processDelete(endpoint);
			break;

		case patch:
			processPatch(endpoint);
			break;

		default:
			break;
		}

		postProcessor();

	}

	private String getTestClassName(EndpointImpl endpoint) {
		String name = endpoint.getContextPath().replace("{", "").replace("}", "").replace("/",
				configuration.getDelimiter());
		oLog.info("Test Class Name: " + name);
		return name;
	}

	private String getTestMethodName(String status, IBody request, IBody response) {
		StringBuilder name = new StringBuilder();
		name.append(className).append(configuration.getDelimiter()).append(status);

		if (request != null) {
			name.append(configuration.getDelimiter())
					.append(((request.getDescription() == null) ? "null" : request.getDescription().replace(" ", "")))
					.append(configuration.getDelimiter()).append(request.getType()
							.replace("/", configuration.getDelimiter()).replace("-", configuration.getDelimiter()));
		}

		if (response != null) {
			name.append(configuration.getDelimiter())
					.append(((response.getDescription() == null) ? "null" : response.getDescription().replace(" ", "")))
					.append(configuration.getDelimiter()).append(response.getType()
							.replace("/", configuration.getDelimiter()).replace("-", configuration.getDelimiter()));
		}

		oLog.info("Test Method Name: " + name.toString());
		return name.toString();
	}

	@Override
	public void preProcessor(EndPoint element) {
		EndpointImpl endpoint = (EndpointImpl) element;
		className = "Test" + getTestClassName(endpoint) + configuration.getDelimiter() + endpoint.getOperation().name();
		absolutePath = configuration.getAbsolutePath() + FileSystems.getDefault().getSeparator() + className + ".java";

	}

	@Override
	public void processGet(EndPoint element) {
		EndpointImpl endpoint = (EndpointImpl) element;
		process(endpoint);

	}

	@Override
	public void processDelete(EndPoint element) {
		EndpointImpl endpoint = (EndpointImpl) element;
		process(endpoint);
	}

	@Override
	public void processPut(EndPoint element) {
		EndpointImpl endpoint = (EndpointImpl) element;
		process(endpoint);
	}

	@Override
	public void processPost(EndPoint element) {
		EndpointImpl endpoint = (EndpointImpl) element;
		process(endpoint);
	}

	@Override
	public void processPatch(EndPoint element) {
		EndpointImpl endpoint = (EndpointImpl) element;
		process(endpoint);
	}

	private void process(EndpointImpl endpoint) {
		createClass(endpoint);

		switch (endpoint.getOperation()) {

		case get:
		case delete:
			endpoint.getResponses().forEach((status, body) -> {
				createTestMethod(endpoint.getContextPath(), endpoint.getParameter(), endpoint.getDescription(),
						endpoint.getOperation(), status, null, body);
			});
			break;

		case put:
		case post:
		case patch:
			if (endpoint.getRequestBody() != null) {
				endpoint.getRequestBody().forEach((requestBody) -> {
					endpoint.getResponses().forEach((status, responseBody) -> {
						createTestMethod(endpoint.getContextPath(), endpoint.getParameter(), endpoint.getDescription(),
								endpoint.getOperation(), status, requestBody, responseBody);
					});
				});

			} else if (endpoint.getResponses() != null) {
				endpoint.getResponses().forEach((status, body) -> {
					createTestMethod(endpoint.getContextPath(), endpoint.getParameter(), endpoint.getDescription(),
							endpoint.getOperation(), status, null, body);
				});
			}

		default:
			break;
		}

		closeClass();
	}

	private void createTestMethod(String contextPath, List<Parameter> parameters, String description,
			HttpMethod operation, String status, IBody requestBody, List<IBody> responses) {

		if (!responses.isEmpty()) {
			responses.forEach((response) -> {
				methodContent(contextPath, parameters, description, operation, status, requestBody, response);
			});
		} else {
			methodContent(contextPath, parameters, description, operation, status, requestBody, null);
		}

	}

	private void methodContent(String contextPath, List<Parameter> parameters, String description, HttpMethod operation,
			String status, IBody request, IBody response) {
		content.append(System.lineSeparator());
		content.append("/** " + description + " **/" + System.lineSeparator());
		String testMethodName = "@Test public void " + getTestMethodName(status, request, response)
				+ "() throws URISyntaxException {";
		content.append(testMethodName + System.lineSeparator());
		content.append("given()" + System.lineSeparator());
		addParameters(parameters);

		switch (operation) {
		
		case get:
			addAcceptHeader(response);
			content.append(".when().get(new URI(\"" + contextPath + "\"))" + System.lineSeparator());
			break;

		case put:
			addAcceptHeader(response);
			addContentTypeHeader(request);
			content.append(".when().put(new URI(\"" + contextPath + "\"))" + System.lineSeparator());
			break;

		case post:
			addAcceptHeader(response);
			addContentTypeHeader(request);
			content.append(".when().post(new URI(\"" + contextPath + "\"))" + System.lineSeparator());
			break;

		case delete:
			addAcceptHeader(response);
			content.append(".when().delete(new URI(\"" + contextPath + "\"))" + System.lineSeparator());
			break;

		case patch:
			addAcceptHeader(response);
			addContentTypeHeader(request);
			content.append(".when().patch(new URI(\"" + contextPath + "\"))" + System.lineSeparator());
			break;

		default:
			break;
		}

		addResponseStatusCode(status);
		content.append("}" + System.lineSeparator());
	}

	private void createClass(EndpointImpl endpoint) {

		content.append(String.format("package %s;", configuration.getPackage()) + System.lineSeparator())
				.append("import static io.restassured.RestAssured.given;" + System.lineSeparator())
				.append("import java.net.URI;" + System.lineSeparator())
				.append("import java.net.URISyntaxException;" + System.lineSeparator())
				.append("import org.junit.jupiter.api.Test;" + System.lineSeparator()).append(System.lineSeparator())
				.append(System.lineSeparator()).append("public class " + className + " {" + System.lineSeparator());
	}

	private void addParameters(List<Parameter> parameters) {
		
		if (parameters != null) {
			parameters.forEach((param) -> {
				if (param instanceof HeaderParameter) {
					content.append(".header(\"" + param.getName() + "\"," + "\"" + "\")" + System.lineSeparator());
				}
				if (param instanceof QueryParameter) {
					content.append(".queryParams(\"" + param.getName() + "\"," + "\"" + "\")" + System.lineSeparator());
				}
				if (param instanceof PathParameter) {
					content.append(".pathParam(\"" + param.getName() + "\"," + "\"" + "\")" + System.lineSeparator());
				}
			});
		}
	}

	private void addAcceptHeader(IBody response) {
		if (response != null)
			content.append(".accept(\"" + response.getType() + "\")" + System.lineSeparator());
	}

	private void addContentTypeHeader(IBody request) {
		if (request != null)
			content.append(".contentType(\"" + request.getType() + "\").body(\"\")" + System.lineSeparator());
	}

	private void addResponseStatusCode(String statusInString) {
		Integer status = 10;
		try {
			status = Integer.valueOf(statusInString);
		} catch (NumberFormatException e) {
			oLog.warn(e.getMessage() + " returning the default status code 200");
			status = 200;
		}
		content.append(".then().assertThat().statusCode(" + status + ");" + System.lineSeparator());
	}

	private void closeClass() {
		content.append("}" + System.lineSeparator());
	}

	@Override
	public void postProcessor() {
		try {
			FileUtils.writeStringToFile(new File(absolutePath), content.toString(), Charset.defaultCharset());
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		content.setLength(0);
		className = "";
		absolutePath = configuration.getAbsolutePath();
	}

	@Override
	public void processOption(EndPoint element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processHead(EndPoint element) {
		// TODO Auto-generated method stub

	}

	@Override
	public void processTrace(EndPoint element) {
		// TODO Auto-generated method stub

	}

}
