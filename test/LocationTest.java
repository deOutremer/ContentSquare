import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class LocationTest {
    @Test
    public void testTurn(){

        Location testLocation = new Location(1, 2, 0.0);
        testLocation.turn(Swivel.L);
        Assertions.assertTrue(testLocation.getFacing() == 'W');

        testLocation.turn(Swivel.L);
        Assertions.assertTrue(testLocation.getFacing() == 'S');

        testLocation.turn(Swivel.L);
        Assertions.assertTrue(testLocation.getFacing() == 'E');

        testLocation.turn(Swivel.L);
        Assertions.assertTrue(testLocation.getFacing() == 'N');

        testLocation.turn(Swivel.R);
        Assertions.assertTrue(testLocation.getFacing() == 'E');

        testLocation.turn(Swivel.R);
        Assertions.assertTrue(testLocation.getFacing() == 'S');

        testLocation.turn(Swivel.R);
        Assertions.assertTrue(testLocation.getFacing() == 'W');

        testLocation.turn(Swivel.R);
        Assertions.assertTrue(testLocation.getFacing() == 'N');
    }

    @Test
    public void testMoveForward() {

        Lawn lawn = new Lawn(5, 5, 1);
        Location testLocation = new Location(1, 2, 0.0);
        testLocation.moveForward(lawn);
        Assertions.assertTrue(testLocation.getLocationAsString().equals("1 3 N"));

        testLocation.setLocation(1, 2, 90.0);
        testLocation.moveForward(lawn);
        Assertions.assertTrue(testLocation.getLocationAsString().equals("2 2 E"));

        testLocation.setLocation(1, 2, 180.0);
        testLocation.moveForward(lawn);
        Assertions.assertTrue(testLocation.getLocationAsString().equals("1 1 S"));

        testLocation.setLocation(1, 2, 270.0);
        testLocation.moveForward(lawn);
        Assertions.assertTrue(testLocation.getLocationAsString().equals("0 2 W"));

        testLocation.moveForward(lawn);
        Assertions.assertTrue(testLocation.getLocationAsString().equals("0 2 W"));
    }
}
