package aiss.gitminer.controller;

import aiss.gitminer.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gitminer/comments")
public class CommentController {

    @Autowired
    CommentController repository;

    @GetMapping
    public List<Comment> findAll() {
        return repository.findAll();
    }
}
