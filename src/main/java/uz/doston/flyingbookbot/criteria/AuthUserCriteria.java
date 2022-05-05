package uz.doston.flyingbookbot.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthUserCriteria extends Criteria {

    private String chatId;

    @Builder(builderMethodName = "childBuilder")
    public AuthUserCriteria(Integer page, Integer size, String chatId) {
        super(page, size);
        this.chatId = chatId;
    }

}
