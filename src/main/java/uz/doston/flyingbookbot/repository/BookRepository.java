package uz.doston.flyingbookbot.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import uz.doston.flyingbookbot.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

    Page<Book> findAllByDownloadsCount(Pageable pageable);

}
