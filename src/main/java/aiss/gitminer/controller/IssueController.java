package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.IssueRepository;
import aiss.gitminer.repository.UserRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Issue", description = "Issue management API")
@RestController
@RequestMapping("/gitminer/issues")

public class IssueController {

    @Autowired
    IssueRepository issueRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping
    public List<Issue> getAllIssues(@RequestParam (required = false) String authorId, @RequestParam (required = false) String state) throws UserNotFoundException {
        List<Issue> issues;
        if (authorId != null && state != null) {
            Optional<User> user = userRepository.findById(authorId);
            if (user.isPresent()) {
                issues = issueRepository.findByAuthorAndState(user.get(), state);
            } else {
                throw new UserNotFoundException();
            }
        } else if (authorId != null) {
            Optional<User> user = userRepository.findById(authorId);
            if (user.isPresent()) {
                issues = issueRepository.findByAuthor(user.get());
            } else {
                throw new UserNotFoundException();
            }
        } else if (state != null) {
            issues = issueRepository.findByState(state);
        } else {
            issues = issueRepository.findAll();
        }
        return issues;
    }

    @GetMapping("{id}")
    public Issue getIssueById(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (issue.isPresent()) {
            return issue.get();
        } else {
            throw new IssueNotFoundException();
        }
    }

    @GetMapping("{id}/comments")
    public List<Comment> getCommentsIssue(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        } else {
            return issue.get().getComments();
        }
    }
}
