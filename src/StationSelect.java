import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class StationSelect extends JDialog {

	public JPanel StationSelectPanel;
	private JLabel StationInputLabel;
    private JTextField StationInput;
    private JButton searchButton;
	private ButtonGroup StationSelection;
	private JRadioButton Station1, Station2, Station3, Station4, Station5;
    private JRadioButton Station6, Station7, Station8, Station9, Station10;
	private JButton enterButton, cancelButton;

	private final Stations stationList;
	private ArrayList<Station> stationChoices;

	public StationSelect(Start start, Stations stationList, boolean delete) {

		super(start, "Station Selection", true);
		selectionReset();
		this.stationList = stationList;

		if (delete) {
			StationInputLabel.setText("Which station would you like to delete (ID or name):");
		} else {
			StationInputLabel.setText("Which station would you like to modify (ID or name):");
		}
        Station1.setMnemonic(1);
        Station2.setMnemonic(2);
        Station3.setMnemonic(3);
        Station4.setMnemonic(4);
        Station5.setMnemonic(5);
        Station6.setMnemonic(6);
        Station7.setMnemonic(7);
        Station8.setMnemonic(8);
        Station9.setMnemonic(9);
        Station10.setMnemonic(10);

		// Station Search
        searchButton.addActionListener(e -> stationSearch());

		StationInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
					stationSearch();
                }
            }
        });

        enterButton.addActionListener(e -> {

	        if (StationSelection.getSelection() != null) {
				int selected = StationSelection.getSelection().getMnemonic() - 1;
				if (!delete) {
					start.setSelectedStation(stationChoices.get(selected));
					closeWindow();
				} else {

					String name = stationChoices.get(selected).getName() + " (" + stationChoices.get(selected).getId() + ")";

					if (showDeletionConfirmation(name)) {
						JOptionPane.showMessageDialog(StationSelect.this, (name + " has been removed"));
						stationList.removeStation(stationChoices.get(selected));
						closeWindow();
					}
				}
	        } else {
		        JOptionPane.showMessageDialog(StationSelect.this, "Please select a station!");
	        }
        });

		cancelButton.addActionListener(e -> closeWindow());
    }

	private boolean showDeletionConfirmation(String name) {

		final boolean[] confirmed = new boolean[1];
		JDialog confirmationDialog = new JDialog(this, "Station Deleter", true);
		confirmationDialog.setSize(300, 150);
		confirmationDialog.setLayout(new GridBagLayout());

		// Components for the confirmation dialog
		JLabel confirmationLabel = new JLabel("Are you sure you want to delete " + name + "?");
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
			confirmed[0] = true;
			confirmationDialog.dispose();

		});

		// Action for "No" button
		noButton.addActionListener(e -> {
			confirmationDialog.dispose();
			confirmed[0] = false;
		});

		// Center the dialog on the screen and make it visible
		confirmationDialog.setLocationRelativeTo(this);
		confirmationDialog.setVisible(true);

		return confirmed[0];
	}

	private void stationSearch() {

		if (!StationInput.getText().isEmpty()) {

			stationChoices = stationList.findStationByID(StationInput.getText());

			if (stationChoices.isEmpty()) {
				stationChoices = stationList.findStationByName(StationInput.getText());
			}

			if (stationChoices.isEmpty()) {
				JOptionPane.showMessageDialog(StationSelect.this, "There was no station found with input \"" + StationInput.getText() + "\"", "Error!", JOptionPane.ERROR_MESSAGE);
			} else {
				int len = stationChoices.size();
				Station1.setText(stationChoices.get(0).stationChoiceDisplay());
				Station1.setEnabled(!Station1.getText().isEmpty());
				Station2.setText((1 < len) ? stationChoices.get(1).stationChoiceDisplay() : "");
				Station2.setEnabled(!Station2.getText().isEmpty());
				Station3.setText((2 < len) ? stationChoices.get(2).stationChoiceDisplay() : "");
				Station3.setEnabled(!Station3.getText().isEmpty());
				Station4.setText((3 < len) ? stationChoices.get(3).stationChoiceDisplay() : "");
				Station4.setEnabled(!Station4.getText().isEmpty());
				Station5.setText((4 < len) ? stationChoices.get(4).stationChoiceDisplay() : "");
				Station5.setEnabled(!Station5.getText().isEmpty());
				Station6.setText((5 < len) ? stationChoices.get(5).stationChoiceDisplay() : "");
				Station6.setEnabled(!Station6.getText().isEmpty());
				Station7.setText((6 < len) ? stationChoices.get(6).stationChoiceDisplay() : "");
				Station7.setEnabled(!Station7.getText().isEmpty());
				Station8.setText((7 < len) ? stationChoices.get(7).stationChoiceDisplay() : "");
				Station8.setEnabled(!Station8.getText().isEmpty());
				Station9.setText((8 < len) ? stationChoices.get(8).stationChoiceDisplay() : "");
				Station9.setEnabled(!Station9.getText().isEmpty());
				Station10.setText((9 < len) ? stationChoices.get(9).stationChoiceDisplay() : "");
				Station10.setEnabled(!Station10.getText().isEmpty());

				if (len == 1) {
					Station1.setSelected(true);
				} else {
					StationSelection.clearSelection();
				}
			}
		} else {
			JOptionPane.showMessageDialog(StationSelect.this, "Field cannot be left blank", "Error!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void selectionReset() {
		Station1.setEnabled(false);
		Station2.setEnabled(false);
		Station3.setEnabled(false);
		Station4.setEnabled(false);
		Station5.setEnabled(false);
		Station6.setEnabled(false);
		Station7.setEnabled(false);
		Station8.setEnabled(false);
		Station9.setEnabled(false);
		Station10.setEnabled(false);
	}

	public void closeWindow() { this.dispose(); }
}
