package _Workshop3;
import java.util.*;
import java.time.*;
import java.time.format.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

class Book {
	private String title;
	private String author;
	private int availableCopies;
    private LocalDate borrowingDate;

	public Book(String title, String author, int availableCopies) {
	    this.title = title;
	    this.author = author;
	    this.availableCopies = availableCopies;
	}
	
	public String getTitle() {
	    return title;
	}
	
	public String getAuthor() {
	    return author;
	}
	
	public int getAvailableCopies() {
	    return availableCopies;
	}
	
	public void decreaseAvailableCopies(int quantity) {
	    availableCopies -= quantity;
	}
	
	public void increaseAvailableCopies(int quantity) {
	    availableCopies += quantity;
	}
    public LocalDate getBorrowingDate() {
        return borrowingDate;
    }

    public void setBorrowingDate(LocalDate borrowingDate) {
        this.borrowingDate = borrowingDate;
    }
}


public class Libros {
    private static Scanner scanner = new Scanner(System.in);
    private static ArrayList<Book> catalog = new ArrayList<>();

    public static void main(String[] args) {
        initializeCatalog();
        performCheckout();
    }

    public static void initializeCatalog() {
        catalog.add(new Book("Book 1", "Author A", 5));
        catalog.add(new Book("Book 2", "Author B", 3));
    }

    public static void displayCatalog() {
        System.out.println("Available Books:");
        System.out.println("Title\tAuthor\tAvailable Copies");
        for (Book book : catalog) {
            System.out.println(book.getTitle() + "\t" + book.getAuthor() + "\t" + book.getAvailableCopies());
        }
    }

    public static void performCheckout() {
        displayCatalog();
        System.out.println("Enter the number of books to checkout (Maximum 10):");
        int numberOfBooks = scanner.nextInt();

        if (numberOfBooks > 10 || numberOfBooks <= 0) {
            System.out.println("Invalid quantity! Please enter a number between 1 and 10.");
            performCheckout();
            return;
        }

        HashMap<Book, Integer> selectedBooks = new HashMap<>();
        for (int i = 0; i < numberOfBooks; i++) {
        	 boolean validBook = false;
             Book selectedBook = null;
             boolean validQuantity = false;
             while (!validBook) {
                 scanner.nextLine();
                 System.out.println("Enter the title of book #" + (i + 1) + ":");
                 String bookTitle = scanner.nextLine();
                 selectedBook = findBook(bookTitle);

                 if (selectedBook != null && selectedBook.getAvailableCopies() > 0) {
                     validBook = true;
                 } else {
                     System.out.println("Book not available or invalid title. Please re-enter." );
                     System.out.println(selectedBook);

                 }
             }
             
             while (!validQuantity) {
                 System.out.println("Enter the quantity :");
                 int quantity = scanner.nextInt();
                 System.out.println(quantity);
                 if (quantity > 0 && quantity <= selectedBook.getAvailableCopies() ) {
                     System.out.println(quantity);
                	 validQuantity = true;
                	 selectedBooks.put(selectedBook, quantity);
                     selectedBook.decreaseAvailableCopies(quantity);
                 }else {
                     System.out.println("Invalid Quantity. Please re-enter.");
                 }

             }
        }
        displaySelectedBooks(selectedBooks);
        calculateDueDates(selectedBooks);
		userConfirmation(selectedBooks);
		processBookReturn();
    }

    public static Book findBook(String title) {
        for (Book book : catalog) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }

    public static void displaySelectedBooks(HashMap<Book, Integer> selectedBooks) {
        System.out.println("\nSelected Books:");
        System.out.println("Title\tAuthor\tQuantity");
        for (Map.Entry<Book, Integer> entry : selectedBooks.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();
            System.out.println(book.getTitle() + "\t" + book.getAuthor() + "\t" + quantity);
        }
    }
    public static void calculateDueDates(HashMap<Book, Integer> selectedBooks) {
        LocalDate currentDate = LocalDate.now();

        for (Map.Entry<Book, Integer> entry : selectedBooks.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();
            
            LocalDate dueDate = currentDate.plusDays(14);
            
            System.out.println("Due date for " + quantity + " copy/copies of " + book.getTitle() + ": " + dueDate);
        }
    }

    public static void calculateLateFees(HashMap<Book, Integer> selectedBooks) {
    	double lateFeePerDay = 1.0;         
        LocalDate currentDate = LocalDate.now();
        double totalLateFees = 0.0;

        for (Map.Entry<Book, Integer> entry : selectedBooks.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();

            LocalDate dueDate = LocalDate.now().plusDays(14);

            long daysOverdue = ChronoUnit.DAYS.between(dueDate, currentDate);
            if (daysOverdue > 0) {
                double lateFeeForBook = daysOverdue * lateFeePerDay * quantity;
                totalLateFees += lateFeeForBook;
                System.out.println("Late fee for " + quantity + " copy/copies of " + book.getTitle() + ": $" + lateFeeForBook);
            }
        }

        System.out.println("Total late fees accrued: $" + totalLateFees);
    }

    public static void userConfirmation(HashMap<Book, Integer> selectedBooks) {
        System.out.println("\nConfirm checkout? (Y/N):");
        String confirm = scanner.next();
        if (confirm.equalsIgnoreCase("Y")) {
            System.out.println("Checkout confirmed!");
            displayCheckoutDetails(selectedBooks);
        } else {
            System.out.println("Checkout cancelled. Please make changes to your selections.");
        }
    }

    public static void displayCheckoutDetails(HashMap<Book, Integer> selectedBooks) {
        System.out.println("\nCheckout Details:");
        System.out.println("Title\tAuthor\tQuantity\tDue Date");

        LocalDate currentDate = LocalDate.now();

        for (Map.Entry<Book, Integer> entry : selectedBooks.entrySet()) {
            Book book = entry.getKey();
            int quantity = entry.getValue();

            LocalDate dueDate = currentDate.plusDays(14);

            System.out.println(book.getTitle() + "\t" + book.getAuthor() + "\t" + quantity + "\t\t" + dueDate);
        }

        calculateLateFees(selectedBooks);
    }
    public static void processBookReturn() {
    	System.out.println("\nReturn Process:");
        System.out.println("Enter the number of books to return:");
        int numberOfBooks = scanner.nextInt();

        HashMap<Book, Integer> returnedBooks = new HashMap<>();

        for (int i = 0; i < numberOfBooks; i++) {
            scanner.nextLine();
            System.out.println("Enter the title of book #" + (i + 1) + " to return:");
            String bookTitle = scanner.nextLine();
            Book returnedBook = findBook(bookTitle);

            if (returnedBook != null) {
                System.out.println("Enter the quantity to return:");
                int returnQuantity = scanner.nextInt();

                scanner.nextLine();
                System.out.println("Enter the borrowing date (YYYY-MM-DD) for " + returnQuantity + " copies of " + bookTitle + ":");
                String borrowingDateStr = scanner.nextLine();
                LocalDate borrowingDate = LocalDate.parse(borrowingDateStr);
                returnedBook.setBorrowingDate(borrowingDate);

                LocalDate currentDate = LocalDate.now();
                long daysBorrowed = ChronoUnit.DAYS.between(borrowingDate, currentDate);
                long daysOverdue = Math.max(0, daysBorrowed - 14);

                double lateFees = 0.0;
                if (daysOverdue > 0) {
                    lateFees = daysOverdue * 1.0 * returnQuantity;
                    System.out.println("Additional late fees for " + returnQuantity + " copies of " +
                            returnedBook.getTitle() + ": $" + lateFees);
                }

                returnedBooks.put(returnedBook, returnQuantity);
                returnedBook.increaseAvailableCopies(returnQuantity);
            } else {
                System.out.println("Book not found. Please re-enter the correct title.");
                i--;
            }
        }

        displayReturnedBooks(returnedBooks);
        calculateLateFeesForReturnedBooks(returnedBooks);
        scanner.close();
    }

    public static void displayReturnedBooks(HashMap<Book, Integer> returnedBooks) {
        System.out.println("\nBooks Returned:");
        System.out.println("Title\tAuthor\tReturned Quantity");
        for (Map.Entry<Book, Integer> entry : returnedBooks.entrySet()) {
            Book book = entry.getKey();
            int quantityReturned = entry.getValue();
            System.out.println(book.getTitle() + "\t" + book.getAuthor() + "\t" + quantityReturned);
        }
    }
    public static void calculateLateFeesForReturnedBooks(HashMap<Book, Integer> returnedBooks) {
        double lateFeePerDay = 1.0; 
        LocalDate currentDate = LocalDate.now();
        double totalLateFees = 0.0;

        for (Map.Entry<Book, Integer> entry : returnedBooks.entrySet()) {
            Book book = entry.getKey();
            int returnQuantity = entry.getValue();

            LocalDate borrowingDate = book.getBorrowingDate();

            long daysBorrowed = ChronoUnit.DAYS.between(borrowingDate, currentDate);
            long daysOverdue = Math.max(0, daysBorrowed - 14);

            if (daysOverdue > 0) {
                double lateFeeForBook = daysOverdue * lateFeePerDay * returnQuantity;
                totalLateFees += lateFeeForBook;
                System.out.println("Additional late fees for " + returnQuantity + " copies of " +
                        book.getTitle() + ": $" + lateFeeForBook);
            }
        }

        System.out.println("Total late fees accrued for returned books: $" + totalLateFees);
    }
    
    public static boolean userConfirmation2(String input) {
        return input.equalsIgnoreCase("Y");
    }

    public static boolean performCheckout2(int numberOfBooks) {
        if (numberOfBooks > 10 || numberOfBooks <= 0) {
            System.out.println("Invalid quantity! Please enter a number between 1 and 10.");
            return true; 
        }


        return false; 
    }


}


