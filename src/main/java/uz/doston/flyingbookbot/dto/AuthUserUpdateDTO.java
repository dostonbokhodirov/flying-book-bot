package uz.doston.flyingbookbot.dto;

import lombok.*;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.enums.AuthRole;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthUserUpdateDTO {
    private String chatId;
    private String fullName;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private String language;
    private AuthRole role;
    private List<Book> books;
}
