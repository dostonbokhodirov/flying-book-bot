package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.buttons.ReplyKeyboard;
import uz.doston.flyingbookbot.criteria.AuthUserCriteria;
import uz.doston.flyingbookbot.criteria.BookCriteria;
import uz.doston.flyingbookbot.dto.AuthUserCreateDTO;
import uz.doston.flyingbookbot.dto.AuthUserUpdateDTO;
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
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CallbackProcessor {

    private final AuthUserService authUserService;
    private final BookService bookService;
    private final MessageExecutor executor;
    private final Translate translate;
    private final Messages messages;

    public void languageProcess(Message message, String data) {
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        String language = UserState.getLanguage(chatId);
        AuthRole role = authUserService.getRoleByChatId(chatId);

        executor.deleteMessage(chatId, messageId);

        if (Objects.isNull(role)) {

            AuthUserCreateDTO dto = UserState.getUserCreateDTO(chatId);
            dto.setLanguage(data);
            UserState.setUserCreateDTO(chatId, dto);

            executor.sendMessage(chatId, translate.getTranslation("enter.full.name", language), new ForceReplyKeyboard());

            UserState.setState(chatId, State.USER_FULL_NAME);

        } else {
            executor.sendMessage(chatId, "%s %s\n%s%s".formatted(Emojis.ADD, translate.getTranslation("language.changed", language), translate.getTranslation("current.language", language), data));
            executor.sendMessage(chatId, translate.getTranslation("settings.menu", language), ReplyKeyboard.settingsMenu(chatId));

            AuthUserUpdateDTO dto = AuthUserUpdateDTO.builder().chatId(chatId).language(data).build();

            authUserService.update(dto);
            UserState.setState(chatId, State.UNDEFINED);
        }
    }

    public void genderProcess(Message message, String data) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);

        executor.deleteMessage(chatId, message.getMessageId());
        executor.sendMessage(chatId, "%s %s:".formatted(Emojis.PHONE, translate.getTranslation("share.phone.number", language)), ReplyKeyboard.sharePhoneNumber(chatId));

        AuthUserCreateDTO dto = UserState.getUserCreateDTO(chatId);
        dto.setGender(data);
        UserState.setUserCreateDTO(chatId, dto);

        UserState.setState(chatId, State.USER_PHONE_NUMBER);
    }

//    public void prevProcess(Message message, Integer page) {
//
//        String chatId = message.getChatId().toString();
//        Integer messageId = message.getMessageId();
//        List<Book> books;
//        BookCriteria bookCriteria;
//
//        if (UserState.getMenuState(chatId).equals(MenuState.SEARCH)) {
//
//            if (UserState.getState(chatId).equals(State.SEARCH_NAME)) {
//
//                bookCriteria = BookCriteria
//                        .childBuilder()
//                        .name(UserState.getSearchName(chatId))
//                        .page(page)
//                        .size(UserState.getSize(chatId))
//                        .build();
//                books = bookService.getAllByName(bookCriteria);
//
//            } else {
//
//                bookCriteria = BookCriteria
//                        .childBuilder()
//                        .genre(UserState.getSearchGenre(chatId))
//                        .page(page)
//                        .size(UserState.getSize(chatId))
//                        .build();
//                books = bookService.getAllByGenre(bookCriteria);
//
//            }
//
//            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.TOP)) {
//
//            bookCriteria = BookCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            books = bookService.getAllTopBooks(bookCriteria);
//            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.UPLOADED)) {
//
//            bookCriteria = BookCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            books = bookService.getAllByUploaded(bookCriteria, chatId);
//            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
//
//            bookCriteria = BookCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            AuthUserCriteria authUserCriteria = AuthUserCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .chatId(chatId)
//                    .build();
//
//            books = authUserService.getAllDownloadedBooks(authUserCriteria);
//            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.USER_LIST)) {
//
//            AuthUserCriteria authUserCriteria = AuthUserCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            List<AuthUser> authUsers = authUserService.getAll(authUserCriteria);
//            List<Long> authUserIds = authUsers.stream().map(AuthUser::getId).toList();
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.authUserMessage(authUsers, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(authUserIds, authUserCriteria));
//
//        }
//    }

//    public void nextProcess(Message message, Integer page) {
//        String chatId = message.getChatId().toString();
//        Integer messageId = message.getMessageId();
//        List<Book> books;
//        BookCriteria bookCriteria;
//
//        if (UserState.getMenuState(chatId).equals(MenuState.SEARCH)) {
//
//            if (UserState.getState(chatId).equals(State.SEARCH_NAME)) {
//
//                bookCriteria = BookCriteria
//                        .childBuilder()
//                        .name(UserState.getSearchName(chatId))
//                        .page(page)
//                        .size(UserState.getSize(chatId))
//                        .build();
//
//                books = bookService.getAllByName(bookCriteria);
//
//            } else {
//
//                bookCriteria = BookCriteria
//                        .childBuilder()
//                        .genre(UserState.getSearchGenre(chatId))
//                        .page(page)
//                        .size(UserState.getSize(chatId))
//                        .build();
//
//                books = bookService.getAllByGenre(bookCriteria);
//
//            }
//
//            List<Long> bookIds = books.stream().map(Book::getId).toList();
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.UPLOADED)) {
//
//            bookCriteria = BookCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            books = bookService.getAllByUploaded(bookCriteria, chatId);
//            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.TOP)) {
//
//            bookCriteria = BookCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            books = bookService.getAllTopBooks(bookCriteria);
//            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
//
//            bookCriteria = BookCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            AuthUserCriteria authUserCriteria = AuthUserCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .chatId(chatId)
//                    .build();
//
//            books = authUserService.getAllDownloadedBooks(authUserCriteria);
//            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.bookMessage(books, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
//
//        } else if (UserState.getMenuState(chatId).equals(MenuState.USER_LIST)) {
//
//            AuthUserCriteria authUserCriteria = AuthUserCriteria
//                    .childBuilder()
//                    .page(page)
//                    .size(UserState.getSize(chatId))
//                    .build();
//
//            List<AuthUser> authUsers = authUserService.getAll(authUserCriteria);
//            List<Long> authUserIds = authUsers.stream().map(AuthUser::getId).toList();
//
//            executor.editMessage(
//                    chatId,
//                    messageId,
//                    messages.authUserMessage(authUsers, chatId).toString(),
//                    InlineKeyboard.bookOrUserButtons(authUserIds, authUserCriteria));
//
//        }
//    }

    public void nextOrPrevProcess(Message message) {

        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        Integer page = UserState.getPage(chatId);
        List<Book> books;
        BookCriteria bookCriteria;

        if (UserState.getMenuState(chatId).equals(MenuState.SEARCH)) {

            if (UserState.getState(chatId).equals(State.SEARCH_NAME)) {

                bookCriteria = BookCriteria
                        .childBuilder()
                        .name(UserState.getSearchName(chatId))
                        .page(page)
                        .size(UserState.getSize(chatId))
                        .build();
                books = bookService.getAllByName(bookCriteria);

            } else {

                bookCriteria = BookCriteria
                        .childBuilder()
                        .genre(UserState.getSearchGenre(chatId))
                        .page(page)
                        .size(UserState.getSize(chatId))
                        .build();
                books = bookService.getAllByGenre(bookCriteria);

            }

            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());
            executor.editMessage(
                    chatId,
                    messageId,
                    messages.bookMessage(books, chatId).toString(),
                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));

        } else if (UserState.getMenuState(chatId).equals(MenuState.TOP)) {

            bookCriteria = BookCriteria
                    .childBuilder()
                    .page(page)
                    .size(UserState.getSize(chatId))
                    .build();

            books = bookService.getAllTopBooks(bookCriteria);
            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());

            executor.editMessage(
                    chatId,
                    messageId,
                    messages.bookMessage(books, chatId).toString(),
                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));

        } else if (UserState.getMenuState(chatId).equals(MenuState.UPLOADED)) {

            bookCriteria = BookCriteria
                    .childBuilder()
                    .page(page)
                    .size(UserState.getSize(chatId))
                    .build();

            books = bookService.getAllUploadedBooks(bookCriteria, chatId);
            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());

            executor.editMessage(
                    chatId,
                    messageId,
                    messages.bookMessage(books, chatId).toString(),
                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));

        } else if (UserState.getMenuState(chatId).equals(MenuState.DOWNLOADED)) {

            bookCriteria = BookCriteria
                    .childBuilder()
                    .page(page)
                    .size(UserState.getSize(chatId))
                    .build();

            AuthUserCriteria authUserCriteria = AuthUserCriteria.childBuilder().page(page).size(UserState.getSize(chatId)).chatId(chatId).build();

            books = authUserService.getAllDownloadedBooks(authUserCriteria);
            List<Long> bookIds = books.stream().map(Book::getId).collect(Collectors.toList());

            executor.editMessage(
                    chatId,
                    messageId,
                    messages.bookMessage(books, chatId).toString(),
                    InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));

        } else if (UserState.getMenuState(chatId).equals(MenuState.USER_LIST)) {

            AuthUserCriteria authUserCriteria = AuthUserCriteria
                    .childBuilder()
                    .page(page)
                    .size(UserState.getSize(chatId))
                    .build();

            List<AuthUser> authUsers = authUserService.getAll(authUserCriteria);
            List<Long> authUserIds = authUsers.stream().map(AuthUser::getId).toList();

            executor.editMessage(
                    chatId,
                    messageId,
                    messages.authUserMessage(authUsers, chatId).toString(),
                    InlineKeyboard.bookOrUserButtons(authUserIds, authUserCriteria));

        }
    }

    public void cancelProcess(Message message) {
        Integer messageId = message.getMessageId();
        String chatId = message.getChatId().toString();
        UserState.setPage(chatId, 0);
        executor.deleteMessage(chatId, messageId);
        UserState.setMenuState(chatId, MenuState.UNDEFINED);
        UserState.setState(chatId, State.UNDEFINED);
    }

    public void addProcess(Message message) {
        String chatId = message.getChatId().toString();
        String bookId = UserState.getBookId(chatId);
        String language = UserState.getLanguage(chatId);

        if (!authUserService.hasBook(chatId)) {

            AuthUser authUser = authUserService.getByChatId(chatId);
            Book book = bookService.getByFileId(bookId);
            List<Book> books = authUser.getBooks();
            books.add(book);

            AuthUserUpdateDTO dto = AuthUserUpdateDTO
                    .builder()
                    .chatId(chatId)
                    .books(books)
                    .build();

            authUserService.update(dto);
            executor.sendMessage(chatId,
                    "%s%s".formatted(Emojis.ADD, translate.getTranslation("book.added", language)));

        } else
            executor.sendMessage(chatId,
                    "%s%s".formatted(Emojis.LOOK, translate.getTranslation("already.add", language)));
    }

    public void removeProcess(Message message) {

        String chatId = message.getChatId().toString();
        String bookId = UserState.getBookId(chatId);
        String language = UserState.getLanguage(chatId);

        AuthUser authUser = authUserService.getByChatId(chatId);
        Book book = bookService.getByFileId(bookId);
        List<Book> books = authUser.getBooks();
        books.remove(book);

        AuthUserUpdateDTO dto = AuthUserUpdateDTO
                .builder()
                .chatId(chatId)
                .books(books)
                .build();

        authUserService.update(dto);
        executor.sendMessage(chatId,
                "%s%s".formatted(Emojis.ADD, translate.getTranslation("book.removed", language)));
    }

    public void genreProcess(Message message) {
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        String language = UserState.getLanguage(chatId);
        UserState.setState(chatId, State.SEARCH_GENRE);
        UserState.setMenuState(chatId, MenuState.SEARCH);

        executor.editMessage(
                chatId,
                messageId,
                "%s %s".formatted(Emojis.GENRE, translate.getTranslation("choose.genre", language)),
                InlineKeyboard.genreButtons(chatId));
    }

    public void nameProcess(Message message) {
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        String language = UserState.getLanguage(chatId);

        executor.deleteMessage(chatId, messageId);

        UserState.setState(chatId, State.SEARCH_NAME);
        executor.sendMessage(chatId,
                "%s %s".formatted(Emojis.SEARCH, translate.getTranslation("enter.book", language)));
        UserState.setMenuState(chatId, MenuState.SEARCH);
        UserState.setPage(chatId, 0);
    }

    public void genreDetailProcess(Message message, String data) {
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        String language = UserState.getLanguage(chatId);

        executor.deleteMessage(chatId, messageId);

        if (UserState.getState(chatId).equals(State.ADD_GENRE)) {

            BookCreateDTO dto = UserState.getBookCreateDTO(chatId);
            dto.setGenre(data);
            bookService.save(dto);
            UserState.removeBookCreateDTO(chatId);


            executor.sendMessage(chatId,
                    "%s %s".formatted(Emojis.ADD, translate.getTranslation("file.uploaded", language)));

            UserState.setState(chatId, State.UNDEFINED);
            UserState.setMenuState(chatId, MenuState.UNDEFINED);

        } else if (UserState.getState(chatId).equals(State.SEARCH_GENRE)) {

            UserState.setSearchGenre(chatId, data);

            BookCriteria bookCriteria = BookCriteria
                    .childBuilder()
                    .genre(data)
                    .page(UserState.getPage(chatId))
                    .size(UserState.getSize(chatId))
                    .build();

            List<Book> books = bookService.getAllByGenre(bookCriteria);

            EditMessageText editMessageText = new EditMessageText();
            editMessageText.setMessageId(message.getMessageId());
            editMessageText.setChatId(chatId);

            if (books.size() == 0) {

                executor.editMessage(chatId, messageId, translate.getTranslation("no.book.genre", language));
                UserState.setMenuState(chatId, MenuState.UNDEFINED);
                UserState.setState(chatId, State.UNDEFINED);

            } else {

                UserState.setPage(chatId, 0);
                List<Long> bookIds = books.stream().map(Book::getId).toList();
                bookCriteria = BookCriteria
                        .childBuilder()
                        .page(UserState.getPage(chatId))
                        .size(UserState.getSize(chatId))
                        .build();

                executor.editMessage(
                        chatId,
                        messageId,
                        messages.bookMessage(books, chatId).toString(),
                        InlineKeyboard.bookOrUserButtons(bookIds, bookCriteria));
            }
        }
    }

    public void cancelDocumentProcess(Message message) {
        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        executor.deleteMessage(chatId, messageId);
    }

    public void sizeProcess(Message message, String data) {
        switch (data) {
            case "five" -> changeSizeProcess(message, 5);
            case "eight" -> changeSizeProcess(message, 8);
            case "ten" -> changeSizeProcess(message, 10);
        }
    }

    private void changeSizeProcess(Message message, Integer size) {

        String chatId = message.getChatId().toString();
        Integer messageId = message.getMessageId();
        String language = UserState.getLanguage(chatId);

        UserState.setSize(chatId, size);
        executor.deleteMessage(chatId, messageId);
        executor.sendMessage(
                chatId,
                "%s %s\n%s%d".formatted(
                        Emojis.ADD,
                        translate.getTranslation("limit.changed", language),
                        translate.getTranslation("current.limit", language),
                        UserState.getSize(chatId)));
    }

    public void documentOrAuthUserProcess(Message message, String data) {
        String chatId = message.getChatId().toString();
        Book book = bookService.get(data);
        String fileId = book.getFileId();
        UserState.setBookId(chatId, book.getFileId());

        if (Objects.isNull(book.getId())) {

            executor.sendMessage(chatId, authUserService.detailMessage(chatId).toString());
            return;

        }
        if (!UserState.getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
            // TODO: 5/4/2022 bad approach | use checking book list of user instead of it
            Book target = bookService.get(fileId);
            target.setDownloadsCount(target.getDownloadsCount() + 1);
            bookService.update(target);
        }
        executor.sendDocument(fileId, chatId, InlineKeyboard.documentButtons(chatId));
    }

}
