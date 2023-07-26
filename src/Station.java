import java.util.ArrayList;
import java.util.Arrays;

public class Station {

	private String id;
	private String name;
	private String stopType;
	private String country;
	private String area;
	private String manager;
	private boolean requestStop;
	private int numberOfPlatforms;
	private ArrayList<String> platformsVisitedList;
	private String stoppedAt;
	private String passedThroughStopping;
	private String passedThrough;
	private String visited;

	public Station(String id, String name, String stopType, String location, String manager, String requestStop, int numberOfPlatforms, String platformsVisited, String stoppedAt, String passedThroughStopping,  String passedThrough, String visited) {
		this.id = id;
		this.name = name;
		this.stopType = stopType;
		this.manager = manager;
		this.requestStop = requestStop.equals("YES");
		this.numberOfPlatforms = numberOfPlatforms;
		this.stoppedAt = stoppedAt;
		this.passedThroughStopping = passedThroughStopping;
		this.passedThrough = passedThrough;
		this.visited = visited;
		platformVisited(platformsVisited);
		stationLocation(location);
	}

	public Station(String id, String name, String stopType, String location, String manager, String requestStop, int numberOfPlatforms, String platformsVisited) {
		this(id, name, stopType, location, manager, requestStop, numberOfPlatforms, platformsVisited, "NULL", "NULL", "NULL", "NULL");
	}

	private void stationLocation(String location) {

		String[] locationData = location.split("\\.");

		country = locationData[0].trim();

		if (locationData.length == 2) {
			area = locationData[1].trim();
		} else {
			area = "";
		}
	}

	private void platformVisited(String platformsVisited) {

		platformsVisitedList = new ArrayList<>();

		if (numberOfPlatforms == 0 || stoppedAt.equals("N/A")) {
			platformsVisitedList.add(platformsVisited);
		} else {
			String[] platforms = platformsVisited.split("\\.");
			platformsVisitedList.addAll(Arrays.asList(platforms));
		}
	}

	public String getId() { return id; }

	public String getName() { return name; }

	public String getStopType() { return stopType; }

	public String getCountry() { return country; }

	public String getArea() { return area; }

	public String getManager() { return manager; }

	public boolean getRequestStop() { return requestStop; }

	public int getNumberOfPlatforms() { return numberOfPlatforms; }

	public ArrayList<String> getPlatformsVisitedList() { return platformsVisitedList; }

	public String getStoppedAt() { return stoppedAt; }

	public String getPassedThroughStopping() { return passedThroughStopping; }

	public String getPassedThrough() { return passedThrough; }

	public String getVisited() { return visited; }

	public void setId(String id) { this.id = id; }

	public void setName(String name) { this.name = name; }

	public void setStopType(String stopType) { this.stopType = stopType; }

	public void setCountry(String country) { this.country = country; }

	public void setArea(String area) { this.area = area; }

	public void setManager(String manager) { this.manager = manager; }

	public void setRequestStop(boolean requestStop) { this.requestStop = requestStop; }

	public void setNumberOfPlatforms(int numberOfPlatforms) { this.numberOfPlatforms = numberOfPlatforms; }


	public void addPlatform(String platform) {

		if (platformsVisitedList.get(0).equals("N/A") || platformsVisitedList.get(0).equals("NULL")) {
			platformsVisitedList.set(0, platform);
		} else {
			int i = 0;
			while (i < platformsVisitedList.size() && comparePlatforms(platform, platformsVisitedList.get(i)) > 0) {
				i++;
			}
			platformsVisitedList.add(i, platform);
		}
	}

	private int comparePlatforms(String platform1, String platform2) {

		if (Character.isDigit(platform1.charAt(0))) {
			if (!Character.isDigit(platform2.charAt(0))) {
				return -1; // platform1 is a number and platform2 is a letter, so platform1 should come first
			} else {
				return Integer.compare(Integer.parseInt(platform1), Integer.parseInt(platform2)); // both platforms are numbers, so compare them numerically
			}
		} else {
			if (Character.isDigit(platform2.charAt(0))) {
				return 1; // platform1 is a letter and platform2 is a number, so platform2 should come first
			} else {
				return platform1.compareTo(platform2); // both platforms are letters, so compare them alphabetically
			}
		}
	}

	public void setStoppedAt(String stoppedAt) { this.stoppedAt = stoppedAt; }

	public void setPassedThroughStopping(String passedThroughStopping) { this.passedThroughStopping = passedThroughStopping; }

	public void setPassedThrough(String passedThrough) { this.passedThrough = passedThrough; }

	public void setVisited(String visited) { this.visited = visited; }

	public String savePlatformsUsed() {

		if (numberOfPlatforms == 0 || stoppedAt.equals("N/A")) {
			return platformsVisitedList.get(0);
		} else {

			StringBuilder output = new StringBuilder();
			int i = 0;

			for (String platform : platformsVisitedList) {
				output.append(platform);
				if (i != platformsVisitedList.size()-1) {
					output.append(".");
				}
				i++;
			}
			return output.toString();
		}
	}

	public String stationInfo() {
		return "ID: " + id + ", name: " + name + ", location: " + area + " " + country + ", Is Request Stop: " + (requestStop ? "Yes": "No") + ", Stopped At: " + stoppedAt + ", Passed Through (Stopping): " + passedThroughStopping + ", Passed Through (Not Stopping): " + passedThrough + ", Visited: " + visited + "\n";
	}

	@Override
	public String toString() {
		return id + "  " + name + "  " + stopType + "  " + requestStop + "  " + stoppedAt + "  " + passedThroughStopping + "  " + passedThrough + " " + visited;
	}
}
