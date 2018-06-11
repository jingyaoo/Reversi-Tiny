#Reversi Tiny
Reversi Tiny is a strategy board game for two players, played on a 4x4 board. There are 16 disks, which are dark on one side (player 1's color) and light on the other (player 2's color). At each turn, any disks of the opponent's color that are in one or more straight lines (horizontal, vertical or diagonal) and bounded by the disk just placed and another disk of the current players's color are turned over to the current player's color. The object of the game is to try to end up with the most disks of your color at the end of the game.

Rules:
A game begins with four disks placed in a square in the middle of the grid, two pieces with the light side
up, the other two pieces with the dark side up, with same-colored disks on a diagonal with each other.




Players take alternate turns. If one player can not make any valid move, play passes back to the other
player (hint: think how to represent the successor in this case). Also, one move may form more than one lines.
For example, sometimes a horizontal line and a diagonal line can be formed simultaneously. All opponent's
pieces on those lines must be 
ipped. The game ends when neither player can move. This occurs when the
grid has filled up or when neither player can legally place a piece in any of the remaining squares. Then the
player with the most disks on the board wins. The game is a tie if both players have the same number of
disks.
Write a program Reversi.java with the following command line format:
$java Reversi FLAG player board
where \FLAG" is an integer that species the output of the program (see below). \player" denotes who
plays at this board, and takes value in 1 or 2. \board" is a string of length 16 that species the board in the
natural reading order (left to right, top to bottom). Each position in the string takes one of three values:
0=empty, 1=dark, 2=light. Note: for this game, a state is actually the (player, board) pair. For example,
with the initial state and FLAG=100, the command line would be
$java Reversi 100 1 0000021001200000

1. When FLAG=100, print out all the successor boards of the given state. The successors should be
printed in the natural reading order of the new piece. That is, a successor with the new piece at the
upper-left corner should be printed before a successor whose new piece is at the lower-right corner.
Each successor board should be printed as a 16-character string on a single line. If the player has no
move for this board but it is not a terminal node, you should simply print the board itself. If the board
is a terminal node, you should simply produce no output at all.

2. When FLAG=200, verify if the board is a terminal node. Recall this is true only when neither players
can move. If the board is a non-terminal node, print \non-terminal" (without the quotation marks).
If the board is a terminal node, print the game theoretic value for it: 1 if dark wins, -1 if light wins, 0
if it is a tie.
3. When FLAG=300, on line one print the game theoretic
value for the given (player, board) pair; on line two print the number of states explored by your
algorithm. This should be the sum of the number of Max-Value() and Min-Value() calls, and one way
to implement it is to have a global counter variable: and you increment the counter as the first line in
both Max-Value() and Min-Value(). Note the board now may or may not be a non-terminal node.
4. When FLAG=400, print the
move (the chosen successor board) for the given (player, board) pair. If the player has no move for the
board but the board is a non-terminal, recall the successor board should be the same board, and you
should print it. If the board is a terminal node, you should simply produce no output at all.
5. When FLAG=500, do the same thing as FLAG=300. You should get the same game
theoretic value, but the number of states explored may be smaller.
6. When FLAG=600, do the same thing as FLAG=400 but with alpha-beta pruning.
