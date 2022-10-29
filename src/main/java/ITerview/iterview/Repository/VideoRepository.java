package ITerview.iterview.Repository;

import ITerview.iterview.Domain.auth.Member;
import ITerview.iterview.Domain.main.Video;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VideoRepository extends JpaRepository<Member, Long> {

}