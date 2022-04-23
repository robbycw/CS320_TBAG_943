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
	
	// Remember that because lists count from 0, the
	// ID X object is stored at index X-1. 
	private List<User> userList;
	private List<Pair<Integer, Integer>> userToGame; 
	private List<Game> gameList;  
	private List<ArrayList<String>> gameLogList; 
	private List<Player> playerList; 
	private List<Map> mapList; 
	private List<Pair<Integer, Integer>> playerToStats;
	private List<Pair<Integer, Integer>> playerInventory; 
	private List<Stat> playerStatsList; 
	private List<Item> itemList; 
	private List<Loot> lootList;
	private List<Location> locationList; 
	private List<Pair<Integer, Integer>> locationToNPC; 
	private List<WinCondition> winConditionList;
	private List<NPC> npcList; 
	private List<Pair<Integer, Integer>> npcToStats; 
	private List<Stat> npcStatsList; 
	private List<Speech> speechList; 
	private HashMap<Integer, ArrayList<String>> speechOptions;
	private HashMap<Integer, ArrayList<String>> speechResponses; 
	private List<Pair<Integer, Integer>> locationToCombat; 
	private List<Pair<Integer, Integer>> combatToNPC; 
	private List<Pair<Integer, Integer>> locationToPuzzle; 
	private List<Combat> combatList; 
	private List<Puzzle> puzzleList; 
	
	// Constructor
	public FakeDatabase() {
		this.userList = new ArrayList<User>();
		this.userToGame = new ArrayList<Pair<Integer, Integer>>();
		this.gameList = new ArrayList<Game>();
		this.gameLogList = new ArrayList<ArrayList<String>>();
		this.playerList = new ArrayList<Player>();
		this.mapList = new ArrayList<Map>();
		this.playerToStats = new ArrayList<Pair<Integer, Integer>>();
		this.playerInventory = new ArrayList<Pair<Integer, Integer>>();
		this.playerStatsList = new ArrayList<Stat>();
		this.itemList = new ArrayList<Item>();
		this.lootList = new ArrayList<Loot>();
		this.locationList = new ArrayList<Location>();
		this.locationToNPC = new ArrayList<Pair<Integer, Integer>>();
		this.winConditionList = new ArrayList<WinCondition>();
		this.npcList = new ArrayList<NPC>();
		this.npcToStats = new ArrayList<Pair<Integer, Integer>>();
		this.npcStatsList = new ArrayList<Stat>();
		this.speechList = new ArrayList<Speech>();
		this.speechOptions = new HashMap<Integer, ArrayList<String>>();
		this.speechResponses = new HashMap<Integer, ArrayList<String>>();
		this.locationToCombat = new ArrayList<Pair<Integer, Integer>>();
		this.combatToNPC = new ArrayList<Pair<Integer, Integer>>();
		this.locationToPuzzle = new ArrayList<Pair<Integer, Integer>>();
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
			this.userList.addAll(InitialData.getUser());
			this.userToGame.addAll(InitialData.getUserToGame());
			 
			this.gameLogList.addAll(InitialData.getGameLog());
			
			this.playerToStats.addAll(InitialData.getPlayerToStats());
			this.playerInventory.addAll(InitialData.getPlayerInventory());
			this.playerStatsList.addAll(InitialData.getPlayerStats()); 
			this.itemList.addAll(InitialData.getItem());
			this.lootList.addAll(InitialData.getLoot(itemList));
			
			this.locationToNPC.addAll(InitialData.getLocationToNPC());
			this.winConditionList.addAll(InitialData.getWinCondition());
			
			this.npcToStats.addAll(InitialData.getNPCToStats());
			this.npcStatsList.addAll(InitialData.getNPCStats());
			
			this.speechOptions = InitialData.getSpeechOptions(); 
			this.speechResponses = InitialData.getSpeechResponses(); 
			this.speechList.addAll(InitialData.getSpeech(speechOptions, speechResponses)); 
			
			this.locationToCombat.addAll(InitialData.getLocationToCombat());
			this.combatToNPC.addAll(InitialData.getCombatToNPC());
			this.locationToPuzzle.addAll(InitialData.getLocationToPuzzle()); 
			this.puzzleList.addAll(InitialData.getPuzzle(playerStatsList, itemList));
			
			this.npcList.addAll(InitialData.getNPC(itemList, speechList, npcToStats, npcStatsList));
			this.combatList.addAll(InitialData.getCombat(npcList, combatToNPC));
			this.locationList.addAll(InitialData.getLocation(lootList, winConditionList, 
					locationToNPC, npcList, locationToCombat, combatList, locationToPuzzle, puzzleList));
			this.mapList.addAll(InitialData.getMap(locationList));
			this.playerList.addAll(InitialData.getPlayer(playerStatsList, playerToStats, 
					itemList, playerInventory, locationList));
			this.gameList.addAll(InitialData.getGame(playerList,gameLogList, mapList, combatList));
		} catch (IOException e) {
			throw new IllegalStateException("Couldn't read initial data", e);
		}
	}
	public User findUserByUsernameAndPassword(String username, String password) {
		// traverse the list of users and return the one that has the same 
		// username and password
		for(User i: userList) {
			if(i.getUsername() == username && i.getPassword() == password) {
				return i;
			}
		}
		System.out.print("your account isn't present would you like to register?");
	}
	public List<Game> findGamesByUserID(int userID){
		// traversing through the game list to compare if the
		// user from the userID owns the particular games
		ArrayList<Game> userGames = new ArrayList<Game>();
		
		for(Game i: gameList) {
			if(i.getUser() == userList.get(userID - 1)) {
				userGames.add(i);
			}
		}
		return userGames;		
	}
	public List<NPC> findNPCsByLocationID(int locationID){
		// returns the list of NPCs at the location ID
		return (List<NPC>) locationList.get(locationID - 1).getNPCs().values();
	
	} 
	public List<NPC> findNPCsByCombatID(int combatID){
		// returns the list of NPCs at the combat ID
		return (List<NPC>) combatList.get(combatID - 1).getNpcs().values();
	}
	
	
	// Finds
	public Loot findLootByLocationID(int locationID) {
		// Get the location from Location Table based on given id. 
		Location loc = locationList.get(locationID - 1); 
		
		// Retrieve lootID from the Location
		int lootID = loc.getTreasure().getId();
		
		// Return the loot. 
		return lootList.get(lootID - 1); 
	}
	
	public HashMap<String, Item> findInventoryByPlayerID(int playerID) {
		// Retrieve a list of item IDs with given player ID. 
		// The getLeft of each pair contains the playerID.
		// Add the item ID to the list if the playerIDs match.
		ArrayList<Integer> itemIDs = new ArrayList<Integer>(); 
		for(Pair<Integer, Integer> i : playerInventory) {
			// Check if item has player's ID (is in their inventory)
			if(i.getLeft() == playerID) {
				// If so, add the item ID to the list of IDs. 
				itemIDs.add(i.getRight()); 
			}
		}
		
		// This list of item IDs will be used to retrieve the items
		// from itemList. 
		HashMap<String, Item> inventory = new HashMap<String, Item>(); 
		for(Integer i : itemIDs) {
			Item item = itemList.get(i - 1); 
			String name = item.getName(); 
			
			inventory.put(name.toLowerCase(), item); 
		}
		
		return inventory; 
	}
	
	public HashMap<String, Stat> findPlayerStatsByPlayerID(int playerID){
		// Retrieve a list of stat IDs with given player ID. 
		// The getLeft of each pair contains the playerID.
		// Add the Stat ID to the list if the playerIDs match.
		ArrayList<Integer> statIDs = new ArrayList<Integer>(); 
		for(Pair<Integer, Integer> i : playerToStats) {
			if(i.getLeft() == playerID) {
				statIDs.add(i.getRight()); 
			}
		}
		
		// Using list of Stat IDs, we want to create a 
		// HashMap that takes the stat name and maps it 
		// to the Stat class. 
		HashMap<String, Stat> stats = new HashMap<String, Stat>(); 
		for(Integer i : statIDs) {
			Stat stat = playerStatsList.get(i - 1); 
			String statName = stat.getName(); 
			stats.put(statName, stat); 
		}
		
		return stats; 
	}
	
	public HashMap<String, Stat> findNPCStatsByNPCID(int npcID){
		// Retrieve a list of stat IDs with given NPC ID. 
		// The getLeft of each pair contains the npcID.
		// Add the item ID to the list if the npcIDs match.
		ArrayList<Integer> statIDs = new ArrayList<Integer>(); 
		for(Pair<Integer, Integer> i : npcToStats) {
			if(i.getLeft() == npcID) {
				statIDs.add(i.getRight()); 
			}
		}
		
		// Using list of Stat IDs, we want to create a 
		// HashMap that takes the stat name and maps it 
		// to the Stat class. 
		HashMap<String, Stat> stats = new HashMap<String, Stat>(); 
		for(Integer i : statIDs) {
			Stat stat = npcStatsList.get(i - 1); 
			String statName = stat.getName(); 
			stats.put(statName, stat); 
		}
		
		return stats; 
	}
	
	public Item findItemByItemID(int itemID) {
		// An item of ID X will be stored at index X-1
		// This is due to lists counting from 0 
		return itemList.get(itemID - 1); 
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
