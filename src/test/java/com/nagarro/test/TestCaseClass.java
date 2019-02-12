package com.nagarro.test;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import myProject.AsserUtils;

public class TestCaseClass {
	
	static LinkedHashMap<String,String> map=new LinkedHashMap<String,String>();
	
	@BeforeMethod
	public static void beforeMethod(Method method)
	{
		map.clear();
		 Test test = method.getAnnotation(Test.class);
		 String[] arrDesc=test.description().split("\\$\\$");
		 for(int i=0;i<arrDesc.length;i++)
		 {
			 String[] jiraFields = arrDesc[i].split(":");
			 
			 map.put(jiraFields[0],jiraFields[1]);
		}
		
	}
	
	@Test(description= "Priority:high$$sevirity:High$$Test:2")
	public void testcase1()
	{
		System.out.println(map);
		AsserUtils.assertEquals("12", "23", map);
	}
	@Test(description= "Priority:high$$sevirity:High$$test:1")
	public void testcase2()
	{
		System.out.println(map);
	}

}
