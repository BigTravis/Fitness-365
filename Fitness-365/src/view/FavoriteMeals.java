/**
 * Class which displays user's favorite meals
 * 
 * @author Jason
 */
package view;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import controller.Controller;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import model.Meal;
import model.SceneNavigation;

public class FavoriteMeals implements SceneNavigation, Initializable {
	@FXML
	private ListView<Meal> favoritesLV;

	@FXML
	private Button deleteButton;

	private Controller mController = Controller.getInstance();
	ObservableList<Meal> favorites;

	private static final String FXML_FILE_NAME = "FavoriteMeals.fxml";

	@FXML
	private void goToHomeScreen() {
		HomePage home = new HomePage();
		mController.changeScene(home.getView(), true);
	}

	@FXML
	private void selectMeal() {
		deleteButton.setVisible(true);
	}

	@FXML
	private void deleteMeal() {
		Meal meal = favoritesLV.getSelectionModel().getSelectedItem();

		mController.deleteFavoriteMeal(meal);
		favorites.remove(meal);

		reset();
	}

	private void reset() {
		favoritesLV.setItems(favorites);
		deleteButton.setVisible(false);
	}

	@FXML
	private void goToFoodSearch() {
		FoodSearch home = new FoodSearch();
		mController.changeScene(home.getView(), true);
	}

	/**
	 * Scene navigator
	 */
	@Override
	public Scene getView() {
		try {
			BorderPane ap = (BorderPane) FXMLLoader.load(getClass().getResource(FXML_FILE_NAME));
			return new Scene(ap);

		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Initializes favorites List View
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		favorites = mController.getFavoriteMeals();
		favoritesLV.setItems(favorites);

	}

}
