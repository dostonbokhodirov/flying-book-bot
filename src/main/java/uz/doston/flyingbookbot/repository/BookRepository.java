package uz.doston.flyingbookbot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import uz.doston.flyingbookbot.entity.Book;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

//    List<Book> findAllByDownloadsCount(Pageable pageable);

    List<Book> findAllByNameContainingIgnoreCase(String name, Pageable pageable);

    List<Book> findAllByGenreContainingIgnoreCase(String genre, Pageable pageable);

    List<Book> findAllByOwnerId(Long ownerId, Pageable pageable);

    Book findFirstByNameContainingIgnoreCase(String name);

    @Query(value = "select * from book where name like :query", nativeQuery = true)
    List<InlineQueryResult> findByQuery(@Param("query") String query);

    Book findByFileId(String bookId);
}
