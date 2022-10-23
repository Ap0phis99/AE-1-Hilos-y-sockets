public class Book {
	private String isbn;
	private String author;
	private String title;
	private int copies;
	private int borrowed;
	private double price;
	
	public Book(String isbn, String title, String author, double price, int copies) {
		this.isbn = isbn;
		this.title = title;
		this.author = author;
		this.price = price;
		this.copies = copies;
		this.borrowed = 0;
	}
	
	public String getIsbn() {
		return isbn;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getTitle() {
		return this.title;
	}	
	
	public double getprice() {
		return price;
	}
	

	public int getCopies() {
		return this.copies;
	}
	
	public int getCopiesAvailable() {
		return this.copies - this.borrowed;
	}
	
	public int addCopies(int n) {
		this.copies += n;		
		return this.copies;
	}
	
	public boolean isAvailable() {
		return this.copies - this.borrowed > 0;
	}
	
	public boolean borrow() {
		if(this.isAvailable()) {
			this.borrowed++;
			return true;
		}else {
			return false;
		}
	}
	
	public void back() {
		this.borrowed--;		
	}
}