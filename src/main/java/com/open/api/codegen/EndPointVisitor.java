package com.open.api.codegen;

import com.open.api.models.EndpointImpl;

public interface EndPointVisitor {
	
	public void visit(EndpointImpl endpoint);

}

