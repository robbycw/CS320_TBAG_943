package edu.ycp.cs320.tbag_943.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.ycp.cs320.tbag_943.classes.*;
import edu.ycp.cs320.tbag_943.controller.GameController;

public class GameServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(false); 
		
		if(session == null || session.getAttribute("model") == null) {
			// need to make a new session. 
			session = req.getSession(); 
			// will also need to make a Game object for this new session! 
			// Game created in static method below. 
			Game game = GameServlet.gameMaker();  
//			game.addOutput("You Enter Tired");
//			game.addOutput("The clock reads 9:43...");
//			game.addOutput("Many murders have occured here...");
//			game.addOutput("You have two hours to investigate those murders...");
//			game.addOutput("Time Starts Now!");
			
			// Game data will be stored in the session, allowing data to be exchanged back and
			// forth in Servlet and JSP.
			session.setAttribute("model", game);
			
			// We will also set attributes for the Map colors and names of rooms. 
			Location current = game.getPlayer().getLocation(); 
			session.setAttribute("northc", game.getMap().getDirectionColor(current, 0));
			session.setAttribute("northr", game.getMap().getDirectionName(current, 0));
			session.setAttribute("eastc", game.getMap().getDirectionColor(current, 1));
			session.setAttribute("eastr", game.getMap().getDirectionName(current, 1));
			session.setAttribute("southc", game.getMap().getDirectionColor(current, 2));
			session.setAttribute("southr", game.getMap().getDirectionName(current, 2));
			session.setAttribute("westc", game.getMap().getDirectionColor(current, 3));
			session.setAttribute("westr", game.getMap().getDirectionName(current, 3));
			session.setAttribute("currentr", game.getPlayer().getLocation().getName());
			
			// Location Description
				Location loc = new Location();
				String desc = loc.getDescription();
				req.setAttribute("description", desc);
		} 
		
		
		System.out.println("GameServlet: doGet");
		
		
		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		HttpSession session = req.getSession(); 
		System.out.println("GameServlet: doPost");
		
		
		
		if(req.getParameter("title") != null) {
			
			System.out.println("GameServlet: titlePage");
			
			resp.sendRedirect("/tbag_943/titlePage");
			
		} else if(req.getParameter("credits") != null) {
			
			System.out.println("GameServlet: credits");
			
			resp.sendRedirect("/tbag_943/credits");
			
		} else if(req.getParameter("combat") != null) {
			
			System.out.println("GameServlet: combat");
			
			resp.sendRedirect("/tbag_943/combat");
			
		}
		// Once we have a Game model that persists each request, we can have it so the Servlet will
		// store Strings from the user input into the outputLog in Game. 
		if(req.getParameter("user") != null) {
			String in = req.getParameter("user"); 
			Game model = (Game) session.getAttribute("model"); 
			GameController controller = new GameController(model); 
			
			model.addOutput(in); // The user's command is added to the output 
			
			// We will want to ensure the input is all lower case, and then split the
			// command into an array of strings, as this is necessary to interpret 
			// multiple-word commands (ex: move north). 
			
			in = in.toLowerCase(); 
			String[] input = in.split(" +"); 
			
			// The following switch-case will interpret the user's command and call the
			// appropriate controller functions. 
			try {
				switch(input[0]) {
					case "move":
						System.out.println(input[1]); 
						controller.move(input[1]);
						break; 
					case "help":
						controller.help(); 
						break; 
					case "attack":
						controller.attack(input[1]);
						break; 
					case "talk":
						controller.talk(input[1]);
						break; 
					case "collect":
						controller.collect(input[1]);
						break; 
					case "puzzle":
						controller.puzzle();
						break;
					default: 
						model.addOutput("Unknown command.");
				}
			} catch (ArrayIndexOutOfBoundsException e) {
				// In the event the user passes a command without a target, 
				// this catch will handle the exception. 
				// Ex: User inputs "attack" or "move "
				
				model.addOutput("Incorrect command syntax: Please specify a target.");
			}
			// Update Map
			Location current = model.getPlayer().getLocation(); 
			session.setAttribute("northc", model.getMap().getDirectionColor(current, 0));
			session.setAttribute("northr", model.getMap().getDirectionName(current, 0));
			session.setAttribute("eastc", model.getMap().getDirectionColor(current, 1));
			session.setAttribute("eastr", model.getMap().getDirectionName(current, 1));
			session.setAttribute("southc", model.getMap().getDirectionColor(current, 2));
			session.setAttribute("southr", model.getMap().getDirectionName(current, 2));
			session.setAttribute("westc", model.getMap().getDirectionColor(current, 3));
			session.setAttribute("westr", model.getMap().getDirectionName(current, 3));
			session.setAttribute("currentr", model.getPlayer().getLocation().getName());
			
			// Put the updated model back in the HttpSession.
			session.setAttribute("model", model);
		}
		
		req.getRequestDispatcher("/_view/game.jsp").forward(req, resp);
	}
	
	// A static function for generating the demo Game upon loading the session. 
	public static Game gameMaker() {
		
		// Create Items
		Item sword1 = new Item("Sword", 5); 
		Item sword2 = new Item("Sword", 5);
		Item sword3 = new Item("Sword", 5);
		Item axe = new Item("Axe", 6);
		Item gold = new Item ("Gold", 0, 200); 
		
		// Create NPCs (add constructor to NPC for automatically making stats)
		NPC dave = new NPC("Dave", true, sword1, 20, 6, 3, 3); 
		NPC steve = new NPC("Steve", true, sword2, 20, 8, 5, 2);
		NPC earl = new NPC("Earl", true, sword3, 20, 10, 1, 6);
		
		ArrayList<NPC> npcs = new ArrayList<NPC>(); 
		npcs.add(dave); 
		npcs.add(steve);
		npcs.add(earl);
		
		// Create Combats: remember that Map keys need to be all lowercase
		Combat c1 = new Combat(npcs); 
		ArrayList<Combat> combats = new ArrayList<Combat>();
		combats.add(c1); 
		
		// Create Loots
		HashMap<String, Item> items = new HashMap<String, Item>(); 
		items.put("gold", gold); 
		items.put("axe", axe); 
		
		Loot loot = new Loot(items);  
		
		// Sets up for adding connections and rooms to the map. 
		String room1, room2, room3, room4; 
		HashMap<String, ArrayList<String>> connections = new HashMap<String, ArrayList<String>>();
		ArrayList<String> room1Con, room2Con, room3Con, room4Con; 
		room1 = "Room1"; 
		room2 = "Room2"; 
		room3 = "Room3";
		room4 = "Room4"; 
		
		room1Con = new ArrayList<String>(); 
		room1Con.add(room2); // north
		room1Con.add(room3); // east
		room1Con.add("-1"); // south
		room1Con.add("-1"); // west
		
		room2Con = new ArrayList<String>(); 
		room2Con.add("-1"); // north
		room2Con.add(room4); // east
		room2Con.add(room1); // south
		room2Con.add("-1"); // west
		
		room3Con = new ArrayList<String>(); 
		room3Con.add(room4); // north
		room3Con.add("-1"); // east
		room3Con.add("-1"); // south
		room3Con.add(room1); // west
		
		room4Con = new ArrayList<String>(); 
		room4Con.add("-1"); // north
		room4Con.add("-1"); // east
		room4Con.add(room3); // south
		room4Con.add(room2); // west
		
		connections.put(room1, room1Con); 
		connections.put(room2, room2Con); 
		connections.put(room3, room3Con); 
		connections.put(room4, room4Con); 
		
		HashMap<String, Location> locations = new HashMap<String, Location>(); 
		Location r1, r2, r3, r4; 
		r1 = new Location(room1); 
		r2 = new Location(room2); 
		r3 = new Location(room3); 
		r4 = new Location(room4); 
		locations.put(room1, r1); 
		locations.put(room2, r2); 
		locations.put(room3,r3); 
		locations.put(room4,r4);
		r1.setDescription("You find yourself in a lobby");
		r2.setDescription("You find yourself in a dark room");
		r3.setDescription("Investigate");
		r4.setDescription("A dark figure is standing in front of you");
		
		// Set NPCs, Loot and Combat in rooms. 
		r1.setTreasure(loot);
		r2.setCombats(combats);
		r2.setNPCs(npcs);
		
		// Create map
		Map map = new Map(locations, connections); 
		
		// Create Player
		Player player = new Player("Jorady", r3, 500, 10, 4, 10); 
		
		// Create Puzzle
		String samplePuzzlePrompt = "I am down when the sun rises but up when the moon shines bright. What am I?";
		Puzzle samplePuzzle = new Puzzle(samplePuzzlePrompt, "A minecraft piston attached to a daylight sensor and a 'not' gate!");
		HashMap<String,Item> puzzleReward = new HashMap<>();
		puzzleReward.put("first",new Item("Spaghetti of Destiny",0));
		samplePuzzle.setReward(new Loot(puzzleReward));
		r1.addPuzzle(samplePuzzle);
		r2.addPuzzle(samplePuzzle);
		r3.addPuzzle(samplePuzzle);
		r4.addPuzzle(samplePuzzle);
		
		// Create Game with proper parameters
		Game game = new Game(1, map, player); 
		
		return game; 
	}

	private Integer getDoubleFromParameter(String s) {
		if (s == null || s.equals("")) {
			return null;
		} else {
			return Integer.parseInt(s);
		}

	}


}
