package uz.doston.flyingbookbot.processors;

import lombok.NonNull;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.doston.flyingbookbot.buttons.ReplyKeyboard;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.utils.Emojis;
import uz.doston.flyingbookbot.utils.MessageExecutor;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

@Component
public class MenuProcessor {

    private final AuthUserService authUserService;
    private final BookProcessor bookProcessor;
    private final AuthUserProcessor authUserProcessor;
    private final Translate translate;
    private final MessageExecutor executor;
    private final ReplyKeyboard replyKeyboard;

    public MenuProcessor(AuthUserService authUserService,
                         @Lazy BookProcessor bookProcessor,
                         @Lazy AuthUserProcessor authUserProcessor,
                         Translate translate,
                         MessageExecutor executor,
                         ReplyKeyboard replyKeyboard) {
        this.authUserService = authUserService;
        this.bookProcessor = bookProcessor;
        this.authUserProcessor = authUserProcessor;
        this.translate = translate;
        this.executor = executor;
        this.replyKeyboard = replyKeyboard;
    }

    public void sendMainMenu(@NonNull String chatId, @NonNull AuthRole role, String text) {
        executor.sendMessage(chatId, text, getMenu(chatId, role));
    }

    private org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard getMenu(String chatId, AuthRole role) {

        return switch (role) {

            case ADMIN -> replyKeyboard.adminMenu(chatId);

            case MANAGER -> replyKeyboard.managerMenu(chatId);

            case USER -> replyKeyboard.userMenu(chatId);

        };
    }

    public void sendSettingsMenu(@NonNull String chatId, @NonNull String text) {
        executor.sendMessage(chatId, text.equals(
                translate.getTranslation("wrong.button", UserState.getLanguage(chatId)))
                ? "%s %s".formatted(Emojis.REMOVE, text)
                : text, replyKeyboard.settingsMenu(chatId));
    }

    public void process(Message message, AuthRole role) {
        switch (role) {
            case ADMIN -> adminMenuProcess(message);
            case MANAGER -> managerMenuProcess(message);
            case USER -> userMenuProcess(message);
        }
    }

    private void userMenuProcess(Message message) {

        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);
        AuthRole role = authUserService.getRoleByChatId(chatId);
        MenuState menuState = UserState.getMenuState(chatId);

        if (menuState.equals(MenuState.UNDEFINED)) {

            if (("%s %s".formatted(Emojis.ADD_BOOK, translate.getTranslation("add.book", language))).equals(text)) {

                UserState.setMenuState(chatId, MenuState.ADD_BOOK);
                bookProcessor.addBookProcess(message, UserState.getState(chatId), role);

            } else if (("%s %s".formatted(Emojis.DOWNLOAD, translate.getTranslation("downloaded.books", language))).equals(text)) {

                UserState.setMenuState(chatId, MenuState.DOWNLOADED);
                bookProcessor.downloadedBookProcess(message);

            } else {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REMOVE, translate.getTranslation("wrong.button", language)));

            }
        } else if (menuState.equals(MenuState.SETTINGS)) {

            authUserProcessor.settingsProcess(message, UserState.getState(chatId));

        }
    }

    private void managerMenuProcess(Message message) {

        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);
        AuthRole role = authUserService.getRoleByChatId(chatId);
        MenuState menuState = UserState.getMenuState(chatId);

        if (MenuState.UNDEFINED.equals(menuState)) {

            if (("%s %s".formatted(Emojis.ADD_BOOK, translate.getTranslation("add.book", language))).equals(text)) {

                bookProcessor.addBookProcess(message, UserState.getState(chatId), role);

            } else if (("%s %s".formatted(Emojis.REMOVE_BOOK, translate.getTranslation("remove.book", language))).equals(text)) {

                bookProcessor.removeBookProcess(message, UserState.getState(chatId));

            } else if (("%s %s".formatted(Emojis.DOWNLOAD, translate.getTranslation("downloaded.books", language))).equals(text)) {

                bookProcessor.downloadedBookProcess(message);

            } else if (("%s %s".formatted(Emojis.UPLOAD, translate.getTranslation("uploaded.books", language))).equals(text)) {

                bookProcessor.uploadedBookProcess(message);

            } else {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REMOVE, translate.getTranslation("wrong.button", language)));

            }
        }
    }

    private void adminMenuProcess(Message message) {

        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);
        AuthRole role = authUserService.getRoleByChatId(chatId);
        MenuState menuState = UserState.getMenuState(chatId);

        if (MenuState.UNDEFINED.equals(menuState)) {

            if (("%s %s".formatted(Emojis.ADD, translate.getTranslation("add.manager", language))).equals(text)) {

                authUserProcessor.addManagerProcess(message, UserState.getState(chatId));

            } else if (("%s %s".formatted(Emojis.REMOVE, translate.getTranslation("remove.manager", language))).equals(text)) {

                authUserProcessor.removeManagerProcess(message, UserState.getState(chatId));

            } else if (("%s %s".formatted(Emojis.ADD_BOOK, translate.getTranslation("add.book", language))).equals(text)) {

                bookProcessor.addBookProcess(message, UserState.getState(chatId), role);

            } else if (("%s %s".formatted(Emojis.REMOVE_BOOK, translate.getTranslation("remove.book", language))).equals(text)) {

                bookProcessor.removeBookProcess(message, UserState.getState(chatId));

            } else if (("%s %s".formatted(Emojis.DOWNLOAD, translate.getTranslation("downloaded.books", language))).equals(text)) {

                bookProcessor.downloadedBookProcess(message);

            } else if (("%s %s".formatted(Emojis.UPLOAD, translate.getTranslation("uploaded.books", language))).equals(text)) {

                bookProcessor.uploadedBookProcess(message);

            } else {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REMOVE, translate.getTranslation("wrong.button", language)));

            }
        }
    }
}
