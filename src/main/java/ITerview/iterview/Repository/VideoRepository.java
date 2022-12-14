package ITerview.iterview.Repository;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Video;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

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

    public String getFilename(Long questionId, String username){
        Video video;
        if (existsByMemberAndQuestion(questionId, username)) {
            video = findOne(questionId, username);
            return video.getFilename();
        }else{
            return "";
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

    public List<Video> findByMember(Member member){
        try{
            return em
                    .createQuery("select v from Video v where v.member = :member")
                    .setParameter("member", member)
                    .getResultList();
        }catch (Exception e){
            return new ArrayList<>();
        }
    }

}