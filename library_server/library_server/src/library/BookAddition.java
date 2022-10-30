package library;

import java.util.Map;

public class BookAddition extends Thread{
	private Map<String, Book> lib;
	private Book book;
	public BookAddition(Map<String, Book> lib, Book book) {
		this.lib = lib;
		this.book = book;
	}
	public void run(){
			this.lib.put(this.book.getIsbn(), this.book);
		
    }
	public Map<String, Book> getBooks(){
		return this.lib;
	}
}

