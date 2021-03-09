package view;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import model.Category;
import model.FoodDiaryEntry;
import model.Meal;
import model.SceneNavigation;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

public class FoodSearch implements SceneNavigation, Initializable{
	@FXML
	private ListView<Meal> foodLV;
	@FXML
	private ComboBox<String> filterCB;
	@FXML
	private TextField searchTF;
	@FXML
	private Slider calorieSlider;
	@FXML
	private Slider macrosSlider;
	@FXML
	private RadioButton proteinRB;
	@FXML
	private RadioButton fatRB;
	@FXML
	private RadioButton carbRB;
	@FXML
	private Label itemCountLabel;
	@FXML
	private Button addButton;
	@FXML
	private RadioButton breakfastRB;
	@FXML
	private RadioButton lunchRB;
	@FXML
	private RadioButton dinnerRB;
	@FXML
	private RadioButton snackRB;
	@FXML
	private TextField servingSizeTF;
	@FXML
	private Label mealtimeLabel;
	@FXML
	private Label errorLabel;
	@FXML
	private Button favoritesButton;

	private Controller mController = Controller.getInstance();
	private static final String FXML_FILE_NAME = "FoodSearch.fxml";

	private ObservableList<Meal> mealsList;

	@FXML
	private void filter()
	{
		String comboBox = filterCB.getSelectionModel().getSelectedItem();
		String search = searchTF.getText();
		int calories = (int) calorieSlider.getValue();
		int macros = (int) macrosSlider.getValue();

		if(proteinRB.isSelected())
		{
			mealsList = mController.filter(b -> ((comboBox == null || comboBox.equals(" ") || b.getGroup().equalsIgnoreCase(comboBox)) &&
		    		(search.isEmpty() || b.getName().contains(search)) && b.getCalories() <= calories) && b.getProtein() >= macros);
		}
		else if (fatRB.isSelected())
		{
			mealsList = mController.filter(b -> ((comboBox == null || comboBox.equals(" ") || b.getGroup().equalsIgnoreCase(comboBox)) &&
		    		(search.isEmpty() || b.getName().contains(search)) && b.getCalories() <= calories) && b.getFat() <= macros);
		}
		else
		{
			mealsList = mController.filter(b -> ((comboBox == null || comboBox.equals(" ") || b.getGroup().equalsIgnoreCase(comboBox)) &&
		    		(search.isEmpty() || b.getName().contains(search)) && b.getCalories() <= calories) && b.getCarbs() <= macros);
		}

	    foodLV.setItems(mealsList);
	    itemCountLabel.setText(mealsList.size() + " item(s) displayed");
	}

	@FXML
	private void reset()
	{
		foodLV.getSelectionModel().select(-1);
		filterCB.getSelectionModel().select(-1);
		mealsList = mController.getAllMeals();
		foodLV.setItems(mealsList);
		searchTF.clear();
		calorieSlider.setValue(1000);
	    itemCountLabel.setText(mealsList.size() + " item(s) displayed");
	    macrosSlider.setValue(0);
	    proteinRB.setSelected(true);

	    servingSizeTF.clear();
	    breakfastRB.setSelected(true);
	    addButton.setVisible(false);
		mealtimeLabel.setVisible(false);
		servingSizeTF.setVisible(false);
		breakfastRB.setVisible(false);
		lunchRB.setVisible(false);
		dinnerRB.setVisible(false);
		snackRB.setVisible(false);
		errorLabel.setVisible(false);
		favoritesButton.setVisible(false);
		searchTF.requestFocus();
	}

	@FXML
	private void addNOpenFavorites()
	{

		Meal meal = foodLV.getSelectionModel().getSelectedItem();
		int id = mController.addMealToFavorites(meal);
		meal.setId(id);

		FavoriteMeals home = new FavoriteMeals();
	    mController.changeScene(home.getView(), true);

	}

	@FXML
	private void addItemtoFD()
	{
		errorLabel.setVisible(false);
		Category category = Category.Breakfast;

		if(snackRB.isSelected())
			category = Category.Snack;
		else if(lunchRB.isSelected())
			category = Category.Lunch;
		else if(dinnerRB.isSelected())
			category = Category.Dinner;

		double servingSize = 0;

		if(!servingSizeTF.getText().isEmpty())
		{
			servingSize = Double.parseDouble(servingSizeTF.getText());
			Meal meal = foodLV.getSelectionModel().getSelectedItem();

			FoodDiaryEntry entry = new FoodDiaryEntry(meal, servingSize, category, LocalDate.now(), mController.getCurrentUser().getId());

			int key = mController.addFoodDiaryEntry(entry);
			entry.setId(key);

			HomePage home = new HomePage();
	        mController.changeScene(home.getView(), true);
		}
		else
			errorLabel.setVisible(true);
	}

	@FXML
	private void selectMealTime()
	{
		addButton.setVisible(true);
		mealtimeLabel.setVisible(true);
		servingSizeTF.setVisible(true);
		breakfastRB.setVisible(true);
		lunchRB.setVisible(true);
		dinnerRB.setVisible(true);
		snackRB.setVisible(true);
		errorLabel.setVisible(false);
		favoritesButton.setVisible(true);
	}

	/**
	 * Scene navigator
	 */
	@Override
	public Scene getView()
	{
		try
		{
			BorderPane ap = (BorderPane) FXMLLoader.load(getClass().getResource(FXML_FILE_NAME));
			return new Scene(ap);

		} catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Initializes the listview and combo box
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		mealsList = mController.getAllMeals();
		foodLV.setItems(mController.getAllMeals());
		filterCB.setItems(mController.getFoodGroups());
	    itemCountLabel.setText(mealsList.size() + " item(s) displayed");
	    searchTF.requestFocus();
	}
}
