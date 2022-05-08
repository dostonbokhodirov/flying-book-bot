package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.dto.AuthUserCreateDTO;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.enums.State;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.utils.Emojis;
import uz.doston.flyingbookbot.utils.MessageExecutor;
import uz.doston.flyingbookbot.utils.Translate;
import uz.doston.flyingbookbot.utils.UserState;

@Component
@RequiredArgsConstructor
public class AuthorizationProcessor {

    private final AuthUserService authUserService;
    private final MessageExecutor executor;
    private final Translate translate;
    private final InlineKeyboard inlineKeyboard;

    public void process(Message message, State state) {
        String chatId = message.getChatId().toString();
        String language = UserState.getLanguage(chatId);
        if (State.USER_LANG.equals(state) || State.UNDEFINED.equals(state)) {
            executor.sendMessage(chatId, """
                    Tilni tanlang:
                    Выберите язык:
                    Choose your language:""", inlineKeyboard.languageButtons());
            UserState.setState(chatId, State.DELETE_ALL);
        } else if (State.USER_FULL_NAME.equals(state)) {
            String text = message.getText();
            if (StringUtils.isNumeric(text) || !text.equals(StringUtils.capitalize(text))) {
                executor.sendMessage(
                        chatId,
                        "%s %s\n%s".formatted(
                                Emojis.LOOK, translate.getTranslation("full.name.correctly", language),
                                translate.getTranslation("without.numbers", language)),
                        new ForceReplyKeyboard(true));
            } else {
                AuthUserCreateDTO dto = UserState.getUserCreateDTO(chatId);
                dto.setChatId(chatId);
                dto.setFullName(text);
                UserState.setUserCreateDTO(chatId, dto);

                executor.sendMessage(chatId, translate.getTranslation("enter.age", language));
                UserState.setState(chatId, State.USER_AGE);
            }
        } else if (State.USER_AGE.equals(state)) {
            String text = message.getText();
            if (StringUtils.isNumeric(text)) {
                if (Integer.parseInt(text) <= 100) {

                    AuthUserCreateDTO dto = UserState.getUserCreateDTO(chatId);
                    dto.setAge(Integer.valueOf(text));
                    UserState.setUserCreateDTO(chatId, dto);

                    executor.sendMessage(
                            chatId,
                            translate.getTranslation("enter.gender", language),
                            inlineKeyboard.genderButtons(chatId));
                    UserState.setState(chatId, State.DELETE_ALL);
                } else {
                    executor.sendMessage(chatId, "%s %s\n%s".formatted(
                            translate.getTranslation("sure.age", language),
                            Emojis.REALLY,
                            translate.getTranslation("enter.age.again", language)));
                }
            } else {
                executor.sendMessage(chatId,
                        "%s %s".formatted(translate.getTranslation("age.correctly", language), Emojis.SAD));
            }
        } else if (State.USER_PHONE_NUMBER.equals(state)) {
            if (message.hasContact()) {
                String phoneNumber = message.getContact().getPhoneNumber();
                if (!phoneNumber.startsWith("+")) phoneNumber = "+" + phoneNumber;

                executor.sendMessage(
                        chatId,
                        "%s <b>%s %s\n%s</b>".formatted(
                                Emojis.ADD,
                                translate.getTranslation("congratulations", language),
                                Emojis.GREAT,
                                translate.getTranslation("welcome", language)));

                AuthUserCreateDTO dto = UserState.getUserCreateDTO(chatId);
                dto.setPhoneNumber(phoneNumber);
                dto.setRole(AuthRole.USER);
                UserState.setUserCreateDTO(chatId, dto);

                authUserService.save(dto);

                UserState.setState(chatId, State.USER_AUTHORIZED);
                UserState.setMenuState(chatId, MenuState.UNDEFINED);

            } else {
                executor.sendMessage(
                        chatId,
                        "%s %s\n%s".formatted(
                                Emojis.REMOVE,
                                translate.getTranslation("invalid.number", language),
                                translate.getTranslation("correct.number", language)));
            }
        }

        if (State.DELETE_ALL.equals(state)) {
            executor.deleteMessage(chatId, message.getMessageId());
        }
    }
}
