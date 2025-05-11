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

    @Operation(summary = "Retrieve all projects",
            description = "Get the indicated page of size {size} the projects",
            tags = {"projects", "get", "paginated"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")})
    })
    @GetMapping
    public List<Project> getAllProjects(@Parameter(description = "Index of the page")
                                            @RequestParam(defaultValue = "0") int page,
                                        @Parameter(description = "Size of the page")
                                            @RequestParam(defaultValue = "10") int size,
                                        @Parameter(description = "Name of the project to retrieve")
                                            @RequestParam(required = false) String name,
                                        @Parameter(description = "Attribute used to sort the projects retrieved. If starts with - uses descending order")
                                        @RequestParam(required = false) String sort) {
        Page<Project> projects;
        Pageable paging;
        if (sort != null) {
            if (sort.startsWith("-")) {
                sort = sort.substring(1);
                paging = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, sort));
            } else {
                paging = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, sort));
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

    @Operation(summary = "Retrieve a project",
            description = "Get a project by its Id",
            tags = {"projects", "get"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                        content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                        content = {@Content(schema = @Schema())})
    })
    @GetMapping("/{id}")
    public Project getProjectById(@Parameter(description = "Id of the project to be searched", required = true)
                                      @PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = projectRepository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    @Operation(summary = "Create a new project",
            description = "Create a new project given the information put in the body",
            tags = {"projects", "post"}
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

    @Operation(summary = "Update a project",
            description = "Update an existing project given the information from the body",
            tags = {"projects", "put"}
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = Project.class), mediaType = "application/json")}),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema())})
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void update(@Parameter(description = "Id of the project to be searched", required = true)
                              @PathVariable String id,
                       @Valid @RequestBody Project updatedProject) throws ProjectNotFoundException {
        Optional<Project> projectData = projectRepository.findById(id);

        if (!projectData.isPresent()) {
            throw new ProjectNotFoundException();
        }
        Project _project = projectData.get();
        _project.setName(updatedProject.getName());
        _project.setWebUrl(updatedProject.getWebUrl());
        _project.setName(updatedProject.getName());
        _project.setCommits(updatedProject.getCommits());
        _project.setIssues(updatedProject.getIssues());
        projectRepository.save(_project);
    }
}