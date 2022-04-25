package uz.doston.flyingbookbot.handlers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.repository.AuthUserRepository;
import uz.doston.flyingbookbot.utils.MessageExecutor;
import uz.doston.flyingbookbot.utils.UserState;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class MessageHandler {

    private final AuthUserRepository authUserRepository;
    private final MessageExecutor executor;

    public void handle(Message message) {
        String chatId = message.getChatId().toString();
        String command = message.getText();
//        UState state = getState(chatId);
//        AuthRole role = authUserRepository.findAuthRoleByChatId(chatId);
//        SettingsState settingsState = getSettingsState(chatId);
//        AddBookState addBookState = getAddBookState(chatId);
//        RemoveBookState removeBookState = getRemoveBookState(chatId);
//        ManagerState managerState = getManagerState(chatId);
//        MenuState menuState = UserState.getMenuState(chatId);

        if ("/start".equals(command)/* || Objects.isNull(role)*/) {
            executor.sendMessage(chatId, "${hello}");

//            if (Objects.isNull(role)) {
//                authorizationProcessor.process(message, state);
//            }
//            if (Objects.nonNull(role) && getMenuState(chatId).equals(MenuState.UNDEFINED)) {
//                menuProcessor.menu(chatId, role);
//            }
//            return;
        }
//        else if ("/settings".equals(command)) {
//            setMenuState(chatId, MenuState.SETTINGS);
//            SendMessage sendMessage = new SendMessage(chatId, "<b>" + LangConfig.get(chatId, "settings.menu") + "</b>");
//            sendMessage.setReplyMarkup(MarkupBoard.settingsMenu(chatId));
//            BOT.executeMessage(sendMessage);
//            return;
//        } else if ("/help".equals(command)) {
//            SendMessage sendMessage = new SendMessage(chatId, botService.getMessage(chatId));
//            BOT.executeMessage(sendMessage);
//            return;
//        } else if ("/search".equals(command)) {
//            SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "choose.search.type"));
//            sendMessage.setReplyMarkup(InlineBoard.searchButtons(chatId));
//            BOT.executeMessage(sendMessage);
//            State.setMenuState(chatId, MenuState.SEARCH);
//            offset.setSearchOffset(chatId, 0);
//            return;
//        } else if ("/top".equals(command)) {
//            State.setMenuState(chatId, MenuState.TOP);
//            topBookProcessor.process(message);
//            return;
//        } else if ("/users".equals(command)) {
//            if (!role.equals("USER")) {
//                State.setMenuState(chatId, MenuState.USERLIST);
//                userListProcessor.process(message);
//            }
//            return;
//        } else if ("/post".equals(command)) {
//            if (!role.equals("USER")) {
//                State.setMenuState(chatId, MenuState.POST);
//                SendMessage sendMessage = new SendMessage(chatId, LangConfig.get(chatId, "send.post"));
//                BOT.executeMessage(sendMessage);
//                return;
//            }
//        } else if ("/stats".equals(command)) {
//            StringBuilder text = authUserService.getStatsMessage(chatId);
//            SendMessage sendMessage = new SendMessage(chatId, text.toString());
//            BOT.executeMessage(sendMessage);
//            return;
//        } else if ("/whoami".equals(command)) {
//            StringBuilder text = authUserRepository.getUser(chatId, chatId);
//            SendMessage sendMessage = new SendMessage(chatId, text.toString());
//            BOT.executeMessage(sendMessage);
//            return;
//        } else if ("/developers".equals(command)) {
//            String developersMessage = "<b><code>" + LangConfig.get(chatId, "team") + "</code> " + "\n\n" +
//                    "Doston Bokhodirov  |  @dostonbokhodirov" + "\n" +
//                    "Nodirbek Juraev  |  @Nodirbeke" + "\n" +
//                    "Jasurbek Mutalov  |  @mutalov777" + "\n" +
//                    "Tojimuradov Maxmadiyor  |  @diyor_unicorn</b>";
//            SendMessage sendMessage = new SendMessage(chatId, developersMessage);
//            BOT.executeMessage(sendMessage);
//            return;
//        }
//        if (menuState.equals(MenuState.UNDEFINED)) {
//            menuProcessor.process(message, role);
//        } else if (menuState.equals(MenuState.SETTINGS)) {
//            settingsProcessor.process(message, settingsState);
//        } else if (getMenuState(chatId).equals(MenuState.ADD_BOOK)) {
//            addBookProcessor.process(message, addBookState, role);
//        } else if (menuState.equals(MenuState.SEARCH)) {
//            searchBookProcessor.process(message, State.getSearchState(chatId));
//        } else if (menuState.equals(MenuState.REMOVE_BOOK)) {
//            removeBookProcessor.process(message, removeBookState);
//        } else if (getMenuState(chatId).equals(MenuState.DOWNLOADED)) {
//            downloadedBookProcessor.process(message);
//        } else if (getMenuState(chatId).equals(MenuState.UPLOADED)) {
//            uploadedBookProcessor.process(message);
//        } else if (menuState.equals(MenuState.TOP)) {
//            topBookProcessor.process(message);
//        } else if (menuState.equals(MenuState.USERLIST)) {
//            userListProcessor.process(message);
//        } else if (menuState.equals(MenuState.ADD_MANAGER)) {
//            addManagerProcessor.process(message, managerState);
//        } else if (menuState.equals(MenuState.REMOVE_MANAGER)) {
//            removeManagerProcessor.process(message, managerState);
//        } else if (menuState.equals(MenuState.POST)) {
//            postProcessor.process(message);
//        }
    }
}
