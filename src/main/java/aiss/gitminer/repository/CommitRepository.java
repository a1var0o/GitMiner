package aiss.gitminer.repository;

import aiss.gitminer.model.Commit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommitRepository extends JpaRepository<Commit, String> {
    List<Commit> findByAuthorName(String authorName);
    List<Commit> findByAuthorNameAndAuthoredDateGreaterThan(String authorName, String date);
    List<Commit> findByAuthoredDateGreaterThan(String date);
}
