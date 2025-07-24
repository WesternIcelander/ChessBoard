# ChessBoard
A library that allows you to do things with chess games.

## How to use ChessBoard
### Reading PGN files
```java
// Open file 'game.pgn'
try (BufferedReader reader = new BufferedReader(new FileReader("game.pgn"))) {
    Game game;
    // PGN.read() expects a BufferedReader
    // Each call to PGN.read() will read a single game.
    // If there are multiple games in the file, you can read them with successive calls to PGN.read().
    while ((game = PGN.read(reader)) != null) {
        // do something with game
    }
}
```
### Serializing a game to PGN
```java
// Serialize the game to PGN format.
System.out.println(PGN.serialize(game));

// Remove the comments.
System.out.println(PGN.serialize(game.withoutComments()));
```

### Printing out moves on a board
```java
// Create a new board.
Board board = new Board();
// A freshly created board is completely blank!
// You need to deserialize a starting position to the board first.
FEN.deserialize(board, game.startingPosition);
// Print out each move.
int moveNumber = 0;
for (Move move : game.moves) {
    if ((moveNumber % 2) == 0) {
        System.out.print((moveNumber + 2) / 2 + ".");
    }
    // Get the shortest possible non-ambiguous written form of the move
    String notation = move.notation(board);
    // Print it out
    System.out.print(notation);
    System.out.print(" ");
    // Apply that move to the board so we can get the next move's non-ambiguous written form
    board.applyMove(move);
    moveNumber += 1;
}
System.out.println();

// Serialize the current state of the board.
System.out.println(FEN.serialize(board));
```

### Make moves on a board
```java
// Create a new board.
Board board = new Board();
// A freshly created board is completely blank!
// You need to deserialize a starting position to the board first.
FEN.deserialize(board, DefaultBoards.STANDARD_CHESS);
// You can make calls to parseMove(notation, apply)
// notation should be the move in the form seen in a PGN file
// it will fail if the move is ambiguous or invalid
// the second parameter is whether to apply it to the board or not.
board.parseMove("e4", true);
board.parseMove("e5", true);
// maybe you want to parse a move but not apply it to the board
ParsedMove knightToF3 = board.parseMove("Nf3", false);
// because you wanted to get details from it without actually making the move
RegularMove knightToF3Move = knightToF3.move.asRegularMoves().get(0);
System.out.println(knightToF3Move.piece + " moves from "
        + knightToF3Move.from + " to " + knightToF3Move.to);
System.out.println("Does that move capture a piece? " + knightToF3Move.takesPiece());
```

## License
ChessBoard is available under the GNU Affero General Public License version 3 or any later version. See the [LICENSE file](LICENSE) for the full legal text.

