package com.open.api.mocks.codegen;

public interface HttpMethodProcessor {
	
	public void processGet(Element element);

	public void processDelete(Element element);

	public void processPut(Element element);

	public void processPost(Element element);

	public void processPatch(Element element);
	
	public void processOption(Element element);
	
	public void processHead(Element element);
	
	public void processTrace(Element element);

}
