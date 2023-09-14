package battleship;

public enum Ship {

    AF("Aircraft Carrier ( 5 cells )", 5),
    BS("Battleship (4 cells)", 4),
    SM("Submarine (3 cells)", 3),
    CR("cruiser (3 cells)", 3),
    DT("destroyer (2 cells)", 2);

    private final String name;
    private final int length;

    Ship(String name, int length) {
        this.name = name;
        this.length = length;
    }

    public String getName() {
        return name;
    }

    public int getLength() {
        return length;
    }
}