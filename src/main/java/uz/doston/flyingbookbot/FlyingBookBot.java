package uz.doston.flyingbookbot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import uz.doston.flyingbookbot.handlers.UpdateHandler;

@Component
@RequiredArgsConstructor
public class FlyingBookBot extends TelegramLongPollingBot {

    private final UpdateHandler updateHandler;

    @Value(value = "${bot.username}")
    private String username;

    @Value(value = "${bot.token}")
    private String token;

    @Override
    public void onUpdateReceived(Update update) {
        updateHandler.hande(update);
    }

    @Override
    public String getBotUsername() {
        return username;
    }

    @Override
    public String getBotToken() {
        return token;
    }

}
