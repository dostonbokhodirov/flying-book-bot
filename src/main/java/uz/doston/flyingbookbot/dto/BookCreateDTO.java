package uz.doston.flyingbookbot.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookCreateDTO {
    private String fileId;
    private String name;
    private String genre;
    private String size;
    private Long ownerId;
    private Integer downloadsCount;
}
