package com.relatosdepapel.catalogue.service.impl;

<<<<<<< HEAD
import com.relatosdepapel.catalogue.dto.BookDTO;
import com.relatosdepapel.catalogue.entity.Book;
import com.relatosdepapel.catalogue.repository.BookRepository;
import com.relatosdepapel.catalogue.service.BookService;
import org.springframework.stereotype.Service;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
=======
import com.relatosdepapel.catalogue.document.BookDocument;
import com.relatosdepapel.catalogue.dto.BookDTO;
import com.relatosdepapel.catalogue.entity.Book;
import com.relatosdepapel.catalogue.repository.BookRepository;
import com.relatosdepapel.catalogue.repository.BookSearchRepository;
import com.relatosdepapel.catalogue.service.BookService;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.StreamSupport;
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5


@Service
public class BookServiceImpl implements BookService {

    private static final String BOOK_NOT_FOUND = "Book not found";

    private final BookRepository repository;
<<<<<<< HEAD

    public BookServiceImpl(BookRepository repository) {
        this.repository = repository;
=======
    private final BookSearchRepository searchRepository;
    private final ElasticsearchOperations elasticsearchOperations;

    public BookServiceImpl(BookRepository repository,
                          BookSearchRepository searchRepository,
                          ElasticsearchOperations elasticsearchOperations) {
        this.repository = repository;
        this.searchRepository = searchRepository;
        this.elasticsearchOperations = elasticsearchOperations;
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
    }


    // Añadir libro

    @Override
    public BookDTO create(BookDTO bookDTO) {
        Book book = mapToEntity(bookDTO);
        Book saved = repository.save(book);
<<<<<<< HEAD
=======
        indexBook(saved);
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
        return mapToDTO(saved);
    }


    // Actualizar

    @Override
    public BookDTO update(Long id, BookDTO bookDTO) {
        Book existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND));

        existing.setTitle(bookDTO.getTitle());
        existing.setAuthor(bookDTO.getAuthor());
        existing.setIsbn(bookDTO.getIsbn());
<<<<<<< HEAD
        existing.setCategory(bookDTO.getCategory());
        existing.setPublicationDate(bookDTO.getPublicationDate());
        existing.setRating(bookDTO.getRating());
        existing.setVisible(bookDTO.getVisible());

        return mapToDTO(repository.save(existing));
=======
        existing.setPrice(bookDTO.getPrice());
        existing.setStock(bookDTO.getStock());
        existing.setCategory(bookDTO.getCategory());
        existing.setPublicationYear(bookDTO.getPublicationDate() != null ? bookDTO.getPublicationDate().getYear() : null);
        existing.setRating(bookDTO.getRating());
        existing.setVisible(bookDTO.getVisible());

        Book saved = repository.save(existing);
        indexBook(saved);
        return mapToDTO(saved);
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
    }


    // Acualizar parcialmente 

    @Override
    public BookDTO partialUpdate(Long id, Map<String, Object> fields) {
        Book existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND));

        fields.forEach((key, value) -> {
            switch (key) {
                case "title" -> existing.setTitle((String) value);
                case "author" -> existing.setAuthor((String) value);
                case "isbn" -> existing.setIsbn((String) value);
<<<<<<< HEAD
                case "category" -> existing.setCategory((String) value);
                case "publicationDate" ->
                        existing.setPublicationDate(java.time.LocalDate.parse(value.toString()));
                case "rating" -> existing.setRating((Integer) value);
                case "visible" -> existing.setVisible((Boolean) value);
                default -> {
                    // campo desconocido
                }
            }
        });

        return mapToDTO(repository.save(existing));
=======
                case "price" -> existing.setPrice(new java.math.BigDecimal(value.toString()));
                case "stock" -> existing.setStock((Integer) value);
                case "category" -> existing.setCategory((String) value);
                case "publicationDate" -> existing.setPublicationYear(LocalDate.parse(value.toString()).getYear());
                case "publicationYear" -> existing.setPublicationYear(value instanceof Number n ? n.intValue() : Integer.parseInt(value.toString()));
                case "rating" -> existing.setRating(value instanceof Number n ? n.intValue() : Integer.parseInt(value.toString()));
                case "visible" -> existing.setVisible(Boolean.parseBoolean(value.toString()));
                case "coverImageUrl" -> existing.setCoverImageUrl((String) value);
                default -> { /* campo desconocido */ }
            }
        });

        Book saved = repository.save(existing);
        indexBook(saved);
        return mapToDTO(saved);
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
    }


    // Eliminar libro

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
<<<<<<< HEAD
=======
        searchRepository.deleteById(id);
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
    }


    // Buscar por ID

    @Override
    public BookDTO findById(Long id) {
        return repository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException(BOOK_NOT_FOUND));
    }


    // Encontrar todos los libros visibles

    @Override
    public List<BookDTO> findAllVisible() {
<<<<<<< HEAD
        return repository.findAll()
                .stream()
=======
        return repository.findAll().stream()
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
                .filter(book -> Boolean.TRUE.equals(book.getVisible()))
                .map(this::mapToDTO)
                .toList();
    }


<<<<<<< HEAD
    // Buscar
=======
    // Buscar (Elasticsearch; ya no usa base de datos relacional)
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5

    @Override
    public List<BookDTO> search(String title, String author, String isbn,
                                String category, Integer rating, Boolean visible) {

<<<<<<< HEAD
        return repository.findAll((root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (title != null) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("title")),
                                "%" + title.toLowerCase() + "%"
                        )
                );
            }

            if (author != null) {
                predicates.add(
                        cb.like(
                                cb.lower(root.get("author")),
                                "%" + author.toLowerCase() + "%"
                        )
                );
            }

            if (isbn != null) {
                predicates.add(cb.equal(root.get("isbn"), isbn));
            }

            if (category != null) {
                predicates.add(cb.equal(root.get("category"), category));
            }

            if (rating != null) {
                predicates.add(cb.equal(root.get("rating"), rating));
            }

            if (visible != null) {
                predicates.add(cb.equal(root.get("visible"), visible));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        }).stream()
          .map(this::mapToDTO)
          .toList();
=======
        Criteria criteria = null;

        if (title != null && !title.isBlank()) {
            criteria = criteria == null
                    ? Criteria.where("title").contains(title)
                    : criteria.and(Criteria.where("title").contains(title));
        }
        if (author != null && !author.isBlank()) {
            criteria = criteria == null
                    ? Criteria.where("author").contains(author)
                    : criteria.and(Criteria.where("author").contains(author));
        }
        if (isbn != null && !isbn.isBlank()) {
            criteria = criteria == null
                    ? Criteria.where("isbn").is(isbn)
                    : criteria.and(Criteria.where("isbn").is(isbn));
        }
        if (category != null && !category.isBlank()) {
            criteria = criteria == null
                    ? Criteria.where("category").is(category)
                    : criteria.and(Criteria.where("category").is(category));
        }
        if (rating != null) {
            criteria = criteria == null
                    ? Criteria.where("rating").is(rating)
                    : criteria.and(Criteria.where("rating").is(rating));
        }
        if (visible != null) {
            criteria = criteria == null
                    ? Criteria.where("visible").is(visible)
                    : criteria.and(Criteria.where("visible").is(visible));
        }

        if (criteria == null) {
            return StreamSupport.stream(searchRepository.findAll().spliterator(), false)
                    .map(this::mapDocumentToDTO)
                    .toList();
        }

        return elasticsearchOperations.search(
                new CriteriaQuery(criteria),
                BookDocument.class
        ).stream()
         .map(SearchHit::getContent)
         .map(this::mapDocumentToDTO)
         .toList();
    }

    @Override
    public void reindex() {
        repository.findAll().forEach(this::indexBook);
    }

    private void indexBook(Book book) {
        searchRepository.save(mapToDocument(book));
    }

    private BookDocument mapToDocument(Book book) {
        BookDocument doc = new BookDocument();
        doc.setId(book.getId());
        doc.setTitle(book.getTitle());
        doc.setAuthor(book.getAuthor());
        doc.setIsbn(book.getIsbn());
        doc.setPrice(book.getPrice() != null ? book.getPrice().doubleValue() : null);
        doc.setStock(book.getStock());
        doc.setCategory(book.getCategory());
        doc.setPublicationYear(book.getPublicationYear());
        doc.setRating(book.getRating());
        doc.setVisible(book.getVisible());
        doc.setCoverImageUrl(book.getCoverImageUrl());
        return doc;
    }

    private BookDTO mapDocumentToDTO(BookDocument doc) {
        BookDTO dto = new BookDTO();
        dto.setId(doc.getId());
        dto.setTitle(doc.getTitle());
        dto.setAuthor(doc.getAuthor());
        dto.setIsbn(doc.getIsbn());
        dto.setPrice(doc.getPrice() != null ? java.math.BigDecimal.valueOf(doc.getPrice()) : null);
        dto.setStock(doc.getStock());
        dto.setCategory(doc.getCategory());
        dto.setPublicationDate(doc.getPublicationYear() != null ? LocalDate.of(doc.getPublicationYear(), 1, 1) : null);
        dto.setRating(doc.getRating());
        dto.setVisible(doc.getVisible());
        dto.setCoverImageUrl(doc.getCoverImageUrl());
        return dto;
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
    }



    private BookDTO mapToDTO(Book book) {
        BookDTO dto = new BookDTO();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        dto.setAuthor(book.getAuthor());
        dto.setIsbn(book.getIsbn());
<<<<<<< HEAD
        dto.setCategory(book.getCategory());
        dto.setPublicationDate(book.getPublicationDate());
        dto.setRating(book.getRating());
        dto.setVisible(book.getVisible());
=======
        dto.setPrice(book.getPrice());
        dto.setStock(book.getStock());
        dto.setCategory(book.getCategory());
        dto.setPublicationDate(book.getPublicationYear() != null ? LocalDate.of(book.getPublicationYear(), 1, 1) : null);
        dto.setRating(book.getRating());
        dto.setVisible(book.getVisible());
        dto.setCoverImageUrl(book.getCoverImageUrl());
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
        return dto;
    }

    private Book mapToEntity(BookDTO dto) {
        Book book = new Book();
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
<<<<<<< HEAD
        book.setIsbn(dto.getIsbn());
        book.setCategory(dto.getCategory());
        book.setPublicationDate(dto.getPublicationDate());
        book.setRating(dto.getRating());
        book.setVisible(dto.getVisible());
=======
        book.setIsbn(dto.getIsbn() != null && !dto.getIsbn().isBlank() ? dto.getIsbn().trim() : null);
        book.setPrice(dto.getPrice() != null ? dto.getPrice() : java.math.BigDecimal.ZERO);
        book.setStock(dto.getStock() != null ? dto.getStock() : 0);
        book.setCategory(dto.getCategory());
        book.setPublicationYear(dto.getPublicationDate() != null ? dto.getPublicationDate().getYear() : null);
        book.setRating(dto.getRating());
        book.setVisible(dto.getVisible() != null ? dto.getVisible() : Boolean.TRUE);
>>>>>>> 6baa5623e840a7fe5089992f8bef7184d6b8b5e5
        return book;
    }
}
