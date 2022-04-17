package edu.ycp.cs320.tbag_943.gamedb.persist;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import edu.ycp.cs320.booksdb.model.Author;
import edu.ycp.cs320.booksdb.model.Book;
import edu.ycp.cs320.booksdb.model.Pair;
import edu.ycp.cs320.tbag_943.classes.*;

//Code comes from CS320 Library Example. 
public class FakeDatabase implements IDatabase {
	
	private List<Author> authorList;
	private List<Book> bookList;
	
	// Fields
	
	// Remember that Map implementations will have the key set
	// contain the unique values and the value set contain the
	// repeated values, as keys cannot have multiple values. 
	
	// Ex: in userToGame, Game IDs will be in the key set (as 
	// they only occur once) and user IDs will be in the value
	// set. 
	private List<User> userList;
	private HashMap<String, Integer> userToGame; 
	private List<Game> gameList;  
	private List<ArrayList<String>> gameLogList; 
	private List<Player> playerList; 
	private List<Map> mapList; 
	private HashMap<Integer, Integer> playerToStats;
	private HashMap<Integer, Integer> playerInventory; 
	private List<Stat> playerStatsList; 
	private List<Item> itemList; 
	private List<Loot> lootList;
	private List<Location> locationList; 
	private HashMap<Integer, Integer> locationToNPC; 
	private List<WinCondition> winConditionList;
	private List<NPC> npcList; 
	private HashMap<Integer, Integer> npcToStats; 
	private HashMap<Integer, Integer> npcToSpeech; 
	private List<Stat> npcStatsList; 
	private List<Speech> speechList; 
	private HashMap<Integer, Integer> locationToCombat; 
	private HashMap<Integer, Integer> combatToNPC; 
	private HashMap<Integer, Integer> locationToPuzzle; 
	private List<Combat> combatList; 
	private List<Puzzle> puzzleList; 
	
	// Constructor
	public FakeDatabase() {
		this.userList = new ArrayList<User>();
		this.userToGame = new HashMap<String, Integer>();
		this.gameList = new ArrayList<Game>();
		this.gameLogList = new ArrayList<ArrayList<String>>();
		this.playerList = new ArrayList<Player>();
		this.mapList = new ArrayList<Map>();
		this.playerToStats = new HashMap<Integer, Integer>();
		this.playerInventory = new HashMap<Integer, Integer>();
		this.playerStatsList = new ArrayList<Stat>();
		this.itemList = new ArrayList<Item>();
		this.lootList = new ArrayList<Loot>();
		this.locationList = new ArrayList<Location>();
		this.locationToNPC = new HashMap<Integer, Integer>();
		this.winConditionList = new ArrayList<WinCondition>();
		this.npcList = new ArrayList<NPC>();
		this.npcToStats = new HashMap<Integer, Integer>();
		this.npcToSpeech = new HashMap<Integer, Integer>();
		this.npcStatsList = new ArrayList<Stat>();
		this.speechList = new ArrayList<Speech>();
		this.locationToCombat = new HashMap<Integer, Integer>();
		this.combatToNPC = new HashMap<Integer, Integer>();
		this.locationToPuzzle = new HashMap<Integer, Integer>();
		this.combatList = new ArrayList<Combat>();
		this.puzzleList = new ArrayList<Puzzle>();
	}

	// Fake database constructor - initializes the DB
	// the DB only consists for a List of Authors and a List of Books
	/*public FakeDatabase() {
		authorList = new ArrayList<Author>();
		bookList = new ArrayList<Book>();
		
		// Add initial data
		readInitialData();
		
//		System.out.println(authorList.size() + " authors");
//		System.out.println(bookList.size() + " books");
	}*/

	// loads the initial data retrieved from the CSV files into the DB
	public void readInitialData() {
		try {
			authorList.addAll(InitialData.getAuthors());
			bookList.addAll(InitialData.getBooks());
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}
	
	// Finds
	public Loot findLootByLocationID(int locationID) {
		// Get the location from Location Table based on given id. 
		Location loc = locationList.get(locationID); 
		
		// Retrieve lootID from the Location
		int lootID = loc.getTreasure().getId();
		
		// Return the loot. 
		return lootList.get(lootID); 
	}
	
	public HashMap<String, Item> findInventoryByPlayerID(int playerID) {
		// Retrieve a list of item IDs with given player ID. 
		// keySet contains all of the item IDs. Add the item
		// to a list of integers if its value is the playerID. 
		ArrayList<Integer> itemIDs = new ArrayList<Integer>(); 
		for(Integer i : playerInventory.keySet()) {
			if(playerInventory.get(i) == playerID) {
				itemIDs.add(i); 
			}
		}
		
		// This list of item IDs will be used to retrieve the items
		// from itemList. 
		HashMap<String, Item> inventory = new HashMap<String, Item>(); 
		for(Integer i : itemIDs) {
			Item item = itemList.get(i); 
			String name = item.getName(); 
			
			inventory.put(name.toLowerCase(), item); 
		}
		
		return inventory; 
	}
	
	public HashMap<String, Stat> findPlayerStatsByPlayerID(int playerID){
		// Retrieve a list of stat IDs with given player ID. 
		// keySet contains all of the stat IDs. Add the statID
		// to a list of integers if its value is the playerID. 
		ArrayList<Integer> statIDs = new ArrayList<Integer>(); 
		for(Integer i : playerToStats.keySet()) {
			if(playerToStats.get(i) == playerID) {
				statIDs.add(i); 
			}
		}
		
		// Using list of Stat IDs, we want to create a 
		// HashMap that takes the stat name and maps it 
		// to the Stat class. 
		HashMap<String, Stat> stats = new HashMap<String, Stat>(); 
		for(Integer i : statIDs) {
			Stat stat = playerStatsList.get(i); 
			String statName = stat.getName(); 
			stats.put(statName, stat); 
		}
		
		return stats; 
	}
	
	public HashMap<String, Stat> findNPCStatsByNPCID(int npcID){
		// Retrieve a list of stat IDs with given NPC ID. 
		// keySet contains all of the stat IDs. Add the statID
		// to a list of integers if its value is the NPCID. 
		ArrayList<Integer> statIDs = new ArrayList<Integer>(); 
		for(Integer i : npcToStats.keySet()) {
			if(npcToStats.get(i) == npcID) {
				statIDs.add(i); 
			}
		}
		
		// Using list of Stat IDs, we want to create a 
		// HashMap that takes the stat name and maps it 
		// to the Stat class. 
		HashMap<String, Stat> stats = new HashMap<String, Stat>(); 
		for(Integer i : statIDs) {
			Stat stat = npcStatsList.get(i); 
			String statName = stat.getName(); 
			stats.put(statName, stat); 
		}
		
		return stats; 
	}
	
	public Item findItemByItemID(int itemID) {
		return itemList.get(itemID); 
	}
	
	
	
	
	
	
	
	// query that retrieves Book and its Author by Title
	@Override
	public List<Pair<Author, Book>> findAuthorAndBookByTitle(String title) {
		List<Pair<Author, Book>> result = new ArrayList<Pair<Author,Book>>();
		for (Book book : bookList) {
//			System.out.println("Book: <" + book.getTitle() + ">" + "  Title: <" + title + ">");
			
			if (book.getTitle().equals(title)) {
				Author author = findAuthorByAuthorId(book.getAuthorId());
				result.add(new Pair<Author, Book>(author, book));
			}
		}
		return result;
	}
	
	// query that retrieves all Books, for the Author's last name
	@Override
	public List<Pair<Author, Book>> findAuthorAndBookByAuthorLastName(String lastName)
	{
		// create list of <Author, Book> for returning result of query
		List<Pair<Author, Book>> result = new ArrayList<Pair<Author, Book>>();
		
		// search through table of Books
		for (Book book : bookList) {
			for (Author author : authorList) {
				if (book.getAuthorId() == author.getAuthorId()) {
					if (author.getLastname().equals(lastName)) {
						// if this book is by the specified author, add it to the result list
						result.add(new Pair<Author, Book>(author, book));						
					}
				}
			}
		}
		return result;
	}

	
	// query that retrieves all Books, with their Authors, from DB
	@Override
	public List<Pair<Author, Book>> findAllBooksWithAuthors() {
		List<Pair<Author, Book>> result = new ArrayList<Pair<Author,Book>>();
		for (Book book : bookList) {
			Author author = findAuthorByAuthorId(book.getAuthorId());
			result.add(new Pair<Author, Book>(author, book));
		}
		return result;
	}
		

	// query that retrieves all Authors from DB
	@Override
	public List<Author> findAllAuthors() {
		List<Author> result = new ArrayList<Author>();
		for (Author author : authorList) {
			result.add(author);
		}
		return result;
	}
	
	
	// query that inserts a new Book, and possibly new Author, into Books and Authors lists
	// insertion requires that we maintain Book and Author id's
	// this can be a real PITA, if we intend to use the IDs to directly access the ArrayLists, since
	// deleting a Book/Author in the list would mean updating the ID's, since other list entries are likely to move to fill the space.
	// or we could mark Book/Author entries as deleted, and leave them open for reuse, but we could not delete an Author
	//    unless they have no Books in the Books table
	@Override
	public Integer insertBookIntoBooksTable(String title, String isbn, int published, String lastName, String firstName)
	{
		int authorId = -1;
		int bookId   = -1;
		
		// search Authors list for the Author, by first and last name, get author_id
		for (Author author : authorList) {
			if (author.getLastname().equals(lastName) && author.getFirstname().equals(firstName)) {
				authorId = author.getAuthorId();
			}
		}
		
		// if the Author wasn't found in Authors list, we have to add new Author to Authors list
		if (authorId < 0) {
			// set author_id to size of Authors list + 1 (before adding Author)
			authorId = authorList.size() + 1;
			
			// add new Author to Authors list
			Author newAuthor = new Author();			
			newAuthor.setAuthorId(authorId);
			newAuthor.setLastname(lastName);
			newAuthor.setFirstname(firstName);
			authorList.add(newAuthor);
			
			System.out.println("New author (ID: " + authorId + ") " + "added to Authors table: <" + lastName + ", " + firstName + ">");
		}

		// set book_id to size of Books list + 1 (before adding Book)
		bookId = bookList.size() + 1;

		// add new Book to Books list
		Book newBook = new Book();
		newBook.setBookId(bookId);
		newBook.setAuthorId(authorId);
		newBook.setTitle(title);
		newBook.setIsbn(isbn);
		newBook.setPublished(published);
		bookList.add(newBook);
		
		// return new Book Id
		return bookId;
	}
	
	//not implemented in FakeDB
	public List<Author> removeBookByTitle(final String title) {
		List<Author> authors = new ArrayList<Author>();
		
		return authors;
	}
	

	// query that retrieves an Author based on author_id
	private Author findAuthorByAuthorId(int authorId) {
		for (Author author : authorList) {
			if (author.getAuthorId() == authorId) {
				return author;
			}
		}
		return null;
	}
}
