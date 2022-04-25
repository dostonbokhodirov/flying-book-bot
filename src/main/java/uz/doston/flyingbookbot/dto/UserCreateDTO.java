package uz.doston.flyingbookbot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserCreateDTO {
    private String fullName;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private String language;
    private String role;
    private String userName;
    private String createdAt;
}
