package ITerview.iterview.Repository;

import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

//@Repository
//@RequiredArgsConstructor
//public class QuestionRepository {
//    private final EntityManager em;
//
//    public List<Category> findCategories(Long questionId){
//        return em
//                .createQuery("select c from Category c where c.questions")
//    }
//}

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{

}