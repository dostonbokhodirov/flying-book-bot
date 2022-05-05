package uz.doston.flyingbookbot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.enums.AuthRole;

import java.util.List;
import java.util.Optional;


@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    AuthRole findAuthRoleByChatId(String chatId);

    Long countByRole(AuthRole role);

    Optional<AuthUser> findByChatId(String chatId);

    Long findIdByChatId(String chatId);

    List<AuthUser> findAllByCreatedAt(Pageable pageable);

    AuthUser findByChatId(String chatId, Pageable pageable);

    @Query(value = "select au.full_name from public.auth_user au where au.chat_id = :chatId", nativeQuery = true)
    String findFullNameByChatId(@Param(value = "chatId") String chatId);

    @Query(value = "select au.age from public.auth_user au where au.chat_id = :chatId", nativeQuery = true)
    Integer findAgeByChatId(String chatId);

    @Query(value = "select au.phone_number from public.auth_user au where au.chat_id = :chatId", nativeQuery = true)
    String findPhoneNumberByChatId(String chatId);
}
