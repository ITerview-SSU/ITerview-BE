package ITerview.iterview.Repository;

import ITerview.iterview.Domain.jwt.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

//@Repository
//public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//    Optional<RefreshToken> findByKey(String key);
//}

@Repository
@RequiredArgsConstructor
public class RefreshTokenRepository{
    private final EntityManager em;

    public void saveRefreshToken(RefreshToken refreshToken){
        em.persist(refreshToken);
    }

    //Delete 함수 구현
    @Transactional
    public void deleteRefreshToken(RefreshToken refreshToken){
        em.remove(refreshToken);
    }

    public Optional<RefreshToken> findByKey(String key){
        RefreshToken refreshToken = em
                .createQuery("select t from RefreshToken t where t.key = :key", RefreshToken.class)
                .setParameter("key", key)
                .getSingleResult();
        return Optional.of(refreshToken);
    }

    public boolean existsByKey(String key){
        try{
            RefreshToken refreshToken = em
                    .createQuery("select t from RefreshToken t where t.key = :key", RefreshToken.class)
                    .setParameter("key", key)
                    .getSingleResult();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
