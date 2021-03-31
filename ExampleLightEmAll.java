import java.awt.Color;
import java.util.*;

import javalib.impworld.*;
import javalib.worldimages.*;
import tester.Tester;

class ExampleLightEmAll {
  ExampleLightEmAll() {
  }

  LightEmAll fullGame;
  LightEmAll tenSeeded;
  LightEmAll threeSeeded;
  LightEmAll two;
  LightEmAll four;
  GamePiece simple;

  void initGame() {
    fullGame = new LightEmAll(10, 10);
    tenSeeded = new LightEmAll(10, 10, new Random(60));
    threeSeeded = new LightEmAll(3, 3, new Random(15));
    two = new LightEmAll(2, 2);
    simple = new GamePiece(2, 2);
    four = new LightEmAll(4,4);
  }

  // test that the proper number of GamePieces have been generated
  boolean testMakeGame(Tester t) {
    this.initGame();

    return t.checkExpect(fullGame.nodes.size(), 100) && t.checkExpect(tenSeeded.nodes.size(), 100)
        && t.checkExpect(threeSeeded.nodes.size(), 9);
  }

  // test that connections were generated properly
  void testManuallyGenerate(Tester t) {
    this.initGame();

    threeSeeded.manualGeneration();

    t.checkExpect(threeSeeded.board.get(0).get(0).left, false);

    t.checkExpect(threeSeeded.board.get(2).get(1).right, false);

    t.checkExpect(threeSeeded.board.get(0).get(0).bottom, false);
    t.checkExpect(threeSeeded.board.get(1).get(0).left, true);
    t.checkExpect(threeSeeded.board.get(2).get(0).top, true);
    t.checkExpect(threeSeeded.board.get(2).get(1).left, true);
    t.checkExpect(threeSeeded.board.get(2).get(1).top, true);
    t.checkExpect(threeSeeded.board.get(2).get(1).bottom, true);
  }

  void testPowerBoard(Tester t) {
    this.initGame();

    fullGame.board = new ArrayList<>();
    fullGame.nodes = new ArrayList<>();

    // every piece is turned on.
    fullGame.makeGame();
    fullGame.fractalGeneration(0, fullGame.width, 0, fullGame.height);
    fullGame.positionStation();
    fullGame.radius = 100;
    fullGame.powerBoard();

    // tests to check if every piece is turned on
    for (GamePiece piece : fullGame.nodes) {
      t.checkExpect(piece.turnedOn, true);
    }

  }

  void testFractalGenerate(Tester t) {
    this.initGame();

    fullGame.fractalGeneration(0, fullGame.width, 0, fullGame.height);
    two.fractalGeneration(0, two.width, 0, two.height);
    threeSeeded.fractalGeneration(0, threeSeeded.width, 0, threeSeeded.height);

    for (GamePiece piece : fullGame.nodes) {
      boolean isVisited = piece.top || piece.bottom || piece.left || piece.right;
      t.checkExpect(isVisited, true);
    }

    t.checkExpect(two.board.get(0).get(0).bottom, true);
    t.checkExpect(two.board.get(0).get(1).right, true);
    t.checkExpect(two.board.get(0).get(1).top, true);

    t.checkExpect(threeSeeded.board.get(0).get(0).bottom, true);
    t.checkExpect(threeSeeded.board.get(0).get(1).bottom, true);
    t.checkExpect(threeSeeded.board.get(0).get(1).top, true);

    t.checkExpect(tenSeeded.board.get(0).get(0).bottom, false);
    tenSeeded.fractalGeneration(0, 1, 0, 1);
    t.checkExpect(tenSeeded.board.get(0).get(0).bottom, false);
  }

  void testScrambleBoard(Tester t) {
    this.initGame();

    threeSeeded.scrambleBoard();

    t.checkExpect(threeSeeded.nodes.get(0).left, false);
    t.checkExpect(threeSeeded.nodes.get(3).left, false);
    t.checkExpect(threeSeeded.nodes.get(3).top, false);

    t.checkExpect(threeSeeded.nodes.get(1).right, true);
    t.checkExpect(threeSeeded.nodes.get(2).top, true);
    t.checkExpect(threeSeeded.nodes.get(3).right, true);

  }

  void testCreateNeighbors(Tester t) {
    this.initGame();

    two.fractalGeneration(0, two.width, 0, two.height);
    two.createNeighbors();
    GamePiece first = two.board.get(0).get(0);
    GamePiece second = two.board.get(0).get(1);
    GamePiece third = two.board.get(1).get(0);
    GamePiece fourth = two.board.get(1).get(1);

    t.checkExpect(first.neighbors.contains(second), true);
    t.checkExpect(second.neighbors.contains(first), true);
    t.checkExpect(second.neighbors.contains(fourth), true);
    t.checkExpect(third.neighbors.contains(fourth), true);

    t.checkExpect(second.neighbors.contains(third), false);
    t.checkExpect(third.neighbors.contains(first), false);
    t.checkExpect(first.neighbors.contains(third), false);

  }

  void testAddNeighbors(Tester t) {
    this.initGame();

    GamePiece piece = two.board.get(0).get(0);

    t.checkExpect(simple.neighbors.contains(piece), false);
    t.checkExpect(piece.neighbors.contains(simple), false);

    two.addNeighbors(simple, piece);

    t.checkExpect(simple.neighbors.contains(piece), true);
    t.checkExpect(piece.neighbors.contains(simple), true);
  }

  void testSetRadius(Tester t) {
    this.initGame();

    two.setRadius();
    t.checkExpect(two.radius, 3);

    tenSeeded.setRadius();
    t.checkExpect(tenSeeded.radius, 20);
  }

  void testDetermineLast(Tester t) {
    this.initGame();

    GamePiece lastTwoPiece = two.board.get(0).get(1);
    GamePiece lastThreePiece = threeSeeded.board.get(2).get(1);
    GamePiece lastTenPiece = tenSeeded.board.get(2).get(1);

    t.checkExpect(two.determineLastPiece(), lastTwoPiece);
    t.checkExpect(threeSeeded.determineLastPiece(), lastThreePiece);
    t.checkExpect(tenSeeded.determineLastPiece(), lastTenPiece);
  }

  void testDetermineDiameter(Tester t) {
    this.initGame();

    GamePiece startTwo = two.board.get(0).get(1);
    GamePiece startThree = threeSeeded.board.get(2).get(1);
    GamePiece startTen = tenSeeded.board.get(2).get(1);

    t.checkExpect(two.determineDiameter(startTwo), 4);
    t.checkExpect(threeSeeded.determineDiameter(startThree), 7);
    t.checkExpect(tenSeeded.determineDiameter(startTen), 39);
  }

  void testPositionStation(Tester t) {
    this.initGame();

    tenSeeded.manualGeneration();

    t.checkExpect(threeSeeded.powerCol, 1);
    t.checkExpect(threeSeeded.powerRow, 0);
    t.checkExpect(tenSeeded.powerCol, 5);
    t.checkExpect(tenSeeded.powerRow, 0);
    t.checkExpect(threeSeeded.board.get(1).get(0).powerStation, true);
    t.checkExpect(tenSeeded.board.get(5).get(5).powerStation, false);
  }

  void testMakeScene(Tester t) {
    this.initGame();

    WorldScene a = new WorldScene(0, 0);
    for (GamePiece piece : threeSeeded.nodes) {
      a.placeImageXY(piece.draw(), piece.col * GamePiece.SIZE + GamePiece.SIZE / 2,
          piece.row * GamePiece.SIZE + GamePiece.SIZE / 2);
    }

    t.checkExpect(threeSeeded.makeScene(), a);

    WorldScene b = new WorldScene(0, 0);
    for (GamePiece piece : tenSeeded.nodes) {
      a.placeImageXY(piece.draw(), piece.col * GamePiece.SIZE + GamePiece.SIZE / 2,
          piece.row * GamePiece.SIZE + GamePiece.SIZE / 2);
    }

    t.checkExpect(tenSeeded.makeScene(), b);
  }

  void testMouseClicked(Tester t) {
    this.initGame();

    threeSeeded.manualGeneration();

    t.checkExpect(threeSeeded.nodes.get(6).turnedOn, false);
    t.checkExpect(threeSeeded.nodes.get(6).top, true);

    threeSeeded.onMouseClicked(new Posn(125, 75));

    t.checkExpect(threeSeeded.board.get(2).get(0).turnedOn, false);
    t.checkExpect(threeSeeded.board.get(2).get(0).top, true);
  }

  void testWinCondition(Tester t) {
    this.initGame();

    t.checkExpect(threeSeeded.winCondition(), false);
    t.checkExpect(tenSeeded.winCondition(), false);

    threeSeeded.manualGeneration();
    threeSeeded.powerBoard();
    tenSeeded.manualGeneration();
    tenSeeded.powerBoard();

    t.checkExpect(threeSeeded.winCondition(), false);
    t.checkExpect(tenSeeded.winCondition(), false);
  }

  void testLastScene(Tester t) {
    this.initGame();

    WorldScene scene = threeSeeded.makeScene();
    WorldImage winMsg = new TextImage("You won!", 25, Color.RED);
    scene.placeImageXY(winMsg, threeSeeded.width * 50 / 2, threeSeeded.height * 50 / 2);

    t.checkExpect(threeSeeded.lastScene("won"), scene);

    WorldScene b = tenSeeded.makeScene();
    b.placeImageXY(winMsg, tenSeeded.width * 50 / 2, tenSeeded.height * 50 / 2);

    t.checkExpect(tenSeeded.lastScene("won"), b);
  }

  void testPowerPiece(Tester t) {
    this.initGame();

    GamePiece p1 = tenSeeded.board.get(2).get(2);
    GamePiece p2 = tenSeeded.board.get(7).get(3);

    t.checkExpect(p1.turnedOn, false);
    t.checkExpect(p2.turnedOn, false);

    tenSeeded.powerPiece(p1, 1);

    t.checkExpect(p1.turnedOn, true);
    t.checkExpect(p2.turnedOn, false);

    tenSeeded.powerPiece(p2, 1);

    t.checkExpect(p1.turnedOn, true);
    t.checkExpect(p2.turnedOn, true);
  }

  void testRotate(Tester t) {
    this.initGame();

    simple.left = true;

    t.checkExpect(simple.left, true);

    simple.rotatePiece();

    t.checkExpect(simple.left, false);
    t.checkExpect(simple.top, true);

    simple.rotatePiece();

    t.checkExpect(simple.top, false);
    t.checkExpect(simple.right, true);
  }


  void testMovePowerStation(Tester t) {
    this.initGame();

    t.checkExpect(tenSeeded.board.get(0).get(0).powerStation, false);
    t.checkExpect(tenSeeded.board.get(tenSeeded.powerCol).get(tenSeeded.powerRow).powerStation,
        true);

    tenSeeded.moveStation(tenSeeded.board.get(tenSeeded.powerCol).get(tenSeeded.powerRow),
        tenSeeded.board.get(0).get(0));

    t.checkExpect(tenSeeded.powerCol, 0);
    t.checkExpect(tenSeeded.powerRow, 0);
    t.checkExpect(tenSeeded.board.get(0).get(0).powerStation, true);
    t.checkExpect(tenSeeded.board.get(tenSeeded.powerCol).get(tenSeeded.powerRow).powerStation,
        true);
  }
  
  void testKeys(Tester t) {
    this.initGame();

    tenSeeded.fractalGeneration(0, tenSeeded.width, 0, tenSeeded.height);

    t.checkExpect(tenSeeded.powerRow, 0);
    tenSeeded.onKeyEvent("up");
    t.checkExpect(tenSeeded.powerRow, 0);
    tenSeeded.onKeyEvent("down");
    t.checkExpect(tenSeeded.powerRow, 1);
    tenSeeded.onKeyEvent("left");
    t.checkExpect(tenSeeded.powerCol, 5);
    tenSeeded.onKeyEvent("right");
    t.checkExpect(tenSeeded.powerCol, 6);
  }

  // starts the Game

  void testRun(Tester t) {
    this.initGame();

    fullGame.bigBang(500, 500);
  }
}
