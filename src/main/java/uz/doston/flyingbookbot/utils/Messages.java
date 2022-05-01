package uz.doston.flyingbookbot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import uz.doston.flyingbookbot.entity.AuthUser;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.enums.AuthRole;
import uz.doston.flyingbookbot.service.AuthUserService;
import uz.doston.flyingbookbot.service.BookService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class Messages {

    private final Translate translate;
    private final AuthUserService authUserService;
    private final BookService bookService;

    public String helpMessage(String chatId) {
        String language = UserState.getLanguage(chatId);

        return "Free Book Bot: @flyingbookbot" + "\n\n" +
                Emojis.MAGIC + " <b>" + translate.getTranslation("magic.commands", language) + "</b> " + "\n\n" +
                "/start -- " + translate.getTranslation("start", language) + "\n" +
                "/search -- " + translate.getTranslation("search", language) + "\n" +
                "<i>" + translate.getTranslation("search.definition", language) + "</i>" + "\n" +
                "/top -- " + translate.getTranslation("top", language) + "\n" +
                "/stats -- " + translate.getTranslation("stats", language) + "\n" +
                "/help -- " + translate.getTranslation("help", language) + "\n" +
                "/developers -- " + translate.getTranslation("developers", language) + "\n" +
                "/settings -- " + translate.getTranslation("settings", language);
    }

    public StringBuilder statsMessage(String chatId) {
        String language = UserState.getLanguage(chatId);
        StringBuilder stats = new StringBuilder();

        stats.append("<code>").append(translate.getTranslation("bot.statistics", language)).append("</code>").append("\n\n")
                .append(Emojis.USERS).append(" ").append(translate.getTranslation("number.all.users", language))
                .append(" <b>").append(authUserService.count()).append("</b>\n")
                .append(Emojis.USER).append(" ").append(translate.getTranslation("number.users.with.user", language))
                .append(" <b>").append(authUserService.count(AuthRole.USER)).append("</b>\n")
                .append(Emojis.MANAGER).append(" ").append(translate.getTranslation("number.users.with.manager", language))
                .append(" <b>").append(authUserService.count(AuthRole.MANAGER)).append("</b>\n")
                .append(Emojis.MANAGER).append(" ").append(translate.getTranslation("number.users.with.admin", language))
                .append(" <b>").append(authUserService.count(AuthRole.ADMIN)).append("</b>\n")
                .append(Emojis.BOOKS).append(" ").append(translate.getTranslation("number.all.books", language))
                .append(" <b>").append(bookService.count()).append("</b>\n");

        return stats;
    }

    public String developerMessage(String chatId) {
        String language = UserState.getLanguage(chatId);
        return ("""
                <b><code>%s</code>\s
                Doston Bokhodirov  |  @dostonbokhodirov
                Nodirbek Juraev  |  @Nodirbeke
                Jasurbek Mutalov  |  @mutalov777
                Tojimuradov Maxmadiyor  |  @diyor_unicorn</b>""")
                .formatted(translate.getTranslation("team", language));
    }

    public StringBuilder bookMessage(List<Book> books, String chatId) {
        String language = UserState.getLanguage(chatId);
        StringBuilder text = new StringBuilder();
        int i = 0;
        if (books.size() == 0) text.append(translate.getTranslation("no.book", language));
        else for (Book book : books) {
            double size = getByMegabytes(book.getSize());
            text.append("<code>")
                    .append(i++).append("</code> ")
                    .append(translate.getTranslation("book.name", language))
                    .append(" <b>").append(book.getName()).append("</b>\n")
                    .append("      ").append(translate.getTranslation("book.size", language))
                    .append(" <i>").append(size).append(" ")
                    .append(translate.getTranslation("mb", language)).append("</i>\n")
                    .append("      ").append(translate.getTranslation("uploaded.at", language))
                    .append(" <code>").append(book.getUploadedAt()).append("</code>\n")
                    .append("      ").append(translate.getTranslation("number.downloads", language))
                    .append(" <b>").append(book.getDownloadsCount()).append("</b>\n");
        }
        return text;
    }

    public StringBuilder authUserMessage(List<AuthUser> authUsers, String chatId) {
        String language = UserState.getLanguage(chatId);
        StringBuilder text = new StringBuilder();
        int i = 0;
        if (authUsers.size() == 0) text.append(translate.getTranslation("no.user", language));
        else for (AuthUser user : authUsers) {
            text.append("<code>")
                    .append(i++).append("</code> ")
                    .append(translate.getTranslation("user.full.name", language)).append(" <b>")
                    .append(user.getFullName()).append("</b>\n")
                    .append("     ").append(translate.getTranslation("user.age", language)).append(" <b>")
                    .append(user.getAge()).append("</b>\n")
                    .append("     ").append(translate.getTranslation("user.name", language)).append(" @")
                    .append(user.getUserName()).append("\n")
                    .append("     ").append(translate.getTranslation("user.role", language)).append(" <code>")
                    .append(user.getRole()).append("</code>\n");
        }
        return text;
    }

    private double getByMegabytes(String byteSize) {
        return Math.round(Double.parseDouble(byteSize) / 1048576 * 100.0) / 100.0;
    }
}
