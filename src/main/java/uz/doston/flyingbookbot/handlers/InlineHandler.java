package uz.doston.flyingbookbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResultDocument;
import uz.doston.flyingbookbot.processors.BookProcessor;
import uz.doston.flyingbookbot.utils.MessageExecutor;

@Component
@RequiredArgsConstructor
public class InlineHandler {

    private final BookProcessor bookProcessor;
    private final MessageExecutor executor;

    public void handle(InlineQuery inlineQuery) {
        executor.answerInlineQuery(
                inlineQuery.getId(),
                bookProcessor.getAllByQueryMatches(inlineQuery.getQuery())
        );

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
