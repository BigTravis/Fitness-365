package com.github.bigtravis.fitness_365.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

import com.github.bigtravis.fitness_365.controller.Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * @author Travis
 *
 */
public class Login extends AnchorPane{
	private static final String LOGIN_FXML_FILENAME = "Login.fxml";

	private static final String EMPTY_FIELDS_MESSAGE = "All fields must be complete";
	private static final String FAILED_LOGIN_MESSAGE = "Invalid username/password";
	
	public Label errorLabel;
	
	public CheckBox rememberUsernameCB;
	
	public TextField usernameTF;
	
	public PasswordField passwordTF;
	
	public Button loginButton;
	
	public Hyperlink forgotPassHL;
	
	public Hyperlink signUpHL;
	
	private Controller mController;
	private static String savedUser = "";
	
	public Login() {
		mController = Controller.getInstance();		
	}
	
	
	public void initialize() {
		if (!savedUser.isEmpty()) {
			rememberUsernameCB.setSelected(true);
			usernameTF.setText(savedUser);
		}
	}
	
	public Scene getLoginScene() {
		try {
					 
			Scanner input = new Scanner(new File("resources/init.txt"));
			
			if (input.hasNextLine())
				savedUser = input.nextLine();
			input.close();
			
			AnchorPane ap = (AnchorPane) FXMLLoader.load(getClass().getResource(LOGIN_FXML_FILENAME));
			return new Scene(ap);
		
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
		
	@FXML
	private void authenticateLogin(ActionEvent event) {
		String username = usernameTF.getText();
		String typedPW = passwordTF.getText();
		
		if (username.isEmpty() || typedPW.isEmpty()) {
			errorLabel.setText(EMPTY_FIELDS_MESSAGE);
			if (!errorLabel.isVisible())
				errorLabel.setVisible(true);
			
			return;
		}
		if (mController.authenticateLogin(username, typedPW)) {
			try (PrintWriter output = new PrintWriter(new File("resources/init.txt"))) {				
				if (rememberUsernameCB.isSelected())
					output.write(username);
				else
					output.write("");
			} catch (FileNotFoundException e1) {				
				e1.printStackTrace();
			}
			mController.ChangeScene(e -> new HomePage().getHomePageScene(), true);
		}
		else {
			errorLabel.setText(FAILED_LOGIN_MESSAGE);
			if (!errorLabel.isVisible())
				errorLabel.setVisible(true);
		}
	}
	

}