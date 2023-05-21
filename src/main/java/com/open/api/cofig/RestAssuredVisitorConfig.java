package com.open.api.cofig;

import java.util.Objects;

public class RestAssuredVisitorConfig extends VisitorConfiguration {

	private RestAssuredVisitorConfig(String packageName, String delimiter, String absolutePath) {
		this.packageName = packageName;
		this.delimiter = delimiter;
		this.absolutePath = absolutePath;
	}

	@Override
	public String getDelimiter() {
		return delimiter;
	}

	@Override
	public String getAbsolutePath() {
		return absolutePath;
	}

	@Override
	public String getPackage() {
		return packageName;
	}

	public static class Builder {

		private String delimiter;
		private String absolutePath;
		private String packageName;

		public Builder setDelimiter(String delimiter) {
			this.delimiter = delimiter;
			return this;
		}

		public Builder setAbsolutePath(String absolutePath) {
			this.absolutePath = absolutePath;
			return this;
		}

		public Builder setPackage(String packageName) {
			this.packageName = packageName;
			return this;
		}

		public VisitorConfiguration build() {
			Objects.requireNonNull(this.delimiter);
			Objects.requireNonNull(this.absolutePath);
			Objects.requireNonNull(this.packageName);
			return new RestAssuredVisitorConfig(this.packageName, this.delimiter, this.absolutePath);
		}
	}

	public static VisitorConfiguration getDefault() {
		return new RestAssuredVisitorConfig("com.api.automation", "_", System.getProperty("user.home"));

	}

}
