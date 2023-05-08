package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.IssueRepository;
import aiss.gitminer.repository.UserRepository;
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
@RequestMapping("/gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository repository;

    @Autowired
    UserRepository usRepos;



    // GET http://localhost:8080/api/issues
    @Operation(
            summary = "Get all issues",
            description = "Get all issues",
            tags = { "issues", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = Issue.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping
    public List<Issue> findAll(
            @RequestParam Optional<String> authorId,
            @RequestParam Optional<String> state) {
        if(authorId.isPresent()) {
            Optional<User> user = usRepos.findById(authorId.get());
            if (user.isPresent()) {
                return repository.findByUser(user.get());
            } else {
                System.out.println("user not found");
                return null;
            }
        } else if (state.isPresent()){
            return repository.findByState(state.get());
        }
        return repository.findAll();
    }




    // GET http://localhost:8080/api/issues/{id}
    @Operation(
            summary = "Get one issues",
            description = "Get an issue that have the id given",
            tags = { "issues", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = Issue.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping("/{id}")
    public Issue findOne(@PathVariable(value="id") String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);

        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }

        return issue.get();
    }



    // GET http://localhost:8080/api/issues/{id}/comments
    @Operation(
            summary = "Get comments by issue ID",
            description = "Get all the comments from the issue that have the id given",
            tags = { "issues", "get" , "comments"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = Issue.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsByIssueId(@PathVariable(value="id") String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);

        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }

        return issue.get().getComments();
    }
}
