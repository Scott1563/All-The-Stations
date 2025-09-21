import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StationCreator extends JDialog {

	public JPanel StationCreatorMainPanel;
	private JComboBox<String> StationType;
	private JTextField IDField;
	private JTextField NameField;
	private JComboBox<String> CountryField;
	private JComboBox<String> AreaField;
	private JComboBox<String> TOCField;
	private JCheckBox RequestStop;
	private JButton CancelButton;
	private JButton SubmitButton;

	private String stopType = null;
	private String ID;
	private String name;
	private String country;
	private String countryID;
	private String area;
	private final ArrayList<ArrayList<String>> countryAreaLink;
	private String toc;
	private String tocID;
	private boolean requestStop;


	public StationCreator(Start start, Stations list, ArrayList<String> countryList, ArrayList<String> areaList, ArrayList<ArrayList<String>> countryAreaLink, ArrayList<ArrayList<String>> countryIDLink, ArrayList<ArrayList<String>> tocIDLink, ArrayList<String> operatorList, ArrayList<String> stationTypeList) {

		super(start, "Station Creator", true);

		// Country, Area, TOC & Stop Type
		dropDownFiller(StationType, stationTypeList, "type", stopType);
		this.countryAreaLink = countryAreaLink;

		CountryField.addActionListener(e -> {
			String selectedCountry = (String) CountryField.getSelectedItem();

			if (selectedCountry != null) {
				if (selectedCountry.equals("Add new country")) {
					String newCountry = showCreatorPopUp("country");
					if (!newCountry.equals("NULL")) {
						String newCountryID = showCreatorIDPopUp("country", newCountry);
						if (!newCountryID.equals("NULL")) {
							countryList.add(newCountry);
							Collections.sort(countryList);
							country = newCountry;
							countryID = newCountryID;
							start.addNewCountryIDLink(newCountry, newCountryID);
							CountryField.removeAllItems();
							dropDownFiller(CountryField, countryList, "country", country);
							CountryField.setSelectedItem(country);
							dropDownFiller(AreaField, areaList, "area", area);
						} else {
							CountryField.setSelectedItem("Select station country");
						}
					} else {
						CountryField.setSelectedItem("Select station country");
					}
				} else if (!selectedCountry.equals("Select station country")) {
					if (country == null) {
						CountryField.removeItemAt(0);
					}
					country = selectedCountry;
					for (ArrayList<String> link : countryIDLink) {
						if (link.get(0).equals(country)) {
							countryID = link.get(1);
						}
					}
					dropDownFiller(AreaField, areaList, "area", area);
				}
			}
		});

		AreaField.addActionListener(e -> {
			String selectedArea = (String) AreaField.getSelectedItem();

			if (selectedArea != null) {
				if (selectedArea.equals("Add new area")) {
					String newArea = showCreatorPopUp("area");
					if (!newArea.equals("NULL")) {
						areaList.add(newArea);
						Collections.sort(areaList);
						area = newArea;
						ArrayList<String> tempArraylist = new ArrayList<>();
						tempArraylist.add(area);
						tempArraylist.add(country);
						countryAreaLink.add(tempArraylist);
						AreaField.removeAllItems();
						dropDownFiller(AreaField, areaList, "area", area);
						AreaField.setSelectedItem(area);
					} else {
						AreaField.setSelectedItem("Select station area");
					}
				} else if (!selectedArea.equals("Select station area")) {
					if (area == null) {
						AreaField.removeItemAt(0);
					}
					area = selectedArea;
				}
			}
		});

		TOCField.addActionListener(e -> {
			String selectedTOC = (String) TOCField.getSelectedItem();

			if (selectedTOC != null) {
				if (selectedTOC.equals("Add new TOC")) {
					String newTOC = showCreatorPopUp("TOC");
					if (!newTOC.equals("NULL")) {
						if (!stopType.equals("Train")) {
							String newTOCID = showCreatorIDPopUp("TOC", newTOC);
							if (!newTOCID.equals("NULL")) {
								operatorList.add(newTOC);
								Collections.sort(operatorList);
								toc = newTOC;
								tocID = newTOCID;
								start.addNewTOCIDLink(newTOC, newTOCID);
								TOCField.removeAllItems();
								dropDownFiller(TOCField, operatorList, "TOC", toc);
								TOCField.setSelectedItem(toc);
							}
						} else {
							operatorList.add(newTOC);
							Collections.sort(operatorList);
							toc = newTOC;
							TOCField.removeAllItems();
							dropDownFiller(TOCField, operatorList, "TOC", toc);
							TOCField.setSelectedItem(toc);
						}
					} else {
						TOCField.setSelectedItem("Select station TOC");
					}
				} else if (!selectedTOC.equals("Select station TOC")) {
					if (toc == null) {
						TOCField.removeItemAt(0);
					}
					toc = selectedTOC;
					if (!stopType.equals("Train")) {
						for (ArrayList<String> link : tocIDLink) {
							if (link.get(0).equals(toc)) {
								tocID = link.get(1);
							}
						}
					}
				}
			}
		});

		StationType.addActionListener(e -> {
			String selectedType = (String) StationType.getSelectedItem();

			if (selectedType != null) {
				if (selectedType.equals("Add new type")) {
					String newStationType = showCreatorPopUp("station type");
					if (!newStationType.equals("NULL")) {
						stationTypeList.add(newStationType);
						Collections.sort(stationTypeList);
						stopType = newStationType;
						StationType.removeAllItems();
						dropDownFiller(StationType, stationTypeList, "type", stopType);
						StationType.setSelectedItem(stopType);
					} else {
						StationType.setSelectedItem("Select station type");
					}
				} else if (!selectedType.equals("Select station type")) {
					if (stopType == null) {
						StationType.removeItemAt(0);
					}
					stopType = selectedType;
					dropDownFiller(CountryField, countryList, "country", country);
					dropDownFiller(TOCField, operatorList, "TOC", toc);
				}
			}
		});

		// Request Stop
		RequestStop.addActionListener(e -> requestStop = RequestStop.isSelected());

		// Name & ID
		NameField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyChar() == '\n') { nameChecker(); System.out.println(name);}
			}
		});

		// Old enter key or ID check no longer required
//		IDField.addKeyListener(new KeyAdapter() {
//			@Override
//			public void keyTyped(KeyEvent e) {
//				if (e.getKeyChar() == '\n') { idChecker(list); }
//			}
//		});

		// Buttons
		CancelButton.addActionListener(e -> {start.setCanceled(true); closeWindow();});

		SubmitButton.addActionListener(e -> {

			boolean failed = false;

			// Double check Country, Area, TOC & Station Type Data
			String selectedCountry = (String) CountryField.getSelectedItem();
			String selectedArea = (String) AreaField.getSelectedItem();
			String selectedTOC = (String) TOCField.getSelectedItem();
			String selectedStationType = (String) StationType.getSelectedItem();

			assert selectedCountry != null;
			if (selectedCountry.equals("Select station country")) {
				failed = true;
				JOptionPane.showMessageDialog(StationCreator.this, "Please enter a country", "Error!", JOptionPane.ERROR_MESSAGE);
			}

			assert selectedArea != null;
			if (!failed && selectedArea.equals("Select station area")) {
				failed = true;
				JOptionPane.showMessageDialog(StationCreator.this, "Please enter an area", "Error!", JOptionPane.ERROR_MESSAGE);
			}

			assert selectedTOC != null;
			if (!failed && selectedTOC.equals("Select station TOC")) {
				failed = true;
				JOptionPane.showMessageDialog(StationCreator.this, "Please enter a TOC", "Error!", JOptionPane.ERROR_MESSAGE);
			}

			assert selectedStationType != null;
			if (!failed && selectedTOC.equals("Select station type")) {
				failed = true;
				JOptionPane.showMessageDialog(StationCreator.this, "Please enter the station type", "Error!", JOptionPane.ERROR_MESSAGE);
			}

			// Double Check Name & ID Data
			if (!failed && (!idChecker(list) || !nameChecker())) {
				failed = true;
			}

			// Grab Request Stop Data (Necessary if button is never touched)
			requestStop = RequestStop.isSelected();

			if (!failed) {
				// Submit station
				start.setCanceled(false);
				Station createdStation = new Station(ID, name, stopType, (country + ". " + area), toc, (requestStop ? "YES" : "NO"), 0, "NULL");
				list.addStation(createdStation);
				start.setSelectedStation(createdStation);
				closeWindow();
			}
		});
	}

	private boolean idChecker(Stations list) {

		boolean valid = false;

		if (IDField.getText().trim().length() == 3) {
			String stationID = IDField.getText().trim();
			if (stopType.equals("Train")) {
				stationID = countryID + stationID;
			} else {
				stationID = tocID + stationID;
			}
			if (list.findStationByID(stationID).isEmpty()) {
				ID = stationID.toUpperCase();
				valid = true;
			} else {
				JOptionPane.showMessageDialog(StationCreator.this, "The ID entered already exists", "Error!", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(StationCreator.this, "The ID entered must be 3 characters", "Error!", JOptionPane.ERROR_MESSAGE);
		}

		// Resets textbox
		if (!valid) {
			IDField.setText("");
		}
		return valid;
	}

	private boolean nameChecker() {

		if (!NameField.getText().isEmpty()) {
			name = NameField.getText();
			return true;
		} else {
			JOptionPane.showMessageDialog(StationCreator.this, "Name cannot be empty!", "Error!", JOptionPane.ERROR_MESSAGE);
			NameField.setText("");
			return false;
		}
	}

	private String showCreatorPopUp(String option) {

		JDialog firstDialog = new JDialog(this, "New " + option + " creator", true);
		firstDialog.setSize(400, 150);
		firstDialog.setLayout(new GridBagLayout());

		// Form Components
		JLabel promptLabel = new JLabel("Enter new " + option + " here:");
		JTextField inputField = new JTextField(10);
		JButton enterButton = new JButton("Enter");
		JButton cancelButton = new JButton("Cancel");

		// Form Layout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		firstDialog.add(promptLabel, gbc);

		gbc.gridx = 1;
		firstDialog.add(inputField, gbc);

		// Create a panel to hold the buttons side by side
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.add(enterButton);
		buttonPanel.add(cancelButton);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		firstDialog.add(buttonPanel, gbc);

		// Action for "Enter" button
		final boolean[] confirmed = new boolean[1];
		enterButton.addActionListener(e -> {
			// Show the second dialog for confirmation
			confirmed[0] = showCreatorConfirmation(inputField.getText(), firstDialog, option);
			if (confirmed[0]) {
				firstDialog.dispose();
			}
		});

		// Action for "Cancel" button
		cancelButton.addActionListener(e -> {
			confirmed[0] = false;
			firstDialog.dispose();
		});

		// Center the dialog on the screen and make it visible
		firstDialog.setLocationRelativeTo(this);
		firstDialog.setVisible(true);

		return confirmed[0] ? inputField.getText() : "NULL";
	}

	private String showCreatorIDPopUp(String option, String answer) {

		JDialog firstDialog = new JDialog(this, "New " + option + " ID creator", true);
		firstDialog.setSize(400, 150);
		firstDialog.setLayout(new GridBagLayout());

		// Form Components
		JLabel promptLabel = new JLabel("Enter " + answer + "'s new ID here:");
		JTextField inputField = new JTextField(10);
		JButton enterButton = new JButton("Enter");
		JButton cancelButton = new JButton("Cancel");

		// Form Layout
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		firstDialog.add(promptLabel, gbc);

		gbc.gridx = 1;
		firstDialog.add(inputField, gbc);

		// Create a panel to hold the buttons side by side
		JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
		buttonPanel.add(enterButton);
		buttonPanel.add(cancelButton);

		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		firstDialog.add(buttonPanel, gbc);

		// Action for "Enter" button
		final boolean[] confirmed = new boolean[1];
		enterButton.addActionListener(e -> {
			// Show the second dialog for confirmation
			confirmed[0] = showCreatorConfirmation(inputField.getText(), firstDialog, (option + " ID"));
			if (confirmed[0]) {
				firstDialog.dispose();
			}
		});

		// Action for "Cancel" button
		cancelButton.addActionListener(e -> {
			confirmed[0] = false;
			firstDialog.dispose();
		});

		// Center the dialog on the screen and make it visible
		firstDialog.setLocationRelativeTo(this);
		firstDialog.setVisible(true);

		return confirmed[0] ? inputField.getText() : "NULL";
	}

	private boolean showCreatorConfirmation(String inputValue, JDialog firstDialog, String option) {

		final boolean[] confirmed = new boolean[1];
		JDialog confirmationDialog = new JDialog(this, "New " + option + " creator", true);
		confirmationDialog.setSize(500, 150);
		confirmationDialog.setLayout(new GridBagLayout());

		// Components for the confirmation dialog
		JLabel confirmationLabel = new JLabel("Is " + inputValue + " the correct " + option + " this cannot be changed later?");
		JButton yesButton = new JButton("Yes");
		JButton noButton = new JButton("No");

		// GridBagLayout setup
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.insets = new Insets(5, 5, 5, 5);

		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		confirmationDialog.add(confirmationLabel, gbc);

		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.anchor = GridBagConstraints.CENTER;
		confirmationDialog.add(yesButton, gbc);

		gbc.gridx = 1;
		confirmationDialog.add(noButton, gbc);

		// Action for "Yes" button
		yesButton.addActionListener(e -> {
			// Close both dialogs
			confirmed[0] = true;
			confirmationDialog.dispose();

		});

		// Action for "No" button
		noButton.addActionListener(e -> {
			// Close the confirmation dialog and reopen the first dialog
			confirmationDialog.dispose();
			firstDialog.setVisible(true);
			confirmed[0] = false;
		});

		// Center the dialog on the screen and make it visible
		confirmationDialog.setLocationRelativeTo(this);
		confirmationDialog.setVisible(true);

		return confirmed[0];
	}

	private void dropDownFiller(JComboBox<String> dropDown, ArrayList<String> contents, String type, String item) {

		if (type.equals("area")) {
			dropDown.removeAllItems();
		}

		if (item == null) {
			dropDown.addItem("Select station " + type);
		}

		if (type.equals("area")) {
			ArrayList<String> areas = new ArrayList<>();
			for (ArrayList<String> pair : countryAreaLink) {

				if (pair.get(1).equals(country)){
					areas.add(pair.get(0));
				}
			}
			Collections.sort(areas);
			for (String area : areas) {
				dropDown.addItem(area);
			}
			dropDown.addItem("Add new " + type);
		} else {
			for (String content : contents) {
				dropDown.addItem(content);
			}
			dropDown.addItem("Add new " + type);
		}

	}

	public void closeWindow() { this.dispose(); }
}

