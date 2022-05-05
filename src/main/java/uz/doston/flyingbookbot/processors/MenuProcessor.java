package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.doston.flyingbookbot.buttons.ReplyKeyboard;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.utils.MessageExecutor;

@Component
@RequiredArgsConstructor
public class MenuProcessor {

    private final MessageExecutor executor;

    public void sendMainMenu(String chatId, AuthRole role, String text) {
        executor.sendMessage(chatId, text, getMenu(chatId, role));
    }

    private org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard getMenu(String chatId, AuthRole role) {
        return switch (role) {
            case ADMIN -> ReplyKeyboard.adminMenu(chatId);
            case MANAGER -> ReplyKeyboard.managerMenu(chatId);
            case USER -> ReplyKeyboard.userMenu(chatId);
        };
    }

}
