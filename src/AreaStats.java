import javax.swing.*;
import java.text.DecimalFormat;
import java.util.*;

public class AreaStats extends JDialog {

	public JPanel AreaStatsMainPanel;
	private JLabel Title;
	private JLabel TitleImage;
	private JTextField TotalField;
	private JTextField ExploredField;
	private JTextField StoppedField;
	private JTextField PassedStoppingField;
	private JTextField PassedField;
	private JTextField VisitedField;
	public JTextArea areaList;
	private JButton backButton;
	private JButton previousButton;
	private JButton nextButton;


	private int pageIndex = 0;
	private final Stations stationList;

	public AreaStats(StatsOptionSelector selector, Stations stationList) {

		super(selector, "Compare Area Display", true);
		this.stationList = stationList;
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

		ArrayList<Integer> info = stationList.totalStations("World", "All Areas", type);
		areaList.setText("");
		stationList.showAreaList(type, areaList);

		if (areaList.getText().isEmpty()) {
			areaList.append("No Stations were found for the selection!");
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

		if (Value1 == 0 && total == 0) {
			return "0.00";
		}
		double percentage = ((double) Value1 / total) * 100;
		return new DecimalFormat("0.00").format(percentage);
	}

	public void closeWindow() { this.dispose(); }
}