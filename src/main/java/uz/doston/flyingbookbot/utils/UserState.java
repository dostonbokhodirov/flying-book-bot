package uz.doston.flyingbookbot.utils;

import uz.doston.flyingbookbot.dto.AuthUserCreateDTO;
import uz.doston.flyingbookbot.enums.MenuState;
import uz.doston.flyingbookbot.enums.State;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class UserState {
    private final static ConcurrentHashMap<String, State> STATES = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, MenuState> MENU_STATES = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, AuthUserCreateDTO> CREATE_DTO_STATES = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, String> LANGUAGES = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, Integer> PAGES = new ConcurrentHashMap<>();
    private final static ConcurrentHashMap<String, Integer> SIZES = new ConcurrentHashMap<>();

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

    public static AuthUserCreateDTO getUserCreateDTO(String chatId) {
        if (Objects.isNull(CREATE_DTO_STATES.get(chatId))) {
            setUserCreateDTO(chatId, new AuthUserCreateDTO());
        }
        return CREATE_DTO_STATES.get(chatId);
    }

    public static String getLanguage(String chatId) {
        if (Objects.isNull(LANGUAGES.get(chatId))) {
            setLanguage(chatId, "uz");
        }
        return LANGUAGES.get(chatId);
    }

    public static Integer getPage(String chatId) {
        if (Objects.isNull(PAGES.get(chatId))) {
            setPage(chatId, 0);
        }
        return PAGES.get(chatId);
    }

    public static Integer getSize(String chatId) {
        if (Objects.isNull(SIZES.get(chatId))) {
            setSize(chatId, 0);
        }
        return SIZES.get(chatId);
    }


    public static void setMenuState(String chatId, MenuState state) {
        CompletableFuture.runAsync(() -> {
            // TODO: write to file
        });
        MENU_STATES.put(chatId, state);
    }

    public static void setState(String chatId, State state) {
        CompletableFuture.runAsync(() -> {
            // TODO: write to file
        });
        STATES.put(chatId, state);
    }

    public static void setUserCreateDTO(String chatId, AuthUserCreateDTO dto) {
        CompletableFuture.runAsync(() -> {
            // TODO: write to file
        });
        CREATE_DTO_STATES.put(chatId, dto);
    }

    public static void setLanguage(String chatId, String language) {
        LANGUAGES.put(chatId, language);
    }

    public static void setPage(String chatId, Integer page) {
        CompletableFuture.runAsync(() -> {
            // TODO: write to file
        });
        if (page == 0) {
            PAGES.put(chatId, page);
        } else PAGES.put(chatId, getPage(chatId) + page);
    }

    public static void setSize(String chatId, Integer size) {
        CompletableFuture.runAsync(() -> {
            // TODO: write to file
        });
        if (size == 0) SIZES.put(chatId, size);
        else SIZES.put(chatId, size);
    }

}
