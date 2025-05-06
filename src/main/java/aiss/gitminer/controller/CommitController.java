package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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

@Tag(name = "Commit", description = "Commit management API")
@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository repository;

    @Operation(summary = "Retrieve all commits", description="Get all commits", tags={"commits", "get"})
    @ApiResponse(responseCode = "200", description = "List of commits", content = { @Content(array = @ArraySchema(schema = @Schema(implementation = Commit.class)), mediaType="application/json") })
    @GetMapping
    public List<Commit> findAll() {
        return repository.findAll();
    }

    @Operation(summary = "Retrieve a commit by its id", description="Get commit by id", tags={"commits", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit", content = { @Content(schema = @Schema(implementation = Commit.class), mediaType = "application/json") }),
            @ApiResponse(responseCode = "404", description = "Commit not found", content = { @Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Commit findById(@Parameter(description = "id of commit to be searched")
                               @PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = repository.findById(id);
        if (commit.isEmpty()) {
            throw new CommitNotFoundException();
        }
        return commit.get();
    }
}
