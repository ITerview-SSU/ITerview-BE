package ITerview.iterview.Repository;

import ITerview.iterview.Domain.main.Question;
import ITerview.iterview.Domain.main.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.*;

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


    public Set<Question> findByVideos(List<Video> videos) {
        Set<Question> questions = new HashSet<>();
        for (Video video : videos){
            Long questionId = video.getQuestion().getId();
            try{
                Question question = em
                        .createQuery("select q from Question q where q.id = :questionId", Question.class)
                        .setParameter("questionId", questionId)
                        .getSingleResult();
                questions.add(question);
            }catch(Exception e){
            }
        }
        return questions;
    }


}


