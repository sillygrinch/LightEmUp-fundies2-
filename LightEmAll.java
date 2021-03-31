import java.util.*;
import javalib.impworld.*;
import java.awt.Color;
import javalib.worldimages.*;

class LightEmAll extends World {
  // a list of columns of GamePieces,
  ArrayList<ArrayList<GamePiece>> board;
  // a list of all nodes
  ArrayList<GamePiece> nodes;
  // a list of edges
  // not implemented yet, for later part
  ArrayList<Edge> mst;
  // size of the board
  int width;
  int height;
  // row column location of the powerStation and size
  int powerRow;
  int powerCol;
  int radius;
  // a random object to generate unique board positions
  Random rand;

  // basic constructor
  LightEmAll(int width, int height) {
    this(width, height, new Random());
  }

  // seeded constructor
  LightEmAll(int width, int height, Random r) {
    this.board = new ArrayList<>();
    this.nodes = new ArrayList<>();
    this.mst = new ArrayList<>();
    this.width = width;
    this.height = height;
    // powerRow and powerCol set to zero (powerStation starts at 1x1 position
    this.powerRow = 0;
    this.powerCol = 0;
    this.radius = 0;
    this.rand = r;

    // create the board
    this.makeGame();
    // create a list of neighbors inside of every GamePiece
    this.createNeighbors();
    // determine the radius of the graph using Breadth-First Search
    this.setRadius();
    // randomize the orientation of pieces
    this.scrambleBoard();
    // place the powerStation in desired location
    this.positionStation();
    // turn on board based on lines and powerStation
    this.powerBoard();
  }

  // create all GamePieces in this LightEmAll game
  public void makeGame() {
    // create all columns and populate them with GamePieces
    for (int i = 0; i < this.width; i++) {
      this.board.add(new ArrayList<GamePiece>());

      ArrayList<GamePiece> col = this.board.get(i);

      for (int j = 0; j < this.height; j++) {
        GamePiece piece = new GamePiece(j, i);

        col.add(piece);
        this.nodes.add(piece);
      }
    }

    // generate powerlines such that they create the shape of a fractal
    this.fractalGeneration(0, this.width, 0, this.height);
  }

  // creates the neighbors of each GamePiece
  public void createNeighbors() {
    for (int i = 0; i < this.width; i++) {
      for (int j = 0; j < this.height; j++) {
        // if the piece has a complete connection to another piece,
        // then add each GamePiece to the other's list of neighbors
        GamePiece piece = this.board.get(i).get(j);

        if (piece.bottom) {

          if (piece.row == this.height - 1) {
            // empty if-else ensures that this case is skipped
          }
          else {
            GamePiece bottomPiece = this.board.get(i).get(j + 1);

            if (bottomPiece.top) {
              this.addNeighbors(piece, bottomPiece);
            }
          }
        }

        if (piece.right) {
          if (piece.col == this.width - 1) {
            // piece is skipped
          }
          else {
            GamePiece rightPiece = this.board.get(i + 1).get(j);

            if (rightPiece.left) {
              this.addNeighbors(piece, rightPiece);
            }
          }
        }
      }
    }
  }

  // add the other neighbors to each piece
  public void addNeighbors(GamePiece one, GamePiece two) {
    one.neighbors.add(two);
    two.neighbors.add(one);
  }

  // randomize the GamePieces
  public void scrambleBoard() {
    // select a random number of rotations to make on each GamePiece
    for (GamePiece piece : this.nodes) {
      int presses = this.rand.nextInt(4);

      for (int i = 0; i < presses; i++) {
        piece.rotatePiece();
      }
    }
  }

  // position the powerStation
  // for manual generation, it should be in middle of board
  // for fractal generation, it should be in the middle of first row
  public void positionStation() {
    this.powerRow = 0;
    this.powerCol = this.width / 2;

    GamePiece pieceWithStation = this.board.get(powerCol).get(powerRow);

    pieceWithStation.powerStation = true;
  }

  // make all connections of the manual generation
  public void manualGeneration() {
    for (int i = 0; i < this.width; i++) {
      for (int j = 0; j < this.height; j++) {
        GamePiece piece = this.board.get(i).get(j);

        // pieces at the top of the board
        if (j == 0) {
          piece.top = true;
        }

        // pieces at the bottom of the board
        if (j == this.height - 1) {
          piece.bottom = true;
        }

        // pieces between top and bottom of the board
        if (j != 0 && j != this.height - 1) {
          piece.bottom = true;
          piece.top = true;
        }

        // pieces in center row of the board
        if (j == this.height / 2) {
          // piece on left side of the board
          if (i == 0) {
            piece.right = true;
          }
          // piece on right side of the board
          else if (i == this.width - 1) {
            piece.left = true;
          }
          // pieces between left and right side of board
          else {
            piece.left = true;
            piece.right = true;
          }
        }
      }
    }
  }

  // generates a board with a fractal design
  public void fractalGeneration(int startWidth, int endWidth, int startHeight, int endHeight) {
    // width || height < 2 then do nothing
    if (endWidth - startWidth < 2 || endHeight - startHeight < 2) {
      return;
    }

    int midWidth = (startWidth + endWidth) / 2;
    int midHeight = (startHeight + endHeight) / 2;

    // if height = 2 || 3, recursive function
    if (endHeight - startHeight == 2 || endHeight - startHeight == 3) {
      this.fractalGeneration(startWidth, midWidth, startHeight, endHeight);
      this.fractalGeneration(midWidth, endWidth, startHeight, endHeight);
    }

    // recur on the quarters
    this.fractalGeneration(startWidth, midWidth, startHeight, midHeight);
    this.fractalGeneration(midWidth, endWidth, startHeight, midHeight);
    this.fractalGeneration(midWidth, endWidth, midHeight, endHeight);
    this.fractalGeneration(startWidth, midWidth, midHeight, endHeight);

    // draw a 'U' shape around (fractal design)
    for (int i = startWidth; i < endWidth; i++) {
      for (int j = startHeight; j < endHeight; j++) {
        GamePiece piece = this.board.get(i).get(j);
        if (i == startWidth || i == endWidth - 1) {
          if (j == startHeight) {
            piece.bottom = true;
          }
          else if (j == endHeight - 1) {
            piece.top = true;

            if (i == startWidth) {
              piece.right = true;
            }

            if (i == endWidth - 1) {
              piece.left = true;
            }

          }
          else {
            piece.top = true;
            piece.bottom = true;
          }
        }
        else {
          if (j == endHeight - 1) {
            piece.left = true;
            piece.right = true;
          }
        }
      }
    }
  }

  // set the radius of the the board
  public void setRadius() {
    // find the last found piece by using breadth-first algo
    GamePiece lastFoundPiece = this.determineLastPiece();

    // find the depth of breadth-first algo
    int diameter = this.determineDiameter(lastFoundPiece);

    // calculate the radius from the diameter
    this.radius = (diameter / 2) + 1;
  }

  // determine the last piece found in a breadth-first algo
  public GamePiece determineLastPiece() {
    // keep track of already seen gamePieces
    ArrayList<GamePiece> alreadySeen = new ArrayList<>();
    // keep track of the GamePieces left to be seen
    Queue<GamePiece> worklist = new Queue<>();

    // starting piece is the powerStation
    GamePiece startingPiece = this.board.get(powerCol).get(powerRow);
    worklist.add(startingPiece);

    // store the next GamePiece until the end of loop
    GamePiece storedPiece = startingPiece;

    while (!worklist.isEmpty()) {
      GamePiece next = worklist.remove();
      storedPiece = next;

      if (alreadySeen.contains(next)) {
        // this case is skipped
      }
      else {
        for (GamePiece piece : next.neighbors) {
          worklist.add(piece);
        }
      }

      alreadySeen.add(next);
    }

    return storedPiece;
  }

  // determine the depth of breadth-first algo
  public int determineDiameter(GamePiece startingPiece) {
    // keep track of the gamePieces already seen
    ArrayList<GamePiece> alreadySeen = new ArrayList<>();
    // keep track of the gamePieces left to be seen
    Queue<GamePiece> worklist = new Queue<>();

 
    int currentLayer = 1;
    int nextLayer = 0;
    int diameterSoFar = 0;

    worklist.add(startingPiece);

    while (!worklist.isEmpty()) {
      GamePiece next = worklist.remove();

      if (alreadySeen.contains(next)) {
        // this case is skipped
      }
      else {
        for (GamePiece piece : next.neighbors) {
          worklist.add(piece);
          nextLayer++;
        }
      }

      currentLayer--;

      if (currentLayer <= 0) {
        diameterSoFar++;
        currentLayer = nextLayer;
        nextLayer = 0;
      }
      alreadySeen.add(next);
    }

    return diameterSoFar;
  }

  // draw the board using for within a for loops
  public WorldScene makeScene() {
    WorldScene scene = getEmptyScene();

    for (int i = 0; i < this.width; i++) {
      for (int j = 0; j < this.height; j++) {
        GamePiece piece = this.board.get(i).get(j);
        scene.placeImageXY(piece.draw(), i * GamePiece.SIZE + GamePiece.SIZE / 2,
            j * GamePiece.SIZE + GamePiece.SIZE / 2);
      }
    }

    return scene;
  }

  //rotate cells when pressed on (Mouse Handler)
  public void onMouseClicked(Posn pos) {
    this.nodes.get(0);
    this.nodes.get(0);
    // extract position in this.board based mouse click
    Posn position = new Posn(pos.x / GamePiece.SIZE, pos.y / GamePiece.SIZE);

    if (position.x >= this.width || position.y >= this.height) {
      return;
    }

    // the pressed piece
    GamePiece piece = this.board.get(position.x).get(position.y);

    // rotate piece
    piece.rotatePiece();

    // light the board
    this.powerBoard();

    // if all pieces are turned on, you win
    if (this.winCondition()) {
      this.endOfWorld("won");
    }
  }

  //check that all GamePieces in this LightEmAll game are turned on
  public boolean winCondition() {
    for (GamePiece piece : this.nodes) {
      if (!piece.turnedOn) {
        return false;
      }
    }

    return true;
  }

  // draw the won state of the world of this game
  // this will be matched to check if you win.
  public WorldScene lastScene(String msg) {
    WorldScene game = this.makeScene();

    this.nodes.get(0);
    int size = GamePiece.SIZE;

    if (msg.equals("won")) {
      WorldImage winMsg = new TextImage("You won!", size / 2, Color.RED);

      game.placeImageXY(winMsg, this.width * size / 2, this.height * size / 2);
    }

    return game;
  }

  // move the powerStation
  public void onKeyEvent(String key) {
    GamePiece oldPiece = this.board.get(this.powerCol).get(this.powerRow);
    GamePiece newPiece;

    if (key.equals("left")) {
      if (this.powerCol != 0) {
        newPiece = this.board.get(this.powerCol - 1).get(this.powerRow);

        if (oldPiece.left && newPiece.right) {
          this.moveStation(oldPiece, newPiece);
        }
      }
    }

    if (key.equals("right")) {
      if (this.powerCol != this.width - 1) {
        newPiece = this.board.get(this.powerCol + 1).get(this.powerRow);

        if (oldPiece.right && newPiece.left) {
          this.moveStation(oldPiece, newPiece);
        }
      }
    }

    if (key.equals("up")) {
      if (this.powerRow != 0) {
        newPiece = this.board.get(this.powerCol).get(this.powerRow - 1);

        if (oldPiece.top && newPiece.bottom) {
          this.moveStation(oldPiece, newPiece);
        }
      }
    }

    if (key.equals("down")) {
      if (this.powerRow != this.height - 1) {
        newPiece = this.board.get(this.powerCol).get(this.powerRow + 1);

        if (oldPiece.bottom && newPiece.top) {
          this.moveStation(oldPiece, newPiece);
        }
      }
    }

    this.powerBoard();

    if (this.winCondition()) {
      this.endOfWorld("won");
    }
  }

  // move the powerStation from one cell to another
  public void moveStation(GamePiece oldPiece, GamePiece newPiece) {
    oldPiece.powerStation = false;
    newPiece.powerStation = true;

    this.powerCol = newPiece.col;
    this.powerRow = newPiece.row;
  }

  //turn on all connected GamePieces
  public void powerBoard() {
    // starts by turning everything off
    for (GamePiece piece : this.nodes) {
      piece.turnedOn = false;
    }

    // turn the powerstation on and spread out electricity
    GamePiece stationPiece = this.board.get(powerCol).get(powerRow);

    this.powerPiece(stationPiece, this.radius);
  }

  // turn on the given GamePiece, and turn on all connected
  // GamePieces
  public void powerPiece(GamePiece piece, int piecesLeft) {

    if (piecesLeft == 0) {
      return;
    }

    piece.turnedOn = true;

    // top connection
    if (piece.top) {
      if (piece.row > 0) {
        GamePiece topPiece = this.board.get(piece.col).get(piece.row - 1);

        if (topPiece.bottom && !topPiece.turnedOn) {
          this.powerPiece(topPiece, piecesLeft - 1);
        }
      }
    }

    // bottom connection
    if (piece.bottom) {
      if (piece.row < this.height - 1) {
        GamePiece bottomPiece = this.board.get(piece.col).get(piece.row + 1);

        if (bottomPiece.top && !bottomPiece.turnedOn) {
          this.powerPiece(bottomPiece, piecesLeft - 1);
        }
      }
    }

    // left connection
    if (piece.left) {
      if (piece.col > 0) {
        GamePiece leftPiece = this.board.get(piece.col - 1).get(piece.row);

        if (leftPiece.right && !leftPiece.turnedOn) {
          this.powerPiece(leftPiece, piecesLeft - 1);
        }
      }
    }

    // right connection
    if (piece.right) {
      if (piece.col < this.width - 1) {
        GamePiece rightPiece = this.board.get(piece.col + 1).get(piece.row);

        if (rightPiece.left && !rightPiece.turnedOn) {
          this.powerPiece(rightPiece, piecesLeft - 1);
        }
      }
    }
  }

}