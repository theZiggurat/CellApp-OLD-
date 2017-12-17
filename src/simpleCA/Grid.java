package simpleCA;

import javafx.scene.paint.Color;

public class Grid {
	
	private Cell[][] GRID;
	private Boolean wrap;
	private Boolean showAge;
	private Boolean showNeighbor;
	private int sustainMin;
	private int sustainMax;
	private int createMin;
	private int createMax;
	private int maxX;
	private int maxY;
	
	public Grid(int x, int y) {
		this.GRID = new Cell[y][x];
		this.wrap = false;
		this.showAge = false;
		this.showNeighbor = false;
		this.maxX = x-1;
		this.maxY = y-1;
		this.sustainMin = 2;
		this.sustainMax = 3;
		this.createMin = 3;
		this.createMax = 3;
	}
	
	
	
	public void iterate() {
		
		for(Cell[] j: GRID) { for(Cell c: j) {
			if(c.isOn()) {
				if(c.getNeighbors()<=sustainMax&&c.getNeighbors()>=sustainMin) {c.addLifeSpan();}
				else {c.setOff();}
			}
			else {
				if(c.getNeighbors()<=createMax&&c.getNeighbors()>=createMin) {c.setOn();}
			}
		}}
		if(showAge) {showAges();}
		if(showNeighbor) {showNeighbors();}
		updateNeighbors();
		graphicsRefresh();
	}
	
	public int getNeighbors(Cell c) {
		int [] indexes = {-1,-1,-1,0,-1,1,0,-1,0,1,1,-1,1,0,1,1};
		int num = 0;
		
		for(int i = 0; i<indexes.length; i++) {
			
			indexes[i]+=c.getX();
			if(indexes[i]>=maxX) {if(wrap) {indexes[i]=0;}else{i++;continue;}}
			if(indexes[i]<0) {if(wrap){indexes[i]=maxX;}else{i++;continue;}}
			
			i++;
			
			indexes[i]+=c.getY();
			if(indexes[i]>=maxY) {if(wrap) {indexes[i]=0;}else{continue;}}
			if(indexes[i]<0) {if(wrap){indexes[i]=maxY;}else{continue;}}
			
			if(get(indexes[i], indexes[i-1]).isOn()) {num++;}
			
		}
		return num;
	}
	
	public void updateNeighbors() {
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.setNeighbors(getNeighbors(c));
		}}
	}
	
	public void showAges() {
		if(showNeighbor) {hideNeighbors();}
		showAge = true;
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.toggleA(true);
		}}
		graphicsRefresh();
	}
	
	public void showNeighbors() {
		if(showAge) {hideAges();}
		showNeighbor = true;
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.toggleN(true);
		}}
		graphicsRefresh();
	}
	
	public void hideAges() {
		showAge = false;
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.toggleA(false);
		}}
		graphicsRefresh();
	}
	
	public void hideNeighbors() {
		showNeighbor = false;
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.toggleN(false);
		}}
		graphicsRefresh();
	}
	
	
	
	public void init(Color[] palette) {
		for(int y = 0; y<=maxY; y++) {for(int x = 0; x<=maxX; x++) {
			GRID[y][x] = new Cell(x,y,false, palette);
			GRID[y][x].setOff();
		}}
	}
	
	public void graphicsRefresh() {
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.updateGraphics();
		}}
	}
	
	public void clear() {
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.setOff();
		}}
		graphicsRefresh();
	}
	
	public void setPalette(Color [] palette) {
		for(Cell[] j: GRID) { for(Cell c: j) {
			c.setColorPalette(palette);
		}}
		graphicsRefresh();
	}

	public Cell get(int ycoord, int xcoord) throws ArrayIndexOutOfBoundsException{
		return GRID[ycoord][xcoord];
	}
	
	public Cell[] getBlock(Cell c) {
		int [] indexes = {-1,-1,-1,0,-1,1,0,-1,0,1,1,-1,1,0,1,1};
		Cell [] ret = new Cell[8];
		
		for(int i = 0; i<indexes.length; i++) {
			
			indexes[i]+=c.getX();
			if(indexes[i]>=maxX) {if(wrap) {indexes[i]=0;}else{i++;continue;}}
			if(indexes[i]<0) {if(wrap){indexes[i]=maxX;}else{i++;continue;}}
			
			i++;
			
			indexes[i]+=c.getY();
			if(indexes[i]>=maxY) {if(wrap) {indexes[i]=0;}else{continue;}}
			if(indexes[i]<0) {if(wrap){indexes[i]=maxY;}else{continue;}}
			
			ret[(i/2)] = get(indexes[i],indexes[i-1]);
		}
		return ret;
	}
	
	public void changeRules(int susM, int susm, int creM, int crem) {
		
	}
	
	public void wrapOn() {wrap = true;}
	
	public void wrapOff() {wrap = false;}
	
	public boolean isWrap() {return wrap;}
	
	public boolean isShowingN() {return showNeighbor;}
	
	public boolean isShowingA() {return showAge;}
	
	

}
