package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.CommentRepository;
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

    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Comment findOne(@PathVariable(value="id") String id) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);

        if(!comment.isPresent()){
            throw new CommentNotFoundException();
        }

        return comment.get();
    }
}
