package aiss.gitminer.controller;

import aiss.gitminer.model.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gitminer/issuess")
public class IssueController {

    @Autowired
    IssueController repository;

    @GetMapping
    public List<Issue> findAll() {
        return repository.findAll();
    }
}
