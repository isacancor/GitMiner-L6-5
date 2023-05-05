package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/commits")
public class CommitController {

    @Autowired
    CommitRepository repository;

    @GetMapping
    public List<Commit> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Commit findOne(@PathVariable(value="id") String id) throws CommitNotFoundException {
        Optional<Commit> commit = repository.findById(id);

        if(!commit.isPresent()){
            throw new CommitNotFoundException();
        }

        return commit.get();
    }

}
