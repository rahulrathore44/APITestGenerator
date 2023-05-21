package com.open.api.cofig;

import java.util.Objects;

public class VisitorConfiguration {

	private String delimiter = "_";
	private String absolutePath = System.getProperty("user.home");
	private String packageName = "";

	private VisitorConfiguration(String packageName, String delimiter, String absolutePath) {
		this.packageName = packageName;
		this.delimiter = delimiter;
		this.absolutePath = absolutePath;
	}

	public String getDelimiter() {
		return delimiter;
	}

	public String getAbsolutePath() {
		return absolutePath;
	}

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
			return new VisitorConfiguration(this.packageName, this.delimiter, this.absolutePath);
		}
	}

	public static VisitorConfiguration getDefault() {
		return new VisitorConfiguration("com.api.automation", "_", System.getProperty("user.home"));

	}

}
