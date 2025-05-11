package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectControllerTest {

    @Autowired
    ProjectController projectController;

    @Test
    void getAllProjects() {
        Project project1 = new Project();
        List<Issue> issues1 = new ArrayList<>();

        Issue issue1 = new Issue();
        issue1.setTitle("Issue 1");
        issue1.setId("1");
        issues1.add(issue1);
        project1.setName("test");
        project1.setId("4564789");
        project1.setWebUrl("https://github.com");
        project1.setIssues(issues1);

        Project project2 = new Project();
        List<Issue> issues2 = new ArrayList<>();

        Issue issue2 = new Issue();
        issue2.setTitle("Issue 2");
        issue2.setId("2");
        issues2.add(issue1);
        issues2.add(issue2);
        project2.setName("project");
        project2.setId("4557");
        project2.setWebUrl("https://bitbucket.com");
        project2.setIssues(issues2);

        projectController.create(project1);
        projectController.create(project2);

        List<Project> projects = projectController.getAllProjects(0, 10, null, "name");
        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertEquals("project", projects.get(0).getName());
        assertEquals("4564789", projects.get(1).getId());
        List<Issue> retrievedIssues = projects.get(0).getIssues();
        assertNotNull(retrievedIssues);
        assertEquals(2, retrievedIssues.size());
        System.out.println(projects);
    }

    @Test
    void getProjectById() throws ProjectNotFoundException {
        Project project1 = new Project();
        List<Issue> issues1 = new ArrayList<>();

        Issue issue1 = new Issue();
        issue1.setTitle("Issue 1");
        issue1.setId("1");
        issues1.add(issue1);
        project1.setName("test");
        project1.setId("4564789");
        project1.setWebUrl("https://github.com");
        project1.setIssues(issues1);

        Project project2 = new Project();
        List<Issue> issues2 = new ArrayList<>();

        Issue issue2 = new Issue();
        issue2.setTitle("Issue 2");
        issue2.setId("2");
        issues2.add(issue1);
        issues2.add(issue2);
        project2.setName("project");
        project2.setId("4557");
        project2.setWebUrl("https://bitbucket.com");
        project2.setIssues(issues2);

        projectController.create(project1);
        projectController.create(project2);

        Project project = projectController.getProjectById("4557");
        assertNotNull(project);
        assertEquals("project", project.getName());
        assertEquals("4557", project.getId());
        List<Issue> retrievedIssues = project.getIssues();
        System.out.println(project);
        assertNotNull(retrievedIssues);
        assertEquals(2, retrievedIssues.size());
    }

    @Test
    void create() {
        Project project = new Project();
        List<Issue> issues = new ArrayList<>();

        Issue issue = new Issue();
        issue.setTitle("Issue 1");
        issue.setId("1");
        issues.add(issue);
        project.setName("test");
        project.setId("4564789");
        project.setWebUrl("https://github.com");
        project.setIssues(issues);

        Project _project = projectController.create(project);
        assertNotNull(_project);
        assertEquals(project.getId(), _project.getId());
    }

    @Test
    void update() {
    }
}