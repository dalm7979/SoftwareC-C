package model;

import java.time.LocalDate;
import java.util.ArrayList;

public class Company{

    private String name;

    private String id;

    private int totalCounts;

    private ArrayList<Client> clients=new ArrayList<>();


	public Company(String name, String id, int totalCounts) {
		this.name = name;
		this.id = id;
        this.totalCounts = totalCounts;
		clients.add(addClient());
	}


	private Client addClient() {
		String fullName = "Jhon Osorio";
		String id = "1143848958";
		String address = "Calle 68 # 48 - 89";
		String phoneNumber = "1143848958";
		LocalDate birthDate = LocalDate.parse("1993-07-09");
		long borrowingCapacity = 1000000;
		Client client = new Client(fullName, id, address, phoneNumber, birthDate, borrowingCapacity);

		LocalDate startCredit = LocalDate.now();
		LocalDate endCredit = LocalDate.parse("2020-05-17");
		long requestedValue = 500000;

		Credit credit = new Credit(startCredit, endCredit, requestedValue);
		client.addCredit(credit);

		Bill bill = new Bill(client, credit, "Nintendo Switch");
		client.addBill(bill);
		return  client;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getTotalCounts() {
		return totalCounts;
	}

	public void setTotalCounts(int totalCounts) {
		this.totalCounts = totalCounts;
	}

	public ArrayList<Client> getClients() {
		return clients;
	}

	public void setClients(ArrayList<Client> clients) {
		this.clients = clients;
	}


	public void addClient(Client client) {
		clients.add(client);
	}






}