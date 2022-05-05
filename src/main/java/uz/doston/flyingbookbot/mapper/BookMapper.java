package uz.doston.flyingbookbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import uz.doston.flyingbookbot.dto.BookCreateDTO;
import uz.doston.flyingbookbot.entity.Book;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface BookMapper {

    Book fromCreateDTO(BookCreateDTO dto);

}
