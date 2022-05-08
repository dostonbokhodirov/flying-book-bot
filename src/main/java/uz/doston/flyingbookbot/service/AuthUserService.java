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
import uz.doston.flyingbookbot.utils.UserState;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserService {

    private final AuthUserMapper authUserMapper;
    private final AuthUserRepository authUserRepository;

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

    public List<AuthUser> getAll(AuthUserCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.by("createdAt").descending());
        return authUserRepository.findAll(pageable).getContent();
    }

    public void update(AuthUserUpdateDTO dto) {
        AuthUser entity = authUserRepository.findByChatId(dto.getChatId()).orElseThrow();
        AuthUser authUser = authUserMapper.fromUpdateDTO(dto, entity);
        authUserRepository.save(authUser);
    }

    public List<Book> getAllDownloadedBooks(AuthUserCriteria criteria) {
        return authUserRepository.findDownloadedBooksByChatId(
                criteria.getChatId(),
                criteria.getPage(),
                criteria.getSize()
        );
    }

    public boolean hasBook(String chatId) {
        String bookId = UserState.getBookId(chatId);
        AuthUser authUser = authUserRepository.findByChatId(chatId).orElseThrow();
        return authUser.getBooks().stream().anyMatch(book -> book.getFileId().equals(bookId));
    }

    public AuthUser getByChatId(String chatId) {
        return authUserRepository.findByChatId(chatId).orElseThrow();
    }

    public String getFullNameByChatId(String chatId) {
        return authUserRepository.findFullNameByChatId(chatId);
    }

    public Integer getAgeByChatId(String chatId) {
        return authUserRepository.findAgeByChatId(chatId);
    }

    public String getPhoneNumberByChatId(String chatId) {
        return authUserRepository.findPhoneNumberByChatId(chatId);
    }

    public AuthUser getRandomByRole(AuthRole role) {
        return authUserRepository.findRandomByRole(role);
    }

    public AuthRole getRoleByPhoneNumber(String phoneNumber) {
        return authUserRepository.findAuthRoleByPhoneNumber(phoneNumber);
    }

    public List<String> getAllChatIdsByRole(AuthRole role) {
        List<AuthUser> authUsers = authUserRepository.findAll();
        return authUsers.stream().map(AuthUser::getChatId).toList();
    }
}
