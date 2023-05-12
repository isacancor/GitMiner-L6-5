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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Comment", description = "Comment management API")
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
            @ApiResponse(responseCode = "200", content = { @Content(schema = @Schema(implementation = Comment.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping
    public List<Comment> findAll(@Parameter(description = "Number of pages to be returned")
                                     @RequestParam(defaultValue = "0") int page,
                                 @Parameter(description = "Number of comments per page")
                                    @RequestParam(defaultValue = "10") int size,
                                 @Parameter(description = "If it is present, the comments will be returned sorted by " +
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
        Page<Comment> pageComments;
        pageComments = repository.findAll(paging);
        return pageComments.getContent();
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
    public Comment findOne(@Parameter(description = "id of the comment to be returned") @PathVariable String id)
            throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);

        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }

        return comment.get();
    }
}
