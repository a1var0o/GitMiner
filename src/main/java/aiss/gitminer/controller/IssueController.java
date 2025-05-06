package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.exception.UserNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.IssueRepository;
import aiss.gitminer.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Retrieve all issues", description="Get all issues. Can be filtered by authorId and state parameters", tags={"issues", "get"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Issue.class), mediaType="application/json") }),
                    @ApiResponse(responseCode="404", description = "The authorId provided doesn't exist", content = {@Content(schema = @Schema())})})
    @GetMapping
    public List<Issue> getAllIssues(@Parameter(description = "Id of the author of which we want to retrieve the issues")
                                        @RequestParam (required = false) String authorId,
                                    @Parameter(description = "State in which the issues retrieves are. It can either be open or closed")
                                        @RequestParam (required = false) String state) throws UserNotFoundException {
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

    @Operation(summary = "Retrieve one issue", description="Get an issue by its Id", tags={"issues", "get"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Issue.class), mediaType="application/json") }),
            @ApiResponse(responseCode="404", description = "The issue with the id provided doesn't exist", content = {@Content(schema = @Schema())})})
    @GetMapping("{id}")
    public Issue getIssueById(@Parameter(description = "Id of the issue to be searched", required = true)
                                  @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (issue.isPresent()) {
            return issue.get();
        } else {
            throw new IssueNotFoundException();
        }
    }

    @Operation(summary = "Retrieve the comments of an issue", description="Given the id of one issue, get all its comments", tags={"comments", "get"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType="application/json") }),
            @ApiResponse(responseCode="404", description = "The issue with the id provided doesn't exist", content = {@Content(schema = @Schema())})})
    @GetMapping("{id}/comments")
    public List<Comment> getCommentsIssue(@Parameter(description = "Id of the issue to be searched", required = true)
                                              @PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = issueRepository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        } else {
            return issue.get().getComments();
        }
    }
}
