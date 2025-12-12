# API Integration Documentation

## Overview

Parche Lector integrates with the **Google Books API** to provide book discovery and search functionality. This document details the API integration, data models, and usage patterns.

---

## Google Books API

### Service File
**Location**: `src/services/googleBooksService.ts`

### Base Configuration

```typescript
import axios from 'axios'

const GOOGLE_BOOKS_API = 'https://www.googleapis.com/books/v1/volumes'
const API_KEY = import.meta.env.VITE_GOOGLE_BOOKS_API_KEY // Optional
```

---

## Data Models

### Book Interface

```typescript
export interface Book {
  id: string              // Unique book identifier from Google Books
  title: string           // Book title
  author: string          // Primary author(s)
  cover: string           // Cover image URL
  rating: number          // Average rating (0-5)
  status?: BookStatus     // User's reading status (optional)
}

export type BookStatus = 'leyendo' | 'leido' | 'por_leer'
```

### API Response Transformation

The Google Books API returns complex nested data structures. Our service transforms these into simplified `Book` objects.

#### Raw Google Books Response
```json
{
  "kind": "books#volume",
  "id": "zyTCAlFPjgYC",
  "volumeInfo": {
    "title": "The Google Story",
    "authors": ["David A. Vise", "Mark Malseed"],
    "imageLinks": {
      "thumbnail": "http://books.google.com/books/content?id=..."
    },
    "averageRating": 4.0
  }
}
```

#### Transformed Book Object
```typescript
{
  id: "zyTCAlFPjgYC",
  title: "The Google Story",
  author: "David A. Vise, Mark Malseed",
  cover: "http://books.google.com/books/content?id=...",
  rating: 4.0
}
```

---

## API Functions

### 1. getRandomBooks()

Fetches a set of random popular books for the trending section.

#### Signature
```typescript
async function getRandomBooks(count: number = 12): Promise<Book[]>
```

#### Parameters
- `count` (optional): Number of books to fetch (default: 12)

#### Returns
- `Promise<Book[]>`: Array of Book objects

#### Implementation Strategy
Since Google Books doesn't have a "random" endpoint, the function:
1. Uses common search terms (e.g., "bestseller", "popular fiction")
2. Randomizes the query each time
3. Extracts and transforms the results

#### Usage Example
```typescript
import { getRandomBooks } from '@/services/googleBooksService'

// Fetch 12 random books
const books = await getRandomBooks(12)

// Fetch 20 random books
const moreBooks = await getRandomBooks(20)
```

#### Error Handling
```typescript
try {
  const books = await getRandomBooks()
  // Handle success
} catch (error) {
  console.error('Failed to fetch books:', error)
  // Show error message to user
}
```

---

### 2. searchBooks()

Searches for books matching a specific query.

#### Signature
```typescript
async function searchBooks(query: string): Promise<Book[]>
```

#### Parameters
- `query`: Search string (book title, author, ISBN, etc.)

#### Returns
- `Promise<Book[]>`: Array of matching Book objects

#### Usage Example
```typescript
import { searchBooks } from '@/services/googleBooksService'

// Search by title
const harryPotter = await searchBooks('Harry Potter')

// Search by author
const tolkienBooks = await searchBooks('J.R.R. Tolkien')

// Search by ISBN
const specificBook = await searchBooks('9780439554930')
```

#### Search Tips
- Use specific terms for better results
- Author names work well
- ISBN provides exact matches
- Generic terms return many results

---

## API Request Details

### Request Format

#### Get Random Books
```http
GET https://www.googleapis.com/books/v1/volumes
  ?q=subject:fiction
  &orderBy=relevance
  &maxResults=12
  &key=YOUR_API_KEY
```

#### Search Books
```http
GET https://www.googleapis.com/books/v1/volumes
  ?q=Harry+Potter
  &maxResults=20
  &key=YOUR_API_KEY
```

### Query Parameters

| Parameter | Description | Values |
|-----------|-------------|--------|
| `q` | Search query | Any text |
| `maxResults` | Number of results | 1-40 (default: 10) |
| `orderBy` | Sort order | `relevance` or `newest` |
| `key` | API key | Your API key (optional) |

---

## Response Handling

### Successful Response

```typescript
const response = await axios.get(GOOGLE_BOOKS_API, { params })

if (response.data.items) {
  const books = response.data.items.map(transformToBook)
  return books
}
```

### Error Scenarios

#### No Results
```typescript
if (!response.data.items || response.data.items.length === 0) {
  return [] // Return empty array
}
```

#### API Error
```typescript
try {
  const response = await axios.get(...)
} catch (error) {
  if (error.response?.status === 429) {
    // Rate limit exceeded
    throw new Error('Too many requests. Please try again later.')
  }
  throw new Error('Failed to fetch books')
}
```

#### Network Error
```typescript
catch (error) {
  if (!error.response) {
    // Network error (no internet, CORS, etc.)
    throw new Error('Network error. Check your connection.')
  }
}
```

---

## Data Transformation

### Transform Function

```typescript
function transformToBook(item: GoogleBooksItem): Book {
  const volumeInfo = item.volumeInfo || {}
  
  return {
    id: item.id || '',
    title: volumeInfo.title || 'Untitled',
    author: volumeInfo.authors?.join(', ') || 'Unknown Author',
    cover: volumeInfo.imageLinks?.thumbnail || '/placeholder-book.png',
    rating: volumeInfo.averageRating || 3.5,
  }
}
```

### Handling Missing Data

- **Missing Authors**: Defaults to `"Unknown Author"`
- **Missing Cover**: Uses placeholder image
- **Missing Rating**: Defaults to 3.5
- **Missing Title**: Defaults to `"Untitled"`

---

## Rate Limiting

### Google Books API Limits

- **Without API Key**: 
  - 1,000 requests per day
  - Enforced per IP address

- **With API Key**:
  - Higher limits (varies by tier)
  - Better tracking and monitoring

### Best Practices

1. **Cache Results**: Store fetched books temporarily
2. **Debounce Search**: Wait for user to finish typing
3. **Limit Requests**: Don't fetch on every keystroke
4. **Handle Errors**: Gracefully handle rate limit errors

---

## Integration in Components

### HomeView Integration

```typescript
// src/views/HomeView.vue
import { getRandomBooks, searchBooks } from '@/services/googleBooksService'

const books = ref<Book[]>([])
const isLoading = ref(false)
const errorMessage = ref('')

// Load trending books
async function loadBooks() {
  isLoading.value = true
  errorMessage.value = ''
  
  try {
    books.value = await getRandomBooks(12)
  } catch (error) {
    errorMessage.value = 'Error loading books'
  } finally {
    isLoading.value = false
  }
}

// Search books
async function handleSearch() {
  if (!searchQuery.value.trim()) {
    return loadBooks() // Reset to trending
  }
  
  isLoading.value = true
  
  try {
    books.value = await searchBooks(searchQuery.value)
  } catch (error) {
    errorMessage.value = 'Search failed'
  } finally {
    isLoading.value = false
  }
}
```

---

## Configuration

### Environment Variables

Create a `.env` file in the frontend root:

```bash
# Optional: Google Books API Key
VITE_GOOGLE_BOOKS_API_KEY=your_api_key_here
```

### Getting an API Key

1. Go to [Google Cloud Console](https://console.cloud.google.com/)
2. Create a new project
3. Enable Google Books API
4. Create credentials (API Key)
5. Add to `.env` file

### Using the API Key

```typescript
const params = {
  q: query,
  maxResults: count,
  ...(API_KEY && { key: API_KEY })
}
```

---

## Future Enhancements

### Possible Improvements

1. **Caching Layer**
   - Implement localStorage caching
   - Cache search results for 5-10 minutes
   - Reduce API calls

2. **Pagination**
   - Add "Load More" functionality
   - Infinite scroll for search results

3. **Advanced Filtering**
   - Filter by genre/category
   - Filter by publication date
   - Filter by language

4. **Book Details View**
   - Show full book description
   - Display publisher information
   - Show preview links

5. **User Ratings**
   - Allow users to rate books
   - Store ratings in backend
   - Display user ratings alongside Google ratings

---

## Testing API Integration

### Manual Testing

```typescript
// Test in browser console
import { getRandomBooks, searchBooks } from '@/services/googleBooksService'

// Test random books
const books = await getRandomBooks(5)
console.log(books)

// Test search
const results = await searchBooks('javascript')
console.log(results)
```

### Error Simulation

```typescript
// Simulate network error
// Turn off internet and try fetching

// Simulate empty results
const results = await searchBooks('asdfqwerzxcv12345')
console.log(results) // Should be empty array
```

---

## API Response Examples

### Successful Search Response

```json
{
  "kind": "books#volumes",
  "totalItems": 425,
  "items": [
    {
      "id": "example-id-123",
      "volumeInfo": {
        "title": "Example Book",
        "authors": ["John Doe"],
        "imageLinks": {
          "thumbnail": "https://example.com/cover.jpg"
        },
        "averageRating": 4.2
      }
    }
  ]
}
```

### Empty Response

```json
{
  "kind": "books#volumes",
  "totalItems": 0
}
```

---

**API Documentation Version**: 1.0  
**Last Updated**: 2025-11-21
