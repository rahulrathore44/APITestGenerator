package com.open.api.codegen;

import com.open.api.models.Endpoint;

public interface EndPointVisitor {
	
	public void visit(Endpoint endpoint);

}

