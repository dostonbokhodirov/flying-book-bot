package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Document;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.criteria.AuthUserCriteria;
import uz.doston.flyingbookbot.criteria.BookCriteria;
import uz.doston.flyingbookbot.dto.BookCreateDTO;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.enums.State;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.service.BookService;
import uz.doston.flyingbookbot.utils.*;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookProcessor {

    private final BookService bookService;
    private final AuthUserService authUserService;

    @Lazy
    private final MenuProcessor menuProcessor;
    private final Translate translate;
    private final MessageExecutor executor;

    private final Messages messages;

    public void topBookProcess(Message message) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);
        UserState.setPage(chatId, 0);

        BookCriteria bookCriteria = BookCriteria
                .childBuilder()
                .page(UserState.getPage(chatId))
                .size(UserState.getSize(chatId))
                .build();

        List<Book> books = bookService.getAllTopBooks(bookCriteria);

        if (books.size() == 0) {
            executor.sendMessage(chatId, translate.getTranslation("no.book", language));
        } else {
            String text = messages.bookMessage(books, chatId).toString();
            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
            executor.sendMessage(chatId, text, InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
        }
    }

    public List<InlineQueryResult> getAllByQueryMatches(String query) {
        return bookService.getAllByMatches(query);
    }

    public void addBookProcess(Message message, State state, AuthRole role) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);

        if (role.equals(AuthRole.USER)) {

            if (state.equals(State.UNDEFINED)) {

                executor.sendMessage(chatId, translate.getTranslation("upload.file", language));
                UserState.setState(chatId, State.ADD_FILE);

            } else if (state.equals(State.ADD_FILE)) {

                if (message.hasDocument()) {

                    executor.sendMessage(chatId,
                            "%s %s".formatted(Emojis.ADD, translate.getTranslation("file.sent", language)));

                    UserState.setState(chatId, State.UNDEFINED);
                    UserState.setMenuState(chatId, MenuState.UNDEFINED);

                    AuthUser authUser = authUserService.getRandomByRole(role);

                    executor.sendDocument(authUser.getChatId(), message.getDocument().getFileId());
                    menuProcessor.sendMainMenu(chatId, role, "");

                } else {

                    executor.sendMessage(
                            chatId,
                            translate.getTranslation("upload.file.again", language),
                            new ForceReplyKeyboard());

                }
            }
        } else {
            if (state.equals(State.UNDEFINED)) {

                executor.sendMessage(chatId, translate.getTranslation("upload.file", language));
                UserState.setState(chatId, State.ADD_FILE);

            } else if (state.equals(State.ADD_FILE)) {

                if (message.hasDocument()) {

                    Document document = message.getDocument();
                    UserState.setBookId(chatId, document.getFileId());

                    BookCreateDTO dto = UserState.getBookCreateDTO(chatId);
                    dto.setFileId(document.getFileId());
                    dto.setSize(document.getFileSize().toString());
                    dto.setName(document.getFileName());
                    dto.setDownloadsCount(0);
                    // TODO: 5/5/2022 owner id => id or chat id
                    dto.setOwnerId(1L);
                    UserState.setBookCreateDTO(chatId, dto);

                    executor.sendMessage(
                            chatId,
                            translate.getTranslation("choose.genre", language),
                            InlineKeyboard.genreButtons(chatId));

                    UserState.setState(chatId, State.ADD_GENRE);

                } else {

                    executor.sendMessage(
                            chatId,
                            translate.getTranslation("upload.file.again", language),
                            new ForceReplyKeyboard());

                }
            }
        }
    }

    public void searchBookProcess(Message message, State state) {

        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);

        UserState.setSearchName(chatId, text);

        if (State.SEARCH_NAME.equals(state)) {

            BookCriteria bookCriteria = BookCriteria
                    .childBuilder()
                    .page(UserState.getPage(chatId))
                    .size(UserState.getSize(chatId))
                    .name(text)
                    .build();

            List<Book> books = bookService.getAllByName(bookCriteria);

            SendMessage sendMessage;
            if (books.size() == 0) {

                executor.sendMessage(chatId, translate.getTranslation("no.book.name", language));
                UserState.setState(chatId, State.UNDEFINED);
                UserState.setMenuState(chatId, MenuState.UNDEFINED);

            } else {

                UserState.setPage(chatId, 0);
                List<Long> bookIds = books.stream().map(Book::getId).toList();
                executor.sendMessage(
                        chatId,
                        messages.bookMessage(books, chatId).toString(),
                        InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));

            }
        }
    }

    public void removeBookProcess(Message message, State state) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);

        if (state.equals(State.UNDEFINED)) {
            executor.sendMessage(chatId, translate.getTranslation("enter.remove.book", language));
            UserState.setState(chatId, State.REMOVE_NAME);
        } else if (state.equals(State.REMOVE_NAME)) {

            // TODO: 5/6/2022 bad approach to delete book | use findFirstByName instead of it

            BookCriteria bookCriteria = BookCriteria
                    .childBuilder()
                    .page(UserState.getPage(chatId))
                    .size(UserState.getSize(chatId))
                    .name(text)
                    .build();

            List<Book> books = bookService.getAllByName(bookCriteria);

            if (books.size() == 0) {

                executor.sendMessage(chatId,
                        "%s %s".formatted(translate.getTranslation("no.book", language), Emojis.CONFUSE));

            } else {

                bookService.deleteByName(text);
                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.ADD, translate.getTranslation("book.deleted", language)));

            }

            UserState.setState(chatId, State.UNDEFINED);
            UserState.setMenuState(chatId, MenuState.UNDEFINED);

        }
    }

    public void downloadedBookProcess(Message message) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);

        UserState.setPage(chatId, 0);

        AuthUserCriteria authUserCriteria = AuthUserCriteria
                .childBuilder()
                .chatId(chatId)
                .page(UserState.getPage(chatId))
                .size(UserState.getSize(chatId))
                .build();


        List<Book> books = authUserService.getAllDownloadedBooks(authUserCriteria);
        if (books.size() == 0) {

            executor.sendMessage(chatId, translate.getTranslation("no.downloaded.book", language));
            UserState.setState(chatId, State.UNDEFINED);

        } else {

            List<Long> bookIds = books.stream().map(Book::getId).toList();
            BookCriteria bookCriteria = BookCriteria
                    .childBuilder()
                    .page(UserState.getPage(chatId))
                    .size(UserState.getSize(chatId))
                    .build();

            executor.sendMessage(
                    chatId,
                    messages.bookMessage(books, chatId).toString(),
                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));

        }
    }

    public void uploadedBookProcess(Message message) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);
        UserState.setPage(chatId, 0);

        BookCriteria bookCriteria = BookCriteria
                .childBuilder()
                .page(UserState.getPage(chatId))
                .size(UserState.getSize(chatId))
                .build();

        List<Book> books = bookService.getAllUploadedBooks(bookCriteria, chatId);

        if (books.size() == 0) {

            executor.sendMessage(chatId, translate.getTranslation("no.uploaded.book", language));
            UserState.setState(chatId, State.UNDEFINED);

        } else {

            List<Long> bookIds = books.stream().map(Book::getId).toList();
            executor.sendMessage(
                    chatId,
                    messages.bookMessage(books, chatId).toString(),
                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));

        }
    }
}
