package application;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;

public class Tile extends Label {
	
	//class variables
	private int value;
	private boolean empty, merge;
	private String bCol, tCol;
	private Text txtNum;
	
	/**
	 * creates a tile with a value 
	 * @param value -value of the tile should be a power of 2
	 */
	public Tile( int value) 
	{	
		empty = false;
		merge = true;
		txtNum = new Text();
		
		setPrefSize(107,107);
		setValue(value);
	}

	/**
	 * creates an empty tile with a value of 0
	 */
	public Tile()
	{
		txtNum = new Text();
		empty =true;
		merge = true;
		setPrefSize(107,107);
		setValue(value=0);	
	}

	/**
	 * depending on the value, the background colour and text colour is changed
	 * @param value- the value of the tile
	 * 
	 */
	private void determineColour(int value)
	{
		switch(value)
		{
		case 0:
			bCol = "rgba(238, 228, 218, 0.35);";
			tCol = "rgba(238, 228, 218, 0.35);";
			break;
		case 2:
			bCol = "#eee4da;";
			tCol = "#776e65;";
			break;
		case 4:
			bCol = "#ede0c8;";
			tCol = "#776e65;";
			break;
		case 8:
			bCol = "#f2b179;";
			tCol = "#f9f6f2;";
			break;
		case 16:
			bCol = "#f59563;";
			tCol = "#f9f6f2;";
			break;
		case 32:
			bCol = "#f67c5f;";
			tCol = "#f9f6f2;";
			break;
		case 64:
			bCol = "#f65e3b;";
			tCol = "#f9f6f2;";
			break;
		case 128:
			bCol = "#edcf72;";
			tCol = "#f9f6f2;";
			break;
		case 256:
			bCol = "#edcc61;";
			tCol = "#f9f6f2;";
			break;
		case 512:
			bCol = "#edc850;";
			tCol = "#f9f6f2;";
			break;
		case 1024:
			bCol = "#edc53f;";
			tCol = "#f9f6f2;";
			break;
		case 2048:
			bCol = "#edc22e";
			tCol = "#f9f6f2;";
			break;
		case 4096:
			bCol = "#B9ED2E";
			tCol = "#f9f6f2;";
			break;	
		case 8192:
			bCol = "#59ED2E";
			tCol = "#f9f6f2;";
			break;
		case 16384:
			bCol = "#2EED62";
			tCol = "#f9f6f2;";
			break;
		case 32768:
			bCol = "#2EEDC2";
			tCol = "#f9f6f2;";
			break;
		case 65536:
			bCol = "#2EB9ED";
			tCol = "#f9f6f2;";
			break;
		default:
			bCol = "#2D46EB";
			tCol = "#f9f6f2;";
			break;
		}	
	}

/**
 * returns the value of the tile 
 * @return - value, int
 */
	public int getValue() {
		return value;
	}

/**
 * sets the value of the tile
 * sets the colour of the background and text
 * if 0 is passed an empty tile is created
 * if a non 0 integer is passed the tile is not empty
 * values should be a power of 2 (2^n)
 * @param value - value of the tile
 */
	public void setValue(int value) 
	{
		this.value = value;
		determineColour(value);
	
		if(value !=0)
		{
			txtNum.setText(""+value);
			empty =false;	
		}
		else
		{
			txtNum.setText("");
			empty = true;
		}
		
		if(value > 65536)
			txtNum.setStyle("-fx-fill: "+tCol+";-fx-font-size: 30px;");
		else
			txtNum.setStyle("-fx-fill: "+tCol+";-fx-font-size: 40px;");
	
		txtNum.setFontSmoothingType(FontSmoothingType.GRAY);	
		setGraphic(txtNum);
		setAlignment(Pos.CENTER);	
		setStyle("-fx-background-color: "+bCol+"; -fx-background-radius: 3;");
		
	}

/**
 * returns if the tile is empty
 * if the value of the tile is 0, it is empty
 * else it is not empty
 * @return boolean true for empty, false for not empty
 */
	public boolean isEmpty() {
		return empty;
	}

/**
 * sets the tile to empty or not empty
 * @param empty, boolean
 */
	public void setEmpty(boolean empty) {
		this.empty = empty;
	}

	/**
	 * @return boolean, true if the tile can merge, false if it can't
	 */
	public boolean isMerge() {
		return merge;
	}

	/** 
	 * @param merge, boolean, true if the tile can merge, false if it cannot merge
	 */
	public void setMerge(boolean merge) {
		this.merge = merge;
	}

}
