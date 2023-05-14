package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.model.Project;
import aiss.gitminer.model.User;
import aiss.gitminer.repository.CommentRepository;
import aiss.gitminer.repository.IssueRepository;
import aiss.gitminer.repository.UserRepository;
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

@Tag(name = "Issue", description = "Issue management API")
@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository repository;

    @Autowired
    CommentRepository commentRepository;



    // GET http://localhost:8080/api/issues
    @Operation(
            summary = "Get all issues",
            description = "Get all issues",
            tags = { "issues", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of issues received",
                    content = { @Content(schema = @Schema(implementation = Issue.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", description = "Bad request", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping
    public List<Issue> findAll(@Parameter(description = "Number of pages to be returned")
                                   @RequestParam(defaultValue = "0") int page,
                               @Parameter(description = "Number of issues per page")
                                    @RequestParam(defaultValue = "10") int size,
                               @Parameter(description = "If it is present, only issues whose " +
                                       "field \"title\" is equals to this param value")
                                   @RequestParam(required = false) String title,
                               @Parameter(description = "If it is present, the issues will be returned sorted by " +
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
        Page<Issue> pageProjects;
        if(title == null)
            pageProjects = repository.findAll(paging);
        else
            pageProjects = repository.findByTitle(title, paging);

        return pageProjects.getContent();
    }




    // GET http://localhost:8080/api/issues/{id}
    @Operation(
            summary = "Get one issue",
            description = "Get an issue that have the id given",
            tags = { "issues", "get" })
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Issue received",
                    content = { @Content(schema = @Schema(implementation = Issue.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", description = "Bad request", content = { @Content (schema = @Schema ()) }),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}")
    public Issue findOne(@Parameter(description = "id of the issue to be returned") @PathVariable(value="id") String id)
            throws IssueNotFoundException {
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
            @ApiResponse (responseCode = "400", description = "Bad request", content = { @Content (schema = @Schema ()) }),
            @ApiResponse(responseCode = "404", description = "Issue not found", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsByIssueId(@Parameter(description = "id of the issue whose comments are to be returned")
                                                   @PathVariable String id)
            throws IssueNotFoundException {

        Optional<Issue> issue = repository.findById(id);

        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }

        return issue.get().getComments();
    }
}
