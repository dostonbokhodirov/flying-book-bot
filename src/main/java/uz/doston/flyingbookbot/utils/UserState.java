package uz.doston.flyingbookbot.utils;

import uz.doston.flyingbookbot.dto.UserCreateDTO;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.enums.State;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UserState {
    private final static ConcurrentHashMap<String, State> STATES = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, MenuState> MENU_STATES = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, UserCreateDTO> CREATE_DTO_STATES = new ConcurrentHashMap<>();

    public static State getState(String chatId) {
        if (Objects.isNull(STATES.get(chatId))) {
            setState(chatId, State.USER_ANONYMOUS);
        }
        return STATES.get(chatId);
    }

    public static MenuState getMenuState(String chatId) {
        if (Objects.isNull(MENU_STATES.get(chatId))) {
            setMenuState(chatId, MenuState.UNDEFINED);
        }
        return MENU_STATES.get(chatId);
    }

    public static UserCreateDTO getUserCreateDTO(String chatId) {
        if (Objects.isNull(CREATE_DTO_STATES.get(chatId))) {
            setUserCreateDTO(chatId, new UserCreateDTO());
        }
        return CREATE_DTO_STATES.get(chatId);
    }

    public static void setMenuState(String chatId, MenuState state) {
        CompletableFuture.runAsync(() -> {
            // TODO: 10/03/22 write to file
        });
        MENU_STATES.put(chatId, state);
    }

    public static void setState(String chatId, State state) {
        CompletableFuture.runAsync(() -> {
            // TODO: 10/03/22 write to file
        });
        STATES.put(chatId, state);
    }

    public static void setUserCreateDTO(String chatId, UserCreateDTO dto) {
        CompletableFuture.runAsync(() -> {
            // TODO: 10/03/22 write to file
        });
        CREATE_DTO_STATES.put(chatId, dto);
    }

}
