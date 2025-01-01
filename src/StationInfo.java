import java.text.DecimalFormat;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StationInfo extends JDialog {

	public JPanel StationInfoMainPanel;
	private JLabel StationIcon;
	private JLabel StationName;
	private JTextField IDField;
	private JTextField NameField;
	private JComboBox<String> CountryField;
	private JComboBox<String> AreaField;
	private JComboBox<String> TOCField;
	private JCheckBox RequestStop;
	private JTextField NumPlatformField;
	private JTextField PlatformListField;
	private JTextField ExploredField;
	private JTextField StoppedAtField;
	private JTextField PassedStoppingField;
	private JTextField PassedNotStoppingField;
	private JTextField VisitedField;
	private JButton PlatformStats;
	private JButton ExitButton;
	private JButton EditButton;
	private JButton NewButton;
	private final Station station;

	public StationInfo(Start start, Station station, Stations list, ArrayList<String> countryList, ArrayList<String> areaList, ArrayList<String> operatorList, boolean random) {

		super(start, "Station Info", true);
		this.station = station;

		// Country/Area & TOC
		dropDownFiller(CountryField, countryList, "country");
		dropDownFiller(AreaField, areaList, "area");
		dropDownFiller(TOCField, operatorList, "TOC");

		fieldUpdate();

		if (random) {
			EditButton.setVisible(false);
			NewButton.setVisible(false);
			IDField.setEditable(false);
			NameField.setEditable(false);
			CountryField.setEditable(false);
			AreaField.setEditable(false);
			TOCField.setEditable(false);
			NumPlatformField.setEditable(false);
			PlatformListField.setEditable(false);
			ExploredField.setEditable(false);
			StoppedAtField.setEditable(false);
			PassedStoppingField.setEditable(false);
			PassedNotStoppingField.setEditable(false);
			VisitedField.setEditable(false);
		}

		CountryField.addActionListener(e -> {
		    String selectedCountry = (String) CountryField.getSelectedItem();

			if (selectedCountry != null) {
				if (selectedCountry.equals("Add new country")) {
					String newCountry = showCreatorPopUp("country");
					if (!newCountry.equals("NULL")) {
						countryList.add(newCountry);
						Collections.sort(countryList);
						station.setCountry(newCountry);
						CountryField.removeAllItems();
						dropDownFiller(CountryField, countryList, "country");
					}
					CountryField.setSelectedItem(station.getCountry());
				} else if (!selectedCountry.equals(station.getCountry())) {
					station.setCountry(selectedCountry);
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
						station.setArea(newArea);
						AreaField.removeAllItems();
						dropDownFiller(AreaField, areaList, "area");
					}
					AreaField.setSelectedItem(station.getArea());
				} else if (!selectedArea.equals(station.getArea())) {
					station.setArea(selectedArea);
				}
			}
		});

		TOCField.addActionListener(e -> {
			String SelectedTOC = (String) TOCField.getSelectedItem();

			if (SelectedTOC != null) {
				if (SelectedTOC.equals("Add new TOC")) {
					String newTOC = showCreatorPopUp("TOC");
					if (!newTOC.equals("NULL")) {
						operatorList.add(newTOC);
						Collections.sort(operatorList);
						station.setManager(newTOC);
						TOCField.removeAllItems();
						dropDownFiller(TOCField, operatorList, "TOC");
					}
					TOCField.setSelectedItem(station.getManager());
				} else if (!SelectedTOC.equals(station.getManager())) {
					station.setManager(SelectedTOC);
				}
			}
		});

		// Request Stop
		RequestStop.addActionListener(e -> station.setRequestStop(RequestStop.isSelected()));

		// Name & ID
		NameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {if (e.getKeyChar() == '\n') { nameChecker(); }
            }
        });

		IDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { idChecker(list); }
            }
        });

		// Date Fields
		ExploredField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { dateChecker(list, ExploredField, "E"); } }
        });

		StoppedAtField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { dateChecker(list, StoppedAtField, "S");} }
        });

		PassedStoppingField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { dateChecker(list, PassedStoppingField, "PS"); } }
        });

		PassedNotStoppingField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { dateChecker(list, PassedNotStoppingField, "P"); } }
        });

		VisitedField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { dateChecker(list, VisitedField, "V"); } }
        });

		// Platform Fields
		NumPlatformField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { platformAmountChecker(list); } }
        });

		PlatformListField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) { if (e.getKeyChar() == '\n') { platformListChecker(); } }});

		PlatformStats.addActionListener(e -> {

			final ArrayList<String> Plist = station.getPlatformsVisitedList();
			double percentage = ((double) Plist.size() / station.getNumberOfPlatforms()) * 100;
			String formattedPercentage = new DecimalFormat("0.00").format(percentage);

			String msg = station.getName() + " Has " + station.getNumberOfPlatforms() + (station.getNumberOfPlatforms() == 1 ? " platform," : " platforms,") + " You have visited " + Plist.size() + (Plist.size() == 1 ? " platform," : " platforms,");
			msg += "\nThat's " + formattedPercentage + "% " + (percentage == 100.0 ? "Well Done :)" : "Keep Going");

			JOptionPane.showMessageDialog(StationInfo.this, msg, "Platform Info", JOptionPane.INFORMATION_MESSAGE);
		});

		// Other
		ExitButton.addActionListener(e -> {

			boolean failed = false;

			if (!random) {
				if (showCreatorConfirmation(null, "Save", "Would you like to save the changes made to this station?")) {
					failed = save(list);
				}
			}

			if (!failed) {
				start.setEditMore(false);
				start.setNew(false);
				closeWindow();
			}
		});

		EditButton.addActionListener(e -> {

			boolean failed = false;

			if (showCreatorConfirmation(null, "Save", "Would you like to save the changes made to this station?")) {
				failed = save(list);
			}
			if (!failed) {
				start.setEditMore(true);
				start.setNew(false);
				closeWindow();
			}
		});

		NewButton.addActionListener(e -> {

			boolean failed = false;

			if (showCreatorConfirmation(null, "Save", "Would you like to save the changes made to this station?")) {
				failed = save(list);
			}
			if (!failed) {
				start.setEditMore(false);
				start.setNew(true);
				closeWindow();
			}
		});
	}

	private boolean idChecker(Stations list) {

		boolean valid = false;

		if (!IDField.getText().equals(station.getId())) {
			if (IDField.getText().length() == 3) {
				if (list.findStationByID(IDField.getText()).isEmpty() || IDField.getText().equals("N/A")) {
					station.setId(IDField.getText());
					fieldUpdate();
					valid = true;
				} else {
					JOptionPane.showMessageDialog(StationInfo.this, "The ID entered already exists", "Error!", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(StationInfo.this, "The ID entered must be 3 characters", "Error!", JOptionPane.ERROR_MESSAGE);
			}
			IDField.setText(station.getId());
		}
		return valid;
	}

	private boolean nameChecker() {

		if (!NameField.getText().isEmpty()) {
			station.setName(NameField.getText());
			fieldUpdate();
			return true;
		}
		NameField.setText(station.getName());
		JOptionPane.showMessageDialog(StationInfo.this, "The station must have a name", "Error!", JOptionPane.ERROR_MESSAGE);
		return false;
	}

	private boolean platformListChecker() {

		boolean valid = false;

		if (!PlatformListField.getText().isEmpty()) {
			String[] unEditPlatforms = PlatformListField.getText().split(",");
			String[] platforms = new String[unEditPlatforms.length];
			int i = 0;

			for (String p : unEditPlatforms) {
				platforms[i] = p.trim();
				i++;
			}
			for (String p : platforms) {
				if (!station.getPlatformsVisitedList().contains(p)) {
					station.addPlatform(p);
				}
			}
			fieldUpdate();
			if (!station.platformList().equals("NULL") && !station.platformList().equals("N/A")) {
				PlatformStats.setVisible(true);
			}
			valid = true;
		} else {
			PlatformListField.setText(station.platformList());
			JOptionPane.showMessageDialog(StationInfo.this, "The platforms list can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
		}
		return valid;
	}

	private boolean platformAmountChecker(Stations list) {

		if (!NumPlatformField.getText().isEmpty()) {
			boolean numerical;
			try {
                Double.parseDouble(NumPlatformField.getText());
			    numerical = true;
			} catch(NumberFormatException e){
			    numerical = false;
			}
			if (numerical) {
				if (Integer.parseInt(NumPlatformField.getText()) > 0) {
					list.updateNumOfPlatforms(station, Integer.parseInt(NumPlatformField.getText()));
					fieldUpdate();
					return true;
				} else {
					JOptionPane.showMessageDialog(StationInfo.this, "The number of platforms can't be less than 1", "Error!", JOptionPane.ERROR_MESSAGE);
				}
			} else {
				JOptionPane.showMessageDialog(StationInfo.this, "The number of platforms must be a number", "Error!", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(StationInfo.this, "The number of platforms can't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
		}
		NumPlatformField.setText(String.valueOf(station.getNumberOfPlatforms()));
		return false;
	}

	private boolean dateChecker(Stations list, JTextField field, String dateType) {

		boolean valid = false;

		if (!field.getText().isEmpty()) {
			switch (dateType) {
				case "E"  -> list.updateExplored(station, field.getText());
				case "S"  -> list.updateStoppedAt(station, field.getText());
				case "PS" -> list.updatePassedThroughStopping(station, field.getText());
				case "P"  -> list.updatePassedThrough(station, field.getText());
				case "V"  -> list.updateVisited(station, field.getText());
			}
			fieldUpdate();
			valid = true;
		} else {
			switch (dateType) {
				case "E"  -> field.setText(station.getExplored());
				case "S"  -> field.setText(station.getStoppedAt());
				case "PS" -> field.setText(station.getPassedThroughStopping());
				case "P"  -> field.setText(station.getPassedThrough());
				case "V"  -> field.setText(station.getVisited());
			}
			JOptionPane.showMessageDialog(StationInfo.this, "The date shouldn't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
		}
		return valid;
	}

	private boolean save(Stations list) {

		boolean failed = false;
		String[] fields = new String[9];
		fields[0] = IDField.getText();
		fields[1] = NameField.getText();
		fields[2] = PlatformListField.getText();
		fields[3] = NumPlatformField.getText();
		fields[4] = ExploredField.getText();
		fields[5] = StoppedAtField.getText();
		fields[6] = PassedStoppingField.getText();
		fields[7] = PassedNotStoppingField.getText();
		fields[8] = VisitedField.getText();

		IDField.setText(fields[0]);
		if (!IDField.getText().equals(station.getId())) {
			if (!idChecker(list)) {
				failed = true;
			}
		}

		NameField.setText(fields[1]);
		if (!failed && !NameField.getText().equals(station.getName())) {
			if (!nameChecker()) {
				failed = true;
			}
		}

		PlatformListField.setText(fields[2]);
		if (!failed && !platformListChecker()) {
			failed = true;
		}

		NumPlatformField.setText(fields[3]);
		if (!failed && !platformAmountChecker(list)) {
			failed = true;
		}

		ExploredField.setText(fields[4]);
		if (!failed && !(ExploredField.getText().equals(station.getExplored()) || fields[4].equals("NULL"))) {
			if (!dateChecker(list, ExploredField, "E")) {
				failed = true;
			}
		}

		StoppedAtField.setText(fields[5]);
		if (!failed && !(StoppedAtField.getText().equals(station.getStoppedAt()) || fields[5].equals("NULL"))) {
			if (!dateChecker(list, StoppedAtField, "S")) {
				failed = true;
			}
		}

		PassedStoppingField.setText(fields[6]);
		if (!failed && !(PassedStoppingField.getText().equals(station.getPassedThroughStopping()) || fields[6].equals("NULL"))) {
			if (!dateChecker(list, PassedStoppingField, "PS")) {
				failed = true;
			}
		}

		PassedNotStoppingField.setText(fields[7]);
		if (!failed && !(PassedNotStoppingField.getText().equals(station.getPassedThrough()) || fields[7].equals("NULL"))) {
			if (!dateChecker(list, PassedNotStoppingField, "P")) {
				failed = true;
			}
		}

		VisitedField.setText(fields[8]);
		if (!failed && !(VisitedField.getText().equals(station.getVisited()) || fields[8].equals("NULL"))) {
			if (!dateChecker(list, VisitedField, "V")) {
				failed = true;
			}
		}

		return failed;
	}

	private void dropDownFiller(JComboBox<String> dropDown, ArrayList<String> contents, String type) {

		for (String content : contents) {
            dropDown.addItem(content);
        }
		dropDown.addItem("Add new " + type);
	}

	private String showCreatorPopUp(String option) {

        JDialog firstDialog = new JDialog(this, "New " + option + " creator", true);
        firstDialog.setSize(300, 150);
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
            confirmed[0] = showCreatorConfirmation(firstDialog, ("New " + option + " creator"), ("Is " + inputField.getText() + " the correct " + option + "?"));
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

    private boolean showCreatorConfirmation(JDialog firstDialog, String title, String question) {

	    final boolean[] confirmed = new boolean[1];
	    JDialog confirmationDialog = new JDialog(this, title, true);
	    confirmationDialog.setSize(500, 150);
	    confirmationDialog.setLayout(new GridBagLayout());

	    // Components for the confirmation dialog
	    JLabel confirmationLabel = new JLabel(question);
	    JButton yesButton = new JButton("Yes");
	    JButton noButton = new JButton("No");

	    // GridBagLayout setup
	    GridBagConstraints gbc = new GridBagConstraints();
	    gbc.insets = new Insets(5, 5, 5, 5);
	    gbc.fill = GridBagConstraints.HORIZONTAL;

	    // Add the confirmation label
	    gbc.gridx = 0;
	    gbc.gridy = 0;
	    gbc.gridwidth = 2; // Span two columns
	    gbc.anchor = GridBagConstraints.CENTER;
	    confirmationDialog.add(confirmationLabel, gbc);

	    // Create a panel to hold the buttons
	    JPanel buttonPanel = new JPanel();
	    buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0)); // Center with 10px gap

	    // Add buttons to the panel
	    buttonPanel.add(yesButton);
	    buttonPanel.add(noButton);

	    // Add the button panel to the dialog
	    gbc.gridx = 0;
	    gbc.gridy = 1;
	    gbc.gridwidth = 2; // Span two columns
	    gbc.anchor = GridBagConstraints.CENTER;
	    confirmationDialog.add(buttonPanel, gbc);

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
	        if (firstDialog != null) {
	            firstDialog.setVisible(true);
	        }
	        confirmed[0] = false;
	    });

	    // Center the dialog on the screen and make it visible
	    confirmationDialog.setLocationRelativeTo(this);
	    confirmationDialog.setVisible(true);

	    return confirmed[0];
	}

	private void fieldUpdate() {

		// Sets fields on form
		StationName.setText(station.getName() + " (" + station.getId() + ")");
		NameField.setText(station.getName());
		IDField.setText(station.getId());
		CountryField.setSelectedItem(station.getCountry());
		AreaField.setSelectedItem(station.getArea());
		TOCField.setSelectedItem(station.getManager());
		RequestStop.setSelected(station.getRequestStop());
		NumPlatformField.setText(String.valueOf(station.getNumberOfPlatforms()));
		PlatformListField.setText(station.platformList());
		if (station.platformList().equals("NULL") || station.platformList().equals("N/A")) {
			PlatformStats.setVisible(false);
		}
		ExploredField.setText(station.getExplored());
		StoppedAtField.setText(station.getStoppedAt());
		PassedStoppingField.setText(station.getPassedThroughStopping());
		PassedNotStoppingField.setText(station.getPassedThrough());
		VisitedField.setText(station.getVisited());
		ImageIcon icon = null;
		String url = "Images/";

		switch (station.getStopType()) {
			case "Train" -> url += "Train";
			case "Tram" -> url += "Tram";
			case "Underground" -> url += "Underground";
			case "Heritage" -> url += "Heritage";
			default -> url += "Other";
		}

		// Updates Icon depending on station status
		if (!station.getStoppedAt().equals("NULL")) {
			if (!station.getStoppedAt().equals("N/A")) {
				icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(url + "Visited.png")));
			} else if (station.getStoppedAt().equals("N/A") && (!station.getPassedThroughStopping().equals("N/A"))) {
				icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(url + "Stopped.png")));
			} else if (station.getStoppedAt().equals("N/A") && station.getPassedThroughStopping().equals("N/A") && (!station.getPassedThrough().equals("N/A"))) {
				icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(url + "Skipped.png")));
			} else if (station.getStoppedAt().equals("N/A") && station.getPassedThroughStopping().equals("N/A") && (station.getPassedThrough().equals("N/A") && !station.getVisited().equals("N/A"))) {
				icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(url + "VisitedFoot.png")));
			}

			StationIcon.setIcon(icon);
		}
	}

	public void closeWindow() { this.dispose(); }
}
