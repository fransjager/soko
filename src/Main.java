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

    private static void readStandardIn() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> input = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                input.add(line);
            }
            Game game = new Game();
            game.readInput(input);
            System.out.println(game.start());
        } catch (IOException io) {
            io.printStackTrace();
            System.exit(-1); // when death is the only option, you shall welcome it as a friend
        }
    }

    private static void testFiles() {
        final String prefix = "resources/sokoban1/xsokoban";
        final String postfix = ".xsb";
        String url = prefix + "01" + postfix;
        Game engine = new Game(url);
        String answer = engine.start();
        SolutionVerifier solutionVerifier = new SolutionVerifier();
        try {
            solutionVerifier.readInputFile(url);
        } catch (IOException io) {
            io.printStackTrace();
            System.exit(-1); // when death is the only option, you shall welcome it as a friend
        }
        solutionVerifier.verify(answer);
    }

}
