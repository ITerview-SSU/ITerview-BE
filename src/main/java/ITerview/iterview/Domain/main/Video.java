package ITerview.iterview.Domain.main;

import ITerview.iterview.Domain.BaseEntity;
import ITerview.iterview.Domain.auth.Member;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter

public class Video extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="video_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Member member;

    @ManyToMany
    @JoinTable(name = "Video_Category",
            joinColumns = @JoinColumn(name = "video_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Category> categories = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private Question quesetion;




    @NotNull
    private String url;

    @Column(columnDefinition = "LONGTEXT")
    private String caption;
}
