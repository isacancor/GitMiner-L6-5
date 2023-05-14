package aiss.gitminer.repository;

import aiss.gitminer.model.Commit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit,String> {
    Page<Commit> findByTitleContaining(String name, Pageable pageable);

    Page<Commit> findByAuthorName(String name, Pageable pageable);

    Page<Commit> findByAuthorEmail(String name, Pageable pageable);
}
