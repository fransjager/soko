import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Main {


    public static void main(String[] args) {
        //readStandardIn();
        testFiles();
    }

    public static void readStandardIn() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> input = new ArrayList();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }
            Game game = new Game();
            game.readInput(input);
            System.out.println(game.start());
        } catch (IOException io) {

        }
    }

    public static void testFiles() {
        try {
            String url = "resources/sokoban1/f1.txt";
            Game engine = new Game(url);
            String answer = engine.start();
            SolutionVerifier solutionVerifier = new SolutionVerifier();
            solutionVerifier.readInputFile(url);
            solutionVerifier.verify(answer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
