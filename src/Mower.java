public class Mower implements Runnable{

    private Location location;
    private Lawn lawn;
    String movements;

    public Mower(Location location, Lawn lawn) {
        this.location = location;
        this.lawn = lawn;
    }

    void setMovements(String movements) {
        this.movements = movements;
    }
    /* Perform the movements */
    public void run() {

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
            }
        }
        System.out.println(getLocation().getLocationAsString());
    }

    public Location getLocation () {
        return location;
    }



}
