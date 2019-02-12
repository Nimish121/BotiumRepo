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

public class JiraUtil {
    private static final String SNAPSHOTDIR = "C:\\Users\\nimishkumar\\Desktop\\images.jfif";
    private static final String SEVERITY_CONST = "severity";
    private static final String DEFECT_CAUSE_CONST = "defectCause";
    private static final String DEFECT_CAUSE_VALUE_CONST = "DefectCause";
    private static final String ASSIGNEE_CONST = "Assignee";
    private static final String ISSUE_TYPE_CONST = "issueType";
    private static final String PROJECT_KEY_CONST = "projectKey";
    private static final String BLANK_CONST = "";
    private static final String REOPEN_ISSUE_CONST = "Reopen Issue";
    private static final String CLOSED_CONST = "CLOSED";
    private static final String JIRA_PASSWORD_CONST = "jiraPassword";
    private static final String JIRA_USER_NAME_CONST = "jiraUserName";
    private static final String JIRA_URL_CONST = "jiraUrl";
    private static final String PROP_PATH_CONST = "D:\\backup\\workspace\\myProject\\src\\main\\resources\\properties";
    private static final Properties prop = new Properties();
    private static final String DEFECT_PHASE_VALUE_CONST = null;
    private static final String DEFECT_PHASE_CONST = null;
    private static final String INJECTED_PHASE_CONST = null;
    private static final String INJECTED_PHASE_VALUE_CONST = null;
    private static JiraClient jiraClient = null;
    private static String jiraUrl = null;
    private static String jiraUserName = null;
    private static String jiraPassword = null;
    private static String projectKey = null;
    private static String issueType = null;
    private static JiraUtil obj = new JiraUtil();

    static {
	FileInputStream file = null;
	try {
	    file = new FileInputStream(new File(PROP_PATH_CONST));
	    prop.load(file);
	    loadParameters();
	} catch (IOException ioexp) {
	    ioexp.printStackTrace();
	}

    }

    private JiraClient loginToJira() {
	loadParameters();
	BasicCredentials creds = isParameterNotNull() ? new BasicCredentials(jiraUserName, jiraPassword) : null;
	return createJiraClient(creds);
    }

    private JiraClient createJiraClient(BasicCredentials creds) {
	try {
	    jiraClient = creds != null ? new JiraClient(jiraUrl, creds) : null;
	} catch (JiraException jiraexp) {
	    jiraexp.printStackTrace();
	}
	return jiraClient;
    }

    private boolean isParameterNotNull() {
	return jiraUrl != null && jiraUserName != null && jiraPassword != null;
    }

    private static void loadParameters() {
	jiraUrl = prop.getProperty(JIRA_URL_CONST).trim();
	jiraUserName = prop.getProperty(JIRA_USER_NAME_CONST);
	jiraPassword = prop.getProperty(JIRA_PASSWORD_CONST);
	projectKey = prop.getProperty(PROJECT_KEY_CONST);
	issueType = prop.getProperty(ISSUE_TYPE_CONST);
    }

    private static SearchResult getIssueBySummary(JiraClient jiraClient, String summary, String projectKey)
	    throws JiraException {
	String query = createSearchQuery(summary, projectKey);
	System.out.println(query);
	SearchResult issue = jiraClient != null ? jiraClient.searchIssues(query) : null;
	return issue;
    }

    private static String createSearchQuery(String summary, String projectKey) {
	String query = "project =" + projectKey + " AND summary ~ " + "\"" + summary + "\"";
	return query;
    }

    public static void updateIssueState(JiraClient jiraClient, String summary, String projectKey) {
	SearchResult issue = null;
	try {
	    issue = getIssueBySummary(jiraClient, summary, projectKey);
	    if (issue != null) {
		iterateAndChangeStatus(jiraClient, issue);
	    }
	} catch (JiraException e) {
	    e.printStackTrace();
	}

    }

    private static void iterateAndChangeStatus(JiraClient jiraClient, SearchResult issue) throws JiraException {
	Status issueStatus;
	Iterator<Issue> itr = issue.iterator();
	while (itr.hasNext()) {
	    String issueKey = itr.next() != null ? itr.next().getKey() : null;
	    System.out.println(issueKey);
	    issueStatus = checkKeyIsNotNull(jiraClient, issueKey);
	    compareAndUpdate(jiraClient, issueStatus, issueKey);
	}
    }

    private static void compareAndUpdate(JiraClient jiraClient, Status issueStatus, String issueKey)
	    throws JiraException {
	if (issueStatus != null && CLOSED_CONST.equalsIgnoreCase(issueStatus.toString())) {
	    jiraClient.getIssue(issueKey).transition().execute(REOPEN_ISSUE_CONST);
	}
    }

    private static Status checkKeyIsNotNull(JiraClient jiraClient, String issueKey) throws JiraException {
	Status issueStatus;
	issueStatus = issueKey != null && issueKey.trim().equalsIgnoreCase(BLANK_CONST)
		? jiraClient.getIssue(issueKey).getStatus()
		: null;
	return issueStatus;
    }

    public static boolean checkProjectExist(JiraClient jiraClient, String projectName) {

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

    private boolean checkIssueExist(JiraClient jiraClient, String summary, String projectKey)

    {
	boolean result = false;
	try {
	    SearchResult issue = checkValuesNotNull(jiraClient, summary, projectKey)
		    ? getIssueBySummary(jiraClient, summary, projectKey)
		    : null;
	    int issueCount = issue != null ? issue.total : 0;
	    result = issueCount > 0 ? true : false;
	} catch (JiraException e) {
	    e.printStackTrace();
	}

	return result;

    }

    private static boolean checkValuesNotNull(JiraClient jiraClient, String summary, String projectKey) {
	return jiraClient != null && summary != null && summary.trim().equalsIgnoreCase(BLANK_CONST)
		&& projectKey != null && projectKey.trim().equalsIgnoreCase(BLANK_CONST);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public static void createIssue(LinkedHashMap<String, String> map) {
	@SuppressWarnings("unused")
	FluentCreate field;

	/* Login to JIRA */
	jiraClient = obj.loginToJira();
	/* Check if Issue Exist */
	boolean issueExist = obj.checkIssueExist(jiraClient, map.get("summary"), projectKey);
	if (issueExist) {
	    /* Check the status of issue if closed then reopen the issue */
	    updateIssueState(jiraClient, map.get("summary"), projectKey);

	} else {

	    try {
		FluentCreate issue = jiraClient.createIssue(projectKey, issueType);
		Set<String> keys = map.keySet();
		for (String key : keys) {
		    String caseValue = key.toUpperCase();
		    switch (caseValue) {
		    case "SUMMARY":
			field = issue.field(Field.SUMMARY, map.get(key));
			break;
		    case "PRIORITY":
			field = issue.field(Field.PRIORITY, Field.valueById(map.get(key)));
			break;
		    case "DESCRIPTION":
			field = issue.field(Field.DESCRIPTION, Field.valueById(map.get(key)));
			break;
		    case "SEVERITY":
			field = issue.field(prop.getProperty(SEVERITY_CONST), new ArrayList() {
			    {
				add(map.get(key));
			    }
			});
			break;
		    default:
			System.out.println("Invalid property");
			break;
		    }

		}
		field = issue.field(Field.ASSIGNEE, ASSIGNEE_CONST);
		field = issue.field(prop.getProperty(DEFECT_CAUSE_CONST), new ArrayList() {{
			add(DEFECT_CAUSE_VALUE_CONST);
		    }
		});
		field = issue.field(prop.getProperty(DEFECT_PHASE_CONST), new ArrayList() {{
			add(DEFECT_PHASE_VALUE_CONST);
		    }
		});
		field = issue.field(prop.getProperty(INJECTED_PHASE_CONST), new ArrayList() {{
			add(INJECTED_PHASE_VALUE_CONST);
		    }
		});
		issue.execute().addAttachment(new File(SNAPSHOTDIR));
	    } catch (JiraException e) {
		e.printStackTrace();
	    }
	}

    }

}
