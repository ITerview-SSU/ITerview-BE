package ITerview.iterview.Domain.main;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_id")
    private Long id;

    @NotNull
    private String question;

    @OneToMany
    @JoinColumn(name="question_id")
    private List<Video> videos = new ArrayList<>();


    @ManyToMany
    @JoinTable(name = "Question_Category",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();;
}
