package uz.doston.flyingbookbot.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.doston.flyingbookbot.enums.AuthRole;

@Getter
@Setter
@NoArgsConstructor
public class AuthUserCreateDTO {
    private String fullName;
    private Integer age;
    private String gender;
    private String phoneNumber;
    private String language;
    private AuthRole role;
    private String userName;
    private String createdAt;
}
