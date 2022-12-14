package ITerview.iterview.Repository;

import ITerview.iterview.Domain.main.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

//@Repository
//public interface CategoryRepository extends JpaRepository<Category, Long> {
//    Category findByCategory_name(String name);
//}

@Repository
@RequiredArgsConstructor
public class CategoryRepository{
    private final EntityManager em;

    public void saveCategory(Category category){
        em.persist(category);
    }
    public Category findByCategory_name(String name){
        return em.
                createQuery("select c from Category c where c.category_name = :name", Category.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    public Category findById(Long categoryId){
        return em
                .createQuery("select c from Category c where c.id = :categoryId", Category.class)
                .setParameter("categoryId", categoryId)
                .getSingleResult();
    }
}