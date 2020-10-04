enum Swivel {
    L,
    R
}


public class Location {

    private int x;
    private int y;
    private double orientation;

    Location (int x, int y, double orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public Location() {

    }

    /* Turn the Mower to te correct orientation */
    public void turn (Swivel direction) {
        orientation = (direction == Swivel.L) ? (orientation - 90) % 360 : (orientation + 90) % 360;
        if (orientation < 0) orientation += 360;
    }

    /* Update coordinates according to initial orientation */
    public void moveForward(Lawn lawn) {
        if (canMove(lawn))
        switch (getFacing()) {
            case 'N':
            {
                moveYForward ();
                break;
            }
            case 'S':
            {
                moveYBackwards();
                break;
            }
            case 'W':
            {
                moveXBackwards();
                break;
            }
            case 'E':
            {
                moveXForward();
                break;
            }
        }
    }

    public void setLocation(Location location) {
        this.x = location.getXLocation();
        this.y = location.getYLocation();
        this.orientation = location.getOrientation();
    }

    public void setLocation (int x, int y, double orientation) {
        this.x = x;
        this.y = y;
        this.orientation = orientation;
    }

    public void setBadLocation() {
        this.x = Integer.MIN_VALUE;
        this.y = Integer.MIN_VALUE;
        this.orientation = 0.0;
    }

    private void moveXForward () {
        x = x + 1;
    }

    private void moveYForward () {
        y = y + 1;
    }

    private void moveXBackwards () {
        x = x - 1;
    }

    private void moveYBackwards () {
        y = y - 1;
    }

    public int getXLocation() {
        return x;
    }

    public int getYLocation() {
        return y;
    }

    private double getOrientation() {
        return orientation;
    }

    public String getLocationAsString() {
        return getXLocation() + " " + getYLocation() + " " + getFacing();
    }

    public char getFacing() {
        if (orientation == 0) return 'N';
        if (orientation == 90) return 'E';
        if (orientation == 180) return 'S';
        if (orientation == 270) return 'W';
        return 0;
    }

    /* Check if the Mower is on the border of the Lawn, so that it wont
    *move when the action asked of it will land it outside the Lawn
    */
    private boolean canMove(Lawn lawn) {
        if ((x == lawn.getLawnX() && getFacing() == 'E') ||
                (y == lawn.getLawnY() && getFacing() == 'N') ||
                (x == 0 && getFacing() == 'W') ||
                (y == 0 && getFacing() == 'S'))
            return false;
        else return true;
    }
}
