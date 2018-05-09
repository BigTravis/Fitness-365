package edu.orangecoastcollege.cs272.capstone.view;

import javafx.fxml.FXML;

import javafx.scene.control.Button;

import javafx.scene.control.TextField;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;

import javafx.scene.control.ComboBox;

import javafx.scene.control.CheckBox;

public class TDEECalc {
	@FXML
	private CheckBox maleCB;
	@FXML
	private CheckBox femaleCB;
	@FXML
	private TextField feetTF;
	@FXML
	private TextField inchesTF;
	@FXML
	private TextField bmrTF;
	@FXML
	private TextField tdeeTF;
	@FXML
	private ComboBox<String> activityCB;
	
	private ObservableList<String> activities;
	
	@FXML
	private TextField weightTF;
	@FXML
	private TextField ageTF;
	
	
	{
		activities = FXCollections.observableArrayList();

		activities.add("");
		activities.add("Sedentary (Little to no exercise)");
		activities.add("Lightly Active (1-3 days/week)");
		activities.add("Moderately Active (3-5 days/week)");
		activities.add("Very Active (6-7 days/week)");
		activities.add("Extremely Active (Exercise/training 2x/day)");
		
        activityCB = new ComboBox<>(activities);

	}

	// Event Listener on ComboBox[#activityCB].onAction
	@FXML
	public void calculateCB() 
	{
		calculate();
	}
	// Event Listener on Button[#updateButton].onAction
	@FXML
	public void updateProfile() {
		// TODO Autogenerated
	}
	// Event Listener on Button[#cancelButton].onAction
	@FXML
	public void cancel() 
	{
		feetTF.clear();
		inchesTF.clear();
		weightTF.clear();
		bmrTF.clear();	
		tdeeTF.clear();
	}
	// Event Listener on Button[#calcButton].onAction
	@FXML
	public void calculate() 
	{
		if(!feetTF.equals(null) && !inchesTF.equals(null) && !weightTF.equals(null) && !ageTF.equals(null)
				&& (maleCB.isSelected() && femaleCB.isSelected()) || (!maleCB.isSelected() && !femaleCB.isSelected()))
		{
    		NumberFormat num = new DecimalFormat("#0.0");

	    	
	    	if(maleCB.isSelected())
	    	{
	    		double w = Double.parseDouble(weightTF.getText()) * 6.23;
	    		double h = ((Double.parseDouble(feetTF.getText()) * 12) + 
	    				Double.parseDouble(inchesTF.getText())) * 12.7;
	    		double calc = 66 + w + h - (Double.parseDouble(ageTF.getText()) * 6.8); 
	    		bmrTF.setText(num.format(calc).toString() + " calories");
	    	}
	    	else if(femaleCB.isSelected())
	    	{
	    		double w = Double.parseDouble(weightTF.getText()) * 4.35;
	    		double h = ((Double.parseDouble(feetTF.getText()) * 12) + 
	    				Double.parseDouble(inchesTF.getText())) * 4.7;
	    		double calc = 655 + w + h - (Double.parseDouble(ageTF.getText()) * 4.7); 
	    		bmrTF.setText(num.format(calc).toString() + " calories");
	    	}
	    	
	    	if(!activityCB.getSelectionModel().isEmpty())
	    	{
	    		int index = activityCB.getSelectionModel().getSelectedIndex();
	    		
	    		switch(index)
	    		{
	    		case 1: 
	    			double tdee1 = Double.parseDouble(bmrTF.getText()) * 1.2;
	    			tdeeTF.setText(num.format(tdee1).toString());
	    			break;
	    		case 2:
	    			double tdee2 = Double.parseDouble(bmrTF.getText()) * 1.375;
	    			tdeeTF.setText(num.format(tdee2).toString());
	    			break;
	    		case 3:
	    			double tdee3 = Double.parseDouble(bmrTF.getText()) * 1.55;
	    			tdeeTF.setText(num.format(tdee3).toString());
	    			break;
	    		case 4:
	    			double tdee4 = Double.parseDouble(bmrTF.getText()) * 1.725;
	    			tdeeTF.setText(num.format(tdee4).toString());
	    			break;
	    		case 5:
	    			double tdee = Double.parseDouble(bmrTF.getText()) * 1.9;
	    			tdeeTF.setText(num.format(tdee).toString());
	    			break;
	    		default:
	    			break;
	    					
	    		}
	    	}
		}
    	else
    	{
    		bmrTF.setText("*Incorrect. Please try again.");
    	}
		
	}
}