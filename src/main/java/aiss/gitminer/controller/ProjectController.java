package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

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
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json") }
            )
    })
    @GetMapping
    public List<Project> findAll() {
        return repository.findAll();
    }



    // GET http://localhost:8080/api/projects/{id}
    @Operation(
            summary = "Get one project",
            description = "Get a project that have the id given",
            tags = { "projects", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id) throws ProjectNotFoundException {
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
            @ApiResponse(responseCode = "201",
                    content = { @Content(schema = @Schema(implementation =Project.class), mediaType= "application/json")}
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
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

}
