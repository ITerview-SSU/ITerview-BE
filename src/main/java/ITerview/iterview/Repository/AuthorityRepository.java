package ITerview.iterview.Repository;

import ITerview.iterview.Domain.auth.Authority;
import ITerview.iterview.Domain.auth.MemberAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.Optional;

//@Repository
//public interface AuthorityRepository extends JpaRepository<Authority, String> {
//    Optional<Authority> findByAuthorityName(MemberAuth authorityName);
//}

@Repository
@RequiredArgsConstructor
public class AuthorityRepository{
    private final EntityManager em;

//    public void saveAuthority(Authority authority){
//        em.persist(authority);
//    }
    public Optional<Authority> findByAuthorityName(MemberAuth authorityName){
        Authority authority = em.
                createQuery("select a from Authority a where a.authorityName = :authorityName", Authority.class)
                .setParameter("authorityName", authorityName)
                .getSingleResult();
        return Optional.of(authority);
    }
}