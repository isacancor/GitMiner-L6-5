package aiss.gitminer.repository;

import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue,String> {
    List<Issue> findByState(String state);
    List<Issue> findByUser(User user);
}
