import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.*;

public class main {

    public static void main(String[] args) throws IOException {
        /* The txt file containing the instructions should be passed as a program argument and be in args[0]*/

        BufferedReader br = new BufferedReader(new FileReader(args[0]));
        try {
            /* We get the number of lines from the file in order to initiate the correct number of Mowers
            * The first line is the size of the lawn. After that, each Mower is represented by two consecutive lines*/
            Path path = Paths.get(args[0]);
            Lawn lawn = initLawn(br, path);

            if (lawn.getNumOfMowers() > 0) {
                ExecutorService executor = Executors.newFixedThreadPool(lawn.getNumOfMowers());

                /* For each Mower, we check the integrity of the data, create the Mower and set it on its way
                 * Currently the Mowers are initialized sequentially, though each Mower might take a different time to
                 * complete it's tasks and they can work in parallel. We use Futures to enable printing the
                 * outputs in the correct order
                 * */
                ArrayList<String> results = new ArrayList<>();
                lawn.resetNumOfMowers();
                while (br.ready()) {
                    Callable<String> mower = initMower(br, lawn);
                    if (null != mower) {
                        Future<String> future = executor.submit(mower);
                        try {
                            results.add(future.get());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        }
                    }
                }


                for (int i = 0; i < results.size(); i++) {
                    System.out.println(results.get(i));
                }
                executor.shutdown();
            }
            else {
                System.out.println("No mowers were initialized");
            }

        } catch (badLawnInitException exception) {
            System.out.println("Lawn can not be initialized to these parameters: \n" + exception.getMessage());

        } finally {
            br.close();
        }

    }

    public static Lawn initLawn (BufferedReader br, Path path) throws IOException, badLawnInitException {
        String line = br.readLine();
        int emptyLines = 0;
        while (null != line && line.isEmpty()) {
            line = br.readLine();
            emptyLines++;
        }
        int numOfMowers = (int) ((Files.lines(path).count()-emptyLines)/2);
        String[] lawnDimensions = line.split(" ");
        int lawnWidth = Integer.parseInt(lawnDimensions[0]);
        int lawnHeight = Integer.parseInt(lawnDimensions[1]);
        if (lawnDimensions.length > 2 || (lawnWidth < 0 || lawnHeight < 0)) throw new badLawnInitException(line);
        return new Lawn(lawnWidth, lawnHeight, numOfMowers);
    }

    /* For each Mower, we check the integrity of the data, create the Mower and set it on its way
     * Currently the Mowers are initialized sequentially, though each Mower might take a different time to
     * complete it's tasks and they can work in parallel. We use Futures to enable printing the
     * outputs in the correct order
     * */
    public static Mower initMower(BufferedReader br, Lawn lawn) throws IOException {
        try {
            int BUFFER_SIZE = 1000;
            String startLocation = br.readLine();
            while (null != startLocation && startLocation.isEmpty()) {
                startLocation = br.readLine();
            }
            br.mark(BUFFER_SIZE);
            String movements = br.readLine();
            while (null != movements && movements.isEmpty()) {
                br.mark(BUFFER_SIZE);
                movements = br.readLine();
            }
            if (null == startLocation && null == movements) { return null; }
            String[] mowerLocation = startLocation.split(" ");
            if (mowerLocation.length != 3 || //Check data integrity
                    Integer.parseInt(mowerLocation[0]) > lawn.getLawnX() ||
                    Integer.parseInt(mowerLocation[0]) < 0 ||
                    Integer.parseInt(mowerLocation[1]) > lawn.getLawnY() ||
                    Integer.parseInt(mowerLocation[1]) < 0)
                throw new badMowerInitException("Start location: " + startLocation);
            double orientationInDegrees = convertFacing(mowerLocation[2]);
            if (null == movements) { throw new badMowerInitException("No movements entered"); }
            boolean correctMovementsInput = movements.split(" ").length == 1 ? true : false;
            if (!correctMovementsInput) {
                br.reset();
            }
            if (orientationInDegrees == -666)
                throw new badMowerInitException("Initial orientation: " + mowerLocation[2]);
            Location newMowerLocation = new Location(
                    Integer.parseInt(mowerLocation[0]),
                    Integer.parseInt(mowerLocation[1]),
                    orientationInDegrees);

            if (correctMovementsInput) {
                lawn.topMowersCountByOne();
                return new Mower(newMowerLocation, movements, lawn);
            } else {
                if (!startLocation.isEmpty()) {
                    throw new badMowerInitException("No movements entered");
                }
                return null;
            }
        } catch (badMowerInitException exception) {
            System.out.println("Mower can not be initialized to these parameters: \n" + exception.getMessage());
            return null;
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

