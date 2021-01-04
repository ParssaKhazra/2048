package application;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


public class Main extends Application {

	//global variables
	private Tile[][] tileContainer, undoGrid, checkGrid;
	private boolean moved, arrow;
	private int count,points, hPoints, tempPoints;
	private Random rand;
	private Text txtStart, sText, hText;
	private Button btnStart, btnUndo, btnLoad, btnSave;
	private Label scoreText,highScoreText;


	/*
	 *parssa khazra
	 *2048 cpt
	 *mr. bulhao
	 *1/1/2018
	 */
	public void start(Stage primaryStage) {
		try 
		{
			//event handler for close button
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				public void handle(WindowEvent e) {

					// create alert and ask the user if they want to quit
					int quit = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "2048", JOptionPane.YES_NO_OPTION);

					// if yes, close else dont close
					if(quit == JOptionPane.YES_OPTION)
					{
						writeHighScore();
						Platform.exit();
					}
					else
						e.consume();
				}
			});
			
			hPoints=0;
			points =0;
			// so you cannot resize the stage
			primaryStage.setResizable(false);

			//create a grid pane and set some properties
			GridPane root = new GridPane();
			root.setPadding(new Insets(15,15,15,15));
			root.setVgap(20);

			// set the scene and the background colour
			Scene scene = new Scene(root,490,750);
			root.setStyle("-fx-background-color:#faf8ef;");

			//title and score
			// title text and setting some properties
			Text text = new Text("2048");
			text.setFontSmoothingType(FontSmoothingType.GRAY);
			text.setStyle("-fx-font-family: Clear Sans; -fx-font-weight: bold; -fx-fill: #776e65;-fx-font-size: 74px;");

			//label for the title and set the size
			Label title = new Label();
			title.setGraphic(text);
			title.setPrefSize(180, 66);

			//create a grid pane for the score box and set various properties
			GridPane scoreBox = new GridPane();
			scoreBox.setStyle("-fx-background-color: #bbada0; -fx-background-radius: 5;");
			scoreBox.setHgap(10);
			scoreBox.setAlignment(Pos.CENTER);
			scoreBox.setPrefWidth(150);

			//create a text object to display "score" 
			Text txtScore = new Text("SCORE");
			txtScore.setStyle("-fx-font-family: Clear Sans; -fx-fill: #eee4da;-fx-font-size: 15px; -fx-font-weight: bold;");
			txtScore.setFontSmoothingType(FontSmoothingType.GRAY);
			txtScore.setTextAlignment(TextAlignment.CENTER);

			// create a label to display the text object
			Label score = new Label();
			score.setGraphic(txtScore);
			score.setAlignment(Pos.CENTER);
			score.setPrefWidth(150);

			//create a text object to hold the value of the score and set various properties
			sText = new Text("0");
			sText.setStyle("-fx-font-family: Clear Sans; -fx-fill: white;-fx-font-size: 30px; -fx-font-weight: bold;");
			sText.setFontSmoothingType(FontSmoothingType.GRAY);
			sText.setTextAlignment(TextAlignment.CENTER);

			//create a label to display the the score value text object
			scoreText = new Label();
			scoreText.setGraphic(sText);
			scoreText.setAlignment(Pos.CENTER);
			scoreText.setPrefWidth(150);

			//add the labels to the grid pane
			scoreBox.add(score, 0,0);
			scoreBox.add(scoreText, 0, 1);

			//create a grid pane for the high score box
			GridPane highScoreBox = new GridPane();
			highScoreBox.setStyle("-fx-background-color: #bbada0; -fx-background-radius: 5;");
			highScoreBox.setHgap(10);
			highScoreBox.setAlignment(Pos.CENTER);
			highScoreBox.setPrefWidth(150);

			// create a text object to hold the text "high score" and set various properties
			Text txtHighScore = new Text("HIGH SCORE");
			txtHighScore.setStyle("-fx-font-family: Clear Sans; -fx-fill: #eee4da;-fx-font-size: 15px; -fx-font-weight: bold;");
			txtHighScore.setFontSmoothingType(FontSmoothingType.GRAY);
			txtHighScore.setTextAlignment(TextAlignment.CENTER);

			// create a label to display the text object above
			Label highScore = new Label();
			highScore.setGraphic(txtHighScore);
			highScore.setAlignment(Pos.CENTER);
			highScore.setPrefWidth(150);

			// create text object for high score and set various properties
			hText = new Text("0");
			hText.setStyle("-fx-font-family: Clear Sans; -fx-fill: white;-fx-font-size: 30px; -fx-font-weight: bold;");
			hText.setFontSmoothingType(FontSmoothingType.GRAY);
			hText.setTextAlignment(TextAlignment.CENTER);

			// create a label to display the text object above
			highScoreText = new Label();
			highScoreText.setGraphic(hText);
			highScoreText.setAlignment(Pos.CENTER);
			highScoreText.setPrefWidth(150);

			// add these to the grid pane
			highScoreBox.add(highScore, 0,0);
			highScoreBox.add(highScoreText, 0, 1);

			// create a HBox to hold the title, and score boxes
			HBox top = new HBox();
			top.setSpacing(25);
			top.getChildren().addAll(title, highScoreBox ,scoreBox );

			// buttons	
			//text object for the text "new game"
			txtStart = new Text("New Game");
			txtStart.setFontSmoothingType(FontSmoothingType.GRAY);
			txtStart.setStyle("-fx-fill: white; -fx-font-family: Clear Sans; -fx-font-size: 18px; -fx-font-weight:bold;");

			//text object for the text "Load Game"
			Text txtLoad= new Text("Load Game");
			txtLoad.setFontSmoothingType(FontSmoothingType.GRAY);
			txtLoad.setStyle("-fx-fill:white; -fx-font-family: Clear Sans; -fx-font-size: 18px; -fx-font-weight:bold;");

			//text object for the text "Save Game"
			Text txtSave = new Text("Save Game");
			txtSave.setFontSmoothingType(FontSmoothingType.GRAY);
			txtSave.setStyle("-fx-fill: white; -fx-font-family: Clear Sans; -fx-font-size: 18px; -fx-font-weight:bold;");

			//text object for the text " Undo"
			Text txtUndo = new Text("Undo"); // \u21B6undo arrow
			txtUndo.setFontSmoothingType(FontSmoothingType.GRAY);
			txtUndo.setStyle("-fx-fill: white; -fx-font-family: Clear Sans; -fx-font-size: 18px; -fx-font-weight:bold;");

			//create the undo button and set various properties
			btnUndo = new Button();
			btnUndo.setGraphic(txtUndo);
			btnUndo.setAlignment(Pos.CENTER);
			btnUndo.setStyle("-fx-background-color: #8f7a66;");
			btnUndo.setPrefWidth(110);
			btnUndo.setFocusTraversable(false);
			btnUndo.setOnAction(e-> undoMove());

			//create a start button  and set various properties
			btnStart = new Button();
			btnStart.setGraphic(txtStart);
			btnStart.setAlignment(Pos.CENTER);
			btnStart.setStyle("-fx-background-color: #8f7a66;");
			btnStart.setPrefWidth(110);
			btnStart.setFocusTraversable(false);
			btnStart.setOnAction(e-> startGame());

			// create the load button and set various properties
			btnLoad = new Button();
			btnLoad.setGraphic(txtLoad);
			btnLoad.setAlignment(Pos.CENTER);
			btnLoad.setPrefWidth(110);
			btnLoad.setFocusTraversable(false);
			btnLoad.setStyle("-fx-background-color: #8f7a66;");
			btnLoad.setOnAction(new EventHandler<ActionEvent>() {
				public void handle(ActionEvent e)
				{
					// create a file choose and set extensions + initial directory
					FileChooser fc = new FileChooser();
					fc.getExtensionFilters().add(new ExtensionFilter("text files","*.txt"));
					fc.setInitialDirectory(new File("..."));

					// create new file
					File f = fc.showOpenDialog(primaryStage);

					// if the file exists...
					if(f != null)
					{
						try 
						{
							// Instantiate variables and buffered reader
							int x=0;
							BufferedReader b = new BufferedReader(new FileReader(f));

							String[] data;
							String line = b.readLine();

							// while there is something to be read....
							while(line != null)
							{
								// set the array
								data = line.split(",");

								// cycle through the array
								for(int i=0; i<data.length; i++)
								{
									// if the length is 4, its a tile value, else the first value is the score, second is the highScore
									if(data.length == 4)
									{
										tileContainer[x][i].setValue(Integer.parseInt(data[i]));
										checkGrid[x][i].setValue(Integer.parseInt(data[i]));
									}
									else
									{
										points = Integer.parseInt(data[0]);
	
									}
								}
								// update variables
								x++;
								line = b.readLine();
							}
							// close the buffered reader and clone the grid
							b.close();
							cloneGrid();

							count=1;

							// update text objects and labels
							sText.setText(""+points);
							scoreText.setGraphic(sText);

							setHighscore();
							update();

							txtStart.setText("Restart");
							btnStart.setGraphic(txtStart);
							
							tempPoints=0;
						}
						catch (FileNotFoundException e1 ) 
						{
							e1.printStackTrace();
						}
						catch (IOException e1) 
						{
							e1.printStackTrace();
						}
					}
				}
			});

			//create button for saving and set various properties 
			btnSave = new Button();
			btnSave.setGraphic(txtSave);
			btnSave.setAlignment(Pos.CENTER);
			btnSave.setPrefWidth(110);
			btnSave.setFocusTraversable(false);
			btnSave.setStyle("-fx-background-color: #8f7a66;");
			btnSave.setOnAction( new EventHandler<ActionEvent>(){
				public void handle(ActionEvent e)
				{
					//create a file chooser and set various properties
					FileChooser fc = new FileChooser();
					fc.getExtensionFilters().add(new ExtensionFilter("text files","*.txt"));
					fc.setInitialDirectory(new File("..."));

					// create a file
					File f = fc.showSaveDialog(primaryStage);

					// if the file exists...
					if(f != null)
					{
						try
						{
							// create a buffered writer
							BufferedWriter out = new BufferedWriter(new FileWriter(f));

							// write the grid
							for(int i=0;i<4; i++)
							{
								out.write(tileContainer[i][0].getValue()+","+tileContainer[i][1].getValue()+","
										+tileContainer[i][2].getValue()+","+tileContainer[i][3].getValue());
								out.newLine();
							}
							// write the score
							out.write(points+"");

							//close the buffered writer
							out.close();
						}
						catch(IOException ex)
						{
							// Output an error message if the data cannot be written to the file
							JOptionPane.showMessageDialog(null, "Problem writing to file! IOException: " + ex.getMessage());
						}
					}
					writeHighScore();
				}
			});

			//create a HBox for the buttons and set various properties
			HBox btns = new HBox();
			btns.setPadding(new Insets(15,0,15,0));
			btns.setSpacing(10);
			btns.setAlignment(Pos.CENTER);
			btns.getChildren().addAll(btnStart, btnSave, btnLoad, btnUndo);

			//game board
			//create a gridPane for the game board and set various properties
			GridPane board = new GridPane();
			board.setPrefSize(470, 470);
			board.setHgap(7.5);
			board.setVgap(7.5);
			board.setPadding(new Insets (7.5,7.5,7.5,7.5));
			board.setStyle("-fx-background-color: #bbada0; -fx-background-radius:3");

			// Instantiate the grid, undo grid and random
			tileContainer = new Tile[4][4];
			undoGrid = new Tile[4][4];
			checkGrid =new Tile[4][4];
			rand = new Random();
			arrow =true;

			// initialize the tiles in the arrays and add them to the game board
			for(int i =0; i<4; i++)
			{
				for (int j=0; j< 4; j++)
				{
					Tile t= new Tile();
					Tile t1= new Tile();
					Tile t2 = new Tile();
					tileContainer[i][j] =t;
					undoGrid[i][j]= t1;
					checkGrid[i][j] =t2;
					board.add(t, j, i);
				}
			}

			//create a text object
			Text how2play = new Text();
			how2play.setText("New game to start. Arrow keys to move. 2+2=4.");
			how2play.setStyle("-fx-fill:#8f7a66; -fx-font-size: 20.5px; -fx-font-weight:bold;");
			how2play.setFontSmoothingType(FontSmoothingType.GRAY);
			how2play.setTextAlignment(TextAlignment.CENTER);

			// create label for the text object
			Label lblplay = new Label();
			lblplay.setGraphic(how2play);
			lblplay.setAlignment(Pos.CENTER);
			lblplay.setPrefWidth(470);


			// add the panes to the root
			root.add(top,0,0);
			root.add(btns, 0, 1);
			root.add(board,0, 2);
			root.add(lblplay,0,3);

			//key events
			scene.setOnKeyPressed(new EventHandler<KeyEvent>() 
			{
				public void handle(KeyEvent e)
				{	
					if(arrow)
					{
						// if they pressed right....
						if(e.getCode() == KeyCode.RIGHT)
						{
							//clone the grid, set temp-points to 0
							copyGrid();
							tempPoints =0;
							arrow=false;

							// run through the process 4 times..
							for(int x=0; x<4; x++)
							{
								// cycle through the grid....
								for(int i =0; i<4; i++)
								{	
									//start the columns from the far right...
									for(int j =3; j>0; j--)
									{
										//if the current tile is empty...
										if(tileContainer[i][j].isEmpty()) 
										{
											//the the tile to the left is not empty...
											if(!tileContainer[i][j-1].isEmpty())
											{
												// set the value of the empty tile to the occupied tile, set the occupied tile to 0
												tileContainer[i][j].setValue(tileContainer[i][j-1].getValue());
												tileContainer[i][j-1].setValue(0);
											}	

										}
										else
										{
											// if the adjacent tiles are the and and are eligible for a merge...
											if(tileContainer[i][j].getValue() == tileContainer[i][j-1].getValue() 
													&& tileContainer[i][j].isMerge() && tileContainer[i][j-1].isMerge())
											{
												//double the current tile's value, set merge to false, set the value of the right tile to 0
												tileContainer[i][j].setValue(tileContainer[i][j-1].getValue()*2);
												tileContainer[i][j].setMerge(false);
												tileContainer[i][j-1].setValue(0);

												//add to temp points and points
												tempPoints += tileContainer[i][j].getValue();
												points += tileContainer[i][j].getValue();										
											}

										}
									}	
								}
							}

							determineMoved();
							// if the tiles moved, spawn in another tile and clone the grid
							if(moved)
							{
								spawn();
								cloneGrid();	
							}

							//update the game and check for game over
							update();
							checkGameOver();
						} 
						// if they pressed left
						else if(e.getCode() == KeyCode.LEFT)
						{
							//clone the grid, set temp-points to 0
							copyGrid();
							tempPoints =0;
							arrow=false;

							// same loop logic as above, but starting from the left
							for(int x=0; x<4; x++)
							{
								for(int i =0; i<4; i++)
								{	
									for(int j =0; j<3; j++)
									{
										// if the current tile is empty...
										if(tileContainer[i][j].isEmpty()) 
										{
											// if the tile to the left is not empty...
											if(!tileContainer[i][j+1].isEmpty())
											{
												//set the empty tiles value, set the occupied tiles value to 0
												tileContainer[i][j].setValue(tileContainer[i][j+1].getValue());
												tileContainer[i][j+1].setValue(0);
												moved =true;

											}

										}
										else
										{
											// if the adjacent tiles are identical and eligible for a merge....
											if(tileContainer[i][j].getValue() == tileContainer[i][j+1].getValue() 
													&& tileContainer[i][j].isMerge() && tileContainer[i][j+1].isMerge())
											{
												//same merge and point logic as above
												tileContainer[i][j].setValue(tileContainer[i][j+1].getValue()*2);
												tileContainer[i][j].setMerge(false);
												tileContainer[i][j+1].setValue(0);

												points += tileContainer[i][j].getValue();
												tempPoints += tileContainer[i][j].getValue();
												moved =true;
											}

										}
									}	
								}
							}

							determineMoved();
							// end of move updates, same as above
							if(moved)
							{
								spawn();
								cloneGrid();
							}

							update();
							checkGameOver();
						}
						// if the pressed up
						else if(e.getCode() == KeyCode.UP)
						{
							//clone the grid, set temp-points to 0
							copyGrid();
							tempPoints=0;
							arrow=false;

							//same loop logic as above, starting at the top
							for(int x=0; x<4; x++)
							{
								for(int i =0; i<4; i++)
								{	
									for(int j =0; j<3; j++)
									{
										//rows and columns are swapped to check up and down...
										//if the current tile is empty
										if(tileContainer[j][i].isEmpty()) 
										{
											//if the tile above it is not empty
											if(!tileContainer[j+1][i].isEmpty())
											{
												//shift the tiles up (to the empty tile)
												tileContainer[j][i].setValue(tileContainer[j+1][i].getValue());
												tileContainer[j+1][i].setValue(0);

											}	

										}
										else
										{
											//if the adjacent tiles are the same and eligible for a merge...
											if(tileContainer[j][i].getValue() == tileContainer[j+1][i].getValue()
													&& tileContainer[j][i].isMerge() && tileContainer[j+1][i].isMerge())
											{
												// same merge logic as above, merges in the up direction
												tileContainer[j][i].setValue(tileContainer[j+1][i].getValue()*2);
												tileContainer[j][i].setMerge(false);
												//	tileContainer[j+1][i].setMerge(false);
												tileContainer[j+1][i].setValue(0);

												points += tileContainer[j][i].getValue();
												tempPoints += tileContainer[j][i].getValue();

											}

										}
									}	
								}
							}

							determineMoved();
							// same end of turn logic as above
							if(moved)
							{	
								cloneGrid();
								spawn();
							}

							update();
							checkGameOver();
						}

						// if they pressed down
						else if(e.getCode() == KeyCode.DOWN)
						{
							//clone the grid, set temp-points to 0
							copyGrid();
							tempPoints=0;
							arrow=false;

							// same loop logic as above 
							for(int x=0; x<4; x++)
							{
								for(int i =0; i<4; i++)
								{	
									//starting at the bottom
									//rows and columns are swapped again
									for(int j =3; j>0; j--)
									{
										// if the current tile is empty
										if(tileContainer[j][i].isEmpty()) 
										{
											// if the tile below it is not empty....
											if(!tileContainer[j-1][i].isEmpty())
											{
												// shift the tile down to the empty tile
												tileContainer[j][i].setValue(tileContainer[j-1][i].getValue());
												tileContainer[j-1][i].setValue(0);

											}

										}
										else
										{
											//if the adjacent tiles are the same and they are both eligible for a merge....
											if(tileContainer[j][i].getValue() == tileContainer[j-1][i].getValue()
													&& tileContainer[j][i].isMerge() && tileContainer[j-1][i].isMerge())
											{
												// same merge logic as above, but merges down
												tileContainer[j][i].setValue(tileContainer[j-1][i].getValue()*2);
												tileContainer[j][i].setMerge(false);
												tileContainer[j-1][i].setValue(0);

												points += tileContainer[j][i].getValue();
												tempPoints += tileContainer[j][i].getValue();

											}

										}
									}	
								}
							}

							determineMoved();
							// same end of turn logic as above
							if(moved)
							{
								cloneGrid();
								spawn();
							}

							update();
							checkGameOver();
						}	
					}
				}

			});

			// set the scene and show it
			primaryStage.setScene(scene);
			primaryStage.show();

			setHighscore();
			
		} 
		catch(Exception e) 
		{
			e.printStackTrace();
		}
	}

	// check game over method
	private void checkGameOver()
	{
		// Instantiate a boolean
		boolean valid = false;

		// run through the grid
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				// if a tile is empty it is not game over
				if (tileContainer[i][j].isEmpty())
				{
					valid = true;
					break;
				}
				else
				{
					// if the columns are less than 3
					if (j<3)
					{
						// if the adjacent tiles to the right are the same, it is not game over
						if(tileContainer[i][j].getValue() == tileContainer[i][j+1].getValue())
						{
							valid = true;
							break;
						}

						// if the adjacent tiles to the bottom are the same it is not a game over
						if(tileContainer[j][i].getValue() == tileContainer[j+1][i].getValue())
						{
							valid = true;
							break;
						}
					}			

				}
			}

			if (valid)
			{
				arrow =true;
				break;			
			}
		}

		// if there is a game over
		if(!valid)
		{
			//disable buttons
			btnStart.setDisable(true);
			btnLoad.setDisable(true);
			btnUndo.setDisable(true);
			btnSave.setDisable(true);
			//create a thread...
			Thread t = new Thread(new Runnable()
			{
				public void run()
				{					
					// call the reset method
					reset();
				}
			});
			t.start();

		}

	}

	//reset method
	private void reset()
	{
		//create a JOptionPane
		int lose  = JOptionPane.showConfirmDialog(null,"GAME OVER\nDo you want to play again?", "2048", JOptionPane.YES_NO_OPTION );

		Platform.runLater(new Runnable() {
			@Override
			public void run() {

				//if they said yes, restart the game
				if(lose == JOptionPane.YES_OPTION)
				{
					// reset the game
					txtStart.setText("New Game");
					clearBoard();
					startGame();
					points =0;
					sText.setText(""+points);
					scoreText.setGraphic(sText);
					arrow = true;
				}
				else 
				{
					// close the game
					writeHighScore();
					Platform.exit();
				}
			}
		});
	}
	
	//undo move method
	private void undoMove() 
	{	
		boolean valid =false;
		
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				// set the displayed board values to the undo board's values
				if (!undoGrid[i][j].isEmpty())
				{
					valid = true;
					break;
				}
			}
			if(valid)
				break;
		}

		//so the user cannot wipe the board with an undo 
		if (valid)
		{
			// cycle through the array
			for(int i=0; i<4; i++)
			{
				for(int j=0; j<4; j++)
				{
					// set the displayed board values to the undo board's values
					tileContainer[i][j].setValue(undoGrid[i][j].getValue());
				}
			}

			// subtract the points they earned, set a new value, update the score
			points -=tempPoints;
			sText.setText(""+points);
			tempPoints =0;
		}

	}

	//method for cloning the grid
	private void cloneGrid() 
	{
		// cycle through the array
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				// set the values of the undo grid to the checkGrid
				/*check grid is copied at the beginning of the turn, if there is a difference 
				 * between the tileContainer and checkGrid a turn has been made, so copy the checkGrid
				 * to the undoGrid as that will be the tileContainer prior to the move
				 */
				undoGrid[i][j].setValue(checkGrid[i][j].getValue());
			}
		}	
	}

	// start game method
	private void startGame()
	{
		//if the text is set to restart...
		if(txtStart.getText().equals("Restart"))
		{
			// create an alert to ask them if they want to restart

			int lose = JOptionPane.showConfirmDialog(null, "Are you sure you want to restart?\nYour progress will not be saved.", "2048", JOptionPane.YES_NO_OPTION);

			// if they say yes... and don't do anything if they say no
			if(lose == JOptionPane.YES_NO_OPTION)
			{
				// clear board change text to start and call the method again
				clearBoard();
				txtStart.setText("New Game");
				startGame();
			}

		}
		else
		{
			// setting variables, text and labels
			points =0;
			count =2;
			spawn();

			txtStart.setText("Restart");
			btnStart.setGraphic(txtStart);

			sText.setText(""+points);
			scoreText.setGraphic(sText);
		}
		
		//enable buttons
		btnStart.setDisable(false);
		btnLoad.setDisable(!true);
		btnUndo.setDisable(!true);
		btnSave.setDisable(!true);
	}

	//spawning method 
	public void spawn()
	{
		// declare variables
		int x,y, val;

		// cycle through how many tiles should be spawned
		for( int i=0; i<count; i++) 
		{
			// do this while the coordinates are not occupied
			do
			{
				// generate two random numbers
				x= rand.nextInt(4);
				y= rand.nextInt(4);

			} while(!tileContainer[x][y].isEmpty());

			// 1 in 10 chance for a 4, 9 in 10 chance for a 2
			if (rand.nextInt(10)+1 == 1)
			{
				val = 4;
			}
			else
			{
				val =2;
			}

			//set the random position to the value 
			tileContainer[x][y].setValue(val);
		}

		// set count
		count =1;

	}
	
	//update method
	public void update()
	{
		//reset the merge variable
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				tileContainer[i][j].setMerge(true);
			}
		}	

		//set the text
		sText.setText(""+points);

		// if they broke their high score...
		if (points >=hPoints)
		{
			//make the high score into the score
			hPoints = points;
			hText.setText(""+hPoints);
		}

	}

	//clear board method
	public void clearBoard()
	{
		//cycle through the array
		for(int i =0; i<4; i++) 
		{
			for(int j=0; j<4; j++) 
			{
				// set the values of the undoGrid and tileContainer to 0
				tileContainer[i][j].setValue(0);
				undoGrid[i][j].setValue(0);
			}
		}
	}

	//copy grid method
	private void copyGrid()
	{
		//cycle through the array
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				//set the values of check grid to the main grid
				checkGrid[i][j].setValue(tileContainer[i][j].getValue());
			}
		}
	}

	// determine move method
	private void determineMoved()
	{
		boolean valid= false;
		//cycle through the array
		for(int i=0; i<4; i++)
		{
			for(int j=0; j<4; j++)
			{
				// if there is a difference between the 2 grids that means a move was made
				if(checkGrid[i][j].getValue() != tileContainer[i][j].getValue())
				{
					valid =true;
					break;
				}
			}
			if(valid)
				break;
		}

		// if a move was made moved =true, else moved =false
		if(valid)
			moved=true;
		else
			moved =false;
	}
	
	//setting the high score
	private void setHighscore()
	{
		File f =new File("highScore.dat");

		// if the file exists...
		if(f != null)
		{
			try 
			{
				// Instantiate variables and buffered reader
				int x=0;
				DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));

				//set the line and the highscore
				x = in.readInt();
				if(hPoints<x)
					hPoints =x;
				// close the buffered reader and clone the grid
				in.close();
			}
			catch(Exception e)
			{
				
			}
		}
		
		// set the text/labels
		hText.setText(""+ hPoints);
		highScoreText.setGraphic(hText);
	}
	
	//writing the high score
	private void writeHighScore()
	{
		File f =new File("highScore.dat");

		// if the file exists...
		if(f != null)
		{
			try 
			{
				// Instantiate data output stream
				DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));

				//write the highscore
				out.writeInt(hPoints);
				// close the buffered reader and clone the grid
				out.close();
			}
			catch(Exception e)
			{
				
			}
		}
		
	}
	//main
	public static void main(String[] args) 
	{
		launch(args);
	}
}
