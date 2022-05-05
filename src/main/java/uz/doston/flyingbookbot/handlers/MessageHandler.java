package uz.doston.flyingbookbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.buttons.ReplyKeyboard;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.enums.State;
import uz.doston.flyingbookbot.processors.AuthUserProcessor;
import uz.doston.flyingbookbot.processors.AuthorizationProcessor;
import uz.doston.flyingbookbot.processors.BookProcessor;
import uz.doston.flyingbookbot.processors.MenuProcessor;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.utils.MessageExecutor;
import uz.doston.flyingbookbot.utils.Messages;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final AuthorizationProcessor authorizationProcessor;
    private final MenuProcessor menuProcessor;
    private final BookProcessor bookProcessor;
    private final AuthUserProcessor authUserProcessor;
    private final AuthUserService authUserService;
    private final Messages messages;

    private final MessageExecutor executor;
    private final Translate translate;

    public void handle(Message message) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);
        String command = message.getText();
        State state = UserState.getState(chatId);
        MenuState menuState = UserState.getMenuState(chatId);

        AuthRole role = authUserService.getRoleByChatId(chatId);

        switch (command) {
            case "/start":
                if (Objects.isNull(role)) {
                    authorizationProcessor.process(message, state);
                } else {
                    menuProcessor.sendMainMenu(chatId, role, "<b>%s</b>"
                            .formatted(translate.getTranslation("choose.menu", language)));
                }
                return;
            case "/settings":
                UserState.setMenuState(chatId, MenuState.SETTINGS);

                executor.sendMessage(
                        chatId,
                        "<b>%s</b>".formatted(translate.getTranslation("settings.menu", language)),
                        ReplyKeyboard.settingsMenu(chatId));
                return;
            case "/help":
                executor.sendMessage(chatId, messages.helpMessage(chatId));
                return;
            case "/search":
                executor.sendMessage(
                        chatId,
                        translate.getTranslation("choose.search.type", language),
                        InlineKeyboard.searchButtons(chatId));

                UserState.setMenuState(chatId, MenuState.SEARCH);
                UserState.setPage(chatId, 0);
                return;
            case "/top":
                UserState.setMenuState(chatId, MenuState.TOP);
                bookProcessor.topBookProcess(message);
                return;
            case "/users":
                if (!role.equals(AuthRole.USER)) {
                    UserState.setMenuState(chatId, MenuState.USER_LIST);
                    UserState.setPage(chatId, 0);
                    authUserProcessor.process(message);
                }
                return;
            case "/post":
                if (!role.equals(AuthRole.USER)) {
                    UserState.setMenuState(chatId, MenuState.POST);
                    executor.sendMessage(chatId, translate.getTranslation("send.post", language));
                    return;
                }
                break;
            case "/stats": {
                StringBuilder text = messages.statsMessage(chatId);
                executor.sendMessage(chatId, text.toString());
                return;
            }
            case "/whoami": {
                StringBuilder text = authUserService.detailMessage(chatId);
                executor.sendMessage(chatId, text.toString());
                return;
            }
            case "/developers": {
                String text = messages.developerMessage(chatId);
                executor.sendMessage(chatId, text);
                return;
            }
        }

        switch (menuState) {
            case UNDEFINED -> menuProcessor.sendMainMenu(chatId, role, "");
            case SETTINGS -> authUserProcessor.settingsProcess(message, state);
            case ADD_BOOK -> bookProcessor.addBookProcess(message, state, role);
            case SEARCH -> bookProcessor.searchBookProcess(message, state);
            case REMOVE_BOOK -> bookProcessor.removeBookProcess(message, state);
            case DOWNLOADED -> bookProcessor.downloadedBookProcess(message);
            case UPLOADED -> bookProcessor.uploadedBookProcess(message);
            case TOP -> bookProcessor.topBookProcess(message);
            case USER_LIST -> authUserProcessor.authUserListProcess(message);
            case ADD_MANAGER -> authUserProcessor.addManagerProcess(message, state);
            case REMOVE_MANAGER -> authUserProcessor.removeManagerProcess(message, UserState.getState(chatId));
            case POST -> authUserProcessor.postProcess(message);
        }
    }
}
