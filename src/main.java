import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.*;

public class main {

    public static void main(String[] args) throws IOException {
        /* The txt file containing the instructions should be passed as a program argument and be in args[0]*/

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        try {

            /* We get the number of lines from the file in order to initiate the correct number of Mowers
            * The first line is the size of the lawn. After that, each Mower is represented by two consecutive lines*/
            Path path = Paths.get(args[0]);
            int numOfMowers = (int) ((Files.lines(path).count()-1)/2);
            ExecutorService executor = Executors.newFixedThreadPool(numOfMowers);
            String line = br.readLine();
            String[] lawnDimensions = line.split(" ");
            if (lawnDimensions.length > 2) throw new badLawnInitException(line);
            Lawn lawn = new Lawn(Integer.parseInt(lawnDimensions[0]), Integer.parseInt(lawnDimensions[1]));



            /* For each Mower, we check the integrity of the data, create the Mower and set it on its way
            * Currently the Mowers are initialized sequentially, though each Mower might take a different time to
            * complete it's tasks and they can work in parallel. We use Futures to enable printing the
            * outputs in the correct order
            * */
            String[] results = new String[numOfMowers];
            //List<Future<String>> results = new ArrayList<>();
            for (int i = 0; i < numOfMowers; i++) {
                String startLocation = br.readLine();
                String movements = br.readLine();
                String[] mowerLocation = startLocation.split(" ");
                if (mowerLocation.length > 3 || //Check data integrity
                        Integer.parseInt(mowerLocation[0]) > lawn.getLawnX() ||
                        Integer.parseInt(mowerLocation[0]) < 0 ||
                        Integer.parseInt(mowerLocation[1]) > lawn.getLawnX() ||
                        Integer.parseInt(mowerLocation[1]) < 0 ||
                        !movements.matches("[LRF]+")) throw new badMowerInitException(line);
                Location newMowerLocation = new Location(
                        Integer.parseInt(mowerLocation[0]),
                        Integer.parseInt(mowerLocation[1]),
                        convertFacing(mowerLocation[2]));
                Callable<String> mower = new Mower(newMowerLocation, lawn);
                ((Mower) mower).setMovements(movements);
                Future<String> future = executor.submit(mower);
                try {
                    results[i] = future.get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            for (int i = 0; i < numOfMowers; i++ ) {
                System.out.println(results[i]);
            }
            executor.shutdown();

        } catch (badLawnInitException exception) {
            System.out.println("Lawn can not be initialized to these parameters: " + exception.getMessage());
        } catch (badMowerInitException exception) {
            System.out.println("Mower can not be initialized to these parameters: " + exception.getMessage());
        } finally {
            br.close();
        }

    }
    /* A function to translate orientation to degrees */
    public static double convertFacing(String orientation) {
        if (orientation.equals("N")) return 0;
        if (orientation.equals("E")) return 90;
        if (orientation.equals("S")) return 180;
        if (orientation.equals("W")) return 270;
        return -666;
    }

}

class badMowerInitException extends Exception {
    public badMowerInitException(String errorMessage) {
        super(errorMessage);
    }
}

class badLawnInitException extends Exception {
    public badLawnInitException(String errorMessage) {
        super(errorMessage);
    }
}
