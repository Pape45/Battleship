package battleship;

public class Ship {
    private String shipName;
    private int shipLength;
    private String[] coordinates;

    public Ship(String shipName, int shipLength) {
        this.shipName = shipName;
        this.shipLength = shipLength;
    }

    public String getShipName() {
        return shipName;
    }

    public void setShipName(String shipName) {
        this.shipName = shipName;
    }

    public int getShipLength() {
        return shipLength;
    }

    public void setShipLength(int shipLength) {
        this.shipLength = shipLength;
    }

    public String[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(String[] coordinates) {
        this.coordinates = coordinates;
    }
}
