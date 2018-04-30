package com.github.bigtravis.fitness_365.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.function.Function;

import com.github.bigtravis.fitness_365.model.DBModel;
import com.github.bigtravis.fitness_365.model.PasswordEncryption;
import com.github.bigtravis.fitness_365.model.Sex;
import com.github.bigtravis.fitness_365.model.Units;
import com.github.bigtravis.fitness_365.model.User;
import com.github.bigtravis.fitness_365.view.Login;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author Travis
 *
 */
public class Controller extends Application {
	
	private static final String DB_NAME = "fitness_365.db";
	private static final String[] TABLE_NAMES = {"users", "exercises", "workout_diary", "food_diary", "saved_workouts", "meals",
												"favorite_meals", "sleep_log", "personal_bests", "pictures"};
	private static final String[][] FIELD_NAMES = {{"_id", "username", "password", "salt", "security_question",
													"security_answer", "full_name", "age", "sex", "units", "height",
													"starting_weight", "goal_weight", "current_weight", "weekly_goals"},			
												{"_id", "name", "muscle_group"},
												{"_id", "user_id", "exercise_id", "weight", "reps", "date"},
												{"_id", "meal_id", "catagory", "date", "user_id"},
												{"_id", "name", "user_id", "exercise_id"}, 
												{"_id", "serving_size", "calories", "fat", "carbs", "protein"},
												{"_id", "meal_id", "user_id"}, 
												{"_id", "user_id", "date", "bed_time", "wake_time", "num_wakeups"}, 
												{"_id", "user_id", "mile_time", "bench_press", "deadlift", "squat"},
												{"_id", "user_id", "pic"}};
	
	private static final String[][] FIELD_TYPES = { {"INTEGER PRIMARY KEY", "TEXT", "BLOB", "BLOB", "TEXT", "TEXT", "TEXT", "INTEGER", "INTEGER", "BLOB", "REAL", "REAL", "REAL", "REAL", "REAL"},
													{"INTEGER PRIMARY KEY", "TEXT", "TEXT"},
													{"INTEGER PRIMARY KEY", "INTEGER", "INTEGER", "REAL", "INTEGER", "TEXT"},
													{"INTEGER PRIMARY KEY", "INTEGER", "TEXT", "TEXT", "INTEGER"},
													{"INTEGER PRIMARY KEY", "TEXT", "INTEGER", "INTEGER"},
													{"INTEGER PRIMARY KEY", "REAL", "INTEGER", "REAL", "REAL", "REAL"},
													{"INTEGER PRIMARY KEY", "INTEGER", "INTEGER"},
													{"INTEGER PRIMARY KEY", "INTEGER", "TEXT", "TEXT", "TEXT", "INTEGER"},
													{"INTEGER PRIMARY KEY", "INTEGER", "INTEGER", "REAL", "REAL", "REAL"},
													{"INTEGER PRIMARY KEY", "INTEGER", "BLOB"}};
	
	private static final String[][] FOREIGN_KEYS = {{}, {}, {"FOREIGN KEY(" + FIELD_NAMES[2][1] + ") REFERENCES " + TABLE_NAMES[0] + "(" + FIELD_NAMES[0][0] + ")",
															"FOREIGN KEY(" + FIELD_NAMES[2][2] + ") REFERENCES " + TABLE_NAMES[1] + "(" + FIELD_NAMES[1][0] + ")"},
													{"FOREIGN KEY(" + FIELD_NAMES[3][1] + ") REFERENCES " + TABLE_NAMES[6] + "(" + FIELD_NAMES[6][0] + ")",
													"FOREIGN KEY(" + FIELD_NAMES[3][4] + ") REFERENCES " + TABLE_NAMES[0] + "(" + FIELD_NAMES[0][0] + ")"},
													{"FOREIGN KEY(" + FIELD_NAMES[4][2] + ") REFERENCES " + TABLE_NAMES[0] + "(" + FIELD_NAMES[0][0] + ")",
														"FOREIGN KEY(" + FIELD_NAMES[4][3] + ") REFERENCES " + TABLE_NAMES[1] + "(" + FIELD_NAMES[1][0] + ")"},
													{},
													{"FOREIGN KEY(" + FIELD_NAMES[6][1] + ") REFERENCES " + TABLE_NAMES[5] + "(" + FIELD_NAMES[5][0] + ")",
														"FOREIGN KEY(" + FIELD_NAMES[6][2] + ") REFERENCES " + TABLE_NAMES[0] + "(" + FIELD_NAMES[0][0] + ")"},
													{"FOREIGN KEY(" + FIELD_NAMES[7][1] + ") REFERENCES " + TABLE_NAMES[0] + "(" + FIELD_NAMES[0][0] + ")"},
													{"FOREIGN KEY(" + FIELD_NAMES[8][1] + ") REFERENCES " + TABLE_NAMES[0] + "(" + FIELD_NAMES[0][0] + ")"},
													{"FOREIGN KEY(" + FIELD_NAMES[9][1] + ") REFERENCES " + TABLE_NAMES[0] + "(" + FIELD_NAMES[0][0] + ")"}};
	
	private static Controller mInstance;
	private DBModel mDB;
	private Stage mMainStage;
	
	
	public Controller() {}
	

	public static Controller getInstance() {
		if (mInstance == null)
			mInstance = new Controller();

		return mInstance;
	}

	
	@Override
	public void init() throws Exception {
//		Uncomment the below statements to create a a test user in the database		
		
//		byte[] salt = PasswordEncryption.generateSalt();
//		byte[] password = PasswordEncryption.getEncryptedPassword("password123", salt);
//		User user = new User(-1, "player1", "Favorite Color?", "Red", "John Smith", 25,
//				Sex.MALE, new Units[] {Units.POUNDS, Units.INCHES, Units.MILES}, 72,
//				185.0, 205.0, 185.0, 1.0);
		
		mInstance = this;
		mDB = new DBModel(DB_NAME, TABLE_NAMES, FIELD_NAMES, FIELD_TYPES, FOREIGN_KEYS);
//		mDB.createUser(TABLE_NAMES[0], Arrays.copyOfRange(FIELD_NAMES[0], 1, FIELD_NAMES[0].length),
//				 user, password, salt);
		super.init();
	}


	@Override
	public void start(Stage primaryStage) throws Exception {
		Login login = new Login();
		//Scene scene = login.getLoginScene();
		mMainStage = primaryStage;
		
		mInstance.ChangeScene(e -> {
			try {
				return login.getLoginScene();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return null;
		});
		
		
		//primaryStage.setScene(scene);		
		primaryStage.show();
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

	public User getUser(String username) {
		if (username != null) {
			try {
				int userID = mDB.searchUsers(username);
				ResultSet rs = mDB.getRecord(TABLE_NAMES[0], Integer.toString(userID));
				if (rs.next()) {
					User user = new User(rs.getInt(1), rs.getString(2), rs.getString(5), rs.getString(6),
							rs.getString(7), rs.getInt(8), Sex.parseInt(rs.getInt(9)),
							Units.parse(rs.getBytes(10)),
							rs.getInt(11), rs.getDouble(12), rs.getDouble(13), rs.getDouble(14), rs.getDouble(15));
					return user;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	private byte[][] getPasswordAndSalt(int userID) throws SQLException {
		ResultSet rs = mDB.getRecord(TABLE_NAMES[0], Integer.toString(userID));
		if (rs.next()) {
			byte[][] set = new byte[][] {rs.getBytes(3), rs.getBytes(4)};
			return set;			
		}
		return null;
	}
	
	public boolean authenticateLogin(String username, String attemptedPW) {
		User user = mInstance.getUser(username);
		if (user == null)
			return false;
		
		try {
			byte[][] passwordAndSalt = getPasswordAndSalt(user.getId());
			if (passwordAndSalt != null) {
				return PasswordEncryption.authenticate(attemptedPW, passwordAndSalt[0], passwordAndSalt[1]);
			}
		} catch (SQLException | NoSuchAlgorithmException | InvalidKeySpecException e) {			
			e.printStackTrace();			
		}		
		return false;
	}
	
	
	public void ChangeScene(Function<Void, Scene> f) {		
		Scene s = f.apply(null);
		mMainStage.setScene(s);
	}
	
}
