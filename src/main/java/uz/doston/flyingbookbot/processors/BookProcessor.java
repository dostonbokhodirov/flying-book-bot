package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.criteria.BookCriteria;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.service.BookService;
import uz.doston.flyingbookbot.utils.MessageExecutor;
import uz.doston.flyingbookbot.utils.Messages;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BookProcessor {

    private final BookService bookService;
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
}
