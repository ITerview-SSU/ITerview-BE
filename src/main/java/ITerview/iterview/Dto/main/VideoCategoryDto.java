package ITerview.iterview.Dto.main;

import ITerview.iterview.Domain.main.Category;
import lombok.Builder;
import lombok.Data;

@Data
public class VideoCategoryDto {
    private Long category_id;

    @Builder
    public VideoCategoryDto(Long category_id){
        this.category_id = category_id;
    }
}
