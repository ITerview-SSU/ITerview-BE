package ITerview.iterview.Repository;

import ITerview.iterview.Domain.Authority;
import ITerview.iterview.Domain.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthorityRepository extends JpaRepository<Authority, String> {
    Optional<Authority> findByAuthorityName(MemberAuth authorityName);
}
