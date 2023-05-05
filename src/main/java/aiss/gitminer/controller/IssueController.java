package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Commit;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository repository;

    @GetMapping
    public List<Issue> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Issue findOne(@PathVariable(value="id") String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);

        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }

        return issue.get();
    }

    @GetMapping("/{id}/comments")
    public List<Comment> findCommentsByIssueId(@PathVariable(value="id") String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);

        if(!issue.isPresent()){
            throw new IssueNotFoundException();
        }

        return issue.get().getComments();
    }
}
