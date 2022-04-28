package uz.doston.flyingbookbot.repository;

import org.apache.commons.codec.language.bm.Languages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.enums.AuthRole;


@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    AuthRole findAuthRoleByChatId(String chatId);





}
