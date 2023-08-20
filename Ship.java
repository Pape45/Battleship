package battleship;

public enum Ship {

    AF("Aircraft Carrier ( 5 cells )", 5, ""),
    BS("Battleship (4 cells)", 4, ""),
    SM("Submarine (3 cells)", 3, ""),
    CR("cruiser (3 cells)", 3, ""),
    DT("destroyer (2 cells)", 2, "");

    private final String name;
    private final int length;
    private String coordinatesCovered;

    Ship(String name, int length, String coordinatesCovered) {
        this.name = name;
        this.length = length;
        this.coordinatesCovered = coordinatesCovered;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }

    public void addCoordinatesCovered(String coordinateToAdd) {
        this.coordinatesCovered += coordinateToAdd;
    }

    public void removeCoordinatesCovered(String coordinateToRemove) {
        coordinatesCovered = this.coordinatesCovered.replace(coordinateToRemove, "");
    }

}
