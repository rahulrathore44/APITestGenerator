package com.open.api.codegen;

public interface Processor {

	public void preProcessor(Element element);

	public void postProcessor();

}
