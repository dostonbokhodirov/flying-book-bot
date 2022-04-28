package uz.doston.flyingbookbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.repository.AuthUserRepository;
import uz.doston.flyingbookbot.utils.UserState;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthUserService {
    private final AuthUserRepository authUserRepository;


    @PostConstruct
    public void readLanguage() {
        List<AuthUser> userList = authUserRepository.findAll();
        userList.forEach(authUser ->
                UserState.setLanguage(authUser.getChatId(), authUser.getLanguage().toLowerCase()));
    }
}
