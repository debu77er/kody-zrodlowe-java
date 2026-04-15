import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The Library class represents a library containing books and users.
 */
public class Library {

    private List<Book> books;
    private List<User> users;
    private List<BorrowTransaction> borrowTransactions;

    public Library() {
        this.books = new ArrayList<>();
        this.users = new ArrayList<>();
        this.borrowTransactions = new ArrayList<>();
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void addUser(User user) {
        users.add(user);
    }

    public boolean borrowBook(User user, Book book) {
        if (!books.contains(book)) {
            System.out.println("The book is not available in the library.");
            return false;
        }
        if (book.isBorrowed()) {
            System.out.println("The book is already borrowed.");
            return false;
        }
        book.setBorrowed(true);
        BorrowTransaction transaction = new BorrowTransaction(user, book);
        borrowTransactions.add(transaction);
        System.out.println(user.getName() + " successfully borrowed the book: " + book.getTitle());
        return true;
    }

    public boolean returnBook(User user, Book book) {
        for (BorrowTransaction transaction : borrowTransactions) {
            if (transaction.getBook().equals(book) && transaction.getUser().equals(user)) {
                book.setBorrowed(false);
                borrowTransactions.remove(transaction);
                System.out.println(user.getName() + " successfully returned the book: " + book.getTitle());
                return true;
            }
        }
        System.out.println("Transaction not found.");
        return false;
    }

    public void displayBooks() {
        System.out.println("Available books in the library:");
        for (Book book : books) {
            if (!book.isBorrowed()) {
                System.out.println(book.getTitle() + " by " + book.getAuthor());
            }
        }
    }

    public void displayUsers() {
        System.out.println("Registered users:");
        for (User user : users) {
            System.out.println(user.getName() + " (ID: " + user.getId() + ")");
        }
    }

    public void showTransactions() {
        System.out.println("All borrow transactions:");
        for (BorrowTransaction transaction : borrowTransactions) {
            System.out.println(transaction.getUser().getName() + " borrowed " + transaction.getBook().getTitle());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Library library = new Library();

        // Sample data
        User user1 = new User(1, "Alice", "alice@example.com");
        User user2 = new User(2, "Bob", "bob@example.com");

        library.addUser(user1);
        library.addUser(user2);

        Book book1 = new Book("The Great Gatsby", "F. Scott Fitzgerald");
        Book book2 = new Book("1984", "George Orwell");

        library.addBook(book1);
        library.addBook(book2);

        while (true) {
            System.out.println("\nLibrary System Menu:");
            System.out.println("1. Display all books");
            System.out.println("2. Display all users");
            System.out.println("3. Borrow a book");
            System.out.println("4. Return a book");
            System.out.println("5. Show all transactions");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    library.displayBooks();
                    break;
                case 2:
                    library.displayUsers();
                    break;
                case 3:
                    System.out.print("Enter user ID to borrow the book: ");
                    int borrowUserId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter book title to borrow: ");
                    String borrowBookTitle = scanner.nextLine();

                    User borrowUser = library.getUserById(borrowUserId);
                    Book borrowBook = library.getBookByTitle(borrowBookTitle);

                    if (borrowUser != null && borrowBook != null) {
                        library.borrowBook(borrowUser, borrowBook);
                    } else {
                        System.out.println("Invalid user or book.");
                    }
                    break;
                case 4:
                    System.out.print("Enter user ID to return the book: ");
                    int returnUserId = scanner.nextInt();
                    scanner.nextLine(); // Consume newline
                    System.out.print("Enter book title to return: ");
                    String returnBookTitle = scanner.nextLine();

                    User returnUser = library.getUserById(returnUserId);
                    Book returnBook = library.getBookByTitle(returnBookTitle);

                    if (returnUser != null && returnBook != null) {
                        library.returnBook(returnUser, returnBook);
                    } else {
                        System.out.println("Invalid user or book.");
                    }
                    break;
                case 5:
                    library.showTransactions();
                    break;
                case 6:
                    System.out.println("Exiting the library system.");
                    return;
                default:
                    System.out.println("Invalid choice, please try again.");
                    break;
            }
        }
    }

    public User getUserById(int id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public Book getBookByTitle(String title) {
        for (Book book : books) {
            if (book.getTitle().equalsIgnoreCase(title)) {
                return book;
            }
        }
        return null;
    }
}

/**
 * The Book class represents a book in the library.
 */
class Book {
    private String title;
    private String author;
    private boolean isBorrowed;

    public Book(String title, String author) {
        this.title = title;
        this.author = author;
        this.isBorrowed = false;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public boolean isBorrowed() {
        return isBorrowed;
    }

    public void setBorrowed(boolean borrowed) {
        isBorrowed = borrowed;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Book book = (Book) obj;
        return title.equals(book.title);
    }

    @Override
    public int hashCode() {
        return title.hashCode();
    }
}

/**
 * The User class represents a user of the library.
 */
class User {
    private int id;
    private String name;
    private String email;

    public User(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}

/**
 * The BorrowTransaction class represents a borrow transaction of a user borrowing a book.
 */
class BorrowTransaction {
    private User user;
    private Book book;

    public BorrowTransaction(User user, Book book) {
        this.user = user;
        this.book = book;
    }

    public User getUser() {
        return user;
    }

    public Book getBook() {
        return book;
    }
}