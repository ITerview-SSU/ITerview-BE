package ITerview.iterview.Domain.main;

import ITerview.iterview.Domain.BaseEntity;
import ITerview.iterview.Domain.auth.Member;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@DynamicInsert
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
    private Question question;
    @Column(length=1000)
    private String url;

    private String filename;

    @Column(columnDefinition = "LONGTEXT")
    @ColumnDefault("null")
    private String transcription;

    public Video(Member member, Question question, String url) {
        this.member = member;
        this.question = question;
        this.url = url;
    }

    public Video updateCategories(List<Category> categories){
        this.categories = categories;
        for (Category category : categories){
            category.getVideos().add(this);
        }

        return this;
    }
}
