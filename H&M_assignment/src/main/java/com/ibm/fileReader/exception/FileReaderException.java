package com.ibm.fileReader.exception;

public class FileReaderException extends Exception{
private static final long serialVersionUID = 1L;
	
	public FileReaderException(String msg) {
		super(msg);
	}
	
	public FileReaderException(Throwable throwable) {
		super(throwable);
	}
	
	public FileReaderException(String msg,Throwable throwable) {
		super(msg,throwable);
	}
}
