package view;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

import controller.Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import jfxtras.scene.control.LocalDatePicker;
import jfxtras.scene.control.LocalTimePicker;
import jfxtras.scene.control.LocalDatePicker.Mode;
import model.SceneNavigation;
import model.SleepLogEntry;

public class AddSleepLogEntryForm extends GridPane implements SceneNavigation {

	@FXML
	private LocalTimePicker bedTimePicker, wakeTimePicker;
	@FXML
	private LocalDatePicker datePicker;
	@FXML
	private Button addSleepLogEntryButton;
	@FXML
	private TextField numInterruptionsTF;
	@FXML
	private Label invalidNumberErrorLabel;

	private Controller mController;

	private SleepLogEntry mEntry;

	@FXML
	private void initialize() {
		mController = Controller.getInstance();
		datePicker.setMode(Mode.SINGLE);
		datePicker.setLocalDate(LocalDate.now());
	}

	public SleepLogEntry getEntry() {
		return mEntry;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see model.SceneNavigation#getView()
	 */
	@Override
	public Scene getView() {
		try {

			Pane pane = FXMLLoader.load(getClass().getResource("addSleepLogEntryForm.fxml"));
			return new Scene(pane);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	@FXML
	private void addSleepLogEntry(ActionEvent e) {
		LocalTime sleepTime, wakeTime;
		LocalDate date;
		int numOfInterruptions;

		try {
			numOfInterruptions = Integer.valueOf(numInterruptionsTF.getText());
			if (numOfInterruptions < 0)
				throw new NumberFormatException();
		} catch (NumberFormatException e1) {
			invalidNumberErrorLabel.setVisible(true);
			return;
		}
		invalidNumberErrorLabel.setVisible(false);

		sleepTime = bedTimePicker.getLocalTime();
		wakeTime = wakeTimePicker.getLocalTime();
		date = datePicker.getLocalDate();

		mEntry = new SleepLogEntry(date, sleepTime, wakeTime, numOfInterruptions);

		mEntry.setID(mController.addSleepLogEntry(mEntry));
		((Button) e.getSource()).getScene().getWindow().hide();

	}

}
