package uz.doston.flyingbookbot.processors;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.doston.flyingbookbot.buttons.ReplyKeyboard;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.utils.Emojis;
import uz.doston.flyingbookbot.utils.MessageExecutor;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

@Component
@RequiredArgsConstructor
public class MenuProcessor {

    private final Translate translate;
    private final MessageExecutor executor;

    public void sendMainMenu(@NonNull String chatId, @NonNull AuthRole role, String text) {
        executor.sendMessage(chatId, text, getMenu(chatId, role));
    }

    private org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard getMenu(String chatId, AuthRole role) {
        return switch (role) {
            case ADMIN -> ReplyKeyboard.adminMenu(chatId);
            case MANAGER -> ReplyKeyboard.managerMenu(chatId);
            case USER -> ReplyKeyboard.userMenu(chatId);
        };
    }

    public void sendSettingsMenu(@NonNull String chatId, @NonNull String text) {
        executor.sendMessage(chatId, text.equals(
                translate.getTranslation("wrong.button", UserState.getLanguage(chatId)))
                ? "%s %s".formatted(Emojis.REMOVE, text)
                : text, ReplyKeyboard.settingsMenu(chatId));
    }

}
