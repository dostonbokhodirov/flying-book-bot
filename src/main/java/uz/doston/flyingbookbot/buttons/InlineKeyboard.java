package uz.doston.flyingbookbot.buttons;

import lombok.NonNull;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import uz.doston.flyingbookbot.criteria.Criteria;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.utils.Emojis;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class InlineKeyboard {

    private static Translate translate;
    private static final InlineKeyboardMarkup board = new InlineKeyboardMarkup();

    public static InlineKeyboardMarkup searchButtons(String chatId) {
        String language = UserState.getLanguage(chatId);

        InlineKeyboardButton genre = getInlineKeyboardButton("%s %s"
                        .formatted(Emojis.GENRE, translate.getTranslation("search.by.genre", language)),
                "genre");
        InlineKeyboardButton name = getInlineKeyboardButton("%s %s"
                        .formatted(Emojis.NAME, translate.getTranslation("search.by.name", language)),
                "name");

        return getInlineKeyboardMarkup(genre, name);
    }

    public static InlineKeyboardMarkup languageButtons() {
        InlineKeyboardButton uz = getInlineKeyboardButton("%s O'zbek".formatted(Emojis.UZ), "uz");
        InlineKeyboardButton ru = getInlineKeyboardButton("%s Русский".formatted(Emojis.RU), "ru");
        InlineKeyboardButton en = getInlineKeyboardButton("%s English".formatted(Emojis.EN), "en");
        return getInlineKeyboardMarkup(uz, ru, en);
    }

    public static ReplyKeyboard genderButtons(String chatId) {
        String language = UserState.getLanguage(chatId);

        InlineKeyboardButton male = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.MALE, translate.getTranslation("male", language)),
                "male");
        InlineKeyboardButton female = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.FEMALE, translate.getTranslation("female", language)),
                "female");

        return getInlineKeyboardMarkup(male, female);
    }

    public static InlineKeyboardMarkup bookOrUserButtons(List<Long> idList, Criteria criteria) {

        Integer page = criteria.getPage();
        Integer size = criteria.getSize();

        List<String> numbers = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> firstNumberButtons = new ArrayList<>();
        List<InlineKeyboardButton> secondNumberButtons = new ArrayList<>();
        int i = 1;
        if (idList.size() <= size) {
            for (Long id : idList) {
                InlineKeyboardButton button =
                        getInlineKeyboardButton(numbers.get(i++ - 1), String.valueOf(id));
                firstNumberButtons.add(button);
            }
            buttons.add(firstNumberButtons);
        } else {
            for (int j = 0; j < idList.size(); j++) {
                InlineKeyboardButton button =
                        getInlineKeyboardButton(numbers.get(i++ - 1), String.valueOf(idList.get(j)));
                if (j >= idList.size() / 2) secondNumberButtons.add(button);
                else firstNumberButtons.add(button);
            }
            buttons.add(firstNumberButtons);
            buttons.add(secondNumberButtons);
        }

        List<InlineKeyboardButton> extraButtons = extraButtons(idList, page, size);
        buttons.add(extraButtons);
        board.setKeyboard(buttons);
        return board;
    }

//    public static InlineKeyboardMarkup book(ArrayList<Book> books, Integer page, Integer size) {
//        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        List<InlineKeyboardButton> firstNumberButtons = new ArrayList<>();
//        List<InlineKeyboardButton> secondNumberButtons = new ArrayList<>();
//        List<String> numbers = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
//        int i = 1;
//        if (books.size() <= 5) {
//            for (Book book : books) {
//                InlineKeyboardButton button =
//                        getInlineKeyboardButton(numbers.get(i++ - 1), String.valueOf(book.getId()));
//                firstNumberButtons.add(button);
//            }
//            buttons.add(firstNumberButtons);
//        } else {
//            for (int j = 0; j < books.size(); j++) {
//                InlineKeyboardButton button =
//                        getInlineKeyboardButton(numbers.get(i++ - 1), String.valueOf(books.get(j).getId()));
//                if (j >= books.size() / 2) secondNumberButtons.add(button);
//                else firstNumberButtons.add(button);
//            }
//            buttons.add(firstNumberButtons);
//            buttons.add(secondNumberButtons);
//        }
//
//        List<Long> idList = books.stream().map(Book::getId).collect(Collectors.toList());
//
//        List<InlineKeyboardButton> extraButtons = extraButtons(idList, page, size);
//        buttons.add(extraButtons);
//        board.setKeyboard(buttons);
//        return board;
//    }

//    public static InlineKeyboardMarkup user(ArrayList<AuthUser> users, Integer page, Integer size) {
//        List<String> numbers = new ArrayList<>(Arrays.asList("1️⃣", "2️⃣", "3️⃣", "4️⃣", "5️⃣", "6️⃣", "7️⃣", "8️⃣", "9️⃣", "\uD83D\uDD1F"));
//        InlineKeyboardMarkup board = new InlineKeyboardMarkup();
//        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
//        List<InlineKeyboardButton> firstNumberButtons = new ArrayList<>();
//        List<InlineKeyboardButton> secondNumberButtons = new ArrayList<>();
//        int i = 1;
//        if (users.size() <= 5) {
//            for (AuthUser user : users) {
//                InlineKeyboardButton button =
//                        getInlineKeyboardButton(numbers.get(i++ - 1), String.valueOf(user.getId()));
//                firstNumberButtons.add(button);
//            }
//            buttons.add(firstNumberButtons);
//        } else {
//            for (int j = 0; j < users.size(); j++) {
//                InlineKeyboardButton button =
//                        getInlineKeyboardButton(numbers.get(i++ - 1), String.valueOf(users.get(j).getId()));
//                if (j >= users.size() / 2) secondNumberButtons.add(button);
//                else firstNumberButtons.add(button);
//            }
//            buttons.add(firstNumberButtons);
//            buttons.add(secondNumberButtons);
//        }
//
//        List<Long> idList = users.stream().map(AuthUser::getId).collect(Collectors.toList());
//        List<InlineKeyboardButton> extraButtons = extraButtons(idList, page, size);
//        buttons.add(extraButtons);
//        board.setKeyboard(buttons);
//        return board;
//    }

    public static List<InlineKeyboardButton> extraButtons(List<Long> idList, Integer page, Integer size) {
        List<InlineKeyboardButton> extraButtons = new ArrayList<>();
        if (page > 0) {
            InlineKeyboardButton prevButton = getInlineKeyboardButton(Emojis.PREVIOUS, "prev");
            extraButtons.add(prevButton);
        }
        InlineKeyboardButton cancelButton = getInlineKeyboardButton(Emojis.REMOVE, "cancel");
        extraButtons.add(cancelButton);
        if (idList.size() == size) {
            InlineKeyboardButton nextButton = getInlineKeyboardButton(Emojis.NEXT, "next");
            extraButtons.add(nextButton);
        }
        return extraButtons;
    }

    public static InlineKeyboardMarkup genreButtons(String chatId) {
        String language = UserState.getLanguage(chatId);

        InlineKeyboardButton adventure = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.ADVENTURE, translate.getTranslation("genre.adventure", language)),
                "adventure");
        InlineKeyboardButton classic = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.CLASSIC, translate.getTranslation("genre.classic", language)),
                "classic");
        InlineKeyboardButton comic = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.COMIC, translate.getTranslation("genre.comic", language)),
                "comic");
        InlineKeyboardButton fiction = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.FICTION, translate.getTranslation("genre.fiction", language)),
                "fiction");
        InlineKeyboardButton horror = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.HORROR, translate.getTranslation("genre.horror", language)),
                "horror");
        InlineKeyboardButton scientific = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.SCIENCE, translate.getTranslation("genre.scientific", language)),
                "scientific");
        InlineKeyboardButton other = getInlineKeyboardButton(
                "%s %s".formatted(Emojis.OTHER, translate.getTranslation("genre.other", language)),
                "other");

        List<InlineKeyboardButton> row1 = new ArrayList<>(List.of(adventure, classic));
        List<InlineKeyboardButton> row2 = new ArrayList<>(List.of(comic, fiction));
        List<InlineKeyboardButton> row3 = new ArrayList<>(List.of(horror, scientific));
        List<InlineKeyboardButton> row4 = new ArrayList<>(List.of(other));

        return new InlineKeyboardMarkup(List.of(row1, row2, row3, row4));
    }

    public static InlineKeyboardMarkup sizeButtons() {
        InlineKeyboardButton five = getInlineKeyboardButton("5️⃣", "five");
        InlineKeyboardButton eight = getInlineKeyboardButton("8️⃣", "eight");
        InlineKeyboardButton ten = getInlineKeyboardButton("\uD83D\uDD1F", "ten");
        return getInlineKeyboardMarkup(five, eight, ten);
    }

    public static ReplyKeyboard documentButtons(String chatId) {
        MenuState menuState = UserState.getMenuState(chatId);

        InlineKeyboardButton button = menuState.equals(MenuState.DOWNLOADED)
                ? getInlineKeyboardButton(Emojis.REMOVE_BOOK, "remove")
                : getInlineKeyboardButton(Emojis.ADD_BOOK, "add");
        InlineKeyboardButton cancel = getInlineKeyboardButton(Emojis.REMOVE, "cancelDocument");
        return getInlineKeyboardMarkup(button, cancel);
    }

    public static InlineKeyboardButton getInlineKeyboardButton(@NonNull String text, @NonNull String callBackData) {
        return getInlineKeyboardButton(text, callBackData, null);
    }

    public static InlineKeyboardButton getInlineKeyboardButton(@NonNull String text, @NonNull String callBackData, String url) {
        InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
        inlineKeyboardButton.setText(text);
        inlineKeyboardButton.setCallbackData(callBackData);
        inlineKeyboardButton.setUrl(url);
        return inlineKeyboardButton;
    }

    public static InlineKeyboardMarkup getInlineKeyboardMarkup(InlineKeyboardButton... buttons) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.setKeyboard(Arrays.stream(buttons).map(List::of).collect(Collectors.toList()));
        return inlineKeyboardMarkup;
    }

    private static List<InlineKeyboardButton> getRow(InlineKeyboardButton... buttons) {
        return Arrays.stream(buttons).toList();
    }
}
