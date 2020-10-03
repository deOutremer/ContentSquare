import java.util.concurrent.Callable;

public class Mower implements Callable<String>{

    private Location location;
    private Location initialLocation;
    private Lawn lawn;
    private String movements;
    private String error;

    public Mower(Location location, String movements, Lawn lawn) {
        this.initialLocation = location;
        this.location = location;
        this.lawn = lawn;
        this.error = "";
        setMovements(movements);
    }

    void setMovements(String movements) {
        this.movements = movements;
    }
    /* Perform the movements */
    public String call() {

        for (char movement : movements.toCharArray()) {
            switch (movement) {
                case 'F': {
                    location.moveForward(lawn);
                    break;
                }
                case 'L': {
                    location.turn(Swivel.L);
                    break;
                }
                case 'R': {
                    location.turn(Swivel.R);
                    break;
                }
                default: {
                    this.location = initialLocation;
                    this.error = "Invalid mower movement: '" + movements + "'. Mower reset to initial location: ";
                    this.movements = "";
                    break;
                }
            }
        }
        return error.concat(getLocation().getLocationAsString());
    }

    public Location getLocation () {
        return location;
    }



}
