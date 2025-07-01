package goorm.nuto.Nuto.Repository;

import goorm.nuto.Nuto.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
