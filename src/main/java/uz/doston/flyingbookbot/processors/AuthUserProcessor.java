package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.criteria.AuthUserCriteria;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.utils.MessageExecutor;
import uz.doston.flyingbookbot.utils.Messages;
import uz.doston.flyingbookbot.utils.UserState;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthUserProcessor {

    private final AuthUserService authUserService;
    private final MessageExecutor executor;

    private final Messages messages;

    public void process(Message message) {
        String chatId = message.getChatId().toString();
        AuthUserCriteria authUserCriteria = AuthUserCriteria
                .childBuilder()
                .page(UserState.getPage(chatId))
                .size(UserState.getSize(chatId))
                .build();

        List<AuthUser> authUsers = authUserService.getAll(authUserCriteria);

        String text = messages.authUserMessage(authUsers, chatId).toString();
        List<Long> authUserIds = authUsers.stream().map(AuthUser::getId).collect(Collectors.toList());
        executor.sendMessage(chatId, text, InlineKeyboard.bookOrUserButtons(authUserIds, authUserCriteria));
        UserState.setMenuState(chatId, MenuState.UNDEFINED);
    }
}
