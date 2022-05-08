package uz.doston.flyingbookbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.doston.flyingbookbot.processors.CallbackProcessor;
import uz.doston.flyingbookbot.utils.UserState;

@Component
@RequiredArgsConstructor
public class CallbackHandler {

    private final CallbackProcessor callbackProcessor;

    public void handle(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        String data = callbackQuery.getData();
        String chatId = message.getChatId().toString();


        if ("uz".equals(data) || "ru".equals(data) || "en".equals(data)) {

            callbackProcessor.languageProcess(message, data);

        } else if ("male".equals(data) || "female".equals(data)) {

            callbackProcessor.genderProcess(message, data);

        } else if (data.equals("prev")) {

            UserState.setPage(chatId, -1);
            callbackProcessor.nextOrPrevProcess(message);
//            callbackProcessor.prevProcess(message, UserState.getPage(chatId));

        } else if (data.equals("next")) {

            UserState.setPage(chatId, 1);
            callbackProcessor.nextOrPrevProcess(message);
//            callbackProcessor.nextProcess(message, UserState.getPage(chatId));

        } else if (data.equals("cancel")) {

            callbackProcessor.cancelProcess(message);

        } else if (data.equals("add")) {

            callbackProcessor.addProcess(message);

        } else if (data.equals("remove")) {

            callbackProcessor.removeProcess(message);

        } else if (data.equals("cancelDocument")) {

            callbackProcessor.cancelDocumentProcess(message);

        } else if (data.equals("genre")) {

            callbackProcessor.genreProcess(message);

        } else if (data.equals("name")) {

            callbackProcessor.nameProcess(message);

        } else if (data.equals("adventure")
                || data.equals("classic")
                || data.equals("comic")
                || data.equals("fiction")
                || data.equals("horror")
                || data.equals("scientific")
                || data.equals("other")) {

            callbackProcessor.genreDetailProcess(message, data);

        } else if (data.equals("five") || data.equals("eight") || data.equals("ten")) {

            callbackProcessor.sizeProcess(message, data);

        } else {

            callbackProcessor.documentOrAuthUserProcess(message, data);

        }
    }
}
