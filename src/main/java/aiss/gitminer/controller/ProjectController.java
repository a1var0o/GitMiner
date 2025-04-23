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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Project", description = "Project management API")
@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository projectRepository;

    @Operation(
            description = "Get {page} pages of size {size} the projects",
            tags = {"get", "paginated"}
    )
    @GetMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
    })
    public List<Project> getAllProjects(@RequestParam(defaultValue = "0") int page,
                                        @RequestParam(defaultValue = "10") int size,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String order) {
        Page<Project> projects;
        Pageable paging;
        if (order != null) {
            if (order.startsWith("-")) {
                order = order.substring(1);
                paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, order));
            } else {
                paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, order));
            }
        } else {
            paging = PageRequest.of(page, size);
        }
        if (name != null) {
            projects = projectRepository.findByName(name, paging);
        } else {
            projects = projectRepository.findAll(paging);
        }

        return projects.getContent();
    }

    @Operation(
            description = "Get a project by its ID",
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
            description = "Create a new project",
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