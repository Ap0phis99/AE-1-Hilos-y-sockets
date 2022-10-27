package library;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Library {
	
	Map<String, Book> books;

	public Library() {
		 this.books = new HashMap<String, Book>();
		 this.loadDefBooks();
	}
	
	private void loadDefBooks() {
		this.addBook("978-8478884452", "La náusea", "Jean-Paul Sartre", 23.99, 5);
		this.addBook("847-8474564232", "1984", "Georges Orwell", 32.99, 4);
		this.addBook("452-8478332452", "Rebelión en la Granja", "Georges Orwell", 19.99, 4);
		this.addBook("898-5678884452", "Harry Potter y la piedra filosofal", "J.K. Rowling", 19.99, 10);
		this.addBook("432-8823584452", "Harry Potter y la cámara secreta", "J.K. Rowling", 19.99, 8);
		this.addBook("123-8478885566", "Harry Potter y el priosionero de Azkabán", "J.K. Rowling", 19.99, 7);
		this.addBook("124-1110001000", "test", "testautor", 24.00, 5);
	}
	
	public void addBook(String isbn, String title, String author, double price) {
		this.addBook(isbn, title, author, price, 1);
	}
	
	public void addBook(String isbn, String title, String author,double price, int copies) {
		if(this.books.containsKey(isbn)) {
			Book the_book = this.books.get(isbn);
			the_book.addCopies(copies);
			this.books.put(isbn, the_book);
		}else {
			books.put(isbn,new Book(isbn, title, author, price, copies));
		}
	}
	
	private String title2isbn(String title) {
		for(String isbn : this.books.keySet()) {
			Book book = this.books.get(isbn);
			if(book.getTitle().equals(title)) {
				return isbn;
			}
		}
		return null;
	}
	
	private Book getBook(String isbn) {
		if(this.books.containsKey(isbn) && isbn != null) {
			return this.books.get(isbn);
		}else {
			return null;
		}
	}
	
	public Book getBookByISBN(String isbn) {
		return this.getBook(isbn);
	}
	
	public Book getBookByTitle(String title) {	
		return this.getBook(this.title2isbn(title));
	}	
	
	public ArrayList<Book> getBooksByAuthor(String author) {
		ArrayList<Book> books_by_auth = new ArrayList<Book>();
		for(String isbn : this.books.keySet()) {
			Book current_book = this.books.get(isbn);
			if(current_book.getAuthor() == author) {
				books_by_auth.add(current_book);
			}
		}
		return books_by_auth;
	}
	
	public Book borrowBookByISBN(String isbn) {
		Book the_book = this.getBookByISBN(isbn);
		if(the_book.borrow()) {
			this.books.put(isbn, the_book);
			return the_book;
		}
		return null;		
	}
	
	public Book borrowBookByTitle(String title) {
		return this.borrowBookByISBN(this.title2isbn(title));
	}
	
	public Book backBookByISBN(String isbn) {
		Book the_book = this.getBookByISBN(isbn);
		the_book.back();
		this.books.put(isbn, the_book);
		return the_book;
	}
	
	public Book backBookByTitle(String title) {
		return this.backBookByISBN(this.title2isbn(title));
	}
}
