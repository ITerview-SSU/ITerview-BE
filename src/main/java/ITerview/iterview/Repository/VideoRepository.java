package ITerview.iterview.Repository;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VideoRepository{
    private final EntityManager em;

    public void saveVideo(Video video){
        em.persist(video);
    }

    public Video findOne(Long questionId, String username){
        return em
                .createQuery("select v from Video v where v.question.id = :questionId and v.member.username = :username", Video.class)
                .setParameter("questionId", questionId)
                .setParameter("username", username)
                .getSingleResult();

    }

    public boolean existsByMemberAndQuestion(Long questionId, String username){
        try{
            em
                .createQuery("select v from Video v where v.question.id = :questionId and v.member.username = :username", Video.class)
                .setParameter("questionId", questionId)
                .setParameter("username", username)
                .getSingleResult();
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public void removeOne(Long questionId, String username){
        if (existsByMemberAndQuestion(questionId, username)) {
            Video video = findOne(questionId, username);
            em.remove(video);
        }
    }

    public String getCreatedAt(Long questionId, String username){
        Video video  = em
                .createQuery("select v from Video v where v.question.id = :questionId and v.member.username = :username", Video.class)
                .setParameter("questionId", questionId)
                .setParameter("username", username)
                .getSingleResult();

        return String.valueOf(video.getCreatedAt());
    }
}