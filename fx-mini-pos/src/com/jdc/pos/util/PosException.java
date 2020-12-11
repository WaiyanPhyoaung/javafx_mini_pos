package com.jdc.pos.util;

public class PosException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PosException(String errorLog) {
		super(errorLog);
	}

}
