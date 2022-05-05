package uz.doston.flyingbookbot.criteria;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookCriteria extends Criteria {

    private String name;
    private String genre;

    @Builder(builderMethodName = "childBuilder")
    public BookCriteria(Integer page, Integer size, String name, String genre) {
        super(page, size);
        this.name = name;
        this.genre = genre;
    }
}
