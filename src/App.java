import java.util.ArrayList;
import java.util.InputMismatchException;
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
			System.out.print("(1) Edit/Select A Station, (2) Show Stats, (3) Show All Station Info, (4) Add New Station or (Q) Quit: ");
			String response = input.nextLine().toUpperCase();

			switch (response) {
				case "1" -> stationSelected();
				case "2" -> statShower();
				case "3" -> allTheStationsDisplay();
				case "4" -> addStation();
				case "Q" -> quit = true;
				default -> System.out.println("Not a valid input please try again");
			}
		}
		shutDown();
	}

	private void addStation() {

		// Determines the amount of data to be gathered
		System.out.print("What station Info do you have: \n(A) All (Basic & Dates), (S) Some (Basic & No Dates) or (B) Back: ");
		String dataType = input.nextLine().toUpperCase();

		while (!(dataType.equals("A") || dataType.equals("S") || dataType.equals("B"))) {
			System.out.println("Invalid input");
			System.out.print("What station Info do you have: \n(A) All (Basic & Dates), (S) Some (Basic & No Dates) or (B) Back: ");
			dataType = input.nextLine().toUpperCase();
		}

		String stationType = "";

		// Sets the station Type
		if (!(dataType.equals("B"))) {
			System.out.print("What type of station is it: \n(1) Train, (2) Tram, (3) Underground or (B) Back): ");
			stationType = input.nextLine().toUpperCase();

			while (!(stationType.equals("1") || stationType.equals("2") || stationType.equals("3") || stationType.equals("B"))) {
				System.out.println("Invalid input");
				System.out.print("What type of station is it: \n(1) Train, (2) Tram, (3) Underground or (B) Back): ");
				stationType = input.nextLine().toUpperCase();
			}
		}

		// Leaves
		if (dataType.equals("B") || stationType.equals("B")) {
			return;
		}

		String ID;
		String name;
		String confirmed = "";
		String requestStop;
		String stopType = "";
		String manager;

		// Grabs Station ID
		if (stationType.equals("1")) {
			do {
				System.out.print("What is the new stations ID: ");
				ID = input.nextLine();

				if (ID.length() == 3 && stationList.findStationByID(ID).isEmpty()) {
					System.out.print("Are you sure that " + ID + " is correct (Y) yes, (N) No or (B) = Back: ");
					confirmed = input.nextLine().toUpperCase();

					if (confirmed.equals("B")) {
						System.out.print("Are you sure you wanna leave without adding a new Station (Y) yes or (N) No ");
						confirmed = input.nextLine().toUpperCase();
						if (confirmed.equals("Y")) {
							return;
						}
					}
				} else if (ID.length() != 3) {
					System.out.println("Not a valid length!");
				} else {
					System.out.println("Station ID is already in use!");
				}
			} while (confirmed.equals("N"));
		} else {
			ID = "N/A";
		}

		// Station Name
		do {
			System.out.print("What is the new stations name: ");
			name = input.nextLine();

			System.out.print("Are you sure that " + name + " is correct (Y) yes, (N) No or (B) Back: ");
			confirmed = input.nextLine().toUpperCase();
			if (confirmed.equals("B")) {
				System.out.print("Are you sure you wanna leave without adding a new Station (Y) yes or (N) No ");
				confirmed = input.nextLine().toUpperCase();
				if (confirmed.equals("Y")) {
					return;
				}
			}
		} while (confirmed.equals("N"));

		// Request Stop
		do {
			System.out.print("Is it a request Stop (YES) Yes or (NO) or (B) Back: ");
			requestStop = input.nextLine();

			if (confirmed.equals("B")) {
				System.out.print("Are you sure you wanna leave without adding a new Station (Y) yes or (N) No ");
				confirmed = input.nextLine().toUpperCase();
				if (confirmed.equals("Y")) {
					return;
				}
			}
		} while (!(requestStop.equals("NO") || requestStop.equals("YES") || requestStop.equals("B")));

		// Station Manager
		do {
			System.out.print("Who manages the station: ");
			manager = input.nextLine();

			System.out.print("Are you sure that " + manager + " manages the station (Y) yes, (N) No or (B) Back: ");
			confirmed = input.nextLine().toUpperCase();
			if (confirmed.equals("B")) {
				System.out.print("Are you sure you wanna leave without adding a new Station (Y) yes or (N) No ");
				confirmed = input.nextLine().toUpperCase();
				if (confirmed.equals("Y")) {
					return;
				}
			}
		} while (!confirmed.equals("Y"));

		// Station Type
		switch (stationType) {
			case "1" -> stopType = "Train";
			case "2" -> stopType = "Tram";
			case "3" -> stopType = "Underground";
		}

		// No date data
		if (dataType.equals("S")) {
			stationList.addStation(new Station(ID, name, stopType, manager, requestStop, 0, "NULL"));
			return;
		}

		System.out.println("Not implemented yet :(");
	}

	private void statShower() {

		System.out.println("No ones here :)");
	}

	private void allTheStationsDisplay() {

		String response;
		boolean back = false;

		do {
			System.out.print("What would you like to see (1) = Trains, (2) = Trams, (3) = Underground (B) = Back: ");
			response = input.nextLine().toUpperCase();

			switch (response) {
				case "1" -> stationList.showList("Train");
				case "2" -> stationList.showList("Tram");
				case "3" -> stationList.showList("Underground");
				case "B" -> back = true;
				default -> System.out.println("Not a valid input please try again");
			}

		} while (!(response.equals("1") || response.equals("2") || response.equals("3") || back));

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

	private Station stationSelect(ArrayList<Station> stations) {

		Station currentStation = null;

		while (currentStation == null) {
			int i = 1;
			System.out.println("Which station is it:");
			for (Station station : stations) {
				System.out.println(i + ": " + station.getName() + " " + station.getId() + " (" + station.getStopType() + ")");
				i++;
			}
			int index = 0;
			boolean error = false;
			try {
				index = input.nextInt() - 1;
			} catch (InputMismatchException e) {
				System.out.println("Input Should be an integer");
				error = true;
			}
			input.nextLine();
			if (!error) {
				if (index < 0 || index >= stations.size()) {
					System.out.println("Invalid input");
				} else {
					currentStation = stations.get(index);
				}
			}
		}
		return currentStation;
	}

	private boolean stationActivities() {

		boolean done = false;
		String response = "";

		while (!done) {

			System.out.println("What would you like to do with " + selectedStation.getName() + "?");
			System.out.println("(0) Update Name and/or ID, (1) Update Stopping Information, (2) Update platform Information, (I) Show Station Info, (E) Edit Different Station or (Q) Quit:");
			response = input.nextLine().toUpperCase();

			switch (response) {
				case "0" -> updateNameAndID();
				case "1" -> updateStoppingInfo();
				case "2" -> updatePlatformInfo();
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

	private void updatePlatformInfo() {

		System.out.print("What would you like to update (1 = Update No. Of Platforms, 2 = Add a Platform, 3 = Display Visited Platforms 4 = Back): ");
		String response = input.nextLine().toUpperCase();

		while (!(response.equals("1") || response.equals("2") || response.equals("3") || response.equals("4"))) {
			System.out.println("Invalid input");
			System.out.print("What would you like to update (1 = Update No. Of Platforms, 2 = Add a Platform, 3 = Display Visited Platforms 4 = Back): ");
			response = input.nextLine().toUpperCase();
		}

		String confirmed = "N";

		// Updates number of platforms
		if (response.equals("1")) {

			int numOfPlatforms = 0;
			do {
				System.out.print("How many platforms does " + selectedStation.getName() + " have: ");
				try {
					numOfPlatforms = Integer.parseInt(input.nextLine());

					if (numOfPlatforms < 1) {
						throw new NumberFormatException();
					}
				} catch (NumberFormatException e) {
					System.out.println("It must be a number greater than 0");
					continue;
				}
				System.out.print("Are you sure " + selectedStation.getName() + " has " + numOfPlatforms + (numOfPlatforms == 1 ? " platform" : " platforms") +" (Y = yes or N = No): ");
				confirmed = input.nextLine().toUpperCase();

			} while (confirmed.equals("N"));
			stationList.updateNumOfPlatforms(selectedStation, numOfPlatforms);
		}

		// Adds new Platform
		if (response.equals("2")) {

			String data;

			do {
				System.out.print("What platform at " + selectedStation.getName() + " would you like to add or type done when finished: ");
				data = input.nextLine().toUpperCase();

				if (!data.equals("DONE")) {
					System.out.print("Are you sure you want to add platform " + data + " to " + selectedStation.getName() + "'s list of platforms visited (Y = yes or N = No): ");
					confirmed = input.nextLine().toUpperCase();

					if (confirmed.equals("Y")) {
						selectedStation.addPlatform(data);
						System.out.println("platform " + data + " has been added");
					}
				}
			} while (!data.equals("DONE"));
		}

		// Show current data
		if (response.equals("3")) {

			if (selectedStation.getStoppedAt().equals("NULL")) {
				// Station has not been visited in any way (Worst Case = Red)
				System.out.println("\nYou have not visited " + selectedStation.getName() + " station.\n");
			} else if (selectedStation.getNumberOfPlatforms() == 0) {
				// Station has not been visited but no data is currently held (Worst Case = Red)
				System.out.println("\nYou have visited " + selectedStation.getName() + " station but have not inputted any platform data.\n");
			} else if (selectedStation.getPlatformsVisitedList().get(0).equals("N/A")) {
				// Station has been visited but did not get off (Average Case = Yellow)
				System.out.println("\n" + selectedStation.getName() + " Has " + selectedStation.getNumberOfPlatforms() + (selectedStation.getNumberOfPlatforms() == 1 ? " platform," : " platforms,") + " You have not visited any platforms yet.\n");
			} else {
				// Station has been visited (Best Case = Green)
				ArrayList<String> visitedPlatforms = selectedStation.getPlatformsVisitedList();
				double percentage = ((double) visitedPlatforms.size() / selectedStation.getNumberOfPlatforms()) * 100;

				System.out.print("\n" + selectedStation.getName() + " Has " + selectedStation.getNumberOfPlatforms() + (selectedStation.getNumberOfPlatforms() == 1 ? " platform," : " platforms,") + " You have visited " + visitedPlatforms.size() + (visitedPlatforms.size() == 1 ? " platform," : " platforms,"));
				System.out.println(" That's " + percentage + "% " + (percentage == 100.0 ? "Well Done :)" : "Keep Going"));

				StringBuilder platformList = new StringBuilder();

				for (int i = 0; i < visitedPlatforms.size(); i++) {
					platformList.append(visitedPlatforms.get(i));
					if (i != visitedPlatforms.size()-1) {
						platformList.append(", ");
					}
				}
				System.out.println("The platforms you've visited are: " + platformList + "\n");
			}
		}
	}

	private void shutDown() {
		stationList.saveFile();
		System.exit(0);
	}
}
