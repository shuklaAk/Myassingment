package com.ibm.fileReader;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ibm.exception.FileReaderException;
import com.ibm.util.FileReaderUtil;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.writer.CsvWriter;

/**
 * @author AkankshaShukla
 *
 */
public class FileReader {

	private static final String flagA ="a";
	private static final String flagB ="b";
	private static final String commonFlag ="ab";
	
	public static void main(String[] args) throws IOException, FileReaderException {

		String fileName = args[0];
		String fileName2 = args[1];
		String createCsvFile = args[2];

		File file = new File(fileName);
		CsvReader csvReader = new CsvReader();
		CsvContainer csv = csvReader.read(file, StandardCharsets.UTF_8);

		File file1 = new File(fileName2);
		CsvReader csvReader1 = new CsvReader();
		CsvContainer csv1 = csvReader1.read(file1, StandardCharsets.UTF_8);
		List<String[]> list = null;

		list = checkHeaders(csv, csv1,createCsvFile);

		if (csv.getRowCount() == csv1.getRowCount()) {
			list = equalCheck(csv.getRowCount(), csv, csv1, list);

		} else if (csv.getRowCount() > csv1.getRowCount()) {
			int count = csv1.getRowCount();
			list = equalCheck(csv1.getRowCount(), csv, csv1, list);
			list = addingExtraRow(count, csv, flagA, list);

		} else {
			int count = csv.getRowCount();
			list = equalCheck(csv.getRowCount(), csv, csv1, list);
			list = addingExtraRow(count, csv1, flagB, list);

		}
		String fileName3 = new SimpleDateFormat("yyyyMMddHHmm'.csv'").format(new Date());
		File generateFile = new File(createCsvFile+fileName3 );
		CsvWriter csvWriter = new CsvWriter();
		csvWriter.write(generateFile, StandardCharsets.UTF_8, list);
	}

	/**
	 * @param csv
	 * @param csv1
	 * @return
	 * @throws FileReaderException
	 * @throws IOException 
	 */
	private static List<String[]> checkHeaders(CsvContainer csv, CsvContainer csv1,String path)
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
			System.exit(0);	}
		return list;
	}

	private static List<String[]> addingExtraRow(int count, CsvContainer csv, String string,
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
	private static List<String[]> equalCheck(int rowCount, CsvContainer csv, CsvContainer csv1, List<String[]> list) {

		for (int i = 1; i < rowCount; i++) {
			List<String> firstFileData = csv.getRow(i).getFields();
			List<String> secFileData = csv1.getRow(i).getFields();
			String s1 = FileReaderUtil.getString(firstFileData);
			String s2 = FileReaderUtil.getString(secFileData);
			if (s1.equals(s2)) {
				List<String> getABCsvData = FileReaderUtil.createRow(firstFileData, commonFlag);
				String[] myArray = getABCsvData.toArray(new String[0]);
				list.add(myArray);

			} else {
				List<String> getACsvData = FileReaderUtil.createRow(firstFileData, flagA);
				String[] myArrayA = getACsvData.toArray(new String[0]);
				list.add(myArrayA);
				List<String> getBCsvData = FileReaderUtil.createRow(secFileData, flagB);
				String[] myArrayB = getBCsvData.toArray(new String[0]);
				list.add(myArrayB);
			}
		}
		return list;
	}
}
