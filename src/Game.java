import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game {
    // Solver
    GameSolver solver;
    // Changing data
    SortedSet<Coordinate> boxCoordinates;
    Coordinate playerCoordinate;
    // Static data
    GameBoard board;
    HashSet<Coordinate> goalCoordinates;
    HashSet<Coordinate> wallCoordinates;
    HashSet<Coordinate> emptyCoordinates;

    boolean isDebug = false;

    public Game(String url) {
        try {
            readInputFile(url);
        } catch (IOException e) {
            System.out.println("Failed to read input file " + e);
        }

    }

    public Game() {
        // Empty and sad
    }

    public String start() {
        State initState = new State(boxCoordinates, playerCoordinate);
        solver = new GameSolver(board, initState);
        return solver.solve();
    }

    public void readInputFile(String url) throws IOException {
        List<String> input = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new FileReader(url));
        String line = reader.readLine();

        while (line != null) {
            input.add(line);
            line = reader.readLine();
        }

        readInput(input);
    }

    public void readInput(List<String> input) {
        boxCoordinates  = new TreeSet<>();
        goalCoordinates = new HashSet<>();
        wallCoordinates = new HashSet<>();
        emptyCoordinates = new HashSet<>();

        char[][] asciiBoard = new char[input.size()][];
        for(int i = 0; i < input.size(); i++) {
            String row = input.get(i);
            asciiBoard[i] = row.toCharArray();
            for (int j = 0; j < row.length(); j++) {
                GameObject current = GameObject.getEnum(row.charAt(j));
                if (isDebug)
                    System.out.println("Found " + row.charAt(j) +" at (" + j + "," + i + ")" );
                switch (current) {
                    case BOX:
                        boxCoordinates.add(new Coordinate(j, i));
                        asciiBoard[i][j] = ' ';
                        emptyCoordinates.add(new Coordinate(j,i));
                        break;
                    case GOAL:
                        goalCoordinates.add(new Coordinate(j, i));
                        break;
                    case PLAYER:
                        playerCoordinate = new Coordinate(j, i);
                        asciiBoard[i][j] = ' ';
                        emptyCoordinates.add(new Coordinate(j,i));
                        break;
                    case PLAYER_ON_GOAL:
                        playerCoordinate = new Coordinate(j, i);
                        goalCoordinates.add(new Coordinate(j, i));
                        asciiBoard[i][j] = '.';
                        break;
                    case BOX_ON_GOAL:
                        boxCoordinates.add(new Coordinate(j, i));
                        goalCoordinates.add(new Coordinate(j, i));
                        asciiBoard[i][j] = '.';
                        break;
                    case WALL:
                        wallCoordinates.add(new Coordinate(j, i));
                        break;
                    case EMPTY:
                        emptyCoordinates.add(new Coordinate(j,i));
                        break;
                    default:
                        if (isDebug)
                            System.out.println("Found " + row.charAt(j) +"?");
                }
            }
        }

        board = new GameBoard(wallCoordinates, goalCoordinates,emptyCoordinates, asciiBoard);
    }
}
