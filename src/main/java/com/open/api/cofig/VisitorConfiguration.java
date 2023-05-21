package com.open.api.cofig;

public abstract class VisitorConfiguration {

	protected String delimiter = "_";
	protected String absolutePath = System.getProperty("user.home");
	protected String packageName = "";

	public abstract String getDelimiter();

	public abstract String getAbsolutePath();

	public abstract String getPackage();

}
