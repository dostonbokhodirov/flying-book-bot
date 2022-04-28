package uz.doston.flyingbookbot.buttons;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import uz.doston.flyingbookbot.utils.Emojis;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

import java.util.Arrays;
import java.util.List;

public class ReplyKeyboard {
    private static Translate translate;

    public static ReplyKeyboardMarkup sharePhoneNumber(String chatId) {
        String language = UserState.getLanguage(chatId);

        KeyboardButton phone = getKeyboardButton(
                "%s %s".formatted(Emojis.PHONE, translate.getTranslation("{share.phone.number}", language)),
                true);

        KeyboardRow row = prepareKeyboardRow(phone);

        return getReplyKeyboardMarkup(List.of(row), true, true, true);
    }

    public static ReplyKeyboardMarkup settingsMenu(String chatId) {
        String language = UserState.getLanguage(chatId);

        KeyboardButton name = getKeyboardButton("%s %s"
                .formatted(Emojis.NAME, translate.getTranslation("change.name", language)));
        KeyboardButton age = getKeyboardButton("%s %s"
                .formatted(Emojis.AGE, translate.getTranslation("change.birthdate", language)));
        KeyboardButton phone = getKeyboardButton("%s %s"
                .formatted(Emojis.PHONE, translate.getTranslation("change.phone.number", language)));
        KeyboardButton lang = getKeyboardButton("%s %s"
                .formatted(Emojis.LANGUAGE, translate.getTranslation("change.language", language)));
        KeyboardButton limit = getKeyboardButton("%s %s"
                .formatted(Emojis.LIMIT, translate.getTranslation("change.limit", language)));
        KeyboardButton back = getKeyboardButton("%s %s"
                .formatted(Emojis.BACK, translate.getTranslation("back", language)));

        KeyboardRow row1 = prepareKeyboardRow(name, age);
        KeyboardRow row2 = prepareKeyboardRow(phone, lang);
        KeyboardRow row3 = prepareKeyboardRow(limit);
        KeyboardRow row4 = prepareKeyboardRow(back);

        return getReplyKeyboardMarkup(List.of(row1, row2, row3, row4), true, true, true);
    }

    public static ReplyKeyboardMarkup userMenu(String chatId) {
        String language = UserState.getLanguage(chatId);

        KeyboardButton addBook = getKeyboardButton("%s %s"
                .formatted(Emojis.ADD_BOOK, translate.getTranslation("add.book", language)));
        KeyboardButton download = getKeyboardButton("%s %s"
                .formatted(Emojis.DOWNLOAD, translate.getTranslation("downloaded.books", language)));

        KeyboardRow row1 = prepareKeyboardRow(addBook, download);

        return getReplyKeyboardMarkup(List.of(row1), true, true, true);
    }

    public static ReplyKeyboardMarkup managerMenu(String chatId) {
        String language = UserState.getLanguage(chatId);

        KeyboardButton download = getKeyboardButton("%s %s"
                .formatted(Emojis.DOWNLOAD, translate.getTranslation("downloaded.books", language)));
        KeyboardButton upload = getKeyboardButton("%s %s"
                .formatted(Emojis.UPLOAD, translate.getTranslation("uploaded.books", language)));

        KeyboardRow row1 = addRemoveBookButtons(language);
        KeyboardRow row2 = prepareKeyboardRow(download, upload);

        return getReplyKeyboardMarkup(List.of(row1, row2), true, true, true);
    }

    public static ReplyKeyboardMarkup adminMenu(String chatId) {
        String language = UserState.getLanguage(chatId);

        KeyboardButton addManager = getKeyboardButton("%s %s"
                .formatted(Emojis.ADD, translate.getTranslation("add.manager", language)));
        KeyboardButton removeManager = getKeyboardButton("%s %s"
                .formatted(Emojis.REMOVE, translate.getTranslation("remove.manager", language)));
        KeyboardButton downloadedBook = getKeyboardButton("%s %s"
                .formatted(Emojis.DOWNLOAD, translate.getTranslation("downloaded.books", language)));
        KeyboardButton uploadedBook = getKeyboardButton("%s %s"
                .formatted(Emojis.UPLOAD, translate.getTranslation("uploaded.books", language)));

        KeyboardRow row1 = prepareKeyboardRow(addManager, removeManager);
        KeyboardRow row2 = addRemoveBookButtons(language);
        KeyboardRow row3 = prepareKeyboardRow(downloadedBook, uploadedBook);

        return getReplyKeyboardMarkup(List.of(row1, row2, row3), true, true, true);
    }

    private static KeyboardRow addRemoveBookButtons(String language) {
        KeyboardButton addBook = getKeyboardButton("%s %s"
                .formatted(Emojis.ADD_BOOK, translate.getTranslation("add.book", language)));
        KeyboardButton removeBook = getKeyboardButton("%s %s"
                .formatted(Emojis.REMOVE_BOOK, translate.getTranslation("remove.book", language)));
        return prepareKeyboardRow(addBook, removeBook);
    }

    private static KeyboardButton getKeyboardButton(@NonNull String text) {
        return getKeyboardButton(text, false);
    }

    private static KeyboardButton getKeyboardButton(@NonNull String text, boolean requestContact) {
        KeyboardButton button = new KeyboardButton();
        button.setText(text);
        button.setRequestContact(requestContact);
        return button;
    }

    private static KeyboardRow prepareKeyboardRow(KeyboardButton... buttons) {
        return new KeyboardRow(Arrays.asList(buttons));
    }

    private static ReplyKeyboardMarkup getReplyKeyboardMarkup(@NonNull List<KeyboardRow> keyboards) {
        return getReplyKeyboardMarkup(keyboards, false, false, false);
    }

    private static ReplyKeyboardMarkup getReplyKeyboardMarkup(
            @NonNull List<KeyboardRow> keyboards,
            boolean resizeKeyboard,
            boolean oneTimeKeyboard,
            boolean selective) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(keyboards);
        replyKeyboardMarkup.setResizeKeyboard(resizeKeyboard);
        replyKeyboardMarkup.setOneTimeKeyboard(oneTimeKeyboard);
        replyKeyboardMarkup.setSelective(selective);
        return replyKeyboardMarkup;
    }
}
