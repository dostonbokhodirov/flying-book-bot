package uz.doston.flyingbookbot.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.doston.flyingbookbot.FlyingBookBot;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public final class MessageExecutor {

    private final FlyingBookBot bot;

    public MessageExecutor(@Lazy FlyingBookBot bot) {
        this.bot = bot;
    }

    public void deleteMessage(@NonNull String chatId, @NonNull Integer messageId) {
        try {
            bot.execute(new DeleteMessage(chatId, messageId));
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendMessage(@NonNull String chatId, @NonNull String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(@NonNull String chatId, @NonNull String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = SendMessage
                .builder()
                .chatId(chatId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .parseMode("HTML")
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void editMessage(@NonNull String chatId, @NonNull Integer messageId, @NonNull String text) {
        editMessage(chatId, messageId, text, null);
    }

    public void editMessage(@NonNull String chatId, @NonNull Integer messageId, @NonNull String text, InlineKeyboardMarkup replyKeyboard) {
        EditMessageText message = EditMessageText.builder()
                .chatId(chatId)
                .messageId(messageId)
                .text(text)
                .replyMarkup(replyKeyboard)
                .parseMode("HTML")
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void answerInlineQuery(@NonNull String inlineQueryId, @NonNull List<InlineQueryResult> results) {
        AnswerInlineQuery message = AnswerInlineQuery
                .builder()
                .inlineQueryId(inlineQueryId)
                .results(results)
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendDocument(String chatId, String fileId) {
        sendDocument(chatId, fileId, null, null);
    }

    public void sendDocument(String chatId, String fileId, ReplyKeyboard replyKeyboard) {
        sendDocument(chatId, fileId, null, replyKeyboard);
    }

    public void sendDocument(String chatId, String fileId, String caption) {
        sendDocument(chatId, fileId, caption, null);
    }


    public void sendDocument(String chatId, String fileId, String caption, ReplyKeyboard replyKeyboard) {
        SendDocument message = SendDocument
                .builder()
                .chatId(chatId)
                .document(new InputFile(fileId))
                .replyMarkup(replyKeyboard)
                .caption(caption)
                .parseMode("HTML")
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendPhoto(String chatId, String fileId) {
        sendPhoto(chatId, fileId, null, null);
    }

    public void sendPhoto(String chatId, String fileId, ReplyKeyboard replyKeyboard) {
        sendPhoto(chatId, fileId, null, replyKeyboard);
    }

    public void sendPhoto(String chatId, String fileId, String caption) {
        sendPhoto(chatId, fileId, caption, null);
    }


    public void sendPhoto(String chatId, String fileId, String caption, ReplyKeyboard replyKeyboard) {
        SendPhoto message = SendPhoto
                .builder()
                .chatId(chatId)
                .photo(new InputFile(fileId))
                .replyMarkup(replyKeyboard)
                .caption(caption)
                .parseMode("HTML")
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendVideo(String chatId, String fileId) {
        sendVideo(chatId, fileId, null, null);
    }

    public void sendVideo(String chatId, String fileId, ReplyKeyboard replyKeyboard) {
        sendVideo(chatId, fileId, null, replyKeyboard);
    }

    public void sendVideo(String chatId, String fileId, String caption) {
        sendVideo(chatId, fileId, caption, null);
    }


    public void sendVideo(String chatId, String fileId, String caption, ReplyKeyboard replyKeyboard) {
        SendVideo message = SendVideo.builder()
                .chatId(chatId)
                .video(new InputFile(fileId))
                .replyMarkup(replyKeyboard)
                .caption(caption)
                .parseMode("HTML")
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendAudio(String chatId, String fileId) {
        sendAudio(chatId, fileId, null, null);
    }

    public void sendAudio(String chatId, String fileId, ReplyKeyboard replyKeyboard) {
        sendAudio(chatId, fileId, null, replyKeyboard);
    }

    public void sendAudio(String chatId, String fileId, String caption) {
        sendAudio(chatId, fileId, caption, null);
    }


    public void sendAudio(String chatId, String fileId, String caption, ReplyKeyboard replyKeyboard) {
        SendAudio message = SendAudio.builder()
                .chatId(chatId)
                .audio(new InputFile(fileId))
                .replyMarkup(replyKeyboard)
                .caption(caption)
                .parseMode("HTML")
                .build();
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Lazy
    public FlyingBookBot bot() {
        return bot;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (MessageExecutor) obj;
        return Objects.equals(this.bot, that.bot);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bot);
    }

    @Override
    public String toString() {
        return "MessageExecutor[" +
                "bot=" + bot + ']';
    }


}
