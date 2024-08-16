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
	private final Station station;

	public StationInfo(Start start, Station station, Stations list, ArrayList<String> countryList, ArrayList<String> areaList, ArrayList<String> operatorList) {

		super(start, "Station Info", true);
		this.station = station;

		// Country/Area & TOC
		dropDownFiller(CountryField, countryList, "country");
		dropDownFiller(AreaField, areaList, "area");
		dropDownFiller(TOCField, operatorList, "TOC");

		fieldUpdate();

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

		// Date & ID
		NameField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!NameField.getText().isEmpty()) {
						station.setName(NameField.getText());
						fieldUpdate();
					} else {
						NameField.setText(station.getName());
					}
                }
            }
        });

		IDField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!IDField.getText().equals(station.getId())) {
						if (IDField.getText().length() == 3) {
							if (list.findStationByID(IDField.getText()).isEmpty() || IDField.getText().equals("N/A")) {
								station.setId(IDField.getText());
								fieldUpdate();
							} else {
								JOptionPane.showMessageDialog(StationInfo.this, "The ID entered already exists", "Error!", JOptionPane.ERROR_MESSAGE);
							}
						} else {
							JOptionPane.showMessageDialog(StationInfo.this, "The ID entered must be 3 characters", "Error!", JOptionPane.ERROR_MESSAGE);
						}
						IDField.setText(station.getId());
					}
                }
            }
        });

		// Date Fields
		ExploredField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!ExploredField.getText().isEmpty()) {
						list.updateExplored(station, ExploredField.getText());
						fieldUpdate();
					} else {
						ExploredField.setText(station.getExplored());
						JOptionPane.showMessageDialog(StationInfo.this, "The date shouldn't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
					}
                }
            }
        });

		StoppedAtField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!StoppedAtField.getText().isEmpty()) {
						list.updateStoppedAt(station, StoppedAtField.getText());
						fieldUpdate();
					} else {
						StoppedAtField.setText(station.getStoppedAt());
						JOptionPane.showMessageDialog(StationInfo.this, "The date shouldn't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
					}
                }
            }
        });

		PassedStoppingField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!PassedStoppingField.getText().isEmpty()) {
						list.updatePassedThroughStopping(station, PassedStoppingField.getText());
						fieldUpdate();
					} else {
						PassedStoppingField.setText(station.getPassedThroughStopping());
						JOptionPane.showMessageDialog(StationInfo.this, "The date shouldn't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
					}
                }
            }
        });

		PassedNotStoppingField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!PassedNotStoppingField.getText().isEmpty()) {
						list.updatePassedThrough(station, PassedNotStoppingField.getText());
						fieldUpdate();
					} else {
						PassedNotStoppingField.setText(station.getPassedThrough());
						JOptionPane.showMessageDialog(StationInfo.this, "The date shouldn't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
					}
                }
            }
        });

		VisitedField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!VisitedField.getText().isEmpty()) {
						list.updateVisited(station, VisitedField.getText());
						fieldUpdate();
					} else {
						VisitedField.setText(station.getVisited());
						JOptionPane.showMessageDialog(StationInfo.this, "The date shouldn't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
					}
                }
            }
        });

		// Platform Fields
		NumPlatformField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					if (!NumPlatformField.getText().isEmpty()) {
						if (Integer.parseInt(NumPlatformField.getText()) > 0) {
							list.updateNumOfPlatforms(station, Integer.parseInt(NumPlatformField.getText()));
							fieldUpdate();
						} else {
							NumPlatformField.setText(String.valueOf(station.getNumberOfPlatforms()));
							JOptionPane.showMessageDialog(StationInfo.this, "The number of platforms can't be less than 1", "Error!", JOptionPane.ERROR_MESSAGE);
						}
					} else {
						NumPlatformField.setText(String.valueOf(station.getNumberOfPlatforms()));
						JOptionPane.showMessageDialog(StationInfo.this, "The number of platforms can't be nothing", "Error!", JOptionPane.ERROR_MESSAGE);
					}
                }
            }
        });

		PlatformListField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
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
					} else {
						PlatformListField.setText(station.platformList());
						JOptionPane.showMessageDialog(StationInfo.this, "The platforms list can't be empty", "Error!", JOptionPane.ERROR_MESSAGE);
					}
                }
            }
        });

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
			start.setEditMore(false);
			closeWindow();
		});

		EditButton.addActionListener(e -> {
			start.setEditMore(true);
			closeWindow();
		});
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

    private boolean showCreatorConfirmation(String inputValue, JDialog firstDialog, String option) {

        final boolean[] confirmed = new boolean[1];
        JDialog confirmationDialog = new JDialog(this, "New " + option + " creator", true);
        confirmationDialog.setSize(300, 150);
        confirmationDialog.setLayout(new GridBagLayout());

        // Components for the confirmation dialog
        JLabel confirmationLabel = new JLabel("Is " + inputValue + " the correct " + option + "?");
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

		if (station.getStopType().equals("Train")) {
			url += "Train";
		} else if (station.getStopType().equals("Tram")) {
			url += "Tram";
		} else {
			url += "Underground";
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
