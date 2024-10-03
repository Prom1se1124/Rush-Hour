import tester.Tester;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javalib.impworld.World;
import javalib.impworld.WorldScene;
import javalib.worldimages.*;



// Represent a List of IVehicle
interface ILoVehicle {

  // check if IVehicle has overlap to a list of vehicle
  boolean hasOverlapping(Vehicle v);

  WorldImage placeVehicles(WorldImage acc, int cellSize);

  // Find target car and determine if it is at exit
  boolean hasWon(Area exit);

  // Check if there is any overlapping the the list
  boolean checkOverlaps();
  
  // Checks if any vehicles are out of bounds
  boolean checkOutBounds(int cols, int rows);
  
  // Updates selected vehicle
  ILoVehicle updateSelected(Posn pos);
  
  // Updates the position of a vehicle
  ILoVehicle updatePosn(String keyName,boolean isKlo);
  
  // Finds the currently selected vehicle if there is one
  Vehicle selectedVehicle();
  
  // find the last moved vehicle
  Vehicle movedVehicle();

}

// Empty list of IVehicle
class MtLoVehicle implements ILoVehicle {
  // Represent an Empty List
  MtLoVehicle() {
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... ...
   * METHODS
   * ... this.hasOverlapping(IVehicle) ... -- boolean
   * ... this.placeVehicles(WorldScene, int) ... -- WorldScene
   * ... this.hasWonHelper(Posn) ... -- boolean
   * ... this.checkOverLaps() ... -- boolean
   * ... this.checksOutBounds(int cols, int rows) -- boolean
   * ... this.updateSelected(Posn pons) -- ILoVehicle
   * ... this.updatePosn(String keyName) -- ILoVehicle
   * ... this.selectedVehicle() -- Vehicle
   */

  // return false in the base case
  public boolean hasOverlapping(Vehicle v) {
    return false;
  }
  
  // place Vehicles into the scene
  public WorldImage placeVehicles(WorldImage acc, int cellSize) {
    return acc;
  }

  // return false in the base case
  public boolean hasWon(Area exit) {
    return false;
  }

  // check if there are overlaps of vehicles in the list
  public boolean checkOverlaps() {
    return false;
  }
  
  // check if there are outbounding vehicles in the list
  public boolean checkOutBounds(int cols, int rows) {
    return false;
  }
  
  // Updates selected vehicle
  public ILoVehicle updateSelected(Posn pos) {
    return new MtLoVehicle();
  }
  
  // updates posn of list of vehicles
  public ILoVehicle updatePosn(String keyName,boolean isKlo) {
    return new MtLoVehicle();
  }
  
  // return the selected vehicle in the list
  public Vehicle selectedVehicle() {
    return null;
  }
  
  // find the last moved vehicle
  public Vehicle movedVehicle() {
    return null;
  }
  
}

// Represent a Cons of IVehicle
class ConsLoVehicle implements ILoVehicle {
  Vehicle first;
  ILoVehicle rest;

  // Represent a Cons of IVehicle
  ConsLoVehicle(Vehicle first, ILoVehicle rest) {
    this.first = first;
    this.rest = rest;
  }
  
  /* TEMPLATE
   * FIELDS:
   * ... this.first ... -- IVehicle
   * ... this.rest ... -- ILoVehicle
   * METHODS
   * ... this.hasOverlapping(IVehicle) ... -- boolean
   * ... this.placeVehicles(WorldScene, int) ... -- WorldScene
   * ... this.hasWonHelper(Posn) ... -- boolean
   * ... this.checkOverLaps() ... -- boolean
   * ... this.checksOutBounds(int cols, int rows) -- boolean
   * ... this.updateSelected(Posn pons) -- ILoVehicle
   * ... this.updatePosn(String keyName) -- ILoVehicle
   * ... this.selectedVehicle() -- Vehicle
   */

  // Determine whether a vehicle is overlapped to a list of vehicles
  public boolean hasOverlapping(Vehicle v) {
    if (v.isOverlapping(this.first)) {
      return true;
    }
    else {
      return this.rest.hasOverlapping(v);
    }
  }

  // place the vehicle into the scene
  public WorldImage placeVehicles(WorldImage acc, int cellSize) {
    return this.rest.placeVehicles(first.draw(acc, cellSize), cellSize);
  }



  // Determine if the player win
  public boolean hasWon(Area exit) {
    if (this.first.isTarget && first.area.isOverlapping(exit)) {
      return true;
    }
    
    return this.rest.hasWon(exit);
  }

  // check if there is any isOverlapping in the list of vehicle
  public boolean checkOverlaps() {
    if (this.rest.hasOverlapping(this.first)) {
      return true;
    }
    else {
      return this.rest.checkOverlaps();
    }
  }

  // check if there are outbounding vehicles in the list
  public boolean checkOutBounds(int cols, int rows) {
    return this.first.outOfBounds(cols,rows) || this.rest.checkOutBounds(cols, rows);
  }
  
  // update the select state in the list
  public ILoVehicle updateSelected(Posn pos) {
    return new ConsLoVehicle(
        this.first.updateSelected(this.first.touchingPoint(pos)),this.rest.updateSelected(pos));
  }
  
  // update the posn of vehicles
  public ILoVehicle updatePosn(String keyName, boolean isKlo) {
    return new ConsLoVehicle(this.first.updatePosn(keyName, isKlo),
        this.rest.updatePosn(keyName,isKlo));
  }
  
  // find the selected vehicle
  public Vehicle selectedVehicle() {
    if (this.first.isSelected) {
      return this.first;
    }
    else {
      return this.rest.selectedVehicle();
    }
  }
  
  // find the last moved vehicle
  public Vehicle movedVehicle() {
    if (this.first.isMoved) {
      return this.first;
    }
    else {
      return this.rest.movedVehicle();
    }
  }
  
}

// represents a space in the board
class Area {
  Posn pos;
  int width;
  int height;
  
  // posn is the left-top corner of the area
  Area(Posn pos,int width,int height) {
    this.pos = pos;
    this.width = width;
    this.height = height;
  }
  
  // check if two area are overlapping
  boolean isOverlapping(Area that) {
    if (this.pos.x + this.width <= that.pos.x || that.pos.x + that.width <= this.pos.x) {
      return false;
    }
    return ! (this.pos.y + this.height <= that.pos.y || that.pos.y + that.height <= this.pos.y);
  }
  
  // check if the area is out of bound
  boolean outOfBounds(int col, int row) {
    if (this.pos.x < 1 || this.pos.y < 1) {
      return true;
    }
    return (this.pos.x + this.width > col - 1) || (this.pos.y + this.height > row - 1);


  }
  
  // check if the area touch one point
  boolean touchingPoint(Posn pos) {
    return this.pos.x <= pos.x 
        && this.pos.x + this.width - 1 >= pos.x
        && this.pos.y <= pos.y
        && this.pos.y + this.height - 1 >= pos.y;
  }
}

 
//Abstract implementation of Vehicle
class Vehicle {
  Area area;
  boolean isTarget;
  boolean isSelected;
  boolean isObstacle;
  boolean isMoved;
  Color color;
    
  Vehicle(Area area, boolean isTarget, boolean isSelected,boolean isObstacle,
      boolean isMoved,Color color) {
    this.area = area;
    this.isTarget = isTarget;
    this.isSelected = isSelected;
    this.isObstacle = isObstacle;
    this.isMoved = isMoved;
    this.color = color;
  }
  
  // check if a vehicle is overlapping with another vehicle
  public boolean isOverlapping(Vehicle that) {
    return this.area.isOverlapping(that.area);
  }
  
  // check if a vehicle is out of bound
  public boolean outOfBounds(int col, int row) {
    return this.area.outOfBounds(col, row);
  }
  
  // check if a vehicle touch one point
  public boolean touchingPoint(Posn pos) {
    return this.area.touchingPoint(pos);
  }
  
  // draw the vehicle
  public WorldImage draw(WorldImage background, int cellSize) {
    int width = this.area.width * cellSize;
    int height = this.area.height * cellSize;
    Color color1;
    if (this.isObstacle) {
      color1 = Color.BLACK;
    }
    else if (this.isSelected) {
      color1 = Color.YELLOW;
    }
    else if (this.isTarget) {
      color1 = Color.RED;
    }
    else {
      color1 = this.color;
    }
    return new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, new RectangleImage(width - 4,
            height - 4, OutlineMode.SOLID, color1), 
        - this.area.pos.x * cellSize - 2, - this.area.pos.y * cellSize - 2, background);
  }
  
  // update the select state of the vehicle
  public Vehicle updateSelected(boolean selected) {
    if (! this.isObstacle) {
      return new Vehicle(this.area, this.isTarget, selected,this.isObstacle,
          this.isMoved, this.color);
    }
    return new Vehicle(this.area, this.isTarget, false, this.isObstacle,this.isMoved, Color.BLACK);
  }
  
  // update the posn of the vehicle
  public Vehicle updatePosn(String keyName,boolean isKlo) {
    if (this.isSelected) {
      if (keyName.equals("up") && (this.area.height > this.area.width || isKlo)) {
        return new Vehicle(new Area(new Posn(this.area.pos.x,this.area.pos.y - 1),
            this.area.width,this.area.height),
            this.isTarget,this.isSelected,this.isObstacle,true,this.color);
      }
      else if (keyName.equals("down") && (this.area.height > this.area.width || isKlo)) {
        return new Vehicle(new Area(new Posn(this.area.pos.x,this.area.pos.y + 1),
            this.area.width,this.area.height),
            this.isTarget,this.isSelected,this.isObstacle,true,this.color);
      }
      else if (keyName.equals("left") && (this.area.height < this.area.width || isKlo)) {
        return new Vehicle(new Area(new Posn(this.area.pos.x - 1,this.area.pos.y),
            this.area.width,this.area.height),
            this.isTarget,this.isSelected,this.isObstacle,true,this.color);
      }
      else if (keyName.equals("right") && (this.area.height < this.area.width || isKlo)) {
        return new Vehicle(new Area(new Posn(this.area.pos.x + 1,this.area.pos.y),
            this.area.width,this.area.height),
            this.isTarget,this.isSelected,this.isObstacle,true,this.color);
      }
      else if (keyName.equals("up") || keyName.equals("down") || keyName.equals("left")
          || keyName.equals("right")) {
        return new Vehicle(this.area,this.isTarget,this.isSelected,
            this.isObstacle,true,this.color);
      }
    }
    return new Vehicle(this.area,this.isTarget,this.isSelected,this.isObstacle,false,this.color);
  }
  
}


// Utility class
class Utility {
  // Transform the given String to the Board
  ILoVehicle stringToList(String s, int targetX, int targetY, int x, int y, 
      Random r,int maxCol, int maxRow) {
    String str1 = s.substring(0, 1);
    boolean isTarget = (targetX == x && targetY == y);
    if (s.length() == 1) {
      return new MtLoVehicle();
    }
    else if (str1.equals("+") || str1.equals("-") || str1.equals("X") || str1.equals(" ")) {
      return stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow);
    }
    else if (str1.equals("|") && x < maxCol - 1 && y != 0 && x != 0) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),1,1), false, false,true,
          false,Color.BLACK),stringToList(s, targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    else if (str1.equals("|")) {
      return stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow);
    }
    else if (str1.equals("\n")) {
      return stringToList(s.substring(1), targetX,targetY, 0, y + 1,r,maxCol,maxRow);
    }
    else if (str1.equals("O")) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),1,1), false, false,true,
          false,Color.BLACK),
          stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    else if (str1.equals("T")) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),1,3), isTarget, false,false,
          false,new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))),
          stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    else if (str1.equals("t")) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),3,1), isTarget, false,false,
          false,new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))),
          stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    else if (str1.equals("C")) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),1,2), 
          isTarget, false,false,false,new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))),
          stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    else if (str1.equals("c")) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),2,1), isTarget, false,false,
          false,new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))),
          stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    
    else if (str1.equals(".")) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),1,1), false, false,false,
          false,new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))),
          stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    else if (str1.equals("S")) {
      return new ConsLoVehicle(new Vehicle(new Area(new Posn(x, y),2,2), true, false,false,
          false,new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255))),
          stringToList(s.substring(1), targetX,targetY, x + 1, y,r,maxCol,maxRow));
    }
    else {
      throw new IllegalArgumentException("Invalid String!");
    }
  }
  
  // Determines number of columns given the string
  int getCol(String s) {
    String[] parts = s.split("\n");
    int longestLength = 0;
    
    for (String part : parts) {
      if (part.length() > longestLength) {
        longestLength = part.length();
      }
    }
    return longestLength; 
  }
  
  // Determines number of rows given the string
  int getRow(String s) {
    int rows = 1;
    for (int i = 0; i < s.length(); i ++) {
      if (s.substring(i, i + 1).equals("\n")) {
        rows += 1;
      }
    }
    return rows;
  }
  
  // return the exit
  Area getExit(String s) {
    String[] lines = s.split("\n");
    int x;
    int y;
    for (int i = 0; i < lines.length; i += 1) {
      if (lines[i].contains("X")) {
        y = i;
        x = lines[i].indexOf("X");
        if (i == 0 || i == lines.length - 1) {
          return new Area(new Posn(x,y),2,1);
        }
        else {
          return new Area(new Posn(x,y),1,2);
        }
      }
    }
    throw new IllegalArgumentException("No Exit!");
  }
  
}

// Represents the RushHour World
class RushHourWorld extends World {
  static final int cellSize = 50;
  int cols;
  int rows;
  Area exit;
  ILoVehicle vehicles;
  int score;
  ArrayList<ILoVehicle> history;
  
  // initiate the world using cols, rows, exit and vehicles
  public RushHourWorld(int cols, int rows, Area exit,ILoVehicle vehicles) {
    this.cols = cols;
    this.rows = rows;
    this.exit = exit;
    this.vehicles = vehicles;
    this.score = 0;
    this.history = new ArrayList<ILoVehicle>();
    if (vehicles.checkOverlaps()) {
      throw new IllegalArgumentException("Vehicles overlap!");
    }
    if (vehicles.checkOutBounds(this.cols, this.rows)) {
      throw new IllegalArgumentException("Vehicles out of bounds!");
    }
  }
  
  // initiate the world using string and posn of target vehicle
  public RushHourWorld(String s, int targetX, int targetY) {
    this.cols = new Utility().getCol(s);
    this.rows = new Utility().getRow(s);
    this.exit = new Area(new Utility().getExit(s).pos,1,1);
    this.vehicles = new Utility().stringToList(s, targetX, targetY, 0, 0, new Random(),cols,rows);
    this.score = 0;
    this.history = new ArrayList<ILoVehicle>();
    if (vehicles.checkOverlaps()) {
      throw new IllegalArgumentException("Vehicles overlap!");
    }
    if (vehicles.checkOutBounds(this.cols, this.rows)) {
      throw new IllegalArgumentException("Vehicles out of bounds!");
    }
  }
  
  
  
  // Determines if the target vehicle has made it to the exit
  public boolean hasWon() {
    return vehicles.hasWon(exit);
  }
  

  // Makes the scene
  // Makes a background which has a board. A board has a border and the inner section 
  // alongside the cars and the exit
  public WorldScene makeScene() {
    int width = cellSize * this.cols;
    int height = cellSize * this.rows;
    WorldScene board = new WorldScene(width, height);
    WorldImage border = new RectangleImage(width, height, OutlineMode.SOLID, Color.GRAY);
    WorldImage inner = new RectangleImage(cellSize * (this.cols - 2),
            cellSize * (this.rows - 2), OutlineMode.SOLID, Color.WHITE);
    WorldImage exit = new RectangleImage(cellSize, cellSize, OutlineMode.SOLID,
            Color.PINK);
    WorldImage background = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, exit,
            - cellSize * this.exit.pos.x, - cellSize * this.exit.pos.y,
            new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.MIDDLE, inner, 0, 0, border));
    WorldImage cars = this.vehicles.placeVehicles(background,cellSize);
    board.placeImageXY(cars, width / 2, height / 2);
    board.placeImageXY(new TextImage("SCORE:" + String.valueOf(score), 30, Color.RED), 
        100, height + 20);
    return board;
  }
  
  // Makes the vehicle clicked on becomes the selected vehicle
  public void onMouseClicked(Posn pos) {  
    vehicles = vehicles.updateSelected(new Posn(pos.x / cellSize,pos.y / cellSize));
    
  }
  
  // Moves the selected vehicle based on the keys pressed
  public void onKeyEvent(String keyName) {
    if (keyName.equals("up") || keyName.equals("down") 
        || keyName.equals("right") || keyName.equals("left")) {  
      if ((! vehicles.updatePosn(keyName,false).checkOverlaps() 
          && ! vehicles.updatePosn(keyName,false).checkOutBounds(cols, rows))
          || (vehicles.updatePosn(keyName,false).hasWon(this.exit))) {
        if (vehicles.selectedVehicle() != null && ! vehicles.selectedVehicle().isMoved) {
          this.score += 1;
        }
        this.history.add(vehicles);
        this.vehicles = vehicles.updatePosn(keyName,false);
      }
    }
    
    if (keyName.equals("u")) {
      if (history.size() != 0) {
        this.vehicles = this.history.remove(history.size() - 1);
        score += 1;
      }
    }
    
    if (this.hasWon()) {
      this.endOfWorld("Level Complete!");
    }
  }
  
  // Displays a message when the player has won
  public WorldScene lastScene(String msg) {
    WorldScene winScreen = this.makeScene();
    winScreen.placeImageXY(new TextImage(msg, 50, Color.RED), 
        cols * cellSize / 2,rows * cellSize / 2);
    return winScreen;
  }
  
  // Outputs the selected vehicle
  public Vehicle selectedVehicle() {
    return this.vehicles.selectedVehicle();
  }
  
}

// Klotski game world
class KlotskiWorld extends World {
  static final int cellSize = 50;
  int cols;
  int rows;
  Area exit;
  ILoVehicle vehicles;
  int score;
  ArrayList<ILoVehicle> history;
  
  // initiate the world using cols, rows,exit, and vehicles
  public KlotskiWorld(int cols, int rows, Area exit,ILoVehicle vehicles) {
    this.cols = cols;
    this.rows = rows;
    this.exit = exit;
    this.vehicles = vehicles;
    this.score = 0;
    this.history = new ArrayList<ILoVehicle>();
    if (vehicles.checkOverlaps()) {
      throw new IllegalArgumentException("Vehicles overlap!");
    }
    if (vehicles.checkOutBounds(this.cols, this.rows)) {
      throw new IllegalArgumentException("Vehicles out of bounds!");
    }
  }
  
  // initiate the world using string
  public KlotskiWorld(String s) {
    this.cols = new Utility().getCol(s);
    this.rows = new Utility().getRow(s);
    this.exit = new Utility().getExit(s);
    this.vehicles = new Utility().stringToList(s, 0, 0, 0, 0, new Random(),cols,rows);
    this.score = 0;
    this.history = new ArrayList<ILoVehicle>();
    if (vehicles.checkOverlaps()) {
      throw new IllegalArgumentException("Vehicles overlap!");
    }
    if (vehicles.checkOutBounds(this.cols, this.rows)) {
      throw new IllegalArgumentException("Vehicles out of bounds!");
    }
  }
  
  
  
  // Determines if the target vehicle has made it to the exit
  public boolean hasWon() {
    return vehicles.hasWon(exit);
  }
  

  // Makes the scene
  // Makes a background which has a board. A board has a border and the inner section 
  // alongside the cars and the exit
  public WorldScene makeScene() {
    int width = cellSize * this.cols;
    int height = cellSize * this.rows;
    WorldScene board = new WorldScene(width, height);
    WorldImage border = new RectangleImage(width, height, OutlineMode.SOLID, Color.GRAY);
    WorldImage inner = new RectangleImage(cellSize * (this.cols - 2),
            cellSize * (this.rows - 2), OutlineMode.SOLID, Color.WHITE);
    WorldImage exit = new RectangleImage(cellSize * this.exit.width, 
        cellSize * this.exit.height, OutlineMode.SOLID,Color.PINK);
    WorldImage background = new OverlayOffsetAlign(AlignModeX.LEFT, AlignModeY.TOP, exit,
            - cellSize * this.exit.pos.x, - cellSize * this.exit.pos.y,
            new OverlayOffsetAlign(AlignModeX.CENTER, AlignModeY.MIDDLE, inner, 0, 0, border));
    WorldImage cars = this.vehicles.placeVehicles(background,cellSize);
    board.placeImageXY(cars, width / 2, height / 2);
    board.placeImageXY(new TextImage("SCORE:" + String.valueOf(score), 30, Color.RED), 
        100, height + 20);
    return board;
  }
  
  // Makes the vehicle clicked on becomes the selected vehicle
  public void onMouseClicked(Posn pos) {  
    vehicles = vehicles.updateSelected(new Posn(pos.x / cellSize,pos.y / cellSize));
    
  }
  
  // Moves the selected vehicle based on the keys pressed
  public void onKeyEvent(String keyName) {
    if (keyName.equals("up") || keyName.equals("down") 
        || keyName.equals("right") || keyName.equals("left")) {  
      if ((! vehicles.updatePosn(keyName,true).checkOverlaps() 
          && ! vehicles.updatePosn(keyName,true).checkOutBounds(cols, rows))
          || (vehicles.updatePosn(keyName,true).hasWon(this.exit))) {
        if (vehicles.selectedVehicle() != null && ! vehicles.selectedVehicle().isMoved) {
          this.score += 1;
        }
        this.history.add(vehicles);
        this.vehicles = vehicles.updatePosn(keyName,true);
      }
    }
    
    if (keyName.equals("u")) {
      if (history.size() != 0) {
        this.vehicles = this.history.remove(history.size() - 1);
        score += 1;
      }
    }
    
    if (this.hasWon()) {
      this.endOfWorld("Level Complete!");
    }
  }
  
  // Displays a message when the player has won
  public WorldScene lastScene(String msg) {
    WorldScene winScreen = this.makeScene();
    winScreen.placeImageXY(new TextImage(msg, 50, Color.RED), 
        cols * cellSize / 2,rows * cellSize / 2);
    return winScreen;
  }
  
  // Outputs the selected vehicle
  public Vehicle selectedVehicle() {
    return this.vehicles.selectedVehicle();
  }
  
}



class ExamplesBoards {
  Vehicle v1 = new Vehicle(new Area(new Posn(1, 1),1,2), false, false,false,false,Color.BLUE);
  Vehicle v2 = new Vehicle(new Area(new Posn(3, 4),1,3), false, false,false,false,Color.GREEN);
  Vehicle v3 = new Vehicle(new Area(new Posn(2, 2),3,1), true, false,false,false,Color.PINK);
  Vehicle v4 = new Vehicle(new Area(new Posn(1, 2),2,1), false, false,false,false,Color.BLUE);
  Vehicle v5 = new Vehicle(new Area(new Posn(0, 1),2,1), false, false,false,false,Color.BLUE);
  Vehicle v3Selected = new Vehicle(new Area(new Posn(2, 2),3,1), true, true,false,false,
      Color.PINK);
  Vehicle o1 = new Vehicle(new Area(new Posn(5, 5),1,1), false, false,true,false,
      Color.BLUE);
  Vehicle o2 = new Vehicle(new Area(new Posn(1, 2),1,1), false, false,true,false,
      Color.BLUE);
  Vehicle o3 = new Vehicle(new Area(new Posn(5, 2),1,1), false, false,true,false,
      Color.BLUE);
  
  String demoLevel1 = 
      "+--------+\n" + 
      "| C  O   |\n" + 
      "|    C   |\n" + 
      "| c   C  X\n" + 
      "|t     C |\n" + 
      "|CCC c   |\n" + 
      "|    c   |\n" + 
      "+-------+";
  
  String demoLevel2 = 
      "+--------+\n" + 
      "| C  O  T  |\n" + 
      "|    C     |\n" + 
      "| c   C    X\n" + 
      "|t     |\n" + 
      "|CCC c |\n" + 
      "|    c |\n" + 
      "+------+";

  String demoLevel3 = 
      "+--------+\n" + 
      "| C  O  T  |\n" + 
      "|    C     |\n" + 
      "| c   C    X\n" + 
      "|t     C  |\n" + 
      "|CCC c    |\n" + 
      "|    c    |\n" + 
      "+-------+";
  
  String demoLevel4 = 
      "+--------+\n" + 
      "| C  .   |\n" + 
      "|    C   |\n" + 
      "|S    C  |\n" + 
      "|      C |\n" + 
      "|CCC .   |\n" + 
      "|    .   |\n" + 
      "+---X---+";
  
  String demoLevel5 = 
      "+--------+\n" + 
      "| C  .. .|\n" + 
      "|    C   |\n" + 
      "|S    C  X\n" + 
      "|      C |\n" + 
      "|CCC ..  |\n" + 
      "|    .   |\n" + 
      "+-------+";
  RushHourWorld demoBoard3 = new RushHourWorld(10, 10, new Area(new Posn(9, 2),1,1),
      new ConsLoVehicle(v1, new ConsLoVehicle(v2, new ConsLoVehicle(v3, new MtLoVehicle()))));
  RushHourWorld demoBoard4 = new RushHourWorld(demoLevel1,2,3);
  ILoVehicle vehicles1 = new ConsLoVehicle(v1, new ConsLoVehicle(v2, new ConsLoVehicle(v3, 
      new ConsLoVehicle(v4,new MtLoVehicle()))));
  ILoVehicle vehicles2 = new ConsLoVehicle(v1, new ConsLoVehicle(v2, new ConsLoVehicle(v3, 
      new MtLoVehicle())));
  ILoVehicle vehicles3 = new ConsLoVehicle(v1, new ConsLoVehicle(v2, new ConsLoVehicle(v3Selected, 
      new MtLoVehicle())));
  ILoVehicle vehicles4 = new ConsLoVehicle(v1, new ConsLoVehicle(o3, new ConsLoVehicle(v3Selected, 
      new MtLoVehicle())));
  ILoVehicle vehicles5 = new ConsLoVehicle(v2, new ConsLoVehicle(v3Selected, new MtLoVehicle()));
  
  RushHourWorld demoBoard5 = new RushHourWorld(demoLevel2,2,3);
  RushHourWorld demoBoard6 = new RushHourWorld(demoLevel3,2,3);
  RushHourWorld demoBoard7 = new RushHourWorld(10, 10, new Area(new Posn(9, 2),1,1),
      this.vehicles3);
  RushHourWorld demoBoard8 = new RushHourWorld(10, 10, new Area(new Posn(9, 2),1,1),
      this.vehicles2);
  KlotskiWorld demoBoard9 = new KlotskiWorld(demoLevel4);
  KlotskiWorld demoBoard10 = new KlotskiWorld(demoLevel5);
  RushHourWorld demoBoard11 = new RushHourWorld(10, 10, new Area(new Posn(9, 2),1,1),
      this.vehicles4);
  RushHourWorld demoBoard12 = new RushHourWorld(10, 10, new Area(new Posn(9, 2),1,1),
      this.vehicles5);
  KlotskiWorld demoBoard13 = new KlotskiWorld(10, 10, new Area(new Posn(9, 2),1,1),
      this.vehicles5);
  
  // Checks if two vehicles are overlapping
  boolean testIsOverlapping(Tester t) {
    return t.checkExpect(v1.isOverlapping(v4), true)
        && t.checkExpect(v1.isOverlapping(o2),true);
  }
  
  // Checks whether a list of vehicles has any two vehicles overlapping
  boolean testCheckOverlapping(Tester t) {
    return t.checkExpect(vehicles1.checkOverlaps(),true)
        && t.checkExpect(vehicles2.checkOverlaps(),false);
  }
  
  // Checks if the given vehicle is out of bounds
  boolean testOutOfBounds(Tester t) {
    return t.checkExpect(v5.outOfBounds(3,3), true)
        && t.checkExpect(v3.outOfBounds(3, 7),true);
    
  }
 
  // Checks if any vehicle in the list of vehicles is out of bounds
  boolean testCheckOutOfBounds(Tester t) {
    return t.checkExpect(vehicles1.checkOutBounds(6, 6), true)
        && t.checkExpect(vehicles1.checkOutBounds(8, 8), false);
  }
  
  // Checks the number of columns in the given string
  boolean testMaxCols(Tester t) {
    return t.checkExpect(new Utility().getCol(demoLevel1), 10)
        && t.checkExpect(new Utility().getCol(demoLevel2), 12)
        && t.checkExpect(new Utility().getCol(demoLevel5), 10);
  }
  
  // Checks the number of rows in the given string
  boolean testMaxRows(Tester t) {
    return t.checkExpect(new Utility().getRow(demoLevel1), 8)
        && t.checkExpect(new Utility().getRow(demoLevel2), 8)
        && t.checkExpect(new Utility().getRow(demoLevel4), 8);
  }
  
  // Checks if selected vehicle is correctly determined
  boolean testSelectedVehicle(Tester t) {
    return t.checkExpect(this.demoBoard7.selectedVehicle(), v3Selected)
        && t.checkExpect(this.demoBoard3.selectedVehicle(), null);
  }
  
  
  
  // Tests if correct vehicle is selected and updated on mouse click
  boolean testOnMouseClicked(Tester t) {
    this.demoBoard8.onMouseClicked(new Posn(100,100));
    return t.checkExpect(demoBoard8.selectedVehicle(), this.v3Selected);
  }
  
  // test updatePosn method in vehicles
  boolean testUpdatePosn(Tester t) {
    return t.checkExpect(vehicles1.updatePosn("right",true),vehicles1) // no selected
        && t.checkExpect(vehicles3.updatePosn("right",false),  // rushhour horizontal move right
            new ConsLoVehicle(v1, new ConsLoVehicle(v2, new ConsLoVehicle(
                new Vehicle(new Area(new Posn(3, 2),3,1), true, true,false,true,Color.PINK), 
                new MtLoVehicle()))))
        && t.checkExpect(vehicles3.updatePosn("up",false),  // rush hour horizontal move upward
            new ConsLoVehicle(v1, new ConsLoVehicle(v2, new ConsLoVehicle(
                new Vehicle(new Area(new Posn(2, 2),3,1), true, true,false,true,Color.PINK), 
                new MtLoVehicle()))))
        && t.checkExpect(vehicles3.updatePosn("up",true), // koltski horizontal move upward
            new ConsLoVehicle(v1, new ConsLoVehicle(v2, new ConsLoVehicle(
                new Vehicle(new Area(new Posn(2, 1),3,1), true, true,false,true,Color.PINK), 
                new MtLoVehicle()))));
  }
  
  // test onKeyEvent in RushHour
  void testOnKeyEvent(Tester t) {
    demoBoard11.onKeyEvent("right");
    t.checkExpect(demoBoard11.vehicles,vehicles4); // move to an Obstacle;
    demoBoard11.onKeyEvent("left");
    t.checkExpect(demoBoard11.vehicles,vehicles4);  // move to a Vehicle 
    demoBoard12.onKeyEvent("left");
    t.checkExpect(demoBoard12.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));   
    t.checkExpect(demoBoard12.score,1);   
    demoBoard12.onKeyEvent("left");
    t.checkExpect(demoBoard12.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));  // move to bounds, cannot move
    demoBoard12.onKeyEvent("right");
    t.checkExpect(demoBoard12.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(2, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));
    t.checkExpect(demoBoard12.score,1); // consecutive move, score not change.
    demoBoard12.onKeyEvent("u");
    t.checkExpect(demoBoard12.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));   // rewind to last step
    t.checkExpect(demoBoard12.score,2);  // use rewind, score + 1
    demoBoard12.onKeyEvent("down");
    t.checkExpect(demoBoard12.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));    // horizontal vehicle cannot move downward
  }
  
  // test onKeyEvent in Kloktski World
  void testOnKeyEvent2(Tester t) {
    demoBoard13.onKeyEvent("left");
    t.checkExpect(demoBoard13.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));   
    t.checkExpect(demoBoard13.score,1);   
    demoBoard13.onKeyEvent("left");
    t.checkExpect(demoBoard13.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));  // move to bounds, cannot move
    demoBoard13.onKeyEvent("right");
    t.checkExpect(demoBoard13.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(2, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));
    t.checkExpect(demoBoard13.score,1); // consecutive move, score not change.
    demoBoard13.onKeyEvent("u");
    t.checkExpect(demoBoard13.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 2),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));   // rewind to last step
    t.checkExpect(demoBoard13.score,2);  // use rewind, score + 1
    demoBoard13.onKeyEvent("down");
    t.checkExpect(demoBoard13.vehicles,new ConsLoVehicle(v2, new ConsLoVehicle(
        new Vehicle(new Area(new Posn(1, 3),3,1), true, true,false,true,Color.PINK), 
        new MtLoVehicle())));    // vehicle can move to any direction in Klotski
  }
  
  // test hasWon method
  boolean testHasWon(Tester t) {
    return t.checkExpect(vehicles1.hasWon(new Area(new Posn(4,2),1,1)),true)
        && t.checkExpect(vehicles1.hasWon(new Area(new Posn(3,6),1,1)),false);
  }
  /*
  // test RushHour World
  void testRushHour1(Tester t) {
    demoBoard3.bigBang(1000, 1000);
  }*/
  
  // test RushHour World
  void testRushHour2(Tester t) {
    demoBoard4.bigBang(1000, 1000);
  }
  
  /*
  // a non-rectangular board
  void testRushHour3(Tester t) {
    demoBoard5.bigBang(1000, 1000);
  }
  
  // another non-rectangular board
  void testRushHour4(Tester t) {
    demoBoard6.bigBang(1000, 1000);
  }
  
  // Runs Koltski Game which exit is at right
  void testKol1(Tester t) {
    demoBoard9.bigBang(1000, 1000);
  }
  
  // Run Koltski which exit is at bottom
  void testKol2(Tester t) {
    demoBoard10.bigBang(1000, 1000);
  }*/
  
}