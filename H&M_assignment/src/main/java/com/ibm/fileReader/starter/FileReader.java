package com.ibm.fileReader.starter;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.ibm.fileReader.exception.FileReaderException;
import com.ibm.fileReader.service.FileReaderService;
import com.ibm.fileReader.service.FileReaderServiceImpl;
import com.ibm.fileReader.util.FileReaderUtil;

import de.siegmar.fastcsv.reader.CsvContainer;
import de.siegmar.fastcsv.reader.CsvReader;
import de.siegmar.fastcsv.writer.CsvWriter;

/**
 * @author AkankshaShukla
 *
 */
public class FileReader {

	private static final String FLAG_A ="a";
	private static final String FLAG_B ="b";
	
	FileReaderService fileReaderService ;
	
	public static void main(String[] args) throws IOException, FileReaderException {
		
		if(args.length !=3) {
			throw new FileReaderException("please input valid parameter");
		}
		String fileName = args[0];
		String fileName2 = args[1];
		String createCsvFile = args[2];
		new FileReader().csvReadWrite(fileName,fileName2,createCsvFile);
		System.out.println("created");
	}
	
	   public void csvReadWrite(String fileName,String fileName2,String createCsvFile) throws IOException, FileReaderException {
		   
		fileReaderService =new FileReaderServiceImpl();
		File file = new File(fileName);
		CsvReader csvReader = new CsvReader();
		CsvContainer csv = csvReader.read(file, StandardCharsets.UTF_8);

		File file1 = new File(fileName2);
		CsvReader csvReader1 = new CsvReader();
		CsvContainer csv1 = csvReader1.read(file1, StandardCharsets.UTF_8);
		List<String[]> list;
		
		list = fileReaderService.checkHeaders(csv, csv1,createCsvFile);
		
		
		if (csv.getRowCount() == csv1.getRowCount()) {
			list = fileReaderService.equalCheck(csv.getRowCount(), csv, csv1, list);

		} else if (csv.getRowCount() > csv1.getRowCount()) {
			int count = csv1.getRowCount();
			list = fileReaderService.equalCheck(csv1.getRowCount(), csv, csv1, list);
			list = fileReaderService.addingExtraRow(count, csv, FLAG_A, list);

		} else {
			int count = csv.getRowCount();
			list = fileReaderService.equalCheck(csv.getRowCount(), csv, csv1, list);
			list = fileReaderService.addingExtraRow(count, csv1, FLAG_B, list);

		}
		String fileName3 = new SimpleDateFormat("yyyyMMddHHmm'.csv'").format(new Date());
		File generateFile = new File(createCsvFile+fileName3 );
		CsvWriter csvWriter = new CsvWriter();
		csvWriter.write(generateFile, StandardCharsets.UTF_8, list);
	}
}
