package com.ibm.util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ibm.constant.FileReaderConstant;
public class FileReaderUtil {
	
	public static String getFileHeader(List<String> list, String value) {
		String str = value;
		for (String s : list) {
			str = str + FileReaderConstant.COMMA_DELIMITER + s;
		}
		return str;
	}

	public static String getString(List<String> list) {
		return list.stream().map(e -> e.toString()).map(e -> e.replaceAll("\"", "")).map(e -> e.trim()).reduce("", String::concat);
	}

	public static List<List<String>> getData(String fileName) throws IOException {
		Stream<String> lines = Files.lines(Paths.get(fileName));
		return lines.map(line -> Arrays.asList(line.split(FileReaderConstant.COMMA_DELIMITER))).collect(Collectors.toList());
	}
	
	public static List<String> createRow(List<String> list, String value) {

		List<String> obj = new ArrayList<>();
		obj.add(value);
		for (String s : list) {
			obj.add(s.replaceAll("\"", "").trim());
		}
		return obj;
	}


}
