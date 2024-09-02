import javax.swing.*;
import java.text.DecimalFormat;
import java.util.*;

public class StatsShower extends JDialog {

	public JPanel StatsShowerMainPanel;
	private JLabel Title;
	private JLabel TitleImage;
	private JTextField TotalField;
	private JTextField ExploredField;
	private JTextField StoppedField;
	private JTextField PassedStoppingField;
	private JTextField PassedField;
	private JTextField VisitedField;
	public JTextArea StationList;
	private JButton backButton;
	private JButton previousButton;
	private JButton nextButton;


	private int pageIndex = 0;
	private final Stations stationList;
	private final String country;
	private final String area;


	public StatsShower(StatsOptionSelector selector, Stations stationList, String country, String area) {

		super(selector, "Area Stats Display", true);
		this.stationList = stationList;
		this.country = country;
		this.area = area;
		updateFields(generateText());

		nextButton.addActionListener(e -> {
			pageIndex += 1;
			updateFields(generateText());
		});

		previousButton.addActionListener(e -> {
			pageIndex -= 1;
			updateFields(generateText());
		});

		backButton.addActionListener(e -> closeWindow());
	}

	private String generateText() {

		String text = "";

		if (country.equals("World")) {
			text += "World";
		} else {
			text += country + ", " + area;
		}

		text += " - ";

		switch (pageIndex) {
			case 0 -> text += "All Stations\n";
			case 1 -> text += "Train Stations\n";
			case 2 -> text += "Tram Stations\n";
			default -> text += "Underground Stations\n";
		}

		return text;
	}

	private void updateFields(String text) {

		Title.setText(text);
		String type;

		switch (pageIndex) {
			case 0 -> type = "ALL";
			case 1 -> type = "Train";
			case 2 -> type = "Tram";
			default -> type = "Underground";
		}

		ArrayList<Integer> info = stationList.totalStations(country, area, type);
		StationList.setText("");
		StationList.setText("Selection: " + text);
		stationList.showList(type, country, area, StationList);

		if (StationList.getText().equals("Selection: " + text)) {
			StationList.append("No Stations were found for the selection!");
		}

		TotalField.setText(info.get(0).toString());
		ExploredField.setText(info.get(1).toString() + "/" + info.get(0).toString() + " = " + percentageCalc(info.get(1), info.get(0)) + "%");
		StoppedField.setText(info.get(2).toString() + "/" + info.get(0).toString() + " = " + percentageCalc(info.get(2), info.get(0)) + "%");
		PassedStoppingField.setText(info.get(3).toString() + "/" + info.get(0).toString() + " = " + percentageCalc(info.get(3), info.get(0)) + "%");
		PassedField.setText(info.get(4).toString() + "/" + info.get(0).toString() + " = " + percentageCalc(info.get(4), info.get(0)) + "%");
		VisitedField.setText(info.get(5).toString() + "/" + info.get(0).toString() + " = " + percentageCalc(info.get(5), info.get(0)) + "%");
		previousButton.setEnabled(pageIndex != 0);
		nextButton.setEnabled(pageIndex != 3);

		String url = "Images/";

		switch (pageIndex) {
			case 0 -> url += "Info.png";
			case 1 -> url += "Train.png";
			case 2 -> url += "Tram.png";
			default -> url += "Underground.png";
		}

		ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource(url)));
		TitleImage.setIcon(icon);
	}

	private String percentageCalc(int Value1, int total) {
		double percentage = ((double) Value1 / total) * 100;
		return new DecimalFormat("0.00").format(percentage);
	}

	public void closeWindow() { this.dispose(); }
}
