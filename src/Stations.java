import java.io.*;
import java.util.*;
import com.opencsv.*;

import javax.swing.*;

public class Stations {

	private static Stations stationsInstance;
	private final ArrayList<Station> stationList;
	private final File stationFile;
	private int maxAreaLength = "Area ".length();
	private int maxCountryLength = "Country ".length();
	private final ArrayList<ArrayList<String>> areas = new ArrayList<>();

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

	public ArrayList<Station> getStationList() { return stationList; }

	public void removeStation(Station station) { stationList.remove(station); }

	public void addStation(Station station) {
		maxAreaLength = Math.max(maxAreaLength, station.getArea().length());
		maxCountryLength = Math.max(maxCountryLength, station.getCountry().length());
		stationList.add(station);
		quickSort(0, stationList.size()-1);
		areaList(station);
		sortAreas();
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
	        if (compareStations(stationList.get(j), pivot) < 0) {
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

	// Custom comparison function
	private int compareStations(Station s1, Station s2) {
	    int priority1 = getStopTypePriority(s1.getStopType());
	    int priority2 = getStopTypePriority(s2.getStopType());

	    // First, compare based on the priority of the stop type
	    if (priority1 != priority2) {
	        return Integer.compare(priority1, priority2);
	    }

	    // If the stop types are the same, compare by name
	    return s1.getName().compareTo(s2.getName());
	}

	// Function to assign priority based on the stop type
	private int getStopTypePriority(String stopType) {
		return switch (stopType) {
			case "Train" -> 1;
			case "Tram" -> 2;
			case "Underground" -> 3;
			default -> 4; // For any other types, if they exist
		};
	}

	private void loadFile() {

		try {
			FileReader fr = new FileReader(stationFile);
			BufferedReader br = new BufferedReader(fr);
			String line;
			br.readLine();
			while ((line = br.readLine()) != null) {
				String[] stationInfo = line.split(",");
				Station newStation = new Station(stationInfo[0], stationInfo[1], stationInfo[2], stationInfo[3], stationInfo[4], stationInfo[5], Integer.parseInt(stationInfo[6]), stationInfo[7], stationInfo[8], stationInfo[9], stationInfo[10], stationInfo[11], stationInfo[12]);
				addStation(newStation);
				maxAreaLength = Math.max(maxAreaLength, newStation.getArea().length());
				maxCountryLength = Math.max(maxCountryLength, newStation.getCountry().length());
				areaList(newStation);
			}
			br.close();
		} catch(IOException e) {
			System.out.println("File not found!");
		}
		sortAreas();
	}

	private void areaList(Station newStation) {

		if (!newStation.getArea().isEmpty()) {
			boolean found = false;
			for (ArrayList<String> area : areas) {
				if (area.get(0).equals(newStation.getArea())) {
					found = true;
					break;
				}
			}
			if (!found) {
				ArrayList<String> newArea = new ArrayList<>();
				newArea.add(newStation.getArea()); // Area 0
				newArea.add(newStation.getCountry()); // Country 1
				newArea.add("0"); // Total 2
				newArea.add("0"); // Explored 3
				newArea.add("0"); // Stopped 4
				newArea.add("0"); // Passed Stopping 5
				newArea.add("0"); // Passed 6
				newArea.add("0"); // Visited 7
				newArea.add("0"); // Total Platforms visited 8
				newArea.add("0"); // Total Platforms 9
				areas.add(newArea);
			}
		}
	}

	private void sortAreas() {
		areas.sort(Comparator.comparing((ArrayList<String> o) -> o.get(1)).thenComparing(o -> o.get(0)));
	}

	public void saveFile() {

		try {
			FileWriter outputFile = new FileWriter(stationFile);
			CSVWriter writer = new CSVWriter(outputFile);

			// adding header to csv
			String[] header = {"Station ID", "Station Name", "Stop Type", "Location", "Managed By", "Request Stop", "No. Of Platforms", "Platforms Used", "Stopped At", "Passed Through (Stopped)", "Passed Through (Not Stopped)", "Visited", "Explored"};
			writer.writeNext(header,false);

			// add data to csv
			for (Station station : stationList) {
				String[] stationData = {station.getId(), station.getName(), station.getStopType(), station.getCountry() + ". " + station.getArea(), station.getManager(), station.getRequestStop() ? "YES" : "NO", Integer.toString(station.getNumberOfPlatforms()), station.savePlatformsUsed(), station.getStoppedAt(), station.getPassedThroughStopping(), station.getPassedThrough(), station.getVisited(), station.getExplored()};
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
			if (station.getId().equalsIgnoreCase(id)) {
				stations.add(station);
			}
		}
		return stations;
	}

	public ArrayList<Station> findStationByName(String name) {

		ArrayList<Station> stations = new ArrayList<>();

		for (Station station : stationList) {
			if (station.getName().equalsIgnoreCase(name)) {
				stations.add(station);
			}
		}
		return stations;
	}

	public void updateExplored(Station station, String date) {

		station.setExplored(date);

		if (station.getStoppedAt().equals("NULL")) {
			station.setStoppedAt("N/A");
		}

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

	public void updateStoppedAt(Station station, String date) {

		if (station.getExplored().equals("NULL")) {
			station.setExplored("N/A");
		}

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

		if (station.getExplored().equals("NULL")) {
			station.setExplored("N/A");
		}

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

		if (station.getExplored().equals("NULL")) {
			station.setExplored("N/A");
		}

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

		if (station.getExplored().equals("NULL")) {
			station.setExplored("N/A");
		}

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

	public ArrayList<Integer> totalStations(String countryFilter, String areaFilter, String stationType) {

		ArrayList<Integer> totals = new ArrayList<>();
		totals.add(0); // Total
		totals.add(0); // Explored
		totals.add(0); // Stopped At
		totals.add(0); // Passed Stopping
		totals.add(0); // Passed Not Stopping
		totals.add(0); // Visited

		for (Station station : stationList) {
			if (station.getStopType().equalsIgnoreCase(stationType) || stationType.equalsIgnoreCase("ALL")) {
				if (!station.getStoppedAt().equals("NULL")) {
					if (station.getArea().equals(areaFilter) || ((areaFilter.equals("All Areas") && station.getCountry().equals(countryFilter)) || countryFilter.equals("World"))) {
						totals.set(0, totals.get(0) + 1);
						if (!station.getExplored().equals("N/A")) {
							totals.set(1, totals.get(1) + 1);
						}
						if (!station.getStoppedAt().equals("N/A")) {
							totals.set(2, totals.get(2) + 1);
						} else if (!station.getPassedThroughStopping().equals("N/A")) {
							totals.set(3, totals.get(3) + 1);
						} else if (!station.getPassedThrough().equals("N/A")) {
							totals.set(4, totals.get(4) + 1);
						} else if (!station.getVisited().equals("N/A")) {
							totals.set(5, totals.get(5) + 1);
						}
					}
				}
			}
		}
		return totals;
	}

	public void showAreaList(String stationType, JTextArea display) {

		StringBuilder headers = new StringBuilder();
		headers.append("|");
		headers.append(String.format("%-" + maxCountryLength + "s","Country")).append(" |");
		headers.append(String.format("%-" + maxAreaLength + "s","Area")).append(" |");

		int maxTotalLength = "Total".length();
		int maxExploredLength = "Explored".length();
		int maxStoppedLength = "Stopped".length();
		int maxPassedStoppingLength = "Passed Stopping".length();
		int maxPassedLength = "Passed".length();
		int maxVisitedLength = "Visited".length();
		int maxPlatformVisited = "Platform Count".length();

		for (Station station : stationList) {
			if (!station.getStoppedAt().equals("NULL") && (station.getStopType().equalsIgnoreCase(stationType) || stationType.equalsIgnoreCase("ALL"))) {

				int index = 0;

				for (ArrayList<String> area : areas) {
					if (area.get(0).equals(station.getArea()) && area.get(1).equals(station.getCountry())) {
						break;
					}
					index++;
				}

				areas.get(index).set(2, String.valueOf(Integer.parseInt(areas.get(index).get(2)) + 1));
				maxTotalLength = Math.max(maxTotalLength, areas.get(index).get(2).length());

				if (!station.getStoppedAt().equals("N/A")) {

					areas.get(index).set(4, String.valueOf(Integer.parseInt(areas.get(index).get(4)) + 1));
					maxStoppedLength = Math.max(maxStoppedLength, areas.get(index).get(4).length());

					areas.get(index).set(8, String.valueOf(Integer.parseInt(areas.get(index).get(8)) + station.getPlatformsVisitedList().size()));

					if (!station.getExplored().equals("N/A")) {
						areas.get(index).set(3, String.valueOf(Integer.parseInt(areas.get(index).get(3)) + 1));
						maxExploredLength = Math.max(maxExploredLength, areas.get(index).get(3).length());
					}
				} else if (!station.getPassedThroughStopping().equals("N/A")) {

					areas.get(index).set(5, String.valueOf(Integer.parseInt(areas.get(index).get(5)) + 1));
					maxPassedStoppingLength = Math.max(maxPassedStoppingLength, areas.get(index).get(5).length());

				} else if (!station.getPassedThrough().equals("N/A")) {

					areas.get(index).set(6, String.valueOf(Integer.parseInt(areas.get(index).get(6)) + 1));
					maxPassedLength = Math.max(maxPassedLength, areas.get(index).get(6).length());

				} else if (!station.getVisited().equals("N/A")) {

					areas.get(index).set(7, String.valueOf(Integer.parseInt(areas.get(index).get(7)) + 1));
					maxVisitedLength = Math.max(maxVisitedLength, areas.get(index).get(7).length());

				}
				areas.get(index).set(9, String.valueOf(Integer.parseInt(areas.get(index).get(9)) + station.getNumberOfPlatforms()));
				maxPlatformVisited = Math.max(maxPlatformVisited, (areas.get(index).get(8) + "/" + areas.get(index).get(9)).length());
			}
		}
		headers.append(String.format("%-" + maxTotalLength + "s","Total")).append(" |");
		headers.append(String.format("%-" + maxExploredLength + "s","Explored")).append(" |");
		headers.append(String.format("%-" + maxStoppedLength + "s","Stopped")).append(" |");
		headers.append(String.format("%-" + maxPassedStoppingLength + "s","Passed Stopping")).append(" |");
		headers.append(String.format("%-" + maxPassedLength + "s","Passed")).append(" |");
		headers.append(String.format("%-" + maxVisitedLength + "s","Visited")).append(" |");
		headers.append(String.format("%-" + maxPlatformVisited + "s","Platform Count")).append(" |\n");

		display.append(headers.toString());
		StringBuilder splitter = new StringBuilder();

		splitter.append("+");
		splitter.append(String.format("%-" + maxCountryLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxAreaLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxTotalLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxExploredLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxStoppedLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxPassedStoppingLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxPassedLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxVisitedLength + "s", "").replace(' ', '-')).append("-+");
		splitter.append(String.format("%-" + maxPlatformVisited + "s", "").replace(' ', '-')).append("-+\n");

		String previousCountry = null;

		for (ArrayList<String> areaInfo : areas) {

			if (previousCountry == null || !previousCountry.equals(areaInfo.get(1))) {
				display.append(splitter.toString());
			}

			StringBuilder info = new StringBuilder();

			String countryLocation = areaInfo.get(1);
			countryLocation = String.format("%-" + maxCountryLength + "s", countryLocation);
			info.append("|").append(countryLocation);

			String areaLocation = areaInfo.get(0);
			areaLocation = String.format("%-" + maxAreaLength + "s", areaLocation);
			info.append(" |").append(areaLocation);

			String total = areaInfo.get(2);
			total = String.format("%-" + maxTotalLength + "s", total);
			info.append(" |").append(total);

			String explored = areaInfo.get(3);
			explored = String.format("%-" + maxExploredLength + "s", explored);
			info.append(" |").append(explored);

			String stopped = areaInfo.get(4);
			stopped = String.format("%-" + maxStoppedLength + "s", stopped);
			info.append(" |").append(stopped);

			String passedStopping = areaInfo.get(5);
			passedStopping = String.format("%-" + maxPassedStoppingLength + "s", passedStopping);
			info.append(" |").append(passedStopping);

			String passed = areaInfo.get(6);
			passed = String.format("%-" + maxPassedLength + "s", passed);
			info.append(" |").append(passed);

			String visited = areaInfo.get(7);
			visited = String.format("%-" + maxVisitedLength + "s", visited);
			info.append(" |").append(visited);

			String platformCount = areaInfo.get(8) + "/" + areaInfo.get(9);
			platformCount = String.format("%-" + maxPlatformVisited + "s", platformCount);
			info.append(" |").append(platformCount);

			info.append(" |\n");
			display.append(info.toString());
			previousCountry = areaInfo.get(1);
	    }
		display.append(splitter.toString());
		reset();
	}

	private void reset() {

		for (ArrayList<String> area : areas) {
			area.set(2, "0");
			area.set(3, "0");
			area.set(4, "0");
			area.set(5, "0");
			area.set(6, "0");
			area.set(7, "0");
			area.set(8, "0");
			area.set(9, "0");
		}
	}

	// Pages decides stations included: ALL, Train, Tram, Underground
	public void showList(String stationType, String country, String area, JTextArea display) {

	    int typeLength = 0;
	    int nameLength = 0;
	    int areaLength = 0;
	    int managerLength = 0;
		int dateLength = 0;
		int platformLength = 0;

	    for (Station station : stationList) {
	        if (!station.getStoppedAt().equals("NULL") && (station.getStopType().equalsIgnoreCase(stationType) || stationType.equalsIgnoreCase("ALL")) && (station.getCountry().equals(country) || country.equalsIgnoreCase("World")) && (station.getArea().equals(area) || area.equalsIgnoreCase("All Areas") || area.equalsIgnoreCase("Null"))) {

	            typeLength = Math.max(typeLength, station.getStopType().length());
	            nameLength = Math.max(nameLength, station.getName().length());

	            if (country.equals("World")) {
	                areaLength = Math.max(areaLength, (station.getArea() + ", " + station.getCountry()).length());
	            } else {
	                areaLength = Math.max(areaLength, station.getArea().length());
	            }
	            managerLength = Math.max(managerLength, station.getManager().length());

				if (!station.getStoppedAt().equals("N/A")) {
					dateLength = Math.max(dateLength, "Stopped".length());
				} else if (!station.getPassedThroughStopping().equals("N/A")) {
					dateLength = "Passed Stopping".length();
				} else if (!station.getPassedThrough().equals("N/A")) {
					dateLength = Math.max(dateLength, "Passed".length());
				} else {
					dateLength = Math.max(dateLength, "Visited".length());
				}

				String platformStat = station.getNumberOfPlatforms() + "/" + station.getPlatformsVisitedList().size();
				platformLength = Math.max(platformLength, platformStat.length());
	        }
	    }

	    for (Station station : stationList) {
	        if (!station.getStoppedAt().equals("NULL") && (station.getStopType().equalsIgnoreCase(stationType) || stationType.equalsIgnoreCase("ALL")) && (station.getCountry().equals(country) || country.equalsIgnoreCase("World")) && (station.getArea().equals(area) || area.equalsIgnoreCase("All Areas") || area.equalsIgnoreCase("Null"))) {

	            StringBuilder info = new StringBuilder();

	            if (stationType.equalsIgnoreCase("ALL")) {
	                String stopType = String.format("%-" + typeLength + "s", station.getStopType());
	                info.append("|").append(stopType);
	            }

	            info.append(stationType.equals("ALL") ? " |ID: " : "|ID: ").append(station.getId()).append(" |Name: ");

	            String name = String.format("%-" + nameLength + "s", station.getName());
	            info.append(name);

	            if (country.equalsIgnoreCase("World") || area.equalsIgnoreCase("All Areas")) {
	                String location;
	                if (country.equalsIgnoreCase("World")) {
						location = station.getArea() + ", " + station.getCountry();
	                } else {
	                    location = station.getArea();
	                }
	                location = String.format("%-" + areaLength + "s", location);
	                info.append(" |Location: ").append(location);
	            }

	            String manager = String.format("%-" + managerLength + "s", station.getManager());
	            info.append(" |Managed by: ").append(manager)
	                .append(" |Is Request Stop: ").append(station.getRequestStop() ? "Yes" : "No ")
			        .append(" |Explored: ").append(station.getExplored().equals("N/A") ? "No " : "Yes")
			        .append(" |Station Status: ").append(getStatus(station, dateLength)) //Stopped, Passed Stopping, Passed, Visited
			        .append(" |Platform Count: ");

				String platformCount = "";

				if (station.getPlatformsVisitedList().get(0).equals("N/A")) {
					platformCount += "0";
				} else {
					platformCount += station.getPlatformsVisitedList().size();
				}

				platformCount += ("/" + station.getNumberOfPlatforms());

				platformCount = String.format("%-" + platformLength + "s", platformCount);
	            info.append(platformCount).append(" |\n");

	            display.append(info.toString());
	        }
	    }
	}

	private String getStatus(Station station, int dateLength) {

		String status;

		if (!station.getStoppedAt().equals("N/A")) {
			status = "Stopped";
		} else if (!station.getPassedThroughStopping().equals("N/A")) {
			status = "Passed Stopping";
		} else if (!station.getPassedThrough().equals("N/A")) {
			status = "Passed";
		} else {
			status = "Visited";
		}
		status = String.format("%-" + dateLength + "s", status);

		return status;
	}
}
