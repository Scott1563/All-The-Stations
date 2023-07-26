import java.io.*;
import java.util.*;
import com.opencsv.*;

public class Stations {

	private static Stations stationsInstance;
	private final ArrayList<Station> stationList;
	private final File stationFile;
	private int largestName = 0;

	private Stations() {
		stationList = new ArrayList<>();
		stationFile = new File("Data/Stations.csv");
		loadFile();
	}

	public static Stations getStationsInstance() {

		if (stationsInstance == null) {
			stationsInstance = new Stations();
		}
		return stationsInstance;
	}

	public void addStation(Station station) {
		stationList.add(station);
		quickSort(0, stationList.size()-1);
	}

	private void quickSort(int low, int high) {
		if (low < high) {
			int pivot = partition(low, high);
			quickSort(low, pivot - 1);
			quickSort(pivot + 1, high);
		}
	}

	private int partition(int low, int high) {
		Station pivot = stationList.get(high);
		int i = low - 1;
		for (int j = low; j < high; j++) {
			if (stationList.get(j).getName().compareTo(pivot.getName()) < 0) {
				i++;
				Station temp = stationList.get(i);
				stationList.set(i, stationList.get(j));
				stationList.set(j, temp);
			}
		}
		Station temp = stationList.get(i + 1);
		stationList.set(i + 1, stationList.get(high));
		stationList.set(high, temp);
		return i + 1;
	}

	private void loadFile() {

		try {
			FileReader fr = new FileReader(stationFile);
			BufferedReader br = new BufferedReader(fr);
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] stationInfo = line.split(",");
				stationList.add(new Station(stationInfo[0], stationInfo[1], stationInfo[2], stationInfo[3], stationInfo[4], stationInfo[5], Integer.parseInt(stationInfo[6]), stationInfo[7], stationInfo[8], stationInfo[9], stationInfo[10], stationInfo[11]));
				if (stationInfo[1].length() > largestName) {
					largestName = stationInfo[1].length();
				}
			}
			br.close();
		} catch(IOException e) {
			System.out.println("File not found!");
		}
	}

	public void saveFile() {

		try {
			FileWriter outputFile = new FileWriter(stationFile);
			CSVWriter writer = new CSVWriter(outputFile);

			// adding header to csv
			String[] header = {"Station ID", "Station Name", "Stop Type", "Location", "Managed By", "Request Stop", "No. Of Platforms", "Platforms Used", "Stopped At", "Passed Through (Stopped)", "Passed Through (Not Stopped)", "Visited"};
			writer.writeNext(header,false);

			// add data to csv
			for (Station station : stationList) {
				String[] stationData = {station.getId(), station.getName(), station.getStopType(), station.getCountry() + ". " + station.getArea(), station.getManager(), station.getRequestStop() ? "YES" : "NO", Integer.toString(station.getNumberOfPlatforms()), station.savePlatformsUsed(), station.getStoppedAt(), station.getPassedThroughStopping(), station.getPassedThrough(), station.getVisited()};
				writer.writeNext(stationData, false);
			}

			// closing writer connection
			writer.close();
		}
		catch (IOException e) {
			System.out.println("File Not Created!");
		}
	}

	public ArrayList<Station> findStationByID(String id) {

		ArrayList<Station> stations = new ArrayList<>();

		for (Station station : stationList) {
			if (station.getId().equals(id)) {
				stations.add(station);
			}
		}
		return stations;
	}

	public ArrayList<Station> findStationByName(String name) {

		ArrayList<Station> stations = new ArrayList<>();

		for (Station station : stationList) {
			if (station.getName().equals(name)) {
				stations.add(station);
			}
		}
		return stations;
	}

	public void updateNameAndID(Station station, String id, String name) {

		if (!id.equals("")) {
			station.setId(id);
		}
		if (!name.equals("")) {
			station.setName(name);
		}
	}

	public void updateCountryAndArea(Station station, String country, String area) {

		if (!country.equals("")) {
			station.setCountry(country);
		}
		if (!area.equals("")) {
			station.setArea(area);
		}
	}

	public void updateStoppedAt(Station station, String date) {

		station.setStoppedAt(date);

		if (station.getPassedThroughStopping().equals("NULL")) {
			station.setPassedThroughStopping("N/A");
		}

		if (station.getPassedThrough().equals("NULL")) {
			station.setPassedThrough("N/A");
		}

		if (station.getVisited().equals("NULL")) {
			station.setVisited("N/A");
		}
	}

	public void updatePassedThroughStopping(Station station, String date) {

		if (station.getStoppedAt().equals("NULL")) {
			station.setStoppedAt("N/A");
		}

		station.setPassedThroughStopping(date);

		if (station.getPassedThrough().equals("NULL")) {
			station.setPassedThrough("N/A");
		}

		if (station.getVisited().equals("NULL")) {
			station.setVisited("N/A");
		}
	}

	public void updatePassedThrough(Station station, String date) {

		if (station.getStoppedAt().equals("NULL")) {
			station.setStoppedAt("N/A");
		}

		if (station.getPassedThroughStopping().equals("NULL")) {
			station.setPassedThroughStopping("N/A");
		}

		station.setPassedThrough(date);

		if (station.getVisited().equals("NULL")) {
			station.setVisited("N/A");
		}
	}

	public void updateVisited(Station station, String date) {

		if (station.getStoppedAt().equals("NULL")) {
			station.setStoppedAt("N/A");
		}

		if (station.getPassedThroughStopping().equals("NULL")) {
			station.setPassedThroughStopping("N/A");
		}

		if (station.getPassedThrough().equals("NULL")) {
			station.setPassedThrough("N/A");
		}

		station.setVisited(date);
	}

	public void updateNumOfPlatforms(Station station, int numOfPlatforms) {

		if (station.getPlatformsVisitedList().get(0).equals("NULL") || station.getPlatformsVisitedList().get(0).equals("N/A")) {
			station.getPlatformsVisitedList().set(0, "N/A");
		}
		station.setNumberOfPlatforms(numOfPlatforms);
	}

	public int totalStations() {

		int total = 0;

		for (Station station : stationList) {
			if (!station.getStoppedAt().equals("NULL")) {
				total++;
			}
		}
		return total;
	}

	public void showList(String stationType) {

		for (Station station : stationList) {
			if (station.getStopType().equals(stationType)) {
				System.out.print("|ID: " + station.getId() + " |Name: ");

				StringBuilder name = new StringBuilder(station.getName());

				while (name.length() != largestName) {
					name.append(" ");
				}

				System.out.print(name + " |Is Request Stop: " + (station.getRequestStop() ? "Yes" : "No ") + " |Stopped At: ");

				if (station.getStoppedAt().equals("NULL")) {
					System.out.print("NULL      ");
				} else if (station.getStoppedAt().equals("N/A")) {
					System.out.print("N/A       ");
				} else {
					System.out.print(station.getStoppedAt());
				}
				System.out.print(" |Passed Through (Stopping): ");
				if (station.getPassedThroughStopping().equals("NULL")) {
					System.out.print("NULL      ");
				} else if (station.getPassedThroughStopping().equals("N/A")) {
					System.out.print("N/A       ");
				} else {
					System.out.print(station.getPassedThroughStopping());
				}
				System.out.print(" |Passed Through (Not Stopping): ");
				if (station.getPassedThrough().equals("NULL")) {
					System.out.print("NULL      ");

				} else if (station.getPassedThrough().equals("N/A")) {
					System.out.print("N/A       ");
				} else {
					System.out.print(station.getPassedThrough());
				}
				System.out.print(" |Visited: ");
				if (station.getVisited().equals("NULL")) {
					System.out.print("NULL      ");

				} else if (station.getVisited().equals("N/A")) {
					System.out.print("N/A       ");
				} else {
					System.out.print(station.getVisited());
				}
				System.out.println("|");
			}
		}
	}
}
