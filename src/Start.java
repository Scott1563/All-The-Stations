import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Start extends JFrame {

    private JPanel StartPanel;
    private JButton EditStationButton;
	private JButton NewStationButton;
	private Stations stationList;
    private Station selectedStation;
    private final ArrayList<String> countryList = new ArrayList<>();
	private final ArrayList<String> areaList = new ArrayList<>();
	private final ArrayList<String> operatorList = new ArrayList<>();
    private Start self;
	private boolean editMore = false;
	private boolean canceled;
	private boolean createNew = false;

    public Start() {

		addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
				stationList.saveFile();
		        System.exit(0);
            }
        });

        EditStationButton.addActionListener(e -> {

			boolean exit = false;

			do {
				do {
			        selectStation();
					editStation();
	            } while (getEditMore());

				if (getNew()) {
					do {
						newStation();
						if (isCanceled()) {
							setNew(false);
						} else {
							editStation();
						}
					} while (getNew());
				}

				if (!getEditMore() && !getNew()) {
					exit = true;
				}
			} while (!exit);
        });

		NewStationButton.addActionListener(e -> {

			boolean exit = false;

			do {
				do {
					newStation();
					if (!isCanceled()) {
						editStation();
					} else {
						setNew(false);
					}
				} while (getNew());

				if (getEditMore()) {
					do {
			            selectStation();
						editStation();
					} while (getEditMore());
				}
				if (!getEditMore() && !getNew()) {
					exit = true;
				}
			} while (!exit);
        });
    }

    public void setUp(Start self) {
        this.self = self;
        self.setContentPane(self.StartPanel);
        self.setTitle("All the Stations");
        self.setSize(500, 500);
        self.setVisible(true);
        self.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        stationList = Stations.getStationsInstance();
        arraySetUp();
    }

    private void arraySetUp() {
        for (Station station : stationList.getStationList()) {
			if (!(countryList.contains(station.getCountry()) || station.getCountry().isEmpty())) {
				countryList.add(station.getCountry());
			}

			if (!(areaList.contains(station.getArea()) || station.getArea().isEmpty())) {
				areaList.add(station.getArea());
			}

			if (!(operatorList.contains(station.getManager()) || station.getManager().isEmpty())) {
				operatorList.add(station.getManager());
			}
		}
		Collections.sort(countryList);
		Collections.sort(areaList);
		Collections.sort(operatorList);
    }

    public static void main(String[] args) {
        Start self = new Start();
        self.setUp(self);
    }

	public void newStation() {

		StationCreator creation = new StationCreator(self, stationList, countryList, areaList, operatorList);
		creation.setContentPane(creation.StationCreatorMainPanel);
		creation.setTitle("Station Creator");
		creation.setSize(600, 400);
		creation.setVisible(true);
		creation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void editStation() {

		StationInfo stationDisplay = new StationInfo(self, getSelectedStation(), stationList, countryList, areaList, operatorList);
		stationDisplay.setContentPane(stationDisplay.StationInfoMainPanel);
		stationDisplay.setTitle("Station Info");
		stationDisplay.setSize(875, 600);
		stationDisplay.setVisible(true);
		stationDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void selectStation() {

		StationSelect selection = new StationSelect(self, stationList);
		selection.setContentPane(selection.StationSelectPanel);
		selection.setTitle("Station Selection");
		selection.setSize(600, 400);
		selection.setVisible(true);
		selection.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

    public Station getSelectedStation() {return selectedStation; }

    public void setSelectedStation(Station selectedStation) { this.selectedStation = selectedStation; }

	public boolean getEditMore() { return editMore; }

	public void setEditMore(boolean editMore) { this.editMore = editMore; }

	public boolean isCanceled() { return canceled; }

	public void setCanceled(boolean canceled) { this.canceled = canceled; }

	public void setNew(boolean createNew) { this.createNew = createNew; }

	public boolean getNew() { return createNew; }
}
