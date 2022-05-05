package uz.doston.flyingbookbot.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import uz.doston.flyingbookbot.dto.AuthUserCreateDTO;
import uz.doston.flyingbookbot.dto.AuthUserUpdateDTO;
import uz.doston.flyingbookbot.entity.AuthUser;

@Component
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface AuthUserMapper {

    AuthUser fromCreateDTO(AuthUserCreateDTO dto);

    AuthUser fromUpdateDTO(AuthUserUpdateDTO dto, @MappingTarget AuthUser entity);

}
