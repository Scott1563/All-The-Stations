import javax.swing.*;
import java.util.*;

public class StatsOptionSelector extends JDialog {

	public JPanel StatsOptionSelectMainPanel;
	private JComboBox<String> CountrySelectField;
	private JLabel AreaLabel;
	private JComboBox<String> AreaSelectField;
	private JButton EnterButton;
	private JButton CancelButton;
	private JButton AreaComparisonButton;

	private StatsOptionSelector self;

	public StatsOptionSelector(Start start, Stations stationList, ArrayList<String> countryList, ArrayList<String> areaList, ArrayList<ArrayList<String>> countryAreaLink, Boolean random) {

		super(start, "Stats Option Selection", true);

		AreaSelectField.setVisible(false);
		AreaLabel.setVisible(false);
		dropDownFiller(CountrySelectField, countryList, null, "World", null);
		if (random) {
			AreaComparisonButton.setVisible(false);
		}


		CountrySelectField.addActionListener(e -> {
		    String selectedCountry = (String) CountrySelectField.getSelectedItem();

			if (selectedCountry != null) {
				if (selectedCountry.equals("World")) {
					AreaSelectField.setVisible(false);
					AreaLabel.setVisible(false);
				} else {
					dropDownFiller(AreaSelectField, areaList, countryAreaLink, "All Areas", selectedCountry);
					AreaSelectField.setVisible(true);
					AreaLabel.setVisible(true);
				}
			}
		});

		CancelButton.addActionListener(e -> {
			start.setCanceled(true);
			closeWindow();
		});

		EnterButton.addActionListener(e -> {

			String selectedCountry = (String) CountrySelectField.getSelectedItem();
			String selectedArea = "NULL";

			assert selectedCountry != null;
			if (!selectedCountry.equals("World")) {
				selectedArea = (String) AreaSelectField.getSelectedItem();
			}

			if (random) {
				ArrayList<Station> stationOptions = new ArrayList<>();

				for (Station station : stationList.getStationList()) {
					if (station.getExplored().equals("N/A") && ((station.getArea().equals(selectedArea) || Objects.equals(selectedArea, "All Areas")) && station.getCountry().equals(selectedCountry)) || selectedCountry.equals("World")) {
						stationOptions.add(station);
					}
				}

				if (!stationOptions.isEmpty()) {
					Random rand = new Random();
					Station selectedStation = stationOptions.get(rand.nextInt(stationOptions.size()));

					if (selectedStation != null) {
						start.setSelectedStation(selectedStation);
						start.setCanceled(false);
						closeWindow();
					}
				} else {
		            JOptionPane.showMessageDialog(this, "There is no unexplored stations for your selection!");
				}

			} else {
				self.setVisible(false);
				StatsShower show = new StatsShower(self, stationList, selectedCountry, selectedArea);
				show.setContentPane(show.StatsShowerMainPanel);
				show.setTitle("Area Stats Display");
				show.setSize(1200, 600);
				show.setVisible(true);
				show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				self.setVisible(true);
				start.setCanceled(false);
			}
		});

		AreaComparisonButton.addActionListener(e -> {

			self.setVisible(false);
			AreaStats show = new AreaStats(self, stationList);
			show.setContentPane(show.AreaStatsMainPanel);
			show.setTitle("Area Comparison Display");
			show.setSize(1200, 600);
			show.setVisible(true);
			show.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			self.setVisible(true);
			start.setCanceled(false);
		});
	}

	private void dropDownFiller(JComboBox<String> dropDown, ArrayList<String> contents, ArrayList<ArrayList<String>> links, String additionalOption, String filter) {

		AreaSelectField.removeAllItems();
		dropDown.addItem(additionalOption);
		if (dropDown == AreaSelectField) {
			ArrayList<String> areas = new ArrayList<>();
			for (ArrayList<String> link : links) {
				if (link.get(1).equals(filter)) {
					areas.add(link.get(0));
				}
            }
			Collections.sort(areas);
			for (String area : areas) {
                dropDown.addItem(area);
			}
		} else {
			for (String content : contents) {
                dropDown.addItem(content);
            }
		}
	}

	public void setSelf(StatsOptionSelector self) { this.self = self; }

	public void closeWindow() { this.dispose(); }
}
