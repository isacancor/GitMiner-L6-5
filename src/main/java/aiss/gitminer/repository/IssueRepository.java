package aiss.gitminer.repository;

import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueRepository extends JpaRepository<Issue,String> {
    Page<Issue> findByRefId(String refId, Pageable paging);
    Page<Issue> findByTitleContaining(String title, Pageable paging);
    Page<Issue> findByAuthorId(String authorId, Pageable paging);
    Page<Issue> findByState(String state, Pageable paging);

}
