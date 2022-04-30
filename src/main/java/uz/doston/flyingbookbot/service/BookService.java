package uz.doston.flyingbookbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.doston.flyingbookbot.repository.BookRepository;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Long count() {
        return bookRepository.count();
    }
}
