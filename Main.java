package battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) {

        try (final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            String[][] field = new String[11][11];
            String[][] field1 = new String[11][11];
            String[][] fogOfWar = new String[11][11];
            String[][] fogOfWar1 = new String[11][11];

            createField(field, fogOfWar);
            createField(field1, fogOfWar1);

            System.out.println("\nPlayer 1, place your ships on the game field\n");
            String[][] coordinatesCovered1 = initialiseFieldWithShips(reader, field, fogOfWar);
            clearScreen(reader, true);
            System.out.println("\nPlayer 2, place your ships on the game field\n");
            String[][] coordinatesCovered2 = initialiseFieldWithShips(reader, field1, fogOfWar1);
            clearScreen(reader, true);

            System.out.println("\nThe game starts !\n");

            while (true) {

                for (int i = 1; i <= 2; i++) {
                    if (i == 1) {
                        showField(fogOfWar1);
                        System.out.println("---------------------");
                        showField(field);
                        System.out.printf("\nPlayer %d, it's your turn!\n".formatted(i));
                        clearScreen(reader, shootAtCoordinates(field1, fogOfWar1, reader, coordinatesCovered2));
                        if (!checkRemainingShips(field1)) {
                            System.out.println("\nCongratulations player 1 !");
                            System.exit(0);
                        }

                     } else {
                        showField(fogOfWar);
                        System.out.println("---------------------");
                        showField(field1);
                        System.out.printf("\nPlayer %d, it's your turn!\n".formatted(i));

                        clearScreen(reader, shootAtCoordinates(field, fogOfWar, reader, coordinatesCovered1));
                        if (!checkRemainingShips(field)) {
                            System.out.println("Congratulations player 2 !\n");
                            System.exit(0);
                        }
                    }
                }
            }


        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static Ship[] createShips() {

        return new Ship[]{
                Ship.AF, Ship.BS, Ship.SM, Ship.CR, Ship.DT
        };
    }

    private static void createField(String[][] field, String[][] fogOfWar) {

        char letter = 'A';

        field[0][0] = " ";
        fogOfWar[0][0] = " ";

        for (int i = 1; i < field.length; i++) {
            for (int j = 1; j < field.length; j++) {
                field[i][j] = "~";
                fogOfWar[i][j] = "~";
            }
            field[0][i] = String.valueOf(i);
            field[i][0] = String.valueOf(letter);
            fogOfWar[0][i] = String.valueOf(i);
            fogOfWar[i][0] = String.valueOf(letter);
            letter++;
        }
    }

    private static void showField(String[][] field) {

        for (String[] row : field) {
            for (String cols : row) {
                System.out.print(cols + " ");
            }
            System.out.println();
        }
    }

    private static String[][] initialiseFieldWithShips(BufferedReader reader, String[][] field, String[][] fogOfWar) throws IOException {

        Ship[] ships = createShips();

        String[][] coordinatesCovered = initiateCoordinatesCoveredTab();

        createField(field, fogOfWar);

        String coordinates;
        int fix = 0;

        for (int i = 0; i < 5; i++) {

            showField(field);
            System.out.println("\nEnter the coordinates of the " + ships[i].getName() + ":\n");
            coordinates = reader.readLine();

            while (!checkCoordinatesCoherence(coordinates, ships[i].getLength(), ships[i].getName())
                    || !putShipsOnField(field, coordinates, coordinatesCovered,fix))
            {
                coordinates = reader.readLine();
            }

            fix++;
        }

        showField(field);

        return coordinatesCovered;
    }

    private static boolean checkCoordinatesCoherence(String firstCoordinates, int shipLength, String shipName) {
        if (!firstCoordinates.contains(" ")) {
            System.out.println("\nError\n");

            return false;
        }

        String[] coordinates = firstCoordinates.split(" ");
        String[] begin = coordinates[0].split("");
        String[] end = coordinates[1].split("");
        int beginLetterInteger = begin[0].charAt(0);
        int endLetterInteger = end[0].charAt(0);
        int beginLength, endLength;
        boolean testLetter = begin[0].equals(end[0]);
        boolean testNumber = false;

        if (begin.length == 2 && end.length == 2) {
            testNumber = begin[1].equals(end[1]);
            beginLength = Integer.parseInt(begin[1]);
            endLength = Integer.parseInt(end[1]);
        } else if (begin.length == 2 && end.length == 3) {
            beginLength = Integer.parseInt(begin[1]);
            endLength = Integer.parseInt(end[1] + end[2]);

        } else if (begin.length == 3 && end.length == 2) {
            beginLength = Integer.parseInt(begin[1] + begin[2]);
            endLength = Integer.parseInt(end[1]);
        } else {
            beginLength = Integer.parseInt(begin[1] + begin[2]);
            endLength = Integer.parseInt(end[1] + end[2]);
            testNumber = (begin[1] + begin[2]).equals(end[1] + end[2]);
        }

        int shipTestLength = (beginLetterInteger > endLetterInteger) ? (beginLetterInteger - endLetterInteger + 1) : (endLetterInteger - beginLetterInteger + 1);
        boolean testLimit = beginLength <= 0 || beginLength > 10 || endLength <= 0 || endLength > 10 || begin[0].matches("[K-Z]") || end[0].matches("[K-Z]");

        if (!testLetter) {
            if (!testNumber) {
                System.out.println("\nError! Wrong ship location! Try again:\n");

                return false;
            } else if (shipTestLength != shipLength) {
                System.out.println("\nError! Wrong length of the " + shipName + "! Try again:\n");

                return false;
            }
        } else if (testLimit) {
            System.out.println("\nError! Wrong ship location! Try again:\n");

            return false;
        } else {
            shipTestLength = (beginLength > endLength) ? (beginLength - endLength + 1) : (endLength - beginLength + 1);

            if(shipTestLength != shipLength) {

                System.out.println("\nError! Wrong length of the " + shipName + "! Try again:\n");

                return false;
            }
        }

        return true;
    }

    private static boolean putShipsOnField(String[][] field, String firstCoordinates, String[][] coordinatesCovered, int fix) {

        String[] coordinates = firstCoordinates.split(" ");
        String coordinatesLeft = searchRightIndexInField(coordinates[0]);
        String coordinatesRight = searchRightIndexInField(coordinates[1]);
        String[] splitLeftCoordinates = coordinatesLeft.split("");
        String[] splitRightCoordinates = coordinatesRight.split("");
        String[] temp;
        int temporary;
        int indexToFixed, indexToStartFilling, indexToStopFilling;

        if (splitLeftCoordinates[0].compareTo(splitRightCoordinates[0]) > 0) {
            temp = splitLeftCoordinates;
            splitLeftCoordinates = splitRightCoordinates;
            splitRightCoordinates = temp;
        }

        boolean testLetter = splitLeftCoordinates[0].equals(splitRightCoordinates[0]);

        if (testLetter) {
            indexToFixed = getFirstCelIndex(splitRightCoordinates);
            indexToStartFilling = getSecondCelIndex(splitLeftCoordinates);
            indexToStopFilling = getSecondCelIndex(splitRightCoordinates);

            if (indexToStartFilling > indexToStopFilling) {
                temporary = indexToStartFilling;
                indexToStartFilling = indexToStopFilling;
                indexToStopFilling = temporary;
            }

            for (int i = indexToStartFilling; i <= indexToStopFilling ; i++) {
                if (checkTooCloseShip(field, indexToFixed, i)) {
                    System.out.println("\nError! You placed it too close to another one. Try again:\n");
                    return false;
                }
            }

            for (int i = indexToStartFilling; i <= indexToStopFilling ; i++) {
                field[indexToFixed][i] = "O";
                coordinatesCovered[0][fix] += String.valueOf(indexToFixed) + i;
            }

        } else {
            indexToFixed = getSecondCelIndex(splitRightCoordinates);
            indexToStartFilling = getFirstCelIndex(splitLeftCoordinates);
            indexToStopFilling = getFirstCelIndex(splitRightCoordinates);


            for (int i = indexToStartFilling; i <= indexToStopFilling ; i++) {
                if (checkTooCloseShip(field, i, indexToFixed)) {
                    System.out.println("Error! You placed it too close to another one. Try again:\n");
                    return false;
                }
            }

            for (int i = indexToStartFilling; i <= indexToStopFilling ; i++) {
                field[i][indexToFixed] = "O";
                coordinatesCovered[0][fix] += String.valueOf(i) + indexToFixed;
            }
        }

        return true;
    }

    public static int getFirstCelIndex(String[] coordinates) {
        if (coordinates.length == 4) {
            return Integer.parseInt(coordinates[2]);
        } else if (coordinates.length == 5) {
            return Integer.parseInt(coordinates[2] + coordinates[3]);
        } else if (coordinates.length == 6) {
            return Integer.parseInt(coordinates[3]);
        } else {
            return Integer.parseInt(coordinates[3] + coordinates[4]);
        }
    }

    public static int getSecondCelIndex(String[] coordinates) {
        if (coordinates.length == 4) {
            return Integer.parseInt(coordinates[3]);
        } else if (coordinates.length == 5) {
            return Integer.parseInt(coordinates[4]);
        } else if (coordinates.length == 6){
            return Integer.parseInt(coordinates[4] + coordinates[5]);
        } else {
            return Integer.parseInt(coordinates[5] + coordinates[6]);
        }
    }

    private static boolean checkTooCloseShip(String[][] field, int indexForShip, int iteration) {

        int leftMe = iteration - 1;
        int rightMe = iteration + 1;
        int belowMe = indexForShip - 1;
        int behindMe = indexForShip + 1;
        boolean test1, test2, test3, test4, test5;

        test1 = field[indexForShip][iteration].equals("O");
        test2 = leftMe > 1 && field[indexForShip][iteration].equals("O");
        test3 = rightMe < 10 && field[indexForShip][rightMe].equals("O");
        test4 = belowMe > 1 && field[belowMe][iteration].equals("O");
        test5 = behindMe < 10 && field[behindMe][iteration].equals("O");

        return test1 || test2 || test3 || test4 || test5;
    }

    private static String[][] allFieldIndex() {

        String[][] tab = new String[10][10];

        for (int i = 0; i < tab.length; i++) {
            char letter = 'A';
            for (int j = 0; j < tab[i].length; j++) {
                tab[i][j] = String.valueOf(letter) + (i + 1) + (j + 1) + (i + 1);
                letter++;
            }
        }

        return tab;
    }

    private static String searchRightIndexInField(String index) {
        String[][] allFieldIndex = allFieldIndex();

        for (String[] fieldIndex : allFieldIndex) {
            for (String s : fieldIndex) {
                if (s.contains(index)) {
                    return s;
                }
            }
        }

        return "";
    }

    private static boolean shootAtCoordinates(String[][] field, String[][] fogOfWar, BufferedReader reader, String[][] remainingCoordinates) throws IOException {

        showField(fogOfWar);
        String randomCoordinates, indexOfCoordinatesInput;
        int leftCoordinates, rightCoordinates;

        System.out.println("\nTake a shot !\n");

        randomCoordinates = reader.readLine();

        while (!checkCoordinatesCoherenceForShooting(randomCoordinates)) {
            randomCoordinates = reader.readLine();
        }

        indexOfCoordinatesInput = searchRightIndexInField(randomCoordinates);
        leftCoordinates = getFirstCelIndex(indexOfCoordinatesInput.split(""));
        rightCoordinates = getSecondCelIndex(indexOfCoordinatesInput.split(""));

        return shootAtCoordinates(leftCoordinates, rightCoordinates, field, fogOfWar, remainingCoordinates);
    }

    private static boolean checkCoordinatesCoherenceForShooting(String coordinates) {
        String[] splitCoordinates = coordinates.split("");

        if (splitCoordinates.length == 2) {
            if (splitCoordinates[0].matches("[K-Z]") || Integer.parseInt(splitCoordinates[1]) <= 0) {
                System.out.println("\nError! You entered wrong coordinates! Try again:\n");

                return false;
            }
        } else if (splitCoordinates.length == 3) {
            if (splitCoordinates[0].matches("[K-Z]") || Integer.parseInt(splitCoordinates[1] + splitCoordinates[2]) > 10) {
                System.out.println("\nError! You entered wrong coordinates! Try again:\n");

                return false;
            }
        } else {
            System.out.println("\nError! You entered wrong coordinates! Try again:\n");

            return false;
        }

        return true;

    }

    private static boolean shootAtCoordinates(int left, int right, String[][] field, String[][] fogOfWar, String[][] remainingCoordinates) {
        if (field[left][right].equals("O") || field[left][right].equals("X")) {
            fogOfWar[left][right] = "X";
            field[left][right] = "X";
            removeCoordinates(remainingCoordinates, left, right);
            System.out.println("\nYou hit a ship!\n");
            return isSank(remainingCoordinates);

        } else {
            fogOfWar[left][right] = "M";
            field[left][right] = "M";
            System.out.println("\nYou missed !\n");
            return false;
        }
    }

    private static boolean checkRemainingShips(String[][] field) {
        for (String[] row : field) {
            for (String cel : row ) {
                if (cel.equals("O")) {
                    return true;
                }
            }
        }

        System.out.println("\nYou sank the last ship. You won. Congratulations");

        return false;
    }

    private static void clearScreen(BufferedReader reader, boolean status) {
        if (status) {
            System.out.println("\nPress Enter\n...");
        } else {
            System.out.println("\nPress Enter and pass the move to another player\n...");
        }

        try {
            reader.readLine();
            Runtime.getRuntime().exec("clear");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private static String[][] initiateCoordinatesCoveredTab() {
        String[][] coordinatesCovered = new String[2][5];

        for (int i = 0; i < 5; i++) {
            coordinatesCovered[1][i] = "1";
            coordinatesCovered[0][i] = "";
        }

        return coordinatesCovered;
    }

    private static void removeCoordinates(String[][] coordinates, int left, int right) {

        for (int i = 0; i < 5; i++) {
            if (coordinates[0][i].contains(String.valueOf(left) + right)) {
                coordinates[0][i] = coordinates[0][i].replace(String.valueOf(left) + right, "");
            }
        }
    }

    private static boolean isSank(String[][] coordinates) {
        for (int i = 0; i < 5; i++) {
            if (coordinates[0][i].isEmpty() && coordinates[1][i].equals("1")) {
                System.out.println("You sank a ship!\n");
                return true;
            }
        }

        return false;
    }
}

