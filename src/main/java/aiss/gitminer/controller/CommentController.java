package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@Tag(name = "Comment", description = "Comment management API")
@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {

    @Autowired
    CommentRepository repository;

    @Operation(summary = "Retrieve all comments", description="Get all comment", tags={"comments", "get"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType="application/json") })})
    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @Operation(summary = "Retrieve one comment", description="Get a comment by its Id", tags={"comments", "get"})
    @ApiResponses({@ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType="application/json") }),
            @ApiResponse(responseCode="404", description = "The comment with the id provided doesn't exist", content = {@Content(schema = @Schema())})})
    @GetMapping("/{id}")
    public Comment findById(@Parameter(description = "id of the comment to be searched", required = true)
                            @PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);
        if (!comment.isPresent()){
            throw new CommentNotFoundException();
        }
        return comment.get();
    }
}