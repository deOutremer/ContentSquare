public class Lawn {

    private int xSize;
    private int ySize;
    private int numOfMowers;

    public Lawn(int x, int y, int numOfMowers) {
        xSize = x;
        ySize = y;
        this.numOfMowers = numOfMowers;
    }

    public int getLawnX() {
        return xSize;
    }

    public int getLawnY() {
        return ySize;
    }

    public int getNumOfMowers() { return numOfMowers; }

    public void resetNumOfMowers() { numOfMowers = 0;}

    public void topMowersCountByOne() { numOfMowers++; }
}
