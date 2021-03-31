import java.awt.Color;
import java.util.ArrayList;

import javalib.worldimages.*;

class GamePiece {
  static int SIZE = 40;
  // in logical coordinates, with the origin
  // at the top-left corner of the screen
  int row;
  int col;
  //neighbors
  boolean left;
  boolean right;
  boolean top;
  boolean bottom;
  //is this gamepiece a powerstation
  boolean powerStation;
  //whether this GamePiece is connected to the powerStation
  boolean turnedOn;
  ArrayList<GamePiece> neighbors;
  
  GamePiece(int row, int column) {
    this.row = row;
    this.col = column;
    this.left = false;
    this.right = false;
    this.top = false;
    this.bottom = false;
    this.powerStation = false;
    this.turnedOn = false;
    this.neighbors = new ArrayList<>();
  }
  
  //draw this piece
  WorldImage draw() {
    //default lineColor as gray;
    Color lineColor = Color.GRAY;
    
    if (this.turnedOn) {
      lineColor = Color.YELLOW;
    }
    
    WorldImage base = new FrameImage(new RectangleImage(GamePiece.SIZE, GamePiece.SIZE, 
        OutlineMode.SOLID, Color.DARK_GRAY));
    WorldImage verticalLine = new LineImage(new Posn(0, GamePiece.SIZE / 2), lineColor);
    WorldImage horizontalLine = new LineImage(new Posn(GamePiece.SIZE / 2, 0), lineColor);
    WorldImage powerStation = new StarImage(GamePiece.SIZE / 2, OutlineMode.SOLID, Color.CYAN);
    
    if (this.left) {
      base = new OverlayOffsetImage(horizontalLine, GamePiece.SIZE / 4, 0, base);
    }
    
    if (this.right) {
      base = new OverlayOffsetImage(horizontalLine, -1 * GamePiece.SIZE / 4, 0, base);
    }
    
    if (this.top) {
      base = new OverlayOffsetImage(verticalLine, 0, GamePiece.SIZE / 4, base);
    }
    
    if (this.bottom) {
      base = new OverlayOffsetImage(verticalLine, 0, -1 * GamePiece.SIZE / 4, base);
    }
    
    if (this.powerStation) {
      base = new OverlayImage(powerStation, base);
    }
    return base;
  }
  
  //rotate piece
  void rotatePiece() {
    boolean leftVal = this.left;
    boolean rightVal = this.right;
    boolean topVal = this.top;
    boolean bottomVal = this.bottom;
    
    this.left = bottomVal;
    this.top = leftVal;
    this.right = topVal;
    this.bottom = rightVal;
  }
}