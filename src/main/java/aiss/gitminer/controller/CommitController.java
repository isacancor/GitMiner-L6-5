package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository repository;


    // GET http://localhost:8080/api/commits/
    @Operation(
            summary = "Get all commits",
            description = "Get all commits",
            tags = { "commits", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = Commit.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping
    public List<Commit> findAll(@RequestParam Optional<String> email) {
        if (email.isEmpty()) {
            return repository.findAll();
        }

        return repository.findByAuthorEmail(email.get());
    }



    // GET http://localhost:8080/api/commits/{id}
    @Operation(
            summary = "Get one commit",
            description = "Get a commit that have the id given",
            tags = { "commit", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = Commit.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping("/{id}")
    public Commit findOne(@PathVariable(value="id") String id) throws CommitNotFoundException {
        Optional<Commit> commit = repository.findById(id);

        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }

        return commit.get();
    }


}
