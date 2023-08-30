package com.open.api.cofig;

import java.util.Objects;

public class KarateVisitorConfig extends VisitorConfiguration {

	private KarateVisitorConfig(String delimiter, String absolutePath) {
		this.packageName = null;
		this.delimiter = delimiter;
		this.absolutePath = absolutePath;
	}

	@Override
	public String getDelimiter() {
		return this.delimiter;
	}

	@Override
	public String getAbsolutePath() {
		return this.absolutePath;
	}

	@Override
	public String getPackage() {
		throw new UnsupportedOperationException("Package Name is not Supported");
	}

	public static class Builder {

		private String delimiter;
		private String absolutePath;

		public Builder setDelimiter(String delimiter) {
			this.delimiter = delimiter;
			return this;
		}

		public Builder setAbsolutePath(String absolutePath) {
			this.absolutePath = absolutePath;
			return this;
		}

		public VisitorConfiguration build() {
			Objects.requireNonNull(this.delimiter);
			Objects.requireNonNull(this.absolutePath);
			return new KarateVisitorConfig(this.delimiter, this.absolutePath);
		}
	}

	public static VisitorConfiguration getDefault() {
		return new KarateVisitorConfig("_", System.getProperty("user.home"));

	}

}
