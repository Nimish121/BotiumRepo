/*package com.nagarro.driven.espo.data;

import java.lang.reflect.Method;
import java.nio.file.Paths;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;

import com.nagarro.driven.core.util.CSVReader;
import com.nagarro.driven.espo.data.config.DataConfigHolder;

public class TestDataProvider {

	private static final Logger LOG = LoggerFactory.getLogger(AccountsDataProvider.class);
	private static final String FILE_PATH = DataConfigHolder.getInstance().csvFilePath();
	private static String USERDIRECTORY = Paths.get(System.getProperty("user.dir")).getParent().toString();
	private static final String FILE_NAME = "CsvPomData.csv";
	private static final String DATA_FILE = USERDIRECTORY + FILE_PATH + FILE_NAME;

	@DataProvider(name = "TestData")
	public static Object[][] credentials(Method method) {
		Object[][] data = null;
		LOG.info("Reading the data from the file :{}", FILE_NAME);
		try {
			List<String[]> csvData = CSVReader.csvDataProvider(method.getName(),
					DataConfigHolder.getInstance().csvDelimiter(), DATA_FILE);
			data = csvData.toArray(new String[csvData.size() - 1][]);
		} catch (Exception e) {
			LOG.error("Unable to load data from file {} : {}", DATA_FILE, e.getMessage());
		}
		return data;
	}

}
*/