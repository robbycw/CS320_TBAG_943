package edu.ycp.cs320.tbag_943.gamedb;

import java.util.List;
import java.util.Scanner;

import edu.ycp.cs320.tbag_943.gamedb.model.Author;
import edu.ycp.cs320.tbag_943.gamedb.model.Book;
import edu.ycp.cs320.tbag_943.gamedb.model.Pair;
import edu.ycp.cs320.tbag_943.gamedb.persist.DatabaseProvider;
import edu.ycp.cs320.tbag_943.gamedb.persist.IDatabase;

//Code comes from CS320 Library Example. 
public class BooksByAuthorLastNameQuery {
	public static void main(String[] args) throws Exception {
		
		Scanner keyboard = new Scanner(System.in);

		// Create the default IDatabase instance
		InitDatabase.init(keyboard);
		
		System.out.print("Enter an author's last name: ");
		String lastName = keyboard.nextLine();
		
		IDatabase db = DatabaseProvider.getInstance();
		List<Pair<Author, Book>> authorBookList = db.findAuthorAndBookByAuthorLastName(lastName);
		if (authorBookList.isEmpty()) {
			System.out.println("No books found for author <" + lastName + ">");
		}
		else {
			for (Pair<Author, Book> authorBook : authorBookList) {
				Author author = authorBook.getLeft();
				Book book = authorBook.getRight();
				System.out.println(author.getLastname() + "," + author.getFirstname() + "," + book.getTitle() + "," + book.getIsbn());
			}			
		}

	}
}