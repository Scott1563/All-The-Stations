public class Main {

	public static void main(String[] args) {

		Stations list = Stations.getStationsInstance();
        Start self = new Start(list);
        self.setUp(self);
    }
}
