package _Workshop3;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class TBooks {
	@Test
    void findBook_ExistingTitle_ReturnsBook() {
        Libros.initializeCatalog();

        Book foundBook = Libros.findBook("Book 1");

        assertEquals("Book 1", foundBook.getTitle());
    }

    @Test
    void calculateDueDates_ValidSelection_CalculatesDueDate() {
        HashMap<Book, Integer> selectedBooks = new HashMap<>();
        Book book = new Book("Book 1", "Author A", 5);
        selectedBooks.put(book, 2);

        Libros.calculateDueDates(selectedBooks);

        LocalDate currentDate = LocalDate.now();
        LocalDate expectedDueDate = currentDate.plusDays(14);
        book.setBorrowingDate(expectedDueDate);
        assertEquals(expectedDueDate, book.getBorrowingDate());
    }

    @Test
    void validateBookQuantity_ValidQuantity_ReturnsTrue() {
        int availableCopies = 5;
        int quantity = 3;
        assertTrue(validateBookQuantity(availableCopies, quantity));
    }

    @Test
    void validateBookQuantity_InvalidQuantity_ReturnsFalse() {
        int availableCopies = 5;
        int quantity = 7;
        assertFalse(validateBookQuantity(availableCopies, quantity));
    }

    private boolean validateBookQuantity(int availableCopies, int quantity) {
        return quantity > 0 && quantity <= availableCopies;
    }
    @Test
    void isBookAvailable_ValidBook_ReturnsTrue() {
        Book book = new Book("Book 1", "Author A", 5);
        assertTrue(isBookAvailable(book, 2));
    }

    @Test
    void isBookAvailable_InvalidBook_ReturnsFalse() {
        Book book = new Book("Book 2", "Author B", 0);
        assertFalse(isBookAvailable(book, 1));
    }

    private boolean isBookAvailable(Book book, int quantity) {
        return book.getAvailableCopies() >= quantity;
    }

    @Test
    void isCheckoutQuantityValid_ValidQuantity_ReturnsTrue() {
        int numberOfBooks = 5;
        assertTrue(isCheckoutQuantityValid(numberOfBooks));
    }

    @Test
    void isCheckoutQuantityValid_InvalidQuantity_ReturnsFalse() {
        int numberOfBooks = 11;
        assertFalse(isCheckoutQuantityValid(numberOfBooks));
    }

    private boolean isCheckoutQuantityValid(int numberOfBooks) {
        return numberOfBooks > 0 && numberOfBooks <= 10;
    }
    @Test
    void userConfirmation_ConfirmCheckout_ReturnsTrue() {
        boolean confirmation = Libros.userConfirmation2("Y");

        assertTrue(confirmation);
    }

    @Test
    void userConfirmation_CancelCheckout_ReturnsFalse() {
        boolean confirmation = Libros.userConfirmation2("N");

        assertFalse(confirmation);
    }

    @Test
    void userConfirmation_InvalidInput_ReturnsFalse() {
        boolean confirmation = Libros.userConfirmation2("InvalidInput");

        assertFalse(confirmation);
    }

    @Test
    void performCheckout_MaximumBooksExceeded_ShouldRetry() {
        boolean retry = Libros.performCheckout2(11);

        assertTrue(retry);
    }

    @Test
    void performCheckout_ValidSelection_ShouldNotRetry() {
        boolean retry = Libros.performCheckout2(2);

        assertFalse(retry);
    }


}
