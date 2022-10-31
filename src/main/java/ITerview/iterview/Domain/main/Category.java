package ITerview.iterview.Domain.main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="category_id")
    private Long id;

    @NotNull
    private String category_name;

    @ManyToMany(mappedBy = "categories")
    private List<Video> videos = new ArrayList<>();

    @ManyToMany(mappedBy = "categories")
    private List<Question> questions = new ArrayList<>();
}
