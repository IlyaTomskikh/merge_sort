import java.io.*;
import java.util.*;

public class Sort {
    public static void main(String[] args) {
        //mode types:
        //-a == по возрастанию
        //-d == по убыванию
        //-i == integers
        //-s == strings
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
        boolean flag;
        if (mode.equals("-a-i") || mode.equals("-d-i")) flag = mergeSort(paths, dest, mode);
        else flag = mergeSortString(paths, dest, mode);
        if (flag) System.out.println("Files have been sorted");
        else System.out.println("Some error appeared");
    }


    public static boolean mergeSort(String[] paths, String destination, String mode) {
        List<Scanner> scanners = new ArrayList<>();
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(destination, false);
        } catch (IOException e) {
            System.out.println("ERROR InputOutput: couldn't open file" + destination);
            return false;
        }
        for (int ix = 0; ix < paths.length; ++ix)
            try {
                scanners.add((new Scanner(new File(paths[ix]))));
            } catch (FileNotFoundException e) {
                System.out.println("ERROR FileNotFound: couldn't open a file" + paths[ix]);
                return false;
            }

        int min;
        int indexOfMin = -1;
        List<String> stringValues = new ArrayList<>();
        List<Integer> intValues = new ArrayList<>();
        int[] indexesToDelete;
        int tmp, iter = 0;

        do {
            tmp = 0;
            for (Scanner scanner: scanners) {
                if (scanner.hasNext()) {
                    if (indexOfMin == -1) stringValues.add(scanner.nextLine());
                    else if (indexOfMin == scanners.indexOf(scanner) && indexOfMin == tmp)
                        stringValues.set(indexOfMin, scanner.nextLine());
                } else
                    if(indexOfMin != -1) stringValues.set(tmp, "No value" + tmp);
                    else stringValues.add("No value" + tmp);
                ++tmp;
            }
            indexesToDelete = new int[scanners.size()];

            tmp = 0;
            for (String stringValue : stringValues) {
                if (stringValue.equals("No value" + tmp)) indexesToDelete[tmp] = 1;
                else indexesToDelete[tmp] = -1;
                ++tmp;
            }

            for (int i = 0; i < indexesToDelete.length; ++i)
                if (indexesToDelete[i] == 1) {
                    scanners.remove(i);
                    stringValues.remove(i);
                    intValues.remove(i);
                }

            if (stringValues.isEmpty()) break;

            for (int i = 0; i < stringValues.size(); ++i) {
                if (indexesToDelete[i] == -1) {
                    if (indexOfMin == -1) intValues.add(Integer.parseInt(stringValues.get(i)));
                    else if (indexOfMin == i) intValues.set(i, Integer.parseInt(stringValues.get(i)));
                }
            }

            min = Collections.min(intValues);
            indexOfMin = 0;
            for (; indexOfMin < intValues.size(); ++indexOfMin) if (intValues.get(indexOfMin) == min) break;

            try {
                final String out = "iter " + iter + ": tried to write min = " + min + ", index = " + indexOfMin;
                System.out.println(out);
                fileWriter.write(String.valueOf(min));
                fileWriter.write(System.lineSeparator());
            } catch (IOException e) {
                System.out.println("ERROR InputOutput: couldn't write in file" + destination);
                break;
            }

            ++iter;
        } while (!scanners.isEmpty());
        iter = 0;
        while (!intValues.isEmpty()) {
            min = Collections.min(intValues);
            indexOfMin = 0;
            for (; indexOfMin < intValues.size(); ++indexOfMin) if (intValues.get(indexOfMin) == min) break;
            try {
                final String out = "iter " + iter + ": tried to write min = " + min + ", index = " + indexOfMin;
                System.out.println(out);
                fileWriter.write(String.valueOf(min));
                fileWriter.write(System.lineSeparator());
            } catch (IOException e) {
                System.out.println("ERROR InputOutput: couldn't write in file" + destination);
                break;
            }
            intValues.remove(indexOfMin);
            ++iter;
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("ERROR InputOutput: couldn't close a file");
            return false;
        }
        if (mode.contains("-d")) {
            File reverseOrder = new File("temp.txt");
            try {
                reverseOrder.createNewFile();
                FileWriter fw = new FileWriter("temp.txt", true);
                BufferedWriter bw = new BufferedWriter(fw);
                Scanner scanner = new Scanner(new File(destination));
                while (scanner.hasNextLine()) bw.write(scanner.nextLine());
                bw.close();
                scanner.close();
            } catch (IOException e) {
                System.out.println("ERROR InputOutput: couldn't create a file for reverse ordered sorting");
                return false;
            }
        }
        return true;
    }

    public static boolean mergeSortString(String[] paths, String destination, String mode) {
        List<Scanner> scanners = new ArrayList<>();
        FileWriter fileWriter;
        try {
            fileWriter = new FileWriter(destination, false);
        } catch (IOException e) {
            System.out.println("ERROR InputOutput: couldn't open file" + destination);
            return false;
        }
        for (int ix = 0; ix < paths.length; ++ix)
            try {
                scanners.add((new Scanner(new File(paths[ix]))));
            } catch (FileNotFoundException e) {
                System.out.println("ERROR FileNotFound: couldn't open a file" + paths[ix]);
                return false;
            }

        int min;
        int indexOfMin = -1;
        List<String> stringValues = new ArrayList<>();
        List<Integer> intValues = new ArrayList<>();
        int[] indexesToDelete;
        int tmp, iter = 0;

        do {
            tmp = 0;
            for (Scanner scanner: scanners) {
                if (scanner.hasNext()) {
                    if (indexOfMin == -1) stringValues.add(scanner.nextLine());
                    else if (indexOfMin == scanners.indexOf(scanner) && indexOfMin == tmp)
                        stringValues.set(indexOfMin, scanner.nextLine());
                } else
                if(indexOfMin != -1) stringValues.set(tmp, "No value" + tmp);
                else stringValues.add("No value" + tmp);
                ++tmp;
            }
            indexesToDelete = new int[scanners.size()];

            tmp = 0;
            for (String stringValue : stringValues) {
                if (stringValue.equals("No value" + tmp)) indexesToDelete[tmp] = 1;
                else indexesToDelete[tmp] = -1;
                ++tmp;
            }

            for (int i = 0; i < indexesToDelete.length; ++i)
                if (indexesToDelete[i] == 1) {
                    scanners.remove(i);
                    stringValues.remove(i);
                    intValues.remove(i);
                }

            if (stringValues.isEmpty()) break;

            for (int i = 0; i < stringValues.size(); ++i) {
                if (indexesToDelete[i] == -1) {
                    if (indexOfMin == -1) intValues.add(Integer.parseInt(stringValues.get(i)));
                    else if (indexOfMin == i) intValues.set(i, Integer.parseInt(stringValues.get(i)));
                }
            }

            min = Collections.min(intValues);
            indexOfMin = 0;
            for (; indexOfMin < intValues.size(); ++indexOfMin) if (intValues.get(indexOfMin) == min) break;

            try {
                final String out = "iter " + iter + ": tried to write min = " + min + ", index = " + indexOfMin;
                System.out.println(out);
                fileWriter.write(String.valueOf(min));
                fileWriter.write(System.lineSeparator());
            } catch (IOException e) {
                System.out.println("ERROR InputOutput: couldn't write in file" + destination);
                break;
            }

            ++iter;
        } while (!scanners.isEmpty());
        iter = 0;
        while (!intValues.isEmpty()) {
            min = Collections.min(intValues);
            indexOfMin = 0;
            for (; indexOfMin < intValues.size(); ++indexOfMin) if (intValues.get(indexOfMin) == min) break;
            try {
                final String out = "iter " + iter + ": tried to write min = " + min + ", index = " + indexOfMin;
                System.out.println(out);
                fileWriter.write(String.valueOf(min));
                fileWriter.write(System.lineSeparator());
            } catch (IOException e) {
                System.out.println("ERROR InputOutput: couldn't write in file" + destination);
                break;
            }
            intValues.remove(indexOfMin);
            ++iter;
        }

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("ERROR InputOutput: couldn't close a file");
            return false;
        }

        return true;
    }
}
