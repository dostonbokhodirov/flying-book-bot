package uz.doston.flyingbookbot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.enums.AuthRole;

import java.util.List;
import java.util.Optional;


@Repository
public interface AuthUserRepository extends JpaRepository<AuthUser, Long> {

    @Query(value = "select au.role from public.auth_user au where au.chat_id = :chatId", nativeQuery = true)
    AuthRole findAuthRoleByChatId(@Param(value = "chatId") String chatId);

    Long countByRole(AuthRole role);

    Optional<AuthUser> findByChatId(String chatId);

    @Query(value = "select au.id from public.auth_user au where au.chat_id = :chatId", nativeQuery = true)
    Long findIdByChatId(@Param(value = "chatId") String chatId);

    @Query(
            value = "select au.* from public.auth_user au where au.role = :authRole order by random() limit 1",
            nativeQuery = true
    )
    AuthUser findRandomByRole(@Param(value = "authRole") AuthRole role);

    @Query(
            value = "select au.full_name from public.auth_user au where au.chat_id = :chatId",
            nativeQuery = true
    )
    String findFullNameByChatId(@Param(value = "chatId") String chatId);

    @Query(
            value = "select au.age from public.auth_user au where au.chat_id = :chatId",
            nativeQuery = true
    )
    Integer findAgeByChatId(String chatId);

    @Query(
            value = "select au.phone_number from public.auth_user au where au.chat_id = :chatId",
            nativeQuery = true
    )
    String findPhoneNumberByChatId(String chatId);

    @Query(
            value = "select au.role from public.auth_user au where au.phone_number = :phoneNumber",
            nativeQuery = true
    )
    AuthRole findAuthRoleByPhoneNumber(@Param(value = "phoneNumber") String phoneNumber);

    @Query(
            value = "select aub.* from public.auth_user au " +
                    "inner join public.auth_user_books aub " +
                    "on au.id = aub.auth_user_id" +
                    " where au.chat_id = :chatId " +
                    "order by au.created_at desc " +
                    "limit :size offset :page",
            nativeQuery = true
    )
    List<Book> findDownloadedBooksByChatId(
            @Param(value = "chatId") String chatId,
            @Param(value = "page") Integer page,
            @Param(value = "size") Integer size
    );
}
