import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.ArrayList;

public class server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		ServerSocket listener;
		int clientid = 0;
		try {
			listener = new ServerSocket(10000, 10);

			while (true) {
				System.out.println("Main thread listening for incoming new connections");
				Socket newconnection = listener.accept();

				System.out.println("New connection received and spanning a thread");
				Connecthandler t = new Connecthandler(newconnection, clientid);
				clientid++;
				t.start();
			}

		}

		catch (IOException e) {
			System.out.println("Socket not opened");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

class Connecthandler extends Thread {

	Socket individualconnection;
	int socketid;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;

	int agentChoice;
	int idCheck;
	ArrayList<Player> players = new ArrayList<>();

	public Connecthandler(Socket s, int i) {
		individualconnection = s;
		socketid = i;
	}

	void sendMessage(String msg) {
		try {
			out.writeObject(msg);
			out.flush();
			System.out.println("client>" + msg);
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
	}

	public void run() {

		try {

			out = new ObjectOutputStream(individualconnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(individualconnection.getInputStream());
			System.out.println("Connection" + socketid + " from IP address " + individualconnection.getInetAddress());

			// Commence
			do {
				sendMessage("\nPlease Enter 1 if you are an Agent" + "\nEnter 2 if you are a Club\n");
				message = (String) in.readObject();

				if (message.equals("1")) {

					do {
						sendMessage("\nPress 1 to add a player" + "\nPress 2 to update a player's valuation"
								+ "\nPress 3 to update a players status"
								+ "\nPress 4 to exit\n");
						message = (String) in.readObject();
						agentChoice = Integer.parseInt(message);

						// Add a player
						if (agentChoice == 1) {
							Player player = new Player();

							sendMessage("\nPlease enter the player name: ");
							player.setName((String) in.readObject());

							sendMessage("\nPlease enter the player age: ");
							player.setAge(Integer.parseInt((String) in.readObject()));

							sendMessage("\nPlease enter the player id: ");
							player.setPlayerId(Integer.parseInt((String) in.readObject()));

							sendMessage("\nPlease enter the club id: ");
							player.setClubId(Integer.parseInt((String) in.readObject()));

							sendMessage("\nPlease enter the agent id: ");
							player.setAgentId(Integer.parseInt((String) in.readObject()));

							sendMessage("\nPlease enter the player valuation: ");
							player.setValue(Double.parseDouble((String) in.readObject()));

							sendMessage("\nPlease enter the player status: ");
							player.setStatus((String) in.readObject());

							sendMessage("\nPlease enter the player position: ");
							player.setPosition((String) in.readObject());
							
							// add the player to the list
							players.add(player);
							for (Player p : players) {
								sendMessage("\nName: " + p.getName() + "\nAge: " + p.getAge() + "\nPlayer Id: " + p.getPlayerId()
								+ "\nClub Id: " + p.getClubId() + "\nAgent Id: " + p.getAgentId() + "\nValuation: " + p.getValue()
								+ "\nStatus: " + p.getStatus() + "\nPosition: " + p.getPosition());
							}

						} else if (agentChoice == 2) {
							sendMessage("Please enter the Id of the player you would like to update: ");
							message = (String) in.readObject();
							idCheck = Integer.parseInt(message);

							for (Player p : players) {
								if (idCheck == p.getPlayerId()) {
									sendMessage("true");
									sendMessage("\nPlease enter the new player valuation: ");
									p.setValue(Double.parseDouble((String) in.readObject()));
									sendMessage("\nValue Changed To: " + p.getValue());

								} else {
									sendMessage("The Id does not exist!\n");
								}
							}

						} else if (agentChoice == 3) {
							sendMessage("Please enter the Id of the player you would like to update: ");
							message = (String) in.readObject();
							idCheck = Integer.parseInt(message);

							for (Player p : players) {
								if (idCheck == p.getPlayerId()) {
									sendMessage("true");
									sendMessage("\nPlease enter the new player Status: ");
									p.setStatus((String) in.readObject());
									sendMessage("\nStatus Changed To: " + p.getStatus());

								} else {
									sendMessage("The Id does not exist!\n");
								}
							}

						} else {
							sendMessage("\nSelect one of the above options!!!: ");
						}

					} while (!message.equals("4"));
				} else {
					sendMessage("\nSelect one of the above options!!!: ");
				}

			} while (!message.equals("3"));

			// } else if (message.equals("2")) {
			//
			// }

			// sendMessage("The app is now finished\n");

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		finally {
			try {
				out.close();
				in.close();
				individualconnection.close();
			}

			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

}
