package com.stefano.osmexception;

import java.util.*;

class OsmException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<Exception> exceptions;

	public OsmException(ArrayList<Exception> exs) {
		exceptions = exs;
	}

	public ArrayList<Exception> getExceptions() {
		return exceptions;
	}
}
