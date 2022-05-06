package uz.doston.flyingbookbot.utils;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.meta.api.methods.send.*;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.doston.flyingbookbot.FlyingBookBot;

@Slf4j
@Component
public record MessageExecutor(@Lazy FlyingBookBot bot) {

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
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(replyKeyboard);
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
        EditMessageText message = new EditMessageText();
        message.setChatId(chatId);
        message.setMessageId(messageId);
        message.setText(text);
        message.setReplyMarkup(replyKeyboard);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void execute(AnswerInlineQuery message) {
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
        SendDocument message = new SendDocument();
        message.setChatId(chatId);
        message.setDocument(new InputFile(fileId));
        message.setReplyMarkup(replyKeyboard);
        message.setCaption(caption);
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
        SendPhoto message = new SendPhoto();
        message.setChatId(chatId);
        message.setPhoto(new InputFile(fileId));
        message.setReplyMarkup(replyKeyboard);
        message.setCaption(caption);
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
        SendVideo message = new SendVideo();
        message.setChatId(chatId);
        message.setVideo(new InputFile(fileId));
        message.setReplyMarkup(replyKeyboard);
        message.setCaption(caption);
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
        SendAudio message = new SendAudio();
        message.setChatId(chatId);
        message.setAudio(new InputFile(fileId));
        message.setReplyMarkup(replyKeyboard);
        message.setCaption(caption);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

}
