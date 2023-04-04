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

		boolean quit = false;

		while (!quit) {
			System.out.println("What would you like to do: ");
			System.out.print("(1) Edit/Select A Station, (2) Show Stats, (3) Show All Station Info or (Q) Quit: ");
			String response = input.next().toUpperCase();

			switch (response) {
				case "1" -> stationSelected();
				case "2" -> System.out.println("No ones here :)");
				case "3" -> System.out.println("No ones here :)");
				case "Q" -> quit = true;
				default -> System.out.println("Not a valid input please try again");
			}
		}
		shutDown();
	}

	private void stationSelected() {

		boolean quit = false;

		while (!quit) {
			// Station input
			ArrayList<Station> stations = stationInput();

			// Station select
			selectedStation = stationSelect(stations);

			// Station activities
			quit = stationActivities() ;
		}
	}

	private ArrayList<Station> stationInput() {

		ArrayList<Station> stations;
		String response;

		do {
			System.out.print("Which station would you like to modify (please enter station ID or name): ");
			response = input.next();
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

	private Station stationSelect(ArrayList<Station> stations) {

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

	private boolean stationActivities() {

		boolean done = false;
		String response = "";

		while (!done) {

			System.out.println("What would you like to do with " + selectedStation.getName() + "?");
			System.out.println("(0) Update Name and/or ID, (1) Update Stopping Information, (I) Show Station Info, (E) Edit Different Station or (Q) Quit:");
			response = input.next().toUpperCase();

			switch (response) {
				case "0" -> updateNameAndID();
				case "1" -> updateStoppingInfo();
				case "I" -> System.out.println(selectedStation.stationInfo());
				case "E", "Q" -> done = true;
				default -> System.out.println("Not a valid input please try again");
			}
		}
		return !response.equals("E");
	}

	private void updateNameAndID() {

		System.out.print("What would you like to update (N = name, I = ID or B = Both, Q = Back): ");
		String response = input.nextLine().toUpperCase();

		while (!(response.equals("N") || response.equals("I") || response.equals("B") || response.equals("Q"))) {
			System.out.println("Invalid input");
			System.out.print("What would you like to update (N = name, I = ID or B = Both, Q = Back): ");
			response = input.nextLine().toUpperCase();
		}

		if (response.equals("Q")) {
			return;
		}

		String name = "";
		String ID = "";

		if (response.equals("N") || response.equals("B")) {
			String confirmed;
			do {
				System.out.print("What would you like to change " + selectedStation.getName() + "'s name to be: ");
				name = input.nextLine();

				System.out.print("Are you sure you wanna change " + selectedStation.getName() + "'s name to be " + name + " (Y = yes or N = No, B = Back) ");
				confirmed = input.nextLine().toUpperCase();

				if (confirmed.equals("B")) {
					System.out.print("Are you sure you wanna leave without changing anything (Y = yes or N = No, B = Back) ");
					confirmed = input.nextLine().toUpperCase();
					if (confirmed.equals("Y")) {
						return;
					}
				}
			} while (confirmed.equals("N"));
		}

		if (response.equals("I") || response.equals("B")) {
			String confirmed;
			do {
				System.out.print("What would you like to change " + selectedStation.getName() + "'s ID to be: ");
				ID = input.nextLine();

				System.out.print("Are you sure you wanna change " + selectedStation.getName() + "'s ID to be " + ID + " (Y = yes or N = No, B = Back)");
				confirmed = input.nextLine().toUpperCase();

				if (confirmed.equals("B")) {
					System.out.print("Are you sure you wanna leave without changing anything (Y = yes or N = No, B = Back) ");
					confirmed = input.nextLine().toUpperCase();
					if (confirmed.equals("Y")) {
						return;
					}
				}
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
