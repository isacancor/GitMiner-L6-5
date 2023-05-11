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
    ProjectRepository repository;

    // GET http://localhost:8080/api/projects
    @Operation(
            summary = "Get all project",
            description = "Get  all project",
            tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of projects received",
                    content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json") }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping
    public List<Project> findAll(@Parameter(description = "Number of pages to be returned") @RequestParam(defaultValue = "0") int page,
                                 @Parameter(description = "Number of projects per page") @RequestParam(defaultValue = "10") int size,
                                 @Parameter(description = "If it is present, only projects whose field \"name\" is equals to this param value")
                                     @RequestParam(required = false) String name,
                                 @Parameter(description = "If it is present, the projects will be returned sorted by " +
                                         "this field depending on whether the param starts with \"-\" " +
                                         "(descending order) " + "or not (ascending orther)")
                                     @RequestParam(required = false) String order) {
        Pageable paging;
        if (order!=null) {
            if (order.startsWith("-"))
                paging = PageRequest.of(page, size, Sort.by(order.substring(1)).descending());
            else
                paging = PageRequest.of(page, size, Sort.by(order).ascending());
        } else {
            paging = PageRequest.of(page, size);
        }
        Page<Project> pageProjects;
        if(name == null)
            pageProjects = repository.findAll(paging);
        else
            pageProjects = repository.findByName(name, paging);

        return pageProjects.getContent();
    }

    // GET http://localhost:8080/api/projects/{id}
    @Operation(
            summary = "Get one project",
            description = "Get a project that have the id given",
            tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Project received", content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json") }),
            @ApiResponse (responseCode = "404", description = "Project not found", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping("/{id}")
    public Project findOne(@Parameter(description = "id of the project to be returned") @PathVariable String id)
            throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if(!project.isPresent()){
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    // POST http://localhost:8080/api/projects
    @Operation(
            summary = "Create a project",
            description = "Create a project",
            tags = { "projects", "post" })
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Project created", content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json")}),
            @ApiResponse (responseCode = "400", description = "Bad request", content = { @Content (schema = @Schema ()) })
    })
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project createProject(@RequestBody @Valid Project project) {
        Project _project = repository.save(new Project(
                project.getId(),
                project.getName(),
                project.getWebUrl(),
                project.getCommits(),
                project.getIssues()
        ));
        return _project;
    }

    // PUT http://localhost:8080/api/projects/{id}
    @Operation(
            summary = "Update one project",
            description = "Update a project that have the id given",
            tags = { "projects", "put" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project updated", content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Project not found", content = { @Content(schema = @Schema()) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateProject(@Valid @RequestBody Project updatedProject,
                              @Parameter(description = "id of the project to be updated") @PathVariable String id)
            throws ProjectNotFoundException{
        Optional<Project> projectData = repository.findById(id);

        if (!projectData.isPresent()) {
            throw new ProjectNotFoundException();
        }

        Project _project = projectData.get();
        _project.setName(updatedProject.getName());
        _project.setWebUrl(updatedProject.getWebUrl());
        _project.setCommits(updatedProject.getCommits());
        _project.setIssues(updatedProject.getIssues());
        repository.save(_project);
    }

    // DELETE http://localhost:8080/api/projects/{id}
    @Operation(
            summary = "Delete one project",
            description = "Delete a project that have the id given",
            tags = { "projects", "delete" }
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Project updated", content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json")}),
            @ApiResponse(responseCode = "400", description = "Bad request", content = { @Content(schema = @Schema()) }),
            @ApiResponse(responseCode = "404", description = "Project not found", content = { @Content(schema = @Schema()) })
    })
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete (@Parameter(description = "id of the project to be deleted") @PathVariable String id)
            throws ProjectNotFoundException {
        if(repository.existsById(id)) {
            repository.deleteById(id);
        } else {
            throw new ProjectNotFoundException();
        }
    }

}
