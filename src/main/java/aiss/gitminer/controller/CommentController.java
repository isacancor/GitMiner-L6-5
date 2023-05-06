package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {

    @Autowired
    CommentRepository repository;



    // GET http://localhost:8080/api/comments
    @Operation(
            summary = "Get all comments",
            description = "Get all comments",
            tags = { "comments", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = Comment.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }




    // GET http://localhost:8080/api/comments/{id}
    @Operation(
            summary = "Get one comments",
            description = "Get a comment that have the id given",
            tags = { "comments", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = { @Content(schema = @Schema(implementation = Comment.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping("/{id}")
    public Comment findOne(@PathVariable(value="id") String id) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);

        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }

        return comment.get();
    }
}
