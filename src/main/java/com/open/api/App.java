package com.open.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;

import com.open.api.codegen.EndPoint;
import com.open.api.codegen.EndPointVisitor;
import com.open.api.codegen.visitors.RestAssuredVisitor;
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

	static void readWithSnakeYml(String file) throws FileNotFoundException {
		InputStream input = new FileInputStream(new File(file));
		Yaml yaml = new Yaml();
		Map<String, Object> obj = yaml.load(input);
		Map<String, Object> paths = (Map<String, Object>) obj.get("paths");
		System.out.println(paths);

	}

	static void readWithSwaggerParser(String file) {
		OpenAPIDocParser parser = new OpenAPIDocParser(file);
		List<EndPoint> endPoints = parser.getEndPoints();

		VisitorConfiguration config = new VisitorConfiguration.Builder().setDelimiter("_")
				.setAbsolutePath("C:\\Data\\log\\restsharp").setPackage("com.api.automation").build();
		EndPointVisitor endPointVisitor = new RestAssuredVisitor(config);

		endPoints.forEach((endpoint) -> {
			endpoint.accept(endPointVisitor);
		});

	}
}
