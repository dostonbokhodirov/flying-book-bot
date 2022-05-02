package uz.doston.flyingbookbot.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCriteria extends Criteria {

    @Builder(builderMethodName = "childBuilder")
    public BookCriteria(Integer page, Integer size) {
        super(page, size);
    }
}
