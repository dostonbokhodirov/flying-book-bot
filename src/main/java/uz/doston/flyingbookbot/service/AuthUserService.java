package uz.doston.flyingbookbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import uz.doston.flyingbookbot.criteria.AuthUserCriteria;
import uz.doston.flyingbookbot.dto.AuthUserCreateDTO;
import uz.doston.flyingbookbot.dto.AuthUserUpdateDTO;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.mapper.AuthUserMapper;
import uz.doston.flyingbookbot.repository.AuthUserRepository;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserMapper authUserMapper;
    private final AuthUserRepository authUserRepository;

    private final Translate translate;

    @PostConstruct
    public void readLanguage() {
        List<AuthUser> userList = authUserRepository.findAll();
        userList.forEach(authUser ->
                UserState.setLanguage(authUser.getChatId(), authUser.getLanguage().toLowerCase()));
    }

    public AuthRole getRoleByChatId(String chatId) {
        return authUserRepository.findAuthRoleByChatId(chatId);
    }

    public void save(AuthUserCreateDTO dto) {
        AuthUser authUser = authUserMapper.fromCreateDTO(dto);
        authUserRepository.save(authUser);
    }

    public Long count() {
        return authUserRepository.count();
    }

    public Long count(AuthRole role) {
        return authUserRepository.countByRole(role);
    }

    public StringBuilder detailMessage(String chatId) {
        String language = UserState.getLanguage(chatId);
        AuthUser authUser = authUserRepository.findByChatId(chatId).orElseThrow();

        String id = authUser.getChatId();
        String fullName = authUser.getFullName();
        Integer age = authUser.getAge();
        String gender = authUser.getGender();
        String phoneNumber = authUser.getPhoneNumber();
        String userLanguage = authUser.getLanguage();
        String role = authUser.getRole().toString();
        String userName = authUser.getUserName();
        LocalDateTime createdAt = authUser.getCreatedAt();

        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append("ID: <code>").append(id).append("</code>\n")
                .append(translate.getTranslation("user.full.name", language))
                .append(" <b>").append(fullName).append("</b>\n")
                .append(translate.getTranslation("user.age", language))
                .append(" <b>").append(age).append("</b>\n")
                .append(translate.getTranslation("user.gender", language))
                .append(" <code>").append(gender).append("</code>\n")
                .append(translate.getTranslation("user.phone.number", language))
                .append(" <b>").append(phoneNumber).append("</b>\n")
                .append(translate.getTranslation("user.language", language))
                .append(" <code>").append(userLanguage).append("</code>\n")
                .append(translate.getTranslation("user.role", language))
                .append(" <code>").append(role).append("</code>\n")
                .append(translate.getTranslation("user.name", language))
                .append(" @").append(userName).append("\n")
                .append(translate.getTranslation("user.created.at", language))
                .append(" <code>").append(createdAt).append("</code>\n");

        return stringBuilder;
    }

    public List<AuthUser> getAll(AuthUserCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.Direction.DESC);
        return authUserRepository.findAllByCreatedAt(pageable);
    }

    public void update(AuthUserUpdateDTO dto) {
        AuthUser entity = authUserRepository.findByChatId(dto.getChatId()).orElseThrow();
        AuthUser authUser = authUserMapper.fromUpdateDTO(dto, entity);
        authUserRepository.save(authUser);
    }

    public List<Book> getAllDownloadedBooks(AuthUserCriteria criteria) {
        // TODO: 5/4/2022 wrong approach, must pagination book list

//        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.Direction.DESC);
//        AuthUser authUser = authUserRepository.findByChatId(criteria.getChatId(), pageable);
//        return authUser.getBooks();
        return null;
    }

    public boolean hasBook(String chatId) {
        String bookId = UserState.getBookId(chatId);
        AuthUser authUser = authUserRepository.findByChatId(chatId).orElseThrow();
        return authUser.getBooks().stream().anyMatch(book -> book.getFileId().equals(bookId));
    }

    public AuthUser getByChatId(String chatId) {
        return authUserRepository.findByChatId(chatId).orElseThrow();
    }
}
