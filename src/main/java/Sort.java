import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
public class Sort {
    public static void main(String[] args) {
        String[] paths;
        String mode = "-a";
        String dest;
        if (args[0].equals("-a") || args[0].equals("-d")) {
            mode = args[0];
            if (args[1].equals("-s") || args[1].equals("-i")) mode += args[1];
            else {
                System.out.println("args[1] must contain a type of file content");
                return;
            }
            paths = new String[args.length - 3];
            System.arraycopy(args, 3, paths, 0, paths.length);
            dest = args[2];
        } else if (args[0].equals("-s") || args[0].equals("-i")) {
            mode += args[0];
            paths = new String[args.length - 2];
            System.arraycopy(args, 2, paths, 0, paths.length);
            dest = args[1];
        } else {
            System.out.println("args[0] must contain atleast a type of file content");
            return;
        }
        boolean flag = mergeSort(paths, dest, mode);
        if (flag) System.out.println("Files have been sorted");
        else System.out.println("Some error appeared");
    }


    public static boolean mergeSort(String[] paths, String destination, String mode) {
        List<Scanner> scanners = new ArrayList<>();
        for (String path: paths) {
            try {
                scanners.add(new Scanner(new File(path)));  //addition of a new scanner based on the path to the current input file
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't open file: " + path);
                return false;
            }
        }

        List<String> lines = new ArrayList<>();
        int counter = 0;
        for (Scanner scanner: scanners) {
            boolean flag = changeList("add", scanner, lines, counter);
            if (flag) scanners.remove(counter);
            ++counter;
        }
        String minValue;
        int minIndex = -1;
        if (mode.contains("-a")) while (!lines.isEmpty()) {
            if (minIndex != -1 && !scanners.isEmpty()) {
                boolean flag = changeList("set", scanners.get(minIndex), lines, minIndex);
                if (flag) scanners.remove(minIndex);
            } else if (minIndex != -1) {
                lines.remove(minIndex);
            }

            AbstractMap.SimpleEntry<Integer, String> pair = findMin(lines);
            minIndex = pair.getKey();
            minValue = pair.getValue();

            FileWriter dest;
            try {
                dest = new FileWriter(destination, false);
            } catch (IOException e) {
                System.out.println("Couldn't open file: " + destination);
                return false;
            }
            try {
                dest.write(minValue);
            } catch (IOException e) {
                System.out.println("Couldn't write value: " + minValue);
                return false;
            }
        }
        else while (!lines.isEmpty()){
            if (minIndex != -1 && !scanners.isEmpty()) {
                boolean flag = changeList("set", scanners.get(minIndex), lines, minIndex);
                if (flag) scanners.remove(minIndex);
            } else if (minIndex != -1) {
                lines.remove(minIndex);
            }

            AbstractMap.SimpleEntry<Integer, String> pair = findMin(lines);
            minIndex = pair.getKey();
            minValue = pair.getValue();

            RandomAccessFile dest;
            try {
                dest = new RandomAccessFile(destination, "w");
            } catch (FileNotFoundException e) {
                System.out.println("Couldn't open file: " + destination);
                return false;
            }
            try {
                dest.seek(0);
                dest.writeBytes(minValue);
            } catch (IOException e) {
                System.out.println("Negative seek offset");
                return false;
            }
        }
        return true;
    }

    /**
     * @param scanner is the thread which is used to read the file
     * @return a new line from the file
     */
    static String readLine(Scanner scanner) {
        if (scanner != null && scanner.hasNextLine()) return scanner.nextLine();
        else return " ";
    }

    /**
     * @param line is a string value
     * @return boolean value which says if the string contains ' ' value
     */
    static boolean hasNoSpace(String line) {
        return !line.contains(" ");
    }

    /**
     * @param list is the list of string values
     * @return pair of minimum string as a value and its index as a key
     */
    static AbstractMap.SimpleEntry<Integer, String> findMin (List<String> list) {
        int minIndex = 0;
        String min = list.get(minIndex);
        for (String string: list)
            if (string.compareTo(min) < 0) min = string;
        return new AbstractMap.SimpleEntry<>(minIndex, min);
    }

    /**
     * @param mode      is responsible for the type of operation
     * @param scanner   is the thread which is used to read the file
     * @param lines     is the list of string values
     * @param ix        is the index of the scanner
     * @return boolean value which is responsible for removing the scanner (true => scanner has to be deleted)
     */
    static boolean changeList(String mode, Scanner scanner, List<String> lines, int ix){
        String line = readLine(scanner);
        boolean flag = false;
        String switcher = mode.toLowerCase();
        switch (switcher) {
            case "set":
                if (hasNoSpace(line)) lines.set(ix, line);
                else {
                    flag = true;
                    lines.remove(ix);
                }
                break;
            case "add":
                if (hasNoSpace(line)) lines.add(line);
                else {
                    flag = true;
                    lines.remove(ix);
                }
                break;
            default:
                System.out.println("Incorrect mode value: " + mode);
                break;
        }
        return flag;
    }
}