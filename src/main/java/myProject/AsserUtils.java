package myProject;

import java.util.LinkedHashMap;

import org.testng.Assert;

public class AsserUtils extends Assert {

	public static void assertEquals(String actual, String expected,
			LinkedHashMap<String, String> map) {
		
		try
		{
		Assert.assertEquals(actual, expected);
		}
		catch(AssertionError e)
		{
			JiraUtil.createIssue(map);
			throw e;
		}
		
		
	}


}
