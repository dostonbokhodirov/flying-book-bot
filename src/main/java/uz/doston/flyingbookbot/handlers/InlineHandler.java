package uz.doston.flyingbookbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultDocument;
import uz.doston.flyingbookbot.processors.BookProcessor;
import uz.doston.flyingbookbot.service.BookService;
import uz.doston.flyingbookbot.utils.MessageExecutor;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InlineHandler {

    private final BookProcessor bookProcessor;
    private final MessageExecutor executor;

    public void handle(InlineQuery inlineQuery) {
        executor.execute(
                new AnswerInlineQuery(
                        inlineQuery.getId(),
                        bookProcessor.getAllByQueryMatches(inlineQuery.getQuery())));
    }

    @Bean
    @Primary
    private InlineQueryResultDocument inlineQueryResultDocumentConfiguration() {
        return new InlineQueryResultDocument(
                "12",
                "Document",
                "www.google.com",
                "application/pdf");
    }
}
