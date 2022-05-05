package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.criteria.AuthUserCriteria;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.enums.State;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.utils.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.sun.tools.javac.code.TypeTag.BOT;

@Component
@RequiredArgsConstructor
public class AuthUserProcessor {

    private final AuthUserService authUserService;
    private final MessageExecutor executor;
    private final Translate translate;

    private final Messages messages;

    public void process(Message message) {
        String chatId = message.getChatId().toString();
        AuthUserCriteria authUserCriteria = AuthUserCriteria
                .childBuilder()
                .page(UserState.getPage(chatId))
                .size(UserState.getSize(chatId))
                .build();

        List<AuthUser> authUsers = authUserService.getAll(authUserCriteria);

        String text = messages.authUserMessage(authUsers, chatId).toString();
        List<Long> authUserIds = authUsers.stream().map(AuthUser::getId).collect(Collectors.toList());
        executor.sendMessage(chatId, text, InlineKeyboard.bookOrUserButtons(authUserIds, authUserCriteria));
        UserState.setMenuState(chatId, MenuState.UNDEFINED);
    }

    public void settingsProcess(Message message, State state) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);

        if (Objects.nonNull(text)
                && text.equals("%s %s".formatted(Emojis.NAME, translate.getTranslation("change.name", language)))
                && state.equals(State.UNDEFINED)) {

            String name = authUserService.findFieldByChatID(chatId, "full_name");

            SendMessage sendMessage = new SendMessage(chatId,
                    translate.getTranslation("current.name", language) + " " + name + "\n" +
                            translate.getTranslation("new.name", language));
            BOT.executeMessage(sendMessage);

            UserState.setState(chatId, State.CHANGE_NAME);

        } else if (state.equals(State.CHANGE_NAME)) {

            if (StringUtils.isNumeric(text) || !text.equals(StringUtils.capitalize(text))) {

                SendMessage sendMessage = new SendMessage(chatId,
                        LangConfig.get(chatId, "full.name.correctly") + "\n" +
                                LangConfig.get(chatId, "without.numbers"));
                sendMessage.setReplyMarkup(new ForceReplyKeyboard());
                BOT.executeMessage(sendMessage);

            } else {

                authUserRepository.save("full_name", text, chatId);
                SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                        LangConfig.get(chatId, "full.name.changed") + "\n" +
                        LangConfig.get(chatId, "current.name") + " " +
                        authUserRepository.findFieldByChatID(chatId, "full_name"));
                BOT.executeMessage(sendMessage);
                State.setState(chatId, State.UNDEFINED);
                menu(chatId, LangConfig.get(chatId, "settings.menu"));

            }
        } else if (Objects.nonNull(text)
                && text.equals(Emojis.AGE + " " + LangConfig.get(chatId, "change.birthdate"))
                && state.equals(State.UNDEFINED)) {

            String age = authUserRepository.findFieldByChatID(chatId, "age");
            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "current.age") + " " + age + "\n" +
                    LangConfig.get(chatId, "new.age"));
            BOT.executeMessage(sendMessage);
            State.setState(chatId, State.CHANGE_AGE);

        } else if (state.equals(State.CHANGE_AGE)) {

            if (StringUtils.isNumeric(text)) {

                if (Integer.parseInt(text) <= 100) {

                    authUserRepository.save("age", text, chatId);
                    SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                            LangConfig.get(chatId, "age.changed") + "\n" +
                            LangConfig.get(chatId, "current.age") + authUserRepository.findFieldByChatID(chatId, "age"));
                    BOT.executeMessage(sendMessage);
                    State.setState(chatId, State.UNDEFINED);
                    menu(chatId, LangConfig.get(chatId, "settings.menu"));

                } else {

                    SendMessage sendMessage = new SendMessage(chatId,
                            LangConfig.get(chatId, "sure.age") + " " + Emojis.REALLY + "\n" +
                                    LangConfig.get(chatId, "enter.age.again"));
                    BOT.executeMessage(sendMessage);

                }
            } else {

                SendMessage sendMessage = new SendMessage(chatId,
                        LangConfig.get(chatId, "age.correctly") + " " + Emojis.SAD);
                BOT.executeMessage(sendMessage);

            }
        } else if (Objects.nonNull(text)
                && text.equals(Emojis.PHONE + " " + LangConfig.get(chatId, "change.phone.number"))
                && state.equals(State.UNDEFINED)) {

            String phoneNumber = authUserRepository.findFieldByChatID(chatId, "phone_number");
            SendMessage sendMessage = new SendMessage(chatId,
                    LangConfig.get(chatId, "current.phone.number") + " " + phoneNumber + "\n" +
                            LangConfig.get(chatId, "new.phone.number"));
            sendMessage.setReplyMarkup(MarkupBoard.sharePhoneNumber(chatId));
            BOT.executeMessage(sendMessage);
            State.setState(chatId, State.CHANGE_NUMBER);

        } else if (Objects.nonNull(text)
                && text.equals(Emojis.LIMIT + " " + LangConfig.get(chatId, "change.limit"))
                && state.equals(State.UNDEFINED)) {

            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            sendMessage.setText(LangConfig.get(chatId, "choose.limit"));
            sendMessage.setReplyMarkup(InlineBoard.limitButtons());
            BOT.executeMessage(sendMessage);

        } else if (state.equals(State.CHANGE_NUMBER)) {

            if (message.hasContact()) {

                authUserRepository.save("phone_number", text, chatId);
                SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                        LangConfig.get(chatId, "phone.number.changed") + "\n" +
                        LangConfig.get(chatId, "current.phone.number") + " " + message.getContact().getPhoneNumber());
                BOT.executeMessage(sendMessage);
                State.setState(chatId, State.UNDEFINED);
                menu(chatId, LangConfig.get(chatId, "settings.menu"));

            } else {

                SendMessage sendMessage = new SendMessage(chatId,
                        LangConfig.get(chatId, "invalid.number") + "\n" +
                                LangConfig.get(chatId, "correct.number"));
                BOT.executeMessage(sendMessage);

            }
        } else if (Objects.nonNull(text)
                && text.equals(Emojis.LANGUAGE + " " + LangConfig.get(chatId, "change.language"))
                && state.equals(State.UNDEFINED)) {

            String language = authUserRepository.findFieldByChatID(chatId, "language");
            SendMessage sendMessage = new SendMessage(chatId,
                    LangConfig.get(chatId, "current.language") + " " + language + "\n" +
                            LangConfig.get(chatId, "new.language"));
            sendMessage.setReplyMarkup(InlineBoard.languageButtons());
            BOT.executeMessage(sendMessage);
            State.setState(chatId, State.CHANGE_LANGUAGE);

        } else if (state.equals(State.CHANGE_LANGUAGE)) {

            SendMessage sendMessage = new SendMessage(chatId, Emojis.ADD + " " +
                    LangConfig.get(chatId, "language.changed") + "\n" +
                    LangConfig.get(chatId, "current.language") + " " +
                    authUserRepository.findFieldByChatID(chatId, "language"));
            BOT.executeMessage(sendMessage);
            State.setState(chatId, State.UNDEFINED);
            menu(chatId, LangConfig.get(chatId, "settings.menu"));

        } else if (Objects.nonNull(text)
                && text.equals(Emojis.BACK + " " + LangConfig.get(chatId, "back"))) {

            String role = authUserRepository.findRoleById(chatId);
            menuProcessor.menu(chatId, role, "<b>" + LangConfig.get(chatId, "back.menu") + "</b>");
            State.setMenuState(chatId, MenuState.UNDEFINED);
            State.setState(chatId, State.UNDEFINED);

        } else {

            menu(chatId, LangConfig.get(chatId, "wrong.button"));

        }
    }

    public void menu(String chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        if (text.equals(LangConfig.get(chatId, "wrong.button"))) {
            sendMessage.setText(Emojis.REMOVE + " " + text);
        } else {
            sendMessage.setText(text);
        }
        sendMessage.setReplyMarkup(MarkupBoard.settingsMenu(chatId));
        BOT.executeMessage(sendMessage);
    }

}
