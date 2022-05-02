package uz.doston.flyingbookbot.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class AuthUserCriteria extends Criteria {

    @Builder(builderMethodName = "childBuilder")
    public AuthUserCriteria(Integer page, Integer size) {
        super(page, size);
    }

}
