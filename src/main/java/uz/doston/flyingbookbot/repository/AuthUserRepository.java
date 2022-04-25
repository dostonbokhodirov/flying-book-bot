package uz.doston.flyingbookbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.enums.AuthRole;

public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    public AuthRole findAuthRoleByChatId(String chatId);

}
