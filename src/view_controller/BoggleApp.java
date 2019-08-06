/**
 * 
 * Description: This class acts as a bridge between the user the Boggle class
 * liberating the user from the pain of console text based games and enabling
 * them to use a graphical user interface.
 * 
 * @author Jacob Phelps
 * @file Boggle.java
 *
 *       Course CSC: 210
 *       Assignment: Boggle Three
 */
package view_controller;

import java.util.Scanner;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Boggle;

public class BoggleApp extends Application {

	private static Boggle boggle;
	private GridPane inputGridPane;
	private TextArea input;
	private Label prompt;
	private Button newGameButton;
	private Button endGameButton;

	private GridPane bogglePane;

	public static void main(String[] args) {
		boggle = new Boggle();
		launch(args);
	}

	/**
	 * Description: Layout the GUI and initialize everything.
	 */
	@Override
	public void start(Stage stage) {
		createFields();
		setupStage(stage);
		setupEventHandlers();

	}

	/**
	 * 
	 * Description: Create the fields in the GUI and enter their values.
	 */
	private void createFields() {
		input = new TextArea();
		input.setWrapText(true);
		prompt = new Label("Enter your attempts below:");
		newGameButton = new Button("New Game");
		endGameButton = new Button("End Game");
	}

	/**
	 * 
	 * Description: Sets up all event handler classes and assigns them to the
	 * appropriate buttons.
	 */
	private void setupEventHandlers() {

		NewGameButtonMouseHandler newGame = new NewGameButtonMouseHandler();
		newGameButton.setOnMouseClicked(newGame);

		EndGameButtonMouseHandler endGame = new EndGameButtonMouseHandler();
		endGameButton.setOnMouseClicked(endGame);

	}

	/**
	 * 
	 * Description: This creates a new boggle game by creating a new instance of
	 * Boggle and updating the dice tray.
	 */
	private void newBoggleGame() {
		boggle = new Boggle();
		input.clear();
		inputGridPane.getChildren().remove(bogglePane);
		bogglePane = makeBoggleTrayGui();
		inputGridPane.add(bogglePane, 1, 0);
	}

	/**
	 * 
	 * Description: Adds all components to the stage
	 * 
	 * @param stage
	 *              The Stage parameter from the start(Stage stage) method.
	 */
	private void setupStage(Stage stage) {
		int pos = 0;
		bogglePane = makeBoggleTrayGui();
		inputGridPane = new GridPane();
		inputGridPane.add(bogglePane, 1, pos++);

		inputGridPane.add(prompt, 1, pos++);
		inputGridPane.add(input, 1, pos++);

		GridPane buttonPane = makeButtonPane();
		inputGridPane.add(buttonPane, 1, pos++);

		Pane rootGroup = new VBox();
		rootGroup.getChildren().addAll(inputGridPane);
		rootGroup.setPadding(new Insets(12));
		stage.setScene(new Scene(rootGroup));
		stage.setTitle("Let's Play Some BOGGLE!");
		stage.show();
	}

	/**
	 * 
	 * Description: Sets up the GridPane which contains the two event buttons.
	 * 
	 * @return
	 *         GridPane with two buttons. One to start the game,
	 *         another to end the game.
	 */
	private GridPane makeButtonPane() {
		GridPane buttonPane = new GridPane();

		buttonPane.add(newGameButton, 1, 0);
		buttonPane.add(endGameButton, 2, 0);
		buttonPane.setAlignment(Pos.CENTER);
		return buttonPane;
	}

	/**
	 * 
	 * Description: Builds the Boggle Dice Tray and returns a stylized GridPane
	 * to be placed in the stage.
	 * 
	 * @return
	 *         GridPane with the stylized Dice Tray
	 */
	private GridPane makeBoggleTrayGui() {
		bogglePane = new GridPane();
		Label letter;
		String[][] str = boggle.getStringArray();
		for (int i = 0; i < str.length; i++) {
			for (int j = 0; j < str[0].length; j++) {
				letter = setupBoggleDice(str[i][j]);
				bogglePane.add(letter, j, i);
			}
		}
		Background b = new Background(
				new BackgroundFill(Color.BLACK, null, null));
		bogglePane.setBackground(b);
		bogglePane.setHgap(10.0);
		bogglePane.setVgap(10.0);
		bogglePane.setPadding(new Insets(25));
		bogglePane.setAlignment(Pos.CENTER);
		return bogglePane;
	}

	/**
	 * 
	 * Description: Creates and styles each boggle dice
	 * 
	 * @param string
	 *               The dice letter(s) as a string
	 * @return
	 *         Label that is styled to look like a dice
	 */
	private Label setupBoggleDice(String string) {
		Font font = new Font("Courier Bold", 24);
		Background dieBackground = new Background(
				new BackgroundFill(Color.WHITE, null, null));
		double width = 40;
		double height = 40;
		Label dice = new Label(string);
		dice.setFont(font);
		dice.setAlignment(Pos.CENTER);
		dice.setTextFill(Color.BLACK);
		dice.setBackground(dieBackground);
		dice.setMinSize(width, height);
		dice.setPrefSize(width, height);
		dice.setMaxSize(width, height);
		return dice;
	}

	/**
	 * 
	 * Description: Creates and calls the alert which shows the end of game
	 * results. When the alert window is closed, a new game is started.
	 */
	private void alertPopup() {
		Alert a = new Alert(AlertType.NONE);
		a.setAlertType(AlertType.INFORMATION);
		a.setTitle("End of Game: Results");
		a.setHeaderText("Your Score: " + boggle.getScore());
		a.setContentText(boggle.gameConclusion());
		a.showAndWait();
	}

	/**
	 * Description: This class is the Mouse Handler for the End Game button.
	 */
	private class EndGameButtonMouseHandler
			implements EventHandler<MouseEvent> {
		/**
		 * Description: When the button is clicked, the user input text is
		 * collected and graded.
		 */
		@Override
		public void handle(MouseEvent arg0) {
			Scanner consoleInput = new Scanner(input.getText());
			boggle.addUserInput(consoleInput);
			alertPopup();
			newBoggleGame();
		}

	}

	/**
	 * Description: This class is the Mouse Handler for the New Game button.
	 */
	private class NewGameButtonMouseHandler
			implements EventHandler<MouseEvent> {
		/**
		 * Description: When the button is clicked, user input is cleared and a
		 * new dice tray is created.
		 */
		@Override
		public void handle(MouseEvent arg0) {
			newBoggleGame();
		}

	}

}
