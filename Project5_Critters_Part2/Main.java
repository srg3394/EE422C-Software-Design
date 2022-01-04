package assignment5;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.locks.Lock;

import javafx.animation.AnimationTimer;
import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.SkinBase;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

	static int x = 0; 
	static int y = 0;
	static GridPane world = new GridPane();
	static double columnWidth;
	static double columnHeight;


	static int stepRate = 1;
	static int displayRate = 1;
	static int animationTimerCheck = 0;
	static int displayCounter = 0;
	static boolean animate = false;


	static 	AnimationTimer time = new AnimationTimer() {
		private long lastUpdate = 0;

		@Override
		public synchronized void handle(long now)
		{
			animate = true;

			if(now - lastUpdate >= 1000000000)
			{
				displayCounter+=1;
				Critter.worldTimeStep();
				if(displayCounter == displayRate)
				{
					Critter.displayWorld(world);
					displayCounter = 0;
				}
				lastUpdate = now;
				animationTimerCheck++;
			}
			if(animationTimerCheck == stepRate)
			{
				Critter.displayWorld(world);
				displayCounter = 0;
				animationTimerCheck = 0;
				animate = false;
				stop();
			}

		}	

		};
	@Override
	public void start(Stage primaryStage) throws Exception {
	
		GridPane buttonIO = new GridPane();
		Button createButton = new Button("Create");
		Button clearButton = new Button("Clear");
		Button displayButton = new Button("Display");
		Button stepButton = new Button("Step");
		Button seedButton = new Button("Seed");
		Button exitButton = new Button("Exit");
		Button statsButton = new Button("Stats");

		TextField critterNameBox = new TextField();
		TextField seedBox = new TextField();
		ArrayList<Button> buttonList = new ArrayList<Button>();
		buttonList.add(createButton);
		buttonList.add(clearButton);
		buttonList.add(displayButton);
		buttonList.add(stepButton);
		buttonList.add(seedButton);
		buttonList.add(statsButton);
		buttonList.add(exitButton);

		ChoiceBox<Integer> displayChoice = new ChoiceBox();
		displayChoice.getItems().addAll(1,2,5,10,100);
		ChoiceBox<Integer> stepChoice = new ChoiceBox();
		ChoiceBox<String> critterChoice = new ChoiceBox();
		stepChoice.getItems().addAll(1,2,5,10,100);
		Platform.runLater(() -> {
		    SkinBase<ChoiceBox<String>> skin = (SkinBase<ChoiceBox<String>>) stepChoice.getSkin();
		    // children contain only "Label label" and "StackPane openButton"
		    for (Node child : skin.getChildren()) {
		        if (child instanceof Label) {
		            Label label = (Label) child;
		            if (label.getText().isEmpty()) {
		                label.setText("Rate");
		            }
		            return;
		        }
		    }
		});
		
		initializeWorld();
		initializeIO(buttonIO, buttonList, critterNameBox, stepChoice, seedBox, critterChoice, displayChoice);
		setButtonActions(createButton, clearButton, displayButton,stepButton, seedButton, exitButton, critterNameBox, stepChoice, seedBox, displayChoice, statsButton);
		SplitPane splitDisplay = new SplitPane(world, buttonIO);
		splitDisplay.setOrientation(javafx.geometry.Orientation.VERTICAL);
		
		displayButton.setOnAction(new EventHandler<ActionEvent>() {
			 
            @Override
            public void handle(ActionEvent event) {
            	Critter.displayWorld(world);
            }
        });		
		
		Scene scene = new Scene(splitDisplay);
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		
		launch(args);

	}
	
	/**
	 * Used to create onAction functions for the buttons
	 * Params: ArrayList of buttons
	 */
	public static void setButtonActions(Button createButton, Button clearButton,Button displayButton,Button stepButton,Button seedButton, Button exitButton, final TextField critterNameBox,
			 ChoiceBox<Integer> stepChoice, final TextField seedBox, ChoiceBox<Integer> displayChoice, Button statsButton)
	{
		createButton.setOnAction(new EventHandler<ActionEvent>() { 
           @Override
           public void handle(ActionEvent event) {
           	if(!animate)
           	{
                  	String critName = critterNameBox.getText();
   				try {
   					Critter.createCritter(critName);
   				} catch (Exception e) {
   					// TODO Auto-generated catch block
   					
   				}
           	}

           }
       });
		
		displayChoice.setOnAction(new EventHandler<ActionEvent>() {
           @Override
           public void handle(ActionEvent event) {
           	if(!animate)
           		displayRate = (int) displayChoice.getValue();
           }
       });	
		
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			
           @Override
           public void handle(ActionEvent event) {
           	if(!animate)
           		Critter.clearWorld();
           }
       });			
		
		stepButton.setOnAction(new EventHandler<ActionEvent>() {
			
           @Override
           public void handle(ActionEvent event) {
           	if(!animate)
               	time.start();
           }
       });		
		
		seedButton.setOnAction(new EventHandler<ActionEvent>() {
			 
           @Override
           public void handle(ActionEvent event) {
           	if(!animate)
           	{
               	String seed = seedBox.getText();
   				try {
   					int setSeed = Integer.parseInt(seed);
   					Critter.setSeed(setSeed);
   				} catch (NumberFormatException e) {
   				}
           	}

           }
       });
		statsButton.setOnAction(new EventHandler<ActionEvent>() {
			
           @Override
           public void handle(ActionEvent event) {
           	if(!animate)
           	{
           	// can use an Alert, Dialog, or PopupWindow as needed...
           		Stage popup = new Stage();
           		// configure UI for popup etc...
           		Pane pane = new Pane();
           		Label showStats = new Label(Critter.runStats(Critter.getPopulation()));
           		pane.getChildren().add(showStats);
           		Scene newScene = new Scene(pane, 300,300);
           		popup.setScene(newScene);
           		// hide popup after 3 seconds:
           		PauseTransition delay = new PauseTransition(Duration.seconds(3));
           		delay.setOnFinished(e -> popup.hide());

           		popup.show();
           		delay.play();

           	}
           }
       });	
		
		stepChoice.setOnAction(new EventHandler<ActionEvent>() {
			 
           @Override
           public void handle(ActionEvent event) {
           	if(!animate)
           		stepRate = (int) stepChoice.getValue();
           }
       });
		
		exitButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(!animate)
					System.exit(0);
			}
		});
	}
	/**
	 * initialize all the IO for the game
	 * 
	 * params: GridPane buttonIO
	 * @throws InvalidCritterException 
	 * @throws SecurityException 
	 * @throws NoSuchMethodException 
	 * @throws InvocationTargetException 
	 * @throws IllegalArgumentException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	public static void initializeIO(GridPane buttonIO, ArrayList<Button> buttonList, TextField critterNameBox, ChoiceBox stepChoice,
			TextField seedBox, ChoiceBox<String> critterChoice, ChoiceBox<Integer> displayChoice) throws InvalidCritterException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException
	{
		buttonIO.setAlignment(Pos.CENTER);


		for(int i = 0; i < buttonList.size(); i++)
		{
			if(i == 0)
			{
				buttonIO.add(critterNameBox, i, 1);
			}
			else if(i == 2)
			{
				buttonIO.add(displayChoice, i, 1);
			}
			else if(i==3)
			{
				buttonIO.add(stepChoice, i, 1);
			}
			else if(i == 4)
			{
				buttonIO.add(seedBox, i, 1);
			}
		     buttonIO.getColumnConstraints().add(new ColumnConstraints(100)); // column 0 is 100 wide
		     buttonIO.add(buttonList.get(i), i, 0);
		}
	}
	/**
	 * initialize world upon starting
	 * params: GridPane world
	 */
	public static void initializeWorld()
	{
		if(Params.WORLD_HEIGHT>75)
		{
			columnWidth = 6;
			columnHeight = 6;
		}
		else if(Params.WORLD_HEIGHT> 50)
		{
			columnWidth = 10;
			columnHeight = 10;
		}
		else
		{
			columnWidth = 20;
			columnHeight = 20;
		}

		for(int i = 0; i < Params.WORLD_HEIGHT; i++)
		{
	         RowConstraints row = new RowConstraints(columnWidth);
	         world.getRowConstraints().add(row);
		     world.getColumnConstraints().add(new ColumnConstraints(columnHeight)); 
		}
		BackgroundFill background_fill = new BackgroundFill(Color.ANTIQUEWHITE,CornerRadii.EMPTY, Insets.EMPTY);
        Background background = new Background(background_fill);
        world.setBackground(background);
		world.setAlignment(Pos.CENTER);
	}
}