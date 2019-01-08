import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SolutionVerifier {
    char[][] board;
    Coordinate playerCoordinate = new Coordinate();

    public void readInputFile(String url) throws IOException {
        List<String> input = new ArrayList<>();
        int maxRowLength = 0;

        BufferedReader reader = new BufferedReader(new FileReader(url));
        String line = reader.readLine();

        while (line != null) {
            input.add(line);
            if (maxRowLength < line.length()) {
                maxRowLength = line.length();
            }
            line = reader.readLine();

        }
        board = new char[input.size()][maxRowLength];
        for (int i = 0; i < input.size(); i++) {
            String row = input.get(i);
            board[i] = row.toCharArray();
            for (int j = 0; j < board[i].length; j++) {
                if ('@' == row.charAt(j) || '+' == row.charAt(j)) {
                    playerCoordinate.x = j;
                    playerCoordinate.y = i;
                }
            }
        }
    }

    public boolean verify(String steps) {
        if (steps == null || steps.isEmpty() || steps.contains("no path")) {
            System.out.println("No path given. Unable to verify.");
            return false;
        }


        printBoard();
        boolean correct = verifySolution(steps);
        printBoard();
        if (!correct) {
            System.out.println("Solution: " + steps + " is not correct...");
            return false;
        } else {
            System.out.println("Solution: " + steps + " is correct!!");
        }

        return true;
    }

    private void printBoard() {
        for (int y = 0; y < board.length; y++) {
            char[] xArray = board[y];
            for (int x = 0; x < xArray.length; x++) {
                if (xArray[x] != '@' && DeadlockManager.isDeadSquare(new Coordinate(x, y))) {
                    System.out.print('x');
                } else {
                    System.out.print(xArray[x]);
                }

            }
            System.out.print("\n");
        }
    }

    private boolean verifySolution(String steps) {
        Coordinate tmpCoordinate = new Coordinate();

        for (Character character : steps.toCharArray()) {
            switch (character) {
                case 'U':
                    tmpCoordinate.x = playerCoordinate.x;
                    tmpCoordinate.y = playerCoordinate.y - 1;
                    break;
                case 'D':
                    tmpCoordinate.x = playerCoordinate.x;
                    tmpCoordinate.y = playerCoordinate.y + 1;
                    break;
                case 'R':
                    tmpCoordinate.x = playerCoordinate.x + 1;
                    tmpCoordinate.y = playerCoordinate.y;
                    break;
                case 'L':
                    tmpCoordinate.x = playerCoordinate.x - 1;
                    tmpCoordinate.y = playerCoordinate.y;
                    break;
                default:
                    continue;
            }
            if (!move(tmpCoordinate, character)) {
                return false;
            }
        }

        return true;
    }

    public boolean move(Coordinate tmpCoordinate, char move) {
        if (!verifyWallCoordinates(tmpCoordinate))
            return false;

        Coordinate boxCoordinate = null;
        if (board[tmpCoordinate.y][tmpCoordinate.x] == '$' || board[tmpCoordinate.y][tmpCoordinate.x] == '*') {
            boxCoordinate = new Coordinate();

            switch (move) {
                case 'U':
                    boxCoordinate.x = tmpCoordinate.x;
                    boxCoordinate.y = tmpCoordinate.y - 1;
                    break;
                case 'D':
                    boxCoordinate.x = tmpCoordinate.x;
                    boxCoordinate.y = tmpCoordinate.y + 1;
                    break;
                case 'R':
                    boxCoordinate.x = tmpCoordinate.x + 1;
                    boxCoordinate.y = tmpCoordinate.y;
                    break;
                case 'L':
                    boxCoordinate.x = tmpCoordinate.x - 1;
                    boxCoordinate.y = tmpCoordinate.y;
                    break;
                default:
                    //Nothing
            }
            if (!verifyBoxPush(boxCoordinate)) {
                return false;
            }
        }

        // OK!
        if (board[playerCoordinate.y][playerCoordinate.x] == '+') {
            board[playerCoordinate.y][playerCoordinate.x] = '.';
        } else {
            board[playerCoordinate.y][playerCoordinate.x] = ' ';
        }

        if (board[tmpCoordinate.y][tmpCoordinate.x] == '.' || board[tmpCoordinate.y][tmpCoordinate.x] == '*') {
            board[tmpCoordinate.y][tmpCoordinate.x] = '+';
        } else {
            board[tmpCoordinate.y][tmpCoordinate.x] = '@';
        }

        playerCoordinate.x = tmpCoordinate.x;
        playerCoordinate.y = tmpCoordinate.y;
        if (boxCoordinate != null) {
            if (board[boxCoordinate.y][boxCoordinate.x] == '.') {
                board[boxCoordinate.y][boxCoordinate.x] = '*';
            } else {
                board[boxCoordinate.y][boxCoordinate.x] = '$';
            }
        }

        return true;
    }

    public boolean verifyBoxPush(Coordinate coordinate) {
        if (!verifyWallCoordinates(coordinate)) {
            return false;
        }

        if (board[coordinate.y][coordinate.x] == '$' || board[coordinate.y][coordinate.x] == '*')
            return false;

        return true;
    }

    public boolean verifyWallCoordinates(Coordinate coordinate) {
        if (board[coordinate.y][coordinate.x] == '#')
            return false;
        return true;
    }
}
