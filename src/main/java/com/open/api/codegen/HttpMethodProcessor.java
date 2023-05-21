package com.open.api.codegen;

public interface HttpMethodProcessor {
	
	public void processGet(EndPoint element);

	public void processDelete(EndPoint element);

	public void processPut(EndPoint element);

	public void processPost(EndPoint element);

	public void processPatch(EndPoint element);
	
	public void processOption(EndPoint element);
	
	public void processHead(EndPoint element);
	
	public void processTrace(EndPoint element);

}
