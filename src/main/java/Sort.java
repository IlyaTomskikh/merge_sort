import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sort {
    public static void main(String @NotNull [] args) {
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


    public static boolean mergeSort(String @NotNull [] paths, String destination, String mode) {
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
        if (mode.contains("-a")) {
            FileWriter dest = openFile(destination);
            if (dest == null) return false;

            while (!lines.isEmpty()) {
                if (minIndex != -1 && !scanners.isEmpty()) {
                    boolean flag = changeList("set", scanners.get(minIndex), lines, minIndex);
                    if (flag) scanners.remove(minIndex);
                } else if (minIndex != -1) lines.remove(minIndex);

                if (lines.isEmpty()) break;
                    AbstractMap.SimpleEntry<Integer, String> pair = findMin(lines, mode);
                    minIndex = pair.getKey();
                    if (minIndex == -1) {
                        System.out.println("Incorrect mode: " + mode);
                        break;
                    }
                    minValue = pair.getValue();

                if (!putValue(dest, minValue)) break;
            }
            return closeFile(dest);
        }
        else {
            FileWriter dest = openFile(destination), temp1, temp2;
            if (dest == null) return false;

            while (!lines.isEmpty()) {
                if (minIndex != -1 && !scanners.isEmpty()) {
                    boolean flag = changeList("set", scanners.get(minIndex), lines, minIndex);
                    if (flag) scanners.remove(minIndex);
                } else if (minIndex != -1) lines.remove(minIndex);

                if (lines.isEmpty()) break;

                AbstractMap.SimpleEntry<Integer, String> pair = findMin(lines, mode);
                minIndex = pair.getKey();
                if (minIndex == -1) {
                    System.out.println("Incorrect mode: " + mode);
                    break;
                }
                minValue = pair.getValue();

                if (!putValue(dest, minValue)) break;
            }
            return closeFile(dest);
        }
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
    static boolean hasNoSpace(@NotNull String line) {
        return !line.contains(" ");
    }

    /**
     * @param mode is a type of sorting
     * @param list is the list of string values
     * @return pair of minimum string as a value and its index as a key
     */
    @Contract("_, _ -> new")
    static AbstractMap.@NotNull SimpleEntry<Integer, String> findMin(@NotNull List<String> list, @NotNull String mode) {
        int minIndex;
        String min = list.get(0);
        if (mode.contains("-s")) {
            for (String line: list) if (line.compareTo(min) < 0) min = line;
        } else if (mode.contains("-i")) {
            int intMin = Integer.parseInt(min);
            for (String line: list) {
                int intLine = Integer.parseInt(line);
                if (intMin > intLine) intMin = intLine;
            }
            min = Integer.toString(intMin);
        } else return new AbstractMap.SimpleEntry<>(-1, min);
        minIndex = list.indexOf(min);
        return new AbstractMap.SimpleEntry<>(minIndex, min);
    }

    /**
     * @param fileWriter is a file which is used to write value
     * @param value is a value forced to be written
     * @return flag if the method has written the value
     */
    static boolean putValue(@NotNull FileWriter fileWriter, String value) {
        try {
            fileWriter.write(value + System.lineSeparator());
        } catch (IOException e) {
            closeFile(fileWriter);
            System.out.println("Couldn't write value: " + value);
            return false;
        }
        return true;
    }

    /**
     * @param path - path to existing file
     * @return opened file to write something
     */
    static FileWriter openFile (String path) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path, false);
        } catch (IOException e) {
            System.out.println("Couldn't open file: " + path);
        }
        return fileWriter;
    }

    /**
     * @param fileWriter is a file to be closed
     * @return if we closed the file
     */
    static boolean closeFile (@NotNull FileWriter fileWriter) {
        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("Couldn't close file " + fileWriter);
            return false;
        }
        return true;
    }

    /**
     * @param mode      is responsible for the type of operation
     * @param scanner   is the thread which is used to read the file
     * @param lines     is the list of string values
     * @param ix        is the index of the scanner
     * @return boolean value which is responsible for removing the scanner (true => scanner has to be deleted)
     */
    static boolean changeList(@NotNull String mode, Scanner scanner, List<String> lines, int ix) {
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
                else flag = true;
                break;
            default:
                System.out.println("Incorrect mode value: " + mode);
                break;
        }
        return flag;
    }

    /**
     * @param aSortedFile is a file with containing information
     * @param fileWriter is a new file
     * @return if method actually worked
     */
    static boolean rewrite (File aSortedFile,  FileWriter fileWriter) throws FileNotFoundException {
        // TODO: 09.02.2022 make this:
        /*
        *
        * BEFORE_CYCLE: def tempFile
        * CYCLE: line = aSortedFile.getLine() -> fileWriter.putLine(line) -> fileWriter.append(tempFile) -> tempFile = copyOf(fileWriter)
        * AFTER_CYCLE: tempFile.delete() -> fileWriter.close()
        *
        * */
        return true;
    }
}