import java.util.ArrayList;
import java.util.Scanner;

public class App {

	private final Stations stationList;
	private Station selectedStation;
	private final Scanner input;

	public App() {
		stationList = Stations.getStationsInstance();
		input = new Scanner(System.in);
	}

	public void start() {

		// Station input
		ArrayList<Station> stations = stationInput();

		// Station select
		selectedStation = stationSelect(stations);

		// Station activities
		stationActivities();
	}

	public ArrayList<Station> stationInput() {

		ArrayList<Station> stations;
		String response;

		do {
			System.out.print("Which station would you like to modify (please enter station ID or name): ");
			response = input.nextLine();
			stations = stationList.findStationByID(response);
			if (stations.size() == 0) {
				stations = stationList.findStationByName(response);
			}

			if (stations.size() == 0) {
				System.out.println("No station found with input: " + response);
			}
		} while (stations.size() == 0);

		return stations;
	}

	public Station stationSelect(ArrayList<Station> stations) {

		Station currentStation = null;
		int i = 1;

		while (currentStation == null) {
			System.out.println("Which station is it:");
			for (Station station : stations) {
				System.out.println(i + ": " + station.getName() + " " + station.getId() + " (" + station.getStopType() + ")");
				i++;
			}

			int index = input.nextInt() - 1;
			if (index < 0 || index >= stations.size()) {
				System.out.println("Invalid input");
			} else {
				currentStation = stations.get(index);
			}
		}
		return currentStation;
	}

	public void stationActivities() {

		boolean done = false;

		while (!done) {

			System.out.println("What would you like to do with " + selectedStation.getName() + "?");
			System.out.println("(0) Update Name and/or ID, (1) Update Stopping Information, (I) Show Station Info, (E) Edit Different Station or (Q) Quit:");
			String response = input.nextLine().toUpperCase();

			switch (response) {
				case "0" -> updateNameAndID();
				case "1" -> updateStoppingInfo();
				case "I" -> System.out.println(selectedStation.stationInfo());
				case "E" -> done = true;
				case "Q" -> shutDown();
				default -> System.out.println("Not a valid input please try again");
			}

		}
		start();
	}

	private void updateNameAndID() {

		System.out.print("What would you like to update (N = name, I = ID or B = Both): ");
		String response = input.nextLine().toUpperCase();

		while (!(response.equals("N") || response.equals("I") || response.equals("B"))) {
			System.out.println("Invalid input");
			System.out.print("What would you like to update (N = name, I = ID or B = Both): ");
			response = input.nextLine().toUpperCase();
		}

		String name = "";
		String ID = "";

		if (response.equals("N") || response.equals("B")) {
			String confirmed;
			do {
				System.out.print("What would you like to change " + selectedStation.getName() + "'s name to be: ");
				name = input.nextLine();

				System.out.print("Are you sure you wanna change " + selectedStation.getName() + "'s name to be " + name + " (Y = yes or N = No) ");
				confirmed = input.nextLine().toUpperCase();
			} while (confirmed.equals("N"));
		}

		if (response.equals("I") || response.equals("B")) {
			String confirmed;
			do {
				System.out.print("What would you like to change " + selectedStation.getName() + "'s ID to be: ");
				ID = input.nextLine();

				System.out.print("Are you sure you wanna change " + selectedStation.getName() + "'s ID to be " + ID + " (Y = yes or N = No) ");
				confirmed = input.nextLine().toUpperCase();
			} while (confirmed.equals("N"));
		}

		stationList.updateNameAndID(selectedStation, ID, name);
	}

	private void updateStoppingInfo() {

		System.out.print("What would you like to update (1 = Stopped At, 2 = Update Passed Through (Stopping), 3 = Update Passed Through (Not Stopping) 4 = Back): ");
		String response = input.nextLine().toUpperCase();

		while (!(response.equals("1") || response.equals("2") || response.equals("3") || response.equals("4"))) {
			System.out.println("Invalid input");
			System.out.print("What would you like to update (1 = Stopped At, 2 = Update Passed Through (Stopping), 3 = Update Passed Through (Not Stopping) 4 = Back): ");
			response = input.nextLine().toUpperCase();
		}

		String date;
		String confirmed;

		if (response.equals("1")) {

			do {
				System.out.print("What would you like to change " + selectedStation.getName() + "'s Stopped At date to be: ");
				date = input.nextLine();

				System.out.print("Are you sure you wanna change " + selectedStation.getName() + "'s Stopped At date to be " + date + " (Y = yes or N = No): ");
				confirmed = input.nextLine().toUpperCase();
			} while (confirmed.equals("N"));
			stationList.updateStoppedAt(selectedStation, date);
		}

		if (response.equals("2")) {

			do {
				System.out.print("What would you like to change " + selectedStation.getName() + "'s Passed Through (Stopping) date to be: ");
				date = input.nextLine();

				System.out.print("Are you sure you wanna change " + selectedStation.getName() + "'s Passed Through (Stopping) date to be " + date + " (Y = yes or N = No): ");
				confirmed = input.nextLine().toUpperCase();
			} while (confirmed.equals("N"));
			stationList.updatePassedThroughStopping(selectedStation, date);
		}

		if (response.equals("3")) {

			do {
				System.out.print("What would you like to change " + selectedStation.getName() + "'s Passed Through (Not Stopping) date to be: ");
				date = input.nextLine();

				System.out.print("Are you sure you wanna change " + selectedStation.getName() + "'s Passed Through (Not Stopping) date to be " + date + " (Y = yes or N = No): ");
				confirmed = input.nextLine().toUpperCase();
			} while (confirmed.equals("N"));
			stationList.updatePassedThrough(selectedStation, date);
		}
	}

	private void shutDown() {
		stationList.saveFile();
		System.exit(0);
	}

}
