package ITerview.iterview.Repository;

import ITerview.iterview.Domain.main.Category;
import ITerview.iterview.Domain.main.Question;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class QuestionRepository{
    private final EntityManager em;

    public void saveQuestion(Question question){
        em.persist(question);
    }

    public List<Question> findByCategory(String category){
        try {
            List questions = em
                .createQuery("select q from Question q where exists (select c from q.categories c where c.category_name = :category)")
                .setParameter("category", category)
                .getResultList();

            return questions;
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

    public List<Question> findByKeyword(String keyword){
        try {
            List questions = em
                    .createQuery("select q from Question q where q.question like :param")
                    .setParameter("param", "%" + keyword + "%")
                    .getResultList();

            return questions;
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    public Optional<Question> findById(Long questionId){
        Question question = em.find(Question.class, questionId);
        return Optional.of(question);
    }


}


