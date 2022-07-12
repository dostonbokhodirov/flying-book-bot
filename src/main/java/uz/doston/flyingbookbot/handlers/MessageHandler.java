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
    private final InlineKeyboard inlineKeyboard;
    private final ReplyKeyboard replyKeyboard;

    public void handle(Message message) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);
        String command = message.getText();
        State state = UserState.getState(chatId);
        MenuState menuState = UserState.getMenuState(chatId);

        AuthRole role = authUserService.getRoleByChatId(chatId);

        if ("/start".equals(command) || Objects.isNull(role)) {
            if (Objects.isNull(role)) {
                authorizationProcessor.process(message, state);
                return;
            }
            menuProcessor.sendMainMenu(chatId, role, "<b>%s</b>"
                    .formatted(translate.getTranslation("choose.menu", language)));
            return;
        } else if ("/settings".equals(command)) {
            UserState.setMenuState(chatId, MenuState.SETTINGS);

            executor.sendMessage(
                    chatId,
                    "<b>%s</b>".formatted(translate.getTranslation("settings.menu", language)),
                    replyKeyboard.settingsMenu(chatId));
            return;

        } else if ("/help".equals(command)) {

            executor.sendMessage(chatId, messages.helpMessage(chatId));
            return;

        } else if ("/search".equals(command)) {

            executor.sendMessage(
                    chatId,
                    translate.getTranslation("choose.search.type", language),
                    inlineKeyboard.searchButtons(chatId));

            UserState.setMenuState(chatId, MenuState.SEARCH);
            UserState.setPage(chatId, 0);
            return;

        } else if ("/top".equals(command)) {

            UserState.setMenuState(chatId, MenuState.TOP);
            bookProcessor.topBookProcess(message);
            return;

        } else if ("/users".equals(command)) {

            if (!role.equals(AuthRole.USER)) {
                UserState.setMenuState(chatId, MenuState.USER_LIST);
                UserState.setPage(chatId, 0);
                authUserProcessor.process(message);
            }
            return;

        } else if ("/post".equals(command)) {

            if (!role.equals(AuthRole.USER)) {

                UserState.setMenuState(chatId, MenuState.POST);
                executor.sendMessage(chatId, translate.getTranslation("send.post", language));
                return;

            }

        } else if ("/stats".equals(command)) {

            StringBuilder text = messages.statsMessage(chatId);
            executor.sendMessage(chatId, text.toString());
            return;

        } else if ("/whoami".equals(command)) {

            StringBuilder text = messages.detailAuthUserMessage(chatId);
            executor.sendMessage(chatId, text.toString());
            return;

        } else if ("/developers".equals(command)) {

            String text = messages.developerMessage(chatId);
            executor.sendMessage(chatId, text);
            return;

        }

        switch (menuState) {
            case UNDEFINED -> menuProcessor.process(message, role);
            case SETTINGS -> authUserProcessor.settingsProcess(message, state);
            case ADD_BOOK -> bookProcessor.addBookProcess(message, state, role);
            case SEARCH -> bookProcessor.searchBookProcess(message, state);
            case REMOVE_BOOK -> bookProcessor.removeBookProcess(message, state);
            case DOWNLOADED -> bookProcessor.downloadedBookProcess(message);
            case UPLOADED -> bookProcessor.uploadedBookProcess(message);
            case TOP -> bookProcessor.topBookProcess(message);
            case USER_LIST -> authUserProcessor.process(message);
            case ADD_MANAGER -> authUserProcessor.addManagerProcess(message, state);
            case REMOVE_MANAGER -> authUserProcessor.removeManagerProcess(message, state);
            case POST -> authUserProcessor.postProcess(message);
        }
    }
}
