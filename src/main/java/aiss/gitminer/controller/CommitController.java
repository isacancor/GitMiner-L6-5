package aiss.gitminer.controller;

import aiss.gitminer.model.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired
    CommitController repository;

    @GetMapping
    public List<Commit> findAll() {
        return repository.findAll();
    }
}
