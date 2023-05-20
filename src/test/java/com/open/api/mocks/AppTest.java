package com.open.api.mocks;

import static io.restassured.RestAssured.given;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

/**
 * Unit test for simple App.
 */
public class AppTest {
	/**
	 * Rigorous Test :-)
	 * 
	 * @throws URISyntaxException
	 */
	@Test
	public void shouldAnswerWithTrue() throws URISyntaxException {

		given().header("", "").queryParams("", "", "").pathParam("", "").accept("application/json").contentType("application/json").body("").when().post().then()
				.assertThat().statusCode(HttpStatus.SC_OK);
	}
}
