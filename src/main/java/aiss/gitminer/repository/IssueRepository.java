package aiss.gitminer.repository;

import aiss.gitminer.model.Issue;
import aiss.gitminer.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, String> {
    List<Issue> findByAuthor(User user);
    List<Issue> findByState(String state);
    List<Issue> findByAuthorAndState(User user, String state);
}
