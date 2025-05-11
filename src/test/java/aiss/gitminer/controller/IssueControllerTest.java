package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueControllerTest {
    @Autowired
    IssueController issueController;
    @Autowired
    ProjectController projectController;

    @Test
    void getAllIssues() throws UserNotFoundException {
        Project project1 = new Project();
        List<Issue> issues1 = new ArrayList<>();

        Issue issue1 = new Issue();
        Issue issue2 = new Issue();
        issue1.setTitle("Issue 1");
        issue1.setId("1");
        issue2.setTitle("Issue 2");
        issue2.setId("2");
        issues1.add(issue1);
        issues1.add(issue2);
        project1.setName("test");
        project1.setId("4564789");
        project1.setWebUrl("https://github.com");
        project1.setIssues(issues1);

        Project project2 = new Project();
        List<Issue> issues2 = new ArrayList<>();

        Issue issue3 = new Issue();
        issue3.setTitle("Issue 3");
        issue3.setId("3");
        issues2.add(issue1);
        issues2.add(issue3);
        project2.setName("project");
        project2.setId("4557");
        project2.setWebUrl("https://bitbucket.com");
        project2.setIssues(issues2);

        projectController.create(project1);
        projectController.create(project2);

        List<Issue> issues = issueController.getAllIssues(null, null, null);
        assertNotNull(issues);
        assertFalse(issues.isEmpty());
        assertTrue(issues.stream().anyMatch(i -> i.getTitle().equals("Issue 1") && i.getId().equals("1")));
        System.out.println(issues);

    }

    @Test
    void getIssueById() throws IssueNotFoundException {
        Project project1 = new Project();
        List<Issue> issues1 = new ArrayList<>();

        Issue issue1 = new Issue();
        Issue issue2 = new Issue();
        issue1.setTitle("Issue 1");
        issue1.setId("1");
        issue2.setTitle("Issue 2");
        issue2.setId("2");
        issues1.add(issue1);
        issues1.add(issue2);
        project1.setName("test");
        project1.setId("4564789");
        project1.setWebUrl("https://github.com");
        project1.setIssues(issues1);

        projectController.create(project1);

        Issue issue = issueController.getIssueById("1");
        assertNotNull(issue);
        assertTrue(issue.getTitle().equals("Issue 1"));
        System.out.println(issue);

    }

    @Test
    void getCommentsIssue() {
    }
}