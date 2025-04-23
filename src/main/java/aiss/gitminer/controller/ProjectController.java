package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.List;
import java.util.Optional;

@Tag(name = "Project", description = "Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Operation(
            description = "Get all the projects",
            tags = {"get"}
    )
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Operation(
            description = "Get an album by its ID",
            tags = {"get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                        content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Project getProjectById(@Parameter(description = "id of the project to be searched", required = true)
                                      @PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    @Operation(
            description = "Create a new album",
            tags = {"post"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "400",
                    content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project create(@Valid @RequestBody Project project) {
        return projectRepository.save(project);
    }

}