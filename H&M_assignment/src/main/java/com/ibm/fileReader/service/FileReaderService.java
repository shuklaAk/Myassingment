package com.ibm.fileReader.service;

import java.io.IOException;
import java.util.List;

import com.ibm.fileReader.exception.FileReaderException;

import de.siegmar.fastcsv.reader.CsvContainer;

public interface FileReaderService {
	public List<String[]> checkHeaders(CsvContainer csv, CsvContainer csv1, String path)
			throws FileReaderException, IOException;

	public List<String[]> addingExtraRow(int count, CsvContainer csv, String string, List<String[]> list);

	public List<String[]> equalCheck(int rowCount, CsvContainer csv, CsvContainer csv1, List<String[]> list);
	
}
