package simpleCA;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Cell extends StackPane {
	
	private int x,y, lifeSpan, neighbors;
	private boolean on, showAge, showNeighbor, border;
	Color alive, dead;
	Color [] selection;
	
	private Rectangle rect = new Rectangle(GUI.TILE_SIZE-1, GUI.TILE_SIZE-1);
	private Label num = new Label(""), num2 = new Label("");
	
	public Cell(int x, int y, boolean on, Color[]selection)
	{
		this.lifeSpan = 0;
		this.neighbors = 0;
		this.showAge = false;
		this.showNeighbor = false;
		this.alive = selection[(lifeSpan)%selection.length];
		this.dead = Color.WHITE;
		this.selection = selection;
		this.on = on;
		this.x = x;
		this.y = y;
		
		rect.setStroke(border ? GUI.BORDER_COLOR:null);
		rect.setFill(on ? GUI.FILL_COLOR:Color.WHITE);
		getChildren().addAll(rect,num,num2);
		
		setTranslateX(x*GUI.TILE_SIZE);
		setTranslateY(y*GUI.TILE_SIZE);
		
		setOnMouseEntered(e -> hovered());
		setOnMouseExited(e -> exited());
	}
	
	public void clicked(){
		if(GUI.editMap) {
			if(on) {setOff();}
			if(!on) {setOn();}
		}
	}
	
	public void hovered(){
		rect.setScaleX(1.25);
		rect.setScaleY(1.25);
		rect.setFill(on ? Color.ORANGE: Color.LIGHTGRAY);
	}
	
	public void exited(){
		rect.setScaleX(1);
		rect.setScaleY(1);
		updateGraphics();
	}
	
	public void setOff(){
		on = false;
		lifeSpan = 0;
		updateGraphics();
	}
	
	public void setOn(){
		if(!on) {lifeSpan = 0;}
		on = true;
		updateGraphics();
	}
	
	public void addLifeSpan(){
		lifeSpan++;
	}
	
	public void setColorPalette(Color[] palette) {
		selection = palette;
	}
	
	public void setNeighbors(int n) {neighbors = n;}
	
	public int getNeighbors() {return neighbors;}
	
	public void toggleN(Boolean n) {showNeighbor = n;}
	
	public void toggleA(Boolean n) {showAge = n;}
	
	public boolean isOn(){return on;}
	
	public int getX(){return x;}
	
	public int getY(){return y;}
	
	
	public void updateGraphics() {
		rect.setFill(on ? alive:dead);
		if (neighbors==0) {num2.setText("");}
		else {num2.setText(showNeighbor ? Integer.toString(neighbors): "");}
		if (!on) {num.setText("");}
		else {num.setText(showAge ? Integer.toString(lifeSpan): "");}
	}
	
}

	


