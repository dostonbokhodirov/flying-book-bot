package uz.doston.flyingbookbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import uz.doston.flyingbookbot.criteria.BookCriteria;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

    public Long count() {
        return bookRepository.count();
    }

    public List<Book> getAllTopBooks(BookCriteria criteria) {
        Pageable pageable = PageRequest.of(criteria.getPage(), criteria.getSize(), Sort.Direction.DESC);
        return bookRepository.findAllByDownloadsCount(pageable).getContent();
    }

    public List<InlineQueryResult> getAllByMatches(String query) {
        return bookRepository.findByQuery(query);
    }
}
