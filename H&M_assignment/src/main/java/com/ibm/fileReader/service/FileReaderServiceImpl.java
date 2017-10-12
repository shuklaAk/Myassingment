package com.ibm.fileReader.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ibm.fileReader.exception.FileReaderException;
import com.ibm.fileReader.util.FileReaderUtil;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.writer.CsvWriter;

@Service
public class FileReaderServiceImpl implements FileReaderService {
	private static final String FLAG_A ="a";
	private static final String FLAG_B ="b";
	private static final String COMMONFLAG ="ab";
	
	public List<String[]> checkHeaders(CsvContainer csv, CsvContainer csv1,String path)
			throws FileReaderException, IOException {

		List<String[]> list = new ArrayList<>();

		List<String> firstHeader = csv.getRow(0).getFields();
		List<String> secondHeader = csv1.getRow(0).getFields();

		String str = FileReaderUtil.getString(firstHeader);
		String str1 = FileReaderUtil.getString(secondHeader);
		if (str.equals(str1)) {
			List<String> getHeader = FileReaderUtil.createRow(firstHeader, "_diff");
			String[] myArray = getHeader.toArray(new String[0]);
			list.add(myArray);
		} else {
			String fileName = new SimpleDateFormat("yyyyMMddHHmm'.csv'").format(new Date());
			String location=path+fileName;
			File createfile = new File(location);
			CsvWriter csvWriter = new CsvWriter();
			List<String[]> list1 = new ArrayList<>();
			csvWriter.write(createfile, StandardCharsets.UTF_8, list1);
			System.err.println("header not matched");
			throw new FileReaderException("header not matched");
			}
		return list;
	}

	public  List<String[]> addingExtraRow(int count, CsvContainer csv, String string,
			List<String[]> list) {
		for (int i = count; i < csv.getRowCount(); i++) {
			List<String> a = csv.getRow(i).getFields();

			List<String> getCsvData = FileReaderUtil.createRow(a, string);
			String[] myArray = getCsvData.toArray(new String[0]);
			list.add(myArray);
		}
		return list;
	}

	/**
	 * @param rowCount
	 * @param csv
	 * @param csv1
	 * @param list
	 * @return
	 */
	public List<String[]> equalCheck(int rowCount, CsvContainer csv, CsvContainer csv1, List<String[]> list) {

		for (int i = 1; i < rowCount; i++) {
			List<String> firstFileData = csv.getRow(i).getFields();
			List<String> secFileData = csv1.getRow(i).getFields();
			String s1 = FileReaderUtil.getString(firstFileData);
			String s2 = FileReaderUtil.getString(secFileData);
			if (s1.equals(s2)) {
				List<String> getABCsvData = FileReaderUtil.createRow(firstFileData, COMMONFLAG);
				String[] myArray = getABCsvData.toArray(new String[0]);
				list.add(myArray);

			} else {
				List<String> getACsvData = FileReaderUtil.createRow(firstFileData, FLAG_A);
				String[] myArrayA = getACsvData.toArray(new String[0]);
				list.add(myArrayA);
				List<String> getBCsvData = FileReaderUtil.createRow(secFileData, FLAG_B);
				String[] myArrayB = getBCsvData.toArray(new String[0]);
				list.add(myArrayB);
			}
		}
		return list;
	}
}
