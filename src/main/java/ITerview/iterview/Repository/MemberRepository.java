package ITerview.iterview.Repository;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.ExceptionHandler.BizException;
import ITerview.iterview.ExceptionHandler.MemberExceptionType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

//@Repository
//public interface MemberRepository extends JpaRepository<Member, Long> {
//    Optional<Member> findByUsername(String username);
//    Optional<Member> findByEmail(String email);
//    boolean existsByEmail(String email);
//}

@Repository
@RequiredArgsConstructor
public class MemberRepository{
    private final EntityManager em;

    public Member saveMember(Member member){
        em.persist(member);
        return em.find(Member.class, member.getId());
    }

    public Optional<Member> findByUsername(String username){
        try {
            Member member = em.
                    createQuery("select m from Member m where m.username = :username", Member.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return Optional.of(member);
        }catch(NoResultException e){
            throw new BizException(MemberExceptionType.NOT_FOUND_USER);
        }
    }

    public Optional<Member> findByEmail(String email){
        try {
            Member member = em.
                    createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.of(member);
        }catch(NoResultException e){
            throw new BizException(MemberExceptionType.NOT_FOUND_USER);
        }
    }

    public boolean existsByEmail(String email){
        try {
            em
                .createQuery("select m from Member m where m.email = :email", Member.class)
                .setParameter("email", email)
                .getSingleResult();
            return true;
        }catch(NoResultException e){
            return false;
        }
    }
}
