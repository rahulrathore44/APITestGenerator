package com.open.api.codegen;

public interface Processor {

	public void preProcessor(EndPoint element);

	public void postProcessor();

}
