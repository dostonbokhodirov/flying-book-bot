package uz.doston.flyingbookbot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import uz.doston.flyingbookbot.FlyingBookBot;

@Slf4j
@Component
public record MessageExecutor(@Lazy FlyingBookBot bot) {

    public void deleteMessage(String chatId, Integer messageId) {
        try {
            bot.execute(new DeleteMessage(chatId, messageId));
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void sendMessage(String chatId, String text) {
        sendMessage(chatId, text, null);
    }

    public void sendMessage(String chatId, String text, ReplyKeyboard replyKeyboard) {
        SendMessage message = new SendMessage(chatId, text);
        message.setReplyMarkup(replyKeyboard);
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void execute(SendMessage message) {
        try {
            bot.execute(message);
        } catch (TelegramApiException e) {
            log.error(e.getMessage(), e);
        }
    }

}
