package com.parchelector.service;

import com.parchelector.dto.request.FavoriteBookRequest;
import com.parchelector.dto.request.ReadingStatusRequest;
import com.parchelector.dto.response.BookResponse;
import com.parchelector.model.entity.*;
import com.parchelector.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BookService.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BookService Tests")
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ReadingStatusRepository readingStatusRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FavoriteBookRepository favoriteBookRepository;

    @InjectMocks
    private BookService bookService;

    private User testUser;
    private Book testBook;
    private Author testAuthor;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");

        // Create test author
        testAuthor = new Author();
        testAuthor.setId(1L);
        testAuthor.setName("Test Author");

        // Create test book
        testBook = new Book();
        testBook.setId(1L);
        testBook.setTitle("Test Book");
        testBook.setCoverUrl("http://example.com/cover.jpg");
        testBook.setAuthors(new HashSet<>(Collections.singletonList(testAuthor)));
    }

    @Nested
    @DisplayName("getTrendingBooks tests")
    class GetTrendingBooksTests {

        @Test
        @DisplayName("Should return list of trending books")
        void shouldReturnTrendingBooks() {
            // Arrange
            List<Book> books = Arrays.asList(testBook);
            Page<Book> bookPage = new PageImpl<>(books);
            
            when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
            when(reviewRepository.getAverageRatingByBookId(testBook.getId())).thenReturn(4.5);

            // Act
            List<BookResponse> result = bookService.getTrendingBooks(1L, 10);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Book", result.get(0).getTitle());
            assertEquals("Test Author", result.get(0).getAuthor());
            assertEquals(4.5, result.get(0).getRating());
        }

        @Test
        @DisplayName("Should return empty list when no books exist")
        void shouldReturnEmptyListWhenNoBooks() {
            // Arrange
            Page<Book> emptyPage = new PageImpl<>(Collections.emptyList());
            when(bookRepository.findAll(any(Pageable.class))).thenReturn(emptyPage);

            // Act
            List<BookResponse> result = bookService.getTrendingBooks(1L, 10);

            // Assert
            assertNotNull(result);
            assertTrue(result.isEmpty());
        }

        @Test
        @DisplayName("Should return 0 rating when no reviews exist")
        void shouldReturnZeroRatingWhenNoReviews() {
            // Arrange
            List<Book> books = Arrays.asList(testBook);
            Page<Book> bookPage = new PageImpl<>(books);
            
            when(bookRepository.findAll(any(Pageable.class))).thenReturn(bookPage);
            when(reviewRepository.getAverageRatingByBookId(testBook.getId())).thenReturn(null);

            // Act
            List<BookResponse> result = bookService.getTrendingBooks(1L, 10);

            // Assert
            assertEquals(0.0, result.get(0).getRating());
        }
    }

    @Nested
    @DisplayName("searchBooks tests")
    class SearchBooksTests {

        @Test
        @DisplayName("Should return books matching search query")
        void shouldReturnBooksMatchingQuery() {
            // Arrange
            List<Book> books = Arrays.asList(testBook);
            
            when(bookRepository.searchByTitleOrAuthor(eq("Test"), any(Pageable.class)))
                    .thenReturn(books);
            when(reviewRepository.getAverageRatingByBookId(testBook.getId())).thenReturn(4.0);

            // Act
            List<BookResponse> result = bookService.searchBooks("Test", 1L, 10);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
            assertEquals("Test Book", result.get(0).getTitle());
        }

        @Test
        @DisplayName("Should return empty list for no matches")
        void shouldReturnEmptyListForNoMatches() {
            // Arrange
            when(bookRepository.searchByTitleOrAuthor(eq("NonExistent"), any(Pageable.class)))
                    .thenReturn(Collections.emptyList());

            // Act
            List<BookResponse> result = bookService.searchBooks("NonExistent", 1L, 10);

            // Assert
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("updateReadingStatus tests")
    class UpdateReadingStatusTests {

        @Test
        @DisplayName("Should create new reading status when none exists")
        void shouldCreateNewReadingStatus() {
            // Arrange
            ReadingStatusRequest request = new ReadingStatusRequest();
            request.setBookId(1L);
            request.setStatus("READING");

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
            when(readingStatusRepository.findByUserIdAndBookId(1L, 1L)).thenReturn(Optional.empty());

            // Act
            bookService.updateReadingStatus(1L, request);

            // Assert
            verify(readingStatusRepository).save(any(ReadingStatus.class));
        }

        @Test
        @DisplayName("Should update existing reading status")
        void shouldUpdateExistingReadingStatus() {
            // Arrange
            ReadingStatus existingStatus = new ReadingStatus();
            existingStatus.setUser(testUser);
            existingStatus.setBook(testBook);
            existingStatus.setStatus(ReadingStatus.ReadingStatusEnum.WANT_TO_READ);

            ReadingStatusRequest request = new ReadingStatusRequest();
            request.setBookId(1L);
            request.setStatus("READ");

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
            when(readingStatusRepository.findByUserIdAndBookId(1L, 1L))
                    .thenReturn(Optional.of(existingStatus));

            // Act
            bookService.updateReadingStatus(1L, request);

            // Assert
            verify(readingStatusRepository).save(existingStatus);
            assertEquals(ReadingStatus.ReadingStatusEnum.READ, existingStatus.getStatus());
        }

        @Test
        @DisplayName("Should throw exception for invalid user")
        void shouldThrowExceptionForInvalidUser() {
            // Arrange
            ReadingStatusRequest request = new ReadingStatusRequest();
            request.setBookId(1L);
            request.setStatus("READING");

            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, 
                    () -> bookService.updateReadingStatus(999L, request));
        }

        @Test
        @DisplayName("Should throw exception for invalid book")
        void shouldThrowExceptionForInvalidBook() {
            // Arrange
            ReadingStatusRequest request = new ReadingStatusRequest();
            request.setBookId(999L);
            request.setStatus("READING");

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(bookRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, 
                    () -> bookService.updateReadingStatus(1L, request));
        }

        @Test
        @DisplayName("Should throw exception for invalid status")
        void shouldThrowExceptionForInvalidStatus() {
            // Arrange
            ReadingStatusRequest request = new ReadingStatusRequest();
            request.setBookId(1L);
            request.setStatus("INVALID_STATUS");

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
            when(readingStatusRepository.findByUserIdAndBookId(1L, 1L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(IllegalArgumentException.class, 
                    () -> bookService.updateReadingStatus(1L, request));
        }
    }

    @Nested
    @DisplayName("addFavorite tests")
    class AddFavoriteTests {

        @Test
        @DisplayName("Should add book to favorites")
        void shouldAddBookToFavorites() {
            // Arrange
            FavoriteBookRequest request = new FavoriteBookRequest();
            request.setBookId(1L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
            when(favoriteBookRepository.existsByUserIdAndBookId(1L, 1L)).thenReturn(false);

            // Act
            bookService.addFavorite(1L, request);

            // Assert
            verify(favoriteBookRepository).save(any(FavoriteBook.class));
        }

        @Test
        @DisplayName("Should throw exception when book is already in favorites")
        void shouldThrowExceptionWhenAlreadyFavorite() {
            // Arrange
            FavoriteBookRequest request = new FavoriteBookRequest();
            request.setBookId(1L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
            when(bookRepository.findById(1L)).thenReturn(Optional.of(testBook));
            when(favoriteBookRepository.existsByUserIdAndBookId(1L, 1L)).thenReturn(true);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, 
                    () -> bookService.addFavorite(1L, request));
        }
    }

    @Nested
    @DisplayName("removeFavorite tests")
    class RemoveFavoriteTests {

        @Test
        @DisplayName("Should remove book from favorites")
        void shouldRemoveBookFromFavorites() {
            // Arrange
            when(favoriteBookRepository.existsByUserIdAndBookId(1L, 1L)).thenReturn(true);

            // Act
            bookService.removeFavorite(1L, 1L);

            // Assert
            verify(favoriteBookRepository).deleteByUserIdAndBookId(1L, 1L);
        }

        @Test
        @DisplayName("Should throw exception when book is not in favorites")
        void shouldThrowExceptionWhenNotInFavorites() {
            // Arrange
            when(favoriteBookRepository.existsByUserIdAndBookId(1L, 1L)).thenReturn(false);

            // Act & Assert
            assertThrows(IllegalArgumentException.class, 
                    () -> bookService.removeFavorite(1L, 1L));
        }
    }

    @Nested
    @DisplayName("filterAndSortBooks tests")
    class FilterAndSortBooksTests {

        @Test
        @DisplayName("Should throw exception for invalid sortBy parameter")
        void shouldThrowExceptionForInvalidSortBy() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class, 
                    () -> bookService.filterAndSortBooks(1L, null, null, null, "invalid", 10));
        }

        @Test
        @DisplayName("Should filter books by rating")
        void shouldFilterBooksByRating() {
            // Arrange
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findBooksFilteredAndSortedByRating(any(), any(), any(), any(Pageable.class)))
                    .thenReturn(books);
            when(reviewRepository.getAverageRatingByBookId(testBook.getId())).thenReturn(4.5);

            // Act
            List<BookResponse> result = bookService.filterAndSortBooks(1L, null, null, null, "rating", 10);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should filter books by newest")
        void shouldFilterBooksByNewest() {
            // Arrange
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findBooksFilteredAndSortedByNewest(any(), any(), any(), any(Pageable.class)))
                    .thenReturn(books);
            when(reviewRepository.getAverageRatingByBookId(testBook.getId())).thenReturn(4.0);

            // Act
            List<BookResponse> result = bookService.filterAndSortBooks(1L, null, null, null, "newest", 10);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }

        @Test
        @DisplayName("Should filter books by popularity")
        void shouldFilterBooksByPopularity() {
            // Arrange
            List<Book> books = Arrays.asList(testBook);
            when(bookRepository.findBooksFilteredAndSortedByPopularity(any(), any(), any(), any(Pageable.class)))
                    .thenReturn(books);
            when(reviewRepository.getAverageRatingByBookId(testBook.getId())).thenReturn(4.0);

            // Act
            List<BookResponse> result = bookService.filterAndSortBooks(1L, null, null, null, "popular", 10);

            // Assert
            assertNotNull(result);
            assertEquals(1, result.size());
        }
    }
}

