package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
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

@Tag(name = "Commit", description = "Commit management API")
@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository repository;


    // GET http://localhost:8080/api/commits/
    @Operation(
            summary = "Get all commits",
            description = "Get all commits. Only one filter can be applied per request.",
            tags = { "commits", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "List of commits received",
                    content = { @Content(schema = @Schema(implementation = Commit.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", description = "Bad request", content = { @Content (schema = @Schema ()) })
    })
    @GetMapping
    public List<Commit> findAll(@Parameter(description = "Number of pages to be returned") @RequestParam(defaultValue = "0") int page,
                                @Parameter(description = "Number of commits per page") @RequestParam(defaultValue = "10") int size,
                                @Parameter(description = "If it is present, only commits whose field \"title\" are like this param value will be returned")
                                    @RequestParam(required = false) String title,
                                @Parameter(description = "If it is present, only commits whose field \"author_name\" are like this param value will be returned")
                                    @RequestParam(required = false) String author_name,
                                @Parameter(description = "If it is present, only commits whose field \"author_email\" are like this param value will be returned")
                                    @RequestParam(required = false) String email,
                                @Parameter(description = "If it is present, the commits will be returned sorted by " +
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
        Page<Commit> pageCommits;
        if(title != null)
            pageCommits = repository.findByTitleContaining(title, paging);
        else if(author_name != null)
            pageCommits = repository.findByAuthorName(author_name ,paging);
        else if(email != null)
            pageCommits = repository.findByAuthorEmail(email, paging);
        else
            pageCommits = repository.findAll(paging);
        return pageCommits.getContent();
    }



    // GET http://localhost:8080/api/commits/{id}
    @Operation(
            summary = "Get one commit",
            description = "Get a commit that have the id given",
            tags = { "commit", "get"})
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Commit received",
                    content = { @Content(schema = @Schema(implementation = Commit.class), mediaType= "application/json") }
            ),
            @ApiResponse (responseCode = "400", description = "Bad request", content = { @Content (schema = @Schema ()) }),
            @ApiResponse(responseCode = "404", description = "Commit not found", content = { @Content(schema = @Schema()) }),
    })
    @GetMapping("/{id}")
    public Commit findOne(@Parameter(description = "id of the commit to be returned") @PathVariable String id)
            throws CommitNotFoundException {
        Optional<Commit> commit = repository.findById(id);

        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }

        return commit.get();
    }


}
