package aiss.gitminer.controller;

import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    ProjectRepository projRepo;

    @GetMapping
    public List<Project> findAll() {
        return projRepo.findAll();
    }

}
