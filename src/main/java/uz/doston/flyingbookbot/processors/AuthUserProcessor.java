package uz.doston.flyingbookbot.processors;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import uz.doston.flyingbookbot.buttons.InlineKeyboard;
import uz.doston.flyingbookbot.buttons.ReplyKeyboard;
import uz.doston.flyingbookbot.criteria.AuthUserCriteria;
import uz.doston.flyingbookbot.dto.AuthUserUpdateDTO;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.enums.State;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.utils.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AuthUserProcessor {
    private final AuthUserService authUserService;
    private final MenuProcessor menuProcessor;
    private final MessageExecutor executor;
    private final Translate translate;

    private final Messages messages;
    private final InlineKeyboard inlineKeyboard;
    private final ReplyKeyboard replyKeyboard;

    public void process(Message message) {

        String chatId = message.getChatId().toString();
        AuthUserCriteria authUserCriteria = AuthUserCriteria
                .childBuilder()
                .page(UserState.getPage(chatId))
                .size(UserState.getSize(chatId))
                .build();

        List<AuthUser> authUsers = authUserService.getAll(authUserCriteria);
        List<Long> authUserIds = authUsers.stream().map(AuthUser::getId).collect(Collectors.toList());

        executor.sendMessage(
                chatId,
                messages.authUserMessage(authUsers, chatId).toString(),
                inlineKeyboard.bookOrUserButtons(authUserIds, authUserCriteria));
        UserState.setMenuState(chatId, MenuState.UNDEFINED);

    }

    public void settingsProcess(Message message, State state) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);

        if (Objects.nonNull(text)
                && text.equals("%s %s".formatted(Emojis.NAME, translate.getTranslation("change.name", language)))
                && state.equals(State.UNDEFINED)) {

            executor.sendMessage(chatId, "%s %s\n%s".formatted(
                    translate.getTranslation("current.name", language),
                    authUserService.getFullNameByChatId(chatId),
                    translate.getTranslation("new.name", language)));

            UserState.setState(chatId, State.CHANGE_NAME);

        } else if (state.equals(State.CHANGE_NAME)) {

            if (StringUtils.isNumeric(text) || !text.equals(StringUtils.capitalize(text))) {

                executor.sendMessage(chatId, "%s\n%s".formatted(
                                translate.getTranslation("full.name.correctly", language),
                                translate.getTranslation("without.numbers", language)),
                        new ForceReplyKeyboard());

            } else {

                AuthUserUpdateDTO dto = AuthUserUpdateDTO
                        .builder()
                        .chatId(chatId)
                        .fullName(text)
                        .build();

                authUserService.update(dto);

                executor.sendMessage(chatId, "%s %s\n%s %s".formatted(
                        Emojis.ADD,
                        translate.getTranslation("full.name.changed", language),
                        translate.getTranslation("current.name", language),
                        text));

                UserState.setState(chatId, State.UNDEFINED);

                menuProcessor.sendSettingsMenu(chatId, translate.getTranslation("settings.menu", language));

            }
        } else if (Objects.nonNull(text)
                && text.equals("%s %s".formatted(
                Emojis.AGE,
                translate.getTranslation("change.birthdate", language)))
                && state.equals(State.UNDEFINED)) {

            executor.sendMessage(chatId, "%s %d\n%s".formatted(
                    translate.getTranslation("current.age", language),
                    authUserService.getAgeByChatId(chatId),
                    translate.getTranslation("new.age", language)));

            UserState.setState(chatId, State.CHANGE_AGE);

        } else if (state.equals(State.CHANGE_AGE)) {

            if (StringUtils.isNumeric(text)) {

                if (Integer.parseInt(text) <= 100) {

                    AuthUserUpdateDTO dto = AuthUserUpdateDTO
                            .builder()
                            .chatId(chatId)
                            .age(Integer.valueOf(text))
                            .build();

                    authUserService.update(dto);

                    executor.sendMessage(chatId, "%s %s\n%s%s".formatted(
                            Emojis.ADD,
                            translate.getTranslation("age.changed", language),
                            translate.getTranslation("current.age", language),
                            text));

                    UserState.setState(chatId, State.UNDEFINED);

                    menuProcessor.sendSettingsMenu(chatId, translate.getTranslation("settings.menu", language));

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
        } else if (
                Objects.nonNull(text)
                && text.equals("%s %s".formatted(Emojis.PHONE, translate.getTranslation("change.phone.number", language)))
                && state.equals(State.UNDEFINED)) {

            executor.sendMessage(chatId, "%s %s\n%s".formatted(
                            translate.getTranslation("current.phone.number", language),
                            authUserService.getPhoneNumberByChatId(chatId),
                            translate.getTranslation("new.phone.number", language)),
                    replyKeyboard.sharePhoneNumber(chatId));

            UserState.setState(chatId, State.CHANGE_NUMBER);

        } else if (
                Objects.nonNull(text)
                && text.equals("%s %s".formatted(Emojis.LIMIT, translate.getTranslation("change.limit", language)))
                && state.equals(State.UNDEFINED)) {

            executor.sendMessage(
                    chatId,
                    translate.getTranslation("choose.limit", language),
                    inlineKeyboard.sizeButtons());

        } else if (state.equals(State.CHANGE_NUMBER)) {

            if (message.hasContact()) {

                AuthUserUpdateDTO dto = AuthUserUpdateDTO
                        .builder()
                        .chatId(chatId)
                        .phoneNumber(text)
                        .build();

                authUserService.update(dto);

                executor.sendMessage(chatId, "%s %s\n%s %s".formatted(
                        Emojis.ADD,
                        translate.getTranslation("phone.number.changed", language),
                        translate.getTranslation("current.phone.number", language),
                        message.getContact().getPhoneNumber()));

                UserState.setState(chatId, State.UNDEFINED);

                menuProcessor.sendSettingsMenu(chatId, translate.getTranslation("settings.menu", language));

            } else {

                executor.sendMessage(chatId, "%s\n%s".formatted(
                        translate.getTranslation("invalid.number", language),
                        translate.getTranslation("correct.number", language)));

            }
        } else if (Objects.nonNull(text)
                && text.equals("%s %s"
                .formatted(Emojis.LANGUAGE, translate.getTranslation("change.language", language)))
                && state.equals(State.UNDEFINED)) {

            executor.sendMessage(
                    chatId, "%s %s\n%s".formatted(
                            translate.getTranslation("current.language", language),
                            language,
                            translate.getTranslation("new.language", language)),
                    inlineKeyboard.languageButtons());

            UserState.setState(chatId, State.CHANGE_LANGUAGE);

        } else if (state.equals(State.CHANGE_LANGUAGE)) {

            executor.sendMessage(chatId, "%s %s\n%s %s".formatted(
                    Emojis.ADD,
                    translate.getTranslation("language.changed", language),
                    translate.getTranslation("current.language", language),
                    language));

            UserState.setState(chatId, State.UNDEFINED);

            menuProcessor.sendSettingsMenu(chatId, translate.getTranslation("settings.menu", language));

        } else if (Objects.nonNull(text)
                && text.equals("%s %s".formatted(Emojis.BACK, translate.getTranslation("back", language)))) {

            menuProcessor.sendMainMenu(
                    chatId,
                    authUserService.getRoleByChatId(chatId),
                    "<b>%s</b>".formatted(translate.getTranslation("back.menu", language)));

            UserState.setMenuState(chatId, MenuState.UNDEFINED);
            UserState.setState(chatId, State.UNDEFINED);

        } else {

            menuProcessor.sendSettingsMenu(chatId, translate.getTranslation("wrong.button", language));

        }
    }

    public void addManagerProcess(Message message, State state) {

        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);

        UserState.setMenuState(chatId, MenuState.ADD_MANAGER);

        if (state.equals(State.UNDEFINED)) {

            executor.sendMessage(chatId, translate.getTranslation("enter.user.id", language));
            UserState.setState(chatId, State.ADD_REMOVE_MANAGER);

        } else if (state.equals(State.ADD_REMOVE_MANAGER)) {

            if (!StringUtils.isNumeric(text)) {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REMOVE, translate.getTranslation("id.consists", language)));
                UserState.setState(chatId, State.UNDEFINED);
                UserState.setMenuState(chatId, MenuState.UNDEFINED);
                return;

            }

            AuthRole authRole = authUserService.getRoleByChatId(text);
            if (Objects.isNull(authRole)) {
                authRole = authUserService.getRoleByPhoneNumber(text);
            }

            if (Objects.nonNull(authRole) && (authRole.equals(AuthRole.MANAGER))) {

                executor.sendMessage(chatId,
                        "%s%s".formatted(Emojis.REALLY, translate.getTranslation("already.manager", language)));

            } else if (Objects.isNull(authRole)) {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REMOVE, translate.getTranslation("user.not.found", language)));

            } else {

                executor.sendMessage(
                        chatId,
                        "%s %s".formatted(Emojis.ADD, translate.getTranslation("user.changed.manager", language)),
                        replyKeyboard.adminMenu(chatId));

            }

            UserState.setState(chatId, State.UNDEFINED);
            UserState.setMenuState(chatId, MenuState.UNDEFINED);
        }
    }

    public void removeManagerProcess(Message message, State state) {
        String chatId = message.getChatId().toString();
        String text = message.getText();
        String language = UserState.getLanguage(chatId);

        UserState.setMenuState(chatId, MenuState.REMOVE_MANAGER);

        if (state.equals(State.UNDEFINED)) {

            executor.sendMessage(chatId, translate.getTranslation("enter.user.id", language));
            UserState.setState(chatId, State.ADD_REMOVE_MANAGER);

        } else if (state.equals(State.ADD_REMOVE_MANAGER)) {

            if (!StringUtils.isNumeric(text)) {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REMOVE, translate.getTranslation("id.consists", language)));
                UserState.setState(chatId, State.UNDEFINED);
                UserState.setMenuState(chatId, MenuState.UNDEFINED);
                return;

            }

            AuthRole authRole = authUserService.getRoleByChatId(text);
            if (Objects.isNull(authRole)) {
                authRole = authUserService.getRoleByPhoneNumber(text);
            }

            if (Objects.nonNull(authRole) && (authRole.equals(AuthRole.USER))) {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REALLY, translate.getTranslation("already.user", language)));

            } else if (Objects.isNull(authRole)) {

                executor.sendMessage(chatId,
                        "%s %s".formatted(Emojis.REMOVE, translate.getTranslation("user.not.found", language)));

            } else {

                executor.sendMessage(
                        chatId,
                        "%s %s".formatted(Emojis.ADD, translate.getTranslation("user.changed.user", language)),
                        replyKeyboard.adminMenu(chatId));

            }

            UserState.setState(chatId, State.UNDEFINED);
            UserState.setMenuState(chatId, MenuState.UNDEFINED);

        }
    }

    public void postProcess(Message message) {
        String chatId = message.getChatId().toString();
        List<String> chatIdList = authUserService.getAllChatIdsByRole(AuthRole.USER);

        if (message.hasPhoto()) {
            chatIdList.forEach(
                    id -> executor.sendPhoto(id, message.getPhoto().get(0).getFileId(), message.getCaption()));
        } else if (message.hasVideo()) {
            chatIdList.forEach(
                    id -> executor.sendVideo(id, message.getVideo().getFileId(), message.getCaption()));
        } else if (message.hasAudio()) {
            chatIdList.forEach(id -> executor.sendAudio(id, message.getAudio().getFileId(), message.getCaption()));
        } else if (message.hasDocument()) {
            chatIdList.forEach(id -> executor.sendDocument(id, message.getDocument().getFileId(), message.getCaption()));
        } else {
            chatIdList.forEach(id -> executor.sendMessage(id, message.getText()));
        }
        UserState.setMenuState(chatId, MenuState.UNDEFINED);
    }

}
