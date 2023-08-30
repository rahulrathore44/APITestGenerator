package com.open.api.codegen.visitors;

import com.open.api.codegen.EndPoint;
import com.open.api.codegen.EndPointVisitor;
import com.open.api.codegen.HttpMethodProcessor;
import com.open.api.codegen.Processor;
import com.open.api.models.EndpointImpl;

public class KarateVisitor implements EndPointVisitor, Processor, HttpMethodProcessor {

	@Override
	public void visit(EndpointImpl endpoint) {

	}

	@Override
	public void processGet(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processDelete(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processPut(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processPost(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processPatch(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processOption(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processHead(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void processTrace(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preProcessor(EndPoint element) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postProcessor() {
		// TODO Auto-generated method stub
		
	}

}
