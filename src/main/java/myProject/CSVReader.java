/*package com.nagarro.driven.core.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.nagarro.driven.core.util.AutomationFrameworkException;

public class CSVReader {
	private static final Logger log = LoggerFactory.getLogger(CSVReader.class);

	public static List<String[]> csvDataProvider(String testname, String separator, String filename)
			throws AutomationFrameworkException {
		log.info(" : CSVDataProvider Method Called");
		List<String[]> dataArr = new ArrayList<String[]>();
		BufferedReader br = null;
		String[] values = null;
		String line = StringUtils.EMPTY;
		List<String[]> strArray = null;
		try {
			File file = new File(filename);
			br = new BufferedReader(new FileReader(file));
			while (null != (line = br.readLine())) {
				if ((testname.equals(line.substring(0, line.indexOf(separator)))) || testname.isEmpty()) {
					if (!testname.isEmpty()) {
						line = line.substring(line.indexOf(separator) + 1, line.length());
					}
					values = line.split(separator);
					dataArr.add(values);
					strArray = dataArr;
				}
			}
		} catch (FileNotFoundException ex) {
			throw new AutomationFrameworkException("Cannot find the CSV file at " + filename);
		} catch (IOException ex) {
			log.error("Exception occurred while reading the csv for data : {}", ex.getMessage());
		} finally {
			if (null != br) {
				try {
					br.close();
				} catch (IOException ex) {
					log.error(ex.getMessage());
				}
			}
		}
		if (null == strArray) {
			throw new AutomationFrameworkException("Something wrong in either CSV file or parameters are sent wrong.");
		}
		return strArray;
	}
}
*/