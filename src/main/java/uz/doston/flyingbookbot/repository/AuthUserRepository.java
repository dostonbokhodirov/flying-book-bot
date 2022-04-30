package uz.doston.flyingbookbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.enums.AuthRole;

import java.util.Optional;


@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    AuthRole findAuthRoleByChatId(String chatId);

    Long countByRole(AuthRole role);

    Optional<AuthUser> findByChatId(String chatId);
}
