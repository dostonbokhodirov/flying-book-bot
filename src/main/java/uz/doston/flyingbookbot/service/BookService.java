package uz.doston.flyingbookbot.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.inlinequery.result.InlineQueryResult;
import uz.doston.flyingbookbot.criteria.BookCriteria;
import uz.doston.flyingbookbot.dto.BookCreateDTO;
import uz.doston.flyingbookbot.entity.Book;
import uz.doston.flyingbookbot.mapper.BookMapper;
import uz.doston.flyingbookbot.repository.AuthUserRepository;
import uz.doston.flyingbookbot.repository.BookRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthUserRepository authUserRepository;

    public Long count() {
        return bookRepository.count();
    }

    public List<Book> getAllTopBooks(BookCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by("downloadsCount").descending()
        );
        return bookRepository.findAll(pageable).getContent();
    }

    public List<InlineQueryResult> getAllByMatches(String query) {
        return bookRepository.findByQuery(query);
    }

    public Book getByFileId(String bookId) {
        return bookRepository.findByFileId(bookId);
    }

    public void save(BookCreateDTO dto) {
        Book book = bookMapper.fromCreateDTO(dto);
        bookRepository.save(book);
    }

    public List<Book> getAllByGenre(BookCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by("uploadedAt").descending()
        );
        return bookRepository.findAllByGenreContainingIgnoreCase(criteria.getGenre(), pageable);
    }

    public List<Book> getAllByName(BookCriteria criteria) {
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by("uploadedAt").descending()
        );
        return bookRepository.findAllByNameContainingIgnoreCase(criteria.getName(), pageable);
    }

    public List<Book> getAllUploadedBooks(BookCriteria criteria, String chatId) {
        Long authUserid = authUserRepository.findIdByChatId(chatId);
        Pageable pageable = PageRequest.of(
                criteria.getPage(),
                criteria.getSize(),
                Sort.by("uploadedAt").descending()
        );
        return bookRepository.findAllByOwnerId(authUserid, pageable);
    }

    public Book get(String bookId) {
        return get(Long.valueOf(bookId));
    }

    public Book get(Long bookId) {
        return bookRepository.findById(bookId).orElse(null);
    }

    public void update(Book target) {
        bookRepository.save(target);
    }

    public void deleteByName(String name) {
        Book book = bookRepository.findFirstByNameContainingIgnoreCase(name);
        bookRepository.delete(book);
    }
}
