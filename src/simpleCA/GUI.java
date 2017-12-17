package simpleCA;
import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class GUI extends Application{

	static int TILE_SIZE = 10;
	private static double height = 60, width = 85;
	private int GAME_HEIGHT = 720, GAME_WIDTH = 1280;
	private static Stage window;
	
	private Grid GRID;
	
	static boolean gameRun = false;
	static boolean editMap = false;
	static boolean showAge = false;
	static boolean wrapToggle = false;
	static boolean showNeighbor = false;
	
	private int ATLEAST = 2;
	private int ATMOST = 3;
	
	int h = (int) ((GAME_HEIGHT-height)/TILE_SIZE);
	int w = (int) ((GAME_WIDTH-width)/TILE_SIZE);
	
	private Stage sizer;
	
	
	public static Color BORDER_COLOR = Color.LIGHTGRAY;
	public static Color FILL_COLOR = Color.GRAY;
	private BackgroundFill BG_DEF = new BackgroundFill(FILL_COLOR, null, null);
	private BackgroundFill BG_EX = new BackgroundFill(Color.WHITE, null, null);
	private static BorderStroke DEF_BORDER = new BorderStroke(Color.web("#757575"), 
			BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 10,0));
	
	
	final static Color[] rainbow = {Color.RED, Color.ORANGERED, Color.ORANGE, 
			Color.YELLOW, Color.YELLOWGREEN, Color.GREENYELLOW, 
			Color.GREEN, Color.TEAL, Color.BLUE, Color.DARKBLUE, 
			Color.VIOLET, Color.PURPLE};
	
	private Border sideButtonPressed = new Border(new BorderStroke(Color.web("#757575"), 
			BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,5,0,0)));
	private Border sideButtonDef = new Border(new BorderStroke(Color.DARKGRAY, 
			BorderStrokeStyle.SOLID, new CornerRadii(1), new BorderWidths(0,0,1,0)));
	private Border topButtonPressed = new Border(new BorderStroke(Color.web("#757575"), 
				BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,1,5,0)));
	private Border topButtonDef = new Border(new BorderStroke(Color.web("#757575"), 
			BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,1,0,0)));
	private Border exitButtonPressed = new Border(new BorderStroke(Color.web("#757575"), 
			BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,5,1)));
	private Border exitButton = new Border(new BorderStroke(Color.web("#757575"), 
			BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0,0,0,1)));
	
	public static void run() throws IOException, InterruptedException
	{
		launch();
	}
	
    @Override
    public void start(Stage primaryStage) throws InterruptedException {
    	window = primaryStage;
    	window.setTitle("Game of Life");
    	//window.setResizable(false);
    	//window.getIcons().add(new Image("file:bmp.bmp"));
    	
    	DropShadow dw = new DropShadow();
    	dw.setRadius(5.0);
    	dw.setOffsetY(3.0);
    	
    	
    	HBox topMenu = hbox();
    	VBox sideMenu = vbox();
    	
    	Pane pane = createContent(h,w);
    	
    	
    	
    	
    	BorderPane bp = new BorderPane();
    	bp.setTop(topMenu);
    	bp.setLeft(sideMenu);
    	bp.setCenter(pane);
    	
    	bp.setEffect(dw);
    	
    	Scene scene = new Scene(bp, 1280, 720);
    	
    	window.setScene(scene);
    	window.initStyle(StageStyle.UNIFIED);
    	window.show();
    }
    
 
    
    // creates the array of cells at startup
   
    private Pane createContent(int h, int w) {
    	
    	Pane pane = new Pane();
    	
    	GRID = new Grid(w,h);
    	GRID.init(rainbow);
    	
    		// Creating array of fresh Cells that can be referenced back 
    	
    	for(int i = 0; i<h; i++){for(int k = 0; k<w; k++){
    			pane.getChildren().add(GRID.get(i, k));
    		}
    	}
    	
    	EventHandler<MouseEvent> me = new EventHandler<MouseEvent>(){
    		private double y;
    		private double x;
    		private int Xindex;
    		private int Yindex;
    		private Cell hover;
    		private Cell [] block;
    		
			public void handle(MouseEvent m){
				y = m.getY();
				x = m.getX();
				Yindex = (int) y/TILE_SIZE;
				Xindex = (int) x/TILE_SIZE;
				hover = GRID.get(Yindex,Xindex);
				
				if(editMap&&(m.getEventType() == MouseEvent.MOUSE_DRAGGED||m.getEventType()==MouseEvent.MOUSE_CLICKED));
				{
					hover.setOn();
					hover.setNeighbors(GRID.getNeighbors(hover));
					hover.updateGraphics();
					Cell [] update = GRID.getBlock(hover);
					for(Cell c: update) {
						if(c == null) {continue;}
						c.setNeighbors(GRID.getNeighbors(c));
						c.updateGraphics();
					}
				}
				
				
			}
				
			};
				
		pane.setOnMouseDragged(me);
		pane.setOnMouseClicked(me);
		//pane.setOnMouseDragReleased(e -> {GRID.updateAges();GRID.updateNeighbors(););
		return pane;
	}
    
  
    
    
    // creates the top menu for main window at startup

	public HBox hbox() {
	
    	HBox topMenu = new HBox();
    	topMenu.maxHeight(height);
    	topMenu.minHeight(height);
    	
    	Button start = new Button("Start");
    	Button stop = new Button("Stop");
    	Button edit = new Button("Edit");
    	Button exit = new Button("Exit");
    	Button iterate = new Button("Iterate");
    	Button clear = new Button("Clear");
    	
    	exit.setOnAction(e -> closeProgram());
    	
    	Button [] buttons = {start,stop,edit,iterate, clear, exit};
    	Button [] clickies = {start, stop, exit, iterate, clear};
    	
    	for(Button k: buttons) {styleTop(k);}
    	for(Button c: clickies) {giveClickTop(c);}
    	
    	clear.setOnAction(e -> {GRID.clear(); System.out.println("Clear");});
    	iterate.setOnAction(e -> GRID.iterate());
    	
    	exit.setTranslateX(1275-(buttons.length * 85 ));
    	exit.setBorder(exitButton);
    	exit.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {exit.setBorder(exitButtonPressed);exit.setBackground(pressed);});
		exit.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {exit.setBorder(exitButton);exit.setBackground(null);});
    	
    	start.setOnAction(null);
    	
    	stop.setOnAction(e -> {gameRun = false;});
    	edit.setOnAction(e -> {if(editMap) {editOff(edit);}else{editOn(edit);}});
    	
    	topMenu.getChildren().addAll(buttons);
    	topMenu.setBorder(new Border(DEF_BORDER));
    	topMenu.setBackground(new Background(new BackgroundFill(Color.web("#424242"),null,null)));
		return topMenu;
    }
	
	
	
	
	
	// creates side menu at startup

	public VBox vbox() {
    	
    	VBox sideMenu = new VBox(0);
    	
    	Label blankSpace = new Label("");
    	blankSpace.setFont(new Font(8));
    	
    	
    	Button color = new Button("Color"), size = new Button("Size"),
    	showNum = new Button("Age"), wrap = new Button("Wrap"), showNeighbors = new Button("Neighbors"),
    	grid = new Button("Grid");
    	
    	Button [] buttons = {color,size,showNum, wrap, showNeighbors, grid};
    	Button [] clickies = {};
    	for(Button k: buttons) {styleSide(k);}
    	for(Button c: clickies) {giveClickSide(c);}
    	
    	GridPane changeRules = changeRulesInit();
    	changeRules.setTranslateY(10);
    	
    	// button event handlers
    	
    	wrap.setOnAction(e -> {if (GRID.isWrap()) {wrapOff(wrap);}else{wrapOn(wrap);}});
    	
    	showNum.setOnAction(e -> {
    		if(!GRID.isShowingA()) {
    			ageOn(showNum,showNeighbors);
    			}
    	else {
    		ageOff(showNum);
    		}
    		}
    	);
    	
    	showNeighbors.setOnAction(e -> {
    		if(!GRID.isShowingN()) {
    			neighborOn(showNeighbors,showNum);
    			}
    	else {
    		neighborOff(showNeighbors);
    		}
    	}
    	);
    	
    	size.setOnAction(e -> {sizeWindow();});
    	
    	
    
    	
    	
    	// configurations for side menu styling
    	
    	sideMenu.setBorder(new Border(DEF_BORDER));
    	sideMenu.setPadding(new Insets(0));
    	sideMenu.minWidth(width);
    	sideMenu.maxWidth(width);
    	sideMenu.getChildren().addAll(buttons[0],buttons[1]);
    	sideMenu.getChildren().addAll(blankSpace);
    	sideMenu.getChildren().addAll(buttons[2],buttons[3],buttons[4],buttons[5]);
    	sideMenu.getChildren().addAll(changeRules);
    	sideMenu.setBackground(new Background(new BackgroundFill(Color.web("#757575"),null,null)));
    	
    	return sideMenu;
	}
	
	/*private GridPane heatMap() {
		GridPane gp = new GridPane();
		
		CheckBox cb1 = new CheckBox();
		
		cb1.setText("Heat");
		
		GridPane.setConstraints(cb1, 0, 0);
		
		gp.getChildren().addAll(cb1);
		
		
		return gp;
		
	}*/
	
	
	// UNFINISHED -- pane below side menu buttons to change game rules

	private GridPane changeRulesInit() {
    	
    	GridPane gp = new GridPane();
    	
    	Label atMost = new Label("Creation");
    	Label atLeast = new Label("Survival");
    	Label heatMap = new Label("Heat");
    	heatMap.setPadding(new Insets(0,0,0,10));
    	atMost.setAccessibleText("Help here");
    	Background bg = new Background(new BackgroundFill(Color.web("#9E9E9E"),null,null));
    	Pane bg1 = new Pane();
    	Pane bg2 = new Pane();
    	Pane bg3 = new Pane();
    	
    	bg1.setBackground(bg);bg1.setPrefHeight(40);bg1.setPrefWidth(90);
    	bg2.setBackground(bg);bg2.setPrefHeight(40);bg2.setPrefWidth(90);
    	bg3.setBackground(bg);bg3.setPrefHeight(40);bg3.setPrefWidth(90);
    	
    	GridPane.setConstraints(bg1, 0, 0, 2, 1);
    	GridPane.setConstraints(bg2, 0, 1, 2, 1);
    	GridPane.setConstraints(bg3, 0, 2, 2, 1);
    	
    	
    	RadioButton cb = new RadioButton();
    	//cb.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, null, new BorderWidths(5))));
    	//cb.setPadding(new Insets(10,0,0,0));
    	GridPane.setConstraints(cb, 1, 0);
    	GridPane.setConstraints(heatMap, 0, 0);
    	GridPane.setConstraints(atMost, 0, 1);
    	GridPane.setConstraints(atLeast, 0, 2);
    	
    	
    	gp.setHgap(5);
    	gp.setVgap(10);
    	
    	gp.setMaxWidth(85);
    	
    	gp.getChildren().addAll(bg1,bg2,bg3,cb,heatMap, atMost, atLeast);
		
		return gp;
	}
    
	
	
	// contains button style for side menu
    
    public void styleSide(Button n) {
    	n.setBorder(sideButtonDef);
    	n.setBackground(new Background(new BackgroundFill(Color.web("#9E9E9E"), null, null) ));
    	n.setPrefSize(85, 35);
    	n.setFont(new Font("Segoe UI", 13));
    	n.setTextFill(Color.web("#424242"));
    }
    
    // contains button style for top menu
    
    public void styleTop(Button n) {
    	n.setBackground(null);
    	n.setBorder(topButtonDef);
    	n.setPrefHeight(50);
    	n.setPrefWidth(85);
    	n.setFont(new Font("Segoe UI", 15));
    	n.setTextFill(Color.web("#ECEFF1"));
    			
    }
    
    
	
    
    // a big mess that creates the exit dialog window
    
    private void closeProgram() {
    	
    	Stage exitWindow = new Stage();
    	exitWindow.setX(window.getX()+(window.getWidth()/2)-100);
    	exitWindow.setY(window.getY()+(window.getHeight()/2)-45);
    	exitWindow.setIconified(false);
    	GridPane gp = new GridPane();
    	Scene exitScreen = new Scene(gp, 200, 90);
    	gp.setPadding(new Insets(0));
    	exitWindow.initStyle(StageStyle.UNDECORATED);
    	
    	gp.setBorder(new Border(DEF_BORDER));
    	gp.setBackground(new Background(new BackgroundFill(Color.web("#424242"),null,null)));
    	
    	
    	Label message = new Label("  Are you sure you want to exit?");
    	message.setFont(Font.font("Segoe",14));
    	message.setTextFill(Color.web("#ECEFF1"));
    	GridPane.setConstraints(message, 0, 0, 2, 1);
    	
    	Button yes = new Button("Yes");
    	styleTop(yes);
    	yes.setPrefSize(100, 50);
    	Button no = new Button("No");
    	styleTop(no);
    	no.setPrefSize(100, 50);
    	giveClickTop(yes);
    	giveClickTop(no);
    	GridPane.setConstraints(yes, 0, 1);
    	GridPane.setConstraints(no, 1, 1);
    	
    	message.setBorder(new Border(new BorderStroke(Color.web("#757575"), 
    			BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(0, 0, 1,0))));
    	
    	message.setPrefSize(200, 40);
    	
    	gp.getChildren().addAll(message,yes,no);
    	
    	yes.setOnAction(e -> {exitWindow.close(); window.close();});
    	no.setOnAction(e -> {exitWindow.close();});
    	
    	exitWindow.setScene(exitScreen);
    	exitWindow.show();
    	
    }
    
    
    private void sizeWindow() {
    	sizer = new Stage();
    	GridPane gp = new GridPane();
    	Scene s = new Scene(gp,100,35);
    	sizer.setResizable(false);
    	sizer.initStyle(StageStyle.UNDECORATED);
    	sizer.show();
    	
    }
    
    
    // NOT WORKING -- freezes at run; loop works fine but will not iterate and program halts at loop start
    
    public void runGame() throws InterruptedException {
    	
    	long TARGET_FPS = 20;
    	long TARGET_SLEEP = 1000/TARGET_FPS;
    	
    	while(gameRun)
    	{
    		System.out.println("I DID IT");
    		GRID.iterate();
    		Thread.sleep(TARGET_SLEEP);
    	}
    }
    
    
    // very unimportant methods that make the edit button react to clicking!
    
    
    public void editOn(Button edit){
		edit.setBorder(topButtonPressed);
		editMap = true;
	}public void editOff(Button edit){
		edit.setBorder(topButtonDef);
		editMap = false;
	}
	
	
	
	public void wrapOn(Button wrap){
		wrap.setBorder(sideButtonPressed);
		GRID.wrapOn();
	}public void wrapOff(Button wrap){
		wrap.setBorder(sideButtonDef);
		GRID.wrapOff();
	}
	
	
	public void ageOn(Button age, Button n) {
		GRID.showAges();
		age.setBorder(sideButtonPressed);
		n.setBorder(sideButtonDef);
	}public void ageOff(Button age) {
		GRID.hideAges();
		age.setBorder(sideButtonDef);
	}
	
	
	public void neighborOn(Button n, Button age) {
		GRID.showNeighbors();
		n.setBorder(sideButtonPressed);
		age.setBorder(sideButtonDef);
	}public void neighborOff(Button n) {
		GRID.hideNeighbors();
		n.setBorder(sideButtonDef);
	}
	
	Background pressed = new Background(new BackgroundFill(Color.web("#EF5350"),null,null));
	
	public void giveClickSide(Button b){
		b.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> b.setBorder(sideButtonPressed));
		b.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> b.setBorder(sideButtonDef));
	}public void giveClickTop(Button b){
		b.addEventHandler(MouseEvent.MOUSE_PRESSED, e -> {b.setBorder(topButtonPressed);b.setBackground(pressed);});
		b.addEventHandler(MouseEvent.MOUSE_RELEASED, e -> {b.setBorder(topButtonDef);b.setBackground(null);});
	}
	
    
    
   
    
    
  
    
    
}