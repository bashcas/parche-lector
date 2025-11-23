package com.parchelector.service;

import com.parchelector.dto.request.AddBookToListRequest;
import com.parchelector.dto.request.CreateListRequest;
import com.parchelector.dto.request.UpdateListRequest;
import com.parchelector.dto.response.ListResponse;
import com.parchelector.dto.response.ListResponse.BookInList;
import com.parchelector.model.entity.*;
import com.parchelector.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service for reading list operations.
 * 
 * @author Nicolas Arciniegas
 */
@Service
public class ListService {

    @Autowired
    private LibraryListRepository libraryListRepository;

    @Autowired
    private ListBookRepository listBookRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ListLikeRepository listLikeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Create a new reading list.
     */
    @Transactional
    public ListResponse createList(Long userId, CreateListRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        LibraryList list = new LibraryList();
        list.setUser(user);
        list.setName(request.getName());
        list.setDescription(request.getDescription());
        list.setVisibility(request.getVisibility());

        LibraryList savedList = libraryListRepository.save(list);
        return mapToListResponse(savedList);
    }

    /**
     * Update an existing reading list.
     */
    @Transactional
    public ListResponse updateList(Long userId, Long listId, UpdateListRequest request) {
        LibraryList list = libraryListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to update this list");
        }

        if (request.getName() != null) {
            list.setName(request.getName());
        }
        if (request.getDescription() != null) {
            list.setDescription(request.getDescription());
        }
        if (request.getVisibility() != null) {
            list.setVisibility(request.getVisibility());
        }

        LibraryList updatedList = libraryListRepository.save(list);
        return mapToListResponse(updatedList);
    }

    /**
     * Delete a reading list.
     */
    @Transactional
    public void deleteList(Long userId, Long listId) {
        LibraryList list = libraryListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to delete this list");
        }

        libraryListRepository.delete(list);
    }

    /**
     * Get list details by ID.
     */
    @Transactional(readOnly = true)
    public ListResponse getListById(Long listId, Long currentUserId) {
        LibraryList list = libraryListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        // Check visibility permissions
        if (list.getVisibility().equals("PRIVATE") && !list.getUser().getId().equals(currentUserId)) {
            throw new IllegalArgumentException("You don't have permission to view this list");
        }

        return mapToListResponse(list);
    }

    /**
     * Add a book to a reading list.
     */
    @Transactional
    public void addBookToList(Long userId, Long listId, AddBookToListRequest request) {
        LibraryList list = libraryListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this list");
        }

        Book book = bookRepository.findById(request.getBookId())
                .orElseThrow(() -> new IllegalArgumentException("Book not found"));

        // Check if book is already in the list
        ListBook.ListBookId id = new ListBook.ListBookId(listId, request.getBookId());
        if (listBookRepository.existsById(id)) {
            throw new IllegalArgumentException("Book is already in this list");
        }

        ListBook listBook = new ListBook();
        listBook.setId(id);
        listBook.setList(list);
        listBook.setBook(book);
        listBook.setPosition(request.getPosition() != null ? request.getPosition() : 1);
        listBook.setNote(request.getNote());

        listBookRepository.save(listBook);
    }

    /**
     * Remove a book from a reading list.
     */
    @Transactional
    public void removeBookFromList(Long userId, Long listId, Long bookId) {
        LibraryList list = libraryListRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("List not found"));

        if (!list.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("You don't have permission to modify this list");
        }

        ListBook.ListBookId id = new ListBook.ListBookId(listId, bookId);
        ListBook listBook = listBookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Book not found in this list"));

        listBookRepository.delete(listBook);
    }

    /**
     * Get public lists for a user.
     */
    public List<ListResponse> getUserPublicLists(Long userId, Long currentUserId) {
        // Check if current user follows the target user
        boolean isFollower = false;
        if (currentUserId != null) {
            isFollower = followRepository.existsByFollowerIdAndFollowedId(currentUserId, userId);
        }

        List<LibraryList> lists = libraryListRepository.findPublicListsByUserId(userId, isFollower);
        
        return lists.stream()
                .map(this::mapToListResponse)
                .collect(Collectors.toList());
    }

    /**
     * Map LibraryList entity to ListResponse DTO.
     */
    private ListResponse mapToListResponse(LibraryList list) {
        List<ListBook> listBooks = listBookRepository.findByListIdWithBooks(list.getId());
        int bookCount = listBooks.size();
        int likeCount = listLikeRepository.countByListId(list.getId());

        List<BookInList> books = listBooks.stream()
                .map(lb -> {
                    List<String> authors = lb.getBook().getAuthors().stream()
                            .map(Author::getName)
                            .collect(Collectors.toList());

                    return new BookInList(
                            lb.getBook().getId(),
                            lb.getBook().getTitle(),
                            lb.getBook().getCoverUrl(),
                            authors,
                            lb.getPosition(),
                            lb.getNote(),
                            lb.getAddedAt().format(DATE_FORMATTER)
                    );
                })
                .collect(Collectors.toList());

        return new ListResponse(
                list.getId(),
                list.getName(),
                list.getDescription(),
                list.getVisibility(),
                list.getUser().getId(),
                list.getUser().getUsername(),
                list.getCreatedAt().format(DATE_FORMATTER),
                list.getUpdatedAt().format(DATE_FORMATTER),
                bookCount,
                likeCount,
                books
        );
    }
}
