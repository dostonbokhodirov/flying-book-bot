package uz.doston.flyingbookbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultDocument;
import uz.doston.flyingbookbot.service.BookService;
import uz.doston.flyingbookbot.utils.MessageExecutor;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InlineHandler {

    private final BookService bookService;
    private final MessageExecutor executor;

    public void handle(InlineQuery inlineQuery) {
        AnswerInlineQuery answerInlineQuery = new AnswerInlineQuery();
        answerInlineQuery.setInlineQueryId(inlineQuery.getId());
        String query = inlineQuery.getQuery();
    bookService.
;
        List<InlineQueryResult> results = new ArrayList<>();
        answerInlineQuery.setResults(results);
        executor.execute(answerInlineQuery);
    }

    @Bean
    @Primary
    private InlineQueryResultDocument inlineQueryResultDocBean() {
        InlineQueryResultDocument inlineQueryResultDocument = new InlineQueryResultDocument();
        inlineQueryResultDocument.setMimeType("application/pdf");
        inlineQueryResultDocument.setId("12");
        inlineQueryResultDocument.setDocumentUrl("www.google.com");
        inlineQueryResultDocument.setTitle("Document");
        return inlineQueryResultDocument;
    }
}
