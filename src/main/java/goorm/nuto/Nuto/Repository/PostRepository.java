package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
