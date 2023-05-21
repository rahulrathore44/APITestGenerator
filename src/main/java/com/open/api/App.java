package com.open.api;

import java.io.FileNotFoundException;
import java.util.List;

import com.open.api.codegen.EndPoint;
import com.open.api.codegen.EndPointVisitor;
import com.open.api.codegen.visitors.RestAssuredVisitor;
import com.open.api.cofig.RestAssuredVisitorConfig;
import com.open.api.cofig.VisitorConfiguration;
import com.open.api.parser.OpenAPIDocParser;

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

	static void readWithSwaggerParser(String file) {
		OpenAPIDocParser parser = new OpenAPIDocParser(file);
		List<EndPoint> endPoints = parser.getEndPoints();

		VisitorConfiguration config = new RestAssuredVisitorConfig.Builder().setDelimiter("_")
				.setAbsolutePath("C:\\Data\\log\\restsharp").setPackage("com.api.automation").build();

		EndPointVisitor endPointVisitor = new RestAssuredVisitor(config);

		endPoints.forEach((endpoint) -> {
			endpoint.accept(endPointVisitor);
		});

	}
}
