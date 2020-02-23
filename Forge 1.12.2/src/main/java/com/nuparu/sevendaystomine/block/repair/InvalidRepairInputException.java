package com.nuparu.sevendaystomine.block.repair;

import java.lang.Exception;
public class InvalidRepairInputException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidRepairInputException() {
	}

	public InvalidRepairInputException(String message) {
		super(message);
	}
}