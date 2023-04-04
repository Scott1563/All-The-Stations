public class Station {
	private String id;
	private String name;
	private String stopType;
	private boolean requestStop;
	private String stoppedAt;
	private String passedThroughStopping;
	private String passedThrough;

	public Station(String id, String name, String stopType, String requestStop, String stoppedAt, String passedThroughStopping,  String passedThrough) {
		this.id = id;
		this.name = name;
		this.stopType = stopType;
		this.requestStop = requestStop.equals("YES");
		this.stoppedAt = stoppedAt;
		this.passedThroughStopping = passedThroughStopping;
		this.passedThrough = passedThrough;
	}

	public String getId() { return id; }

	public String getName() { return name; }

	public String getStopType() { return stopType; }

	public boolean getRequestStop() { return requestStop; }

	public String getStoppedAt() { return stoppedAt; }

	public String getPassedThroughStopping() { return passedThroughStopping; }

	public String getPassedThrough() { return passedThrough; }

	public void setId(String id) { this.id = id; }

	public void setName(String name) { this.name = name; }

	public void setStopType(String stopType) { this.stopType = stopType; }

	public void setRequestStop(boolean requestStop) { this.requestStop = requestStop; }

	public void setStoppedAt(String stoppedAt) { this.stoppedAt = stoppedAt; }

	public void setPassedThroughStopping(String passedThroughStopping) { this.passedThroughStopping = passedThroughStopping; }

	public void setPassedThrough(String passedThrough) { this.passedThrough = passedThrough; }

	public String stationInfo() {
		return "ID: " + id + ", name: " + name + ", Is Request Stop: " + (requestStop ? "Yes": "No") + ", Stopped At: " + stoppedAt + ", Passed Through (Stopping): " + passedThroughStopping + ", Passed Through (Not Stopping): " + passedThrough + "\n";
	}

	@Override
	public String toString() {
		return id + "  " + name + "  " + stopType + "  " + requestStop + "  " + stoppedAt + "  " + passedThroughStopping + "  " + passedThrough;
	}
}
