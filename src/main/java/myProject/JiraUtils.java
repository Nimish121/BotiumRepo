package myProject;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import net.rcarz.jiraclient.BasicCredentials;
import net.rcarz.jiraclient.Field;
import net.rcarz.jiraclient.Issue;
import net.rcarz.jiraclient.Issue.FluentCreate;
import net.rcarz.jiraclient.Issue.SearchResult;
import net.rcarz.jiraclient.JiraClient;
import net.rcarz.jiraclient.JiraException;
import net.rcarz.jiraclient.Project;
import net.rcarz.jiraclient.Status;

public class JiraUtils {
	static Properties prop = new Properties();
	private static JiraClient jiraClient = null;

	static {
		FileInputStream file = null;
		try {
			file = new FileInputStream(new File(
					"D:\\backup\\workspace\\myProject\\src\\main\\resources\\properties"));
			prop.load(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	public static JiraClient loginToJira() {
		String jiraUrl = prop.getProperty("jiraUrl").trim();
		String jiraUserName = prop.getProperty("jiraUserName");
		String jiraPassword = prop.getProperty("jiraPassword");

		BasicCredentials creds = new BasicCredentials(jiraUserName,
				jiraPassword);

		try {
			jiraClient = new JiraClient(jiraUrl, creds);
		} catch (JiraException e) {
			e.printStackTrace();
		}
		return jiraClient;
	}

	public static SearchResult getIssueBySummary(JiraClient jiraClient,
			String summary, String projectKey) throws JiraException {
		String query = "project =" + projectKey + " AND summary ~ " + "\""
				+ summary + "\"";
		System.out.println(query);
		SearchResult issue = jiraClient.searchIssues(query);
		return issue;
	}

	public static void updateIssueState(JiraClient jiraClient, String summary,
			String projectKey) {
		Status issueStatus = null;
		SearchResult issue = null;
		try {
			issue = getIssueBySummary(jiraClient, summary, projectKey);
			Iterator<Issue> itr = issue.iterator();
			while (itr.hasNext()) {
				String issueKey = itr.next().getKey();
				System.out.println(issueKey);
				issueStatus = jiraClient.getIssue(issueKey).getStatus();
				if (issueStatus.toString().equalsIgnoreCase("CLOSED"))
					jiraClient.getIssue(issueKey).transition()
							.execute("Reopen Issue");
			}
		} catch (JiraException e) {
			e.printStackTrace();
		}

	}

	public static boolean checkProjectExist(JiraClient jiraClient,
			String projectName) {

		List<Project> projets = new ArrayList<Project>();
		try {
			projets = jiraClient.getProjects();
		} catch (JiraException e) {
			e.printStackTrace();
		}
		boolean flag = false;
		for (Project p : projets) {
			if (p.getName().equalsIgnoreCase(projectName)) {
				flag = true;
				break;
			}
		}
		return flag;

	}

	public static boolean checkIssueExist(JiraClient jiraClient, String summary,
			String projectKey)

	{
		int issueCount = 0;
		SearchResult issue = null;
		try {
			issue = getIssueBySummary(jiraClient, summary, projectKey);;
			issueCount = issue.total;
		} catch (JiraException e) {
			e.printStackTrace();
		}
		if (issueCount > 0) {
			return true;
		}
		return false;

	}

	@SuppressWarnings({"unchecked", "rawtypes"})
	public static void createIssue(LinkedHashMap<String,String> map) {

		String projectKey = prop.getProperty("projectKey");
		String issueType = prop.getProperty("issueType");

		/*LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();
		map.put("summary", "create a dummy issue for testing purpose Reopened");
		map.put("priority", "1");
		map.put("DefectCause", "123");
		map.put("Severity", "123");
		map.put("DefectedPhase", "123");
		map.put("InjectedPhase", "123");*/

		@SuppressWarnings("unused")
		FluentCreate field;

		/* Login to JIRA */
		jiraClient = loginToJira();
		/* Check if Issue Exist */
		boolean issueExist = checkIssueExist(jiraClient, map.get("summary"),
				projectKey);
		if (issueExist) {
			/* Check the status of issue if closed then reopen the issue */
			updateIssueState(jiraClient, map.get("summary"), projectKey);

		} else {

			try {
				FluentCreate issue = jiraClient.createIssue(projectKey,
						issueType);
				Set<String> keys = map.keySet();
				for (String key : keys) {
					String caseValue = key.toUpperCase();
					switch (caseValue) {
						case "SUMMARY" :
							field = issue.field(Field.SUMMARY, map.get(key));
							break;
						case "PRIORITY" :
							field = issue.field(Field.PRIORITY,
									Field.valueById(map.get(key)));
							break;
						case "ASSIGNEE" :
							field = issue.field(Field.ASSIGNEE, map.get(key));
							break;
						case "SEVERITY" :
							field = issue.field(prop.getProperty("severity"),
									new ArrayList() {
										{
											add(map.get(key));
										}
									});
							break;
						case "DEFECTCAUSE" :
							field = issue.field(prop.getProperty("defectCause"),
									new ArrayList() {
										{
											add(map.get(key));
										}
									});
							break;
						case "DEFECTEDPHASE" :
							field = issue.field(
									prop.getProperty("defectedPhase"),
									new ArrayList() {
										{
											add(map.get(key));
										}
									});
							break;
						case "INJECTEDPHASE" :
							field = issue.field(
									prop.getProperty("injectedPhase"),
									new ArrayList() {
										{
											add(map.get(key));
										}
									});
							break;
						default :
							System.out.println("Invalid property");
							break;
					}

				}
				issue.execute().addAttachment(new File(
						"C:\\Users\\nimishkumar\\Desktop\\images.jfif"));
			} catch (JiraException e) {
				e.printStackTrace();
			}
		}

	}

}
