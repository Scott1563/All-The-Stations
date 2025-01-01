import javax.swing.*;
import java.awt.event.*;
import java.util.*;

public class Start extends JFrame {

    private JPanel StartPanel;
    private JButton EditStationButton;
	private JButton NewStationButton;
	private JButton ShowStatsButton;
	private JButton DeleteButton;
	private JButton RandomStationButton;

	private final Stations stationList;
    private Station selectedStation;
    private final ArrayList<String> countryList = new ArrayList<>();
	private final ArrayList<String> areaList = new ArrayList<>();
	private final ArrayList<String> operatorList = new ArrayList<>();
	private final ArrayList<String> stopTypes = new ArrayList<>();
	private final ArrayList<ArrayList<String>> countryAreaLink = new ArrayList<>();
    private Start self;
	private boolean editMore = false;
	private boolean canceled;
	private boolean createNew = false;

    public Start(Stations stationList) {

		this.stationList = stationList;
		stopTypes.add("Train");
		stopTypes.add("Tram");
		stopTypes.add("Underground");
		stopTypes.add("Heritage");
		arraySetUp();

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
			        selectStation(false);
					editStation(false);
	            } while (getEditMore());

				if (getNew()) {
					do {
						newStation();
						if (isCanceled()) {
							setNew(false);
						} else {
							editStation(false);
						}
					} while (getNew());
				}

				if (!getEditMore() && !getNew()) {
					exit = true;
				}
			} while (!exit);
			areaList.clear();
			countryAreaLink.clear();
			arraySetUp();
        });

		NewStationButton.addActionListener(e -> {

			boolean exit = false;

			do {
				do {
					newStation();
					if (!isCanceled()) {
						editStation(false);
					} else {
						setNew(false);
					}
				} while (getNew());

				if (getEditMore()) {
					do {
			            selectStation(false);
						editStation(false);
					} while (getEditMore());
				}
				if (!getEditMore() && !getNew()) {
					exit = true;
				}
			} while (!exit);
			areaList.clear();
			countryAreaLink.clear();
			arraySetUp();
        });

		ShowStatsButton.addActionListener(e -> statsShower());

		DeleteButton.addActionListener(e -> selectStation(true));

		RandomStationButton.addActionListener(e -> {
			randomStation();
			if (!isCanceled()) {
				editStation(true);
			}
		});
    }

    public void setUp(Start self) {
        this.self = self;
        self.setContentPane(self.StartPanel);
        self.setTitle("All the Stations");
        self.setSize(500, 500);
        self.setVisible(true);
        self.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    }

    private void arraySetUp() {

        for (Station station : stationList.getStationList()) {
			if (!(countryList.contains(station.getCountry()) || station.getCountry().isEmpty())) {
				countryList.add(station.getCountry());
			}

			if (!(areaList.contains(station.getArea()) || station.getArea().isEmpty())) {
				areaList.add(station.getArea());
				ArrayList<String> tempArraylist = new ArrayList<>();
				tempArraylist.add(station.getArea());
				tempArraylist.add(station.getCountry());
				countryAreaLink.add(tempArraylist);
			}

			if (!(operatorList.contains(station.getManager()) || station.getManager().isEmpty())) {
				operatorList.add(station.getManager());
			}

			if (!(stopTypes.contains(station.getStopType()) || station.getStopType().isEmpty())) {
				stopTypes.add(station.getStopType());
			}
		}
		Collections.sort(countryList);
		Collections.sort(areaList);
		Collections.sort(operatorList);
		Collections.sort(stopTypes);
    }

	public void newStation() {

		StationCreator creation = new StationCreator(self, stationList, countryList, areaList, operatorList, stopTypes);
		creation.setContentPane(creation.StationCreatorMainPanel);
		creation.setTitle("Station Creator");
		creation.setSize(600, 400);
		creation.setVisible(true);
		creation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void editStation(boolean random) {

		StationInfo stationDisplay = new StationInfo(self, getSelectedStation(), stationList, countryList, areaList, operatorList, random);
		stationDisplay.setContentPane(stationDisplay.StationInfoMainPanel);
		stationDisplay.setTitle("Station Info");
		stationDisplay.setSize(875, 600);
		stationDisplay.setVisible(true);
		stationDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	public void selectStation(Boolean delete) {

		self.setVisible(false);
		StationSelect selection = new StationSelect(self, stationList, delete);
		selection.setContentPane(selection.StationSelectPanel);
		selection.setTitle("Station Selection");
		selection.setSize(600, 400);
		selection.setVisible(true);
		selection.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		self.setVisible(true);
	}

	public void statsShower() {

		self.setVisible(false);
		StatsOptionSelector stats = new StatsOptionSelector(self, stationList, countryList, areaList, countryAreaLink, false);
		stats.setSelf(stats);
		stats.setContentPane(stats.StatsOptionSelectMainPanel);
		stats.setTitle("Stats Option Selection");
		stats.setSize(550, 150);
		stats.setVisible(true);
		stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		self.setVisible(true);
	}

	public void randomStation() {

		self.setVisible(false);
		StatsOptionSelector stats = new StatsOptionSelector(self, stationList, countryList, areaList, countryAreaLink, true);
		stats.setSelf(stats);
		stats.setContentPane(stats.StatsOptionSelectMainPanel);
		stats.setTitle("Random Option Selection");
		stats.setSize(550, 150);
		stats.setVisible(true);
		stats.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		self.setVisible(true);
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
