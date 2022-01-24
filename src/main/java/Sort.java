import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class Sort {
    public static void main(String[] args) {
        String[] paths = new String[args.length - 2];
        System.arraycopy(args, 2, paths, 0, paths.length);
        boolean flag = mergeSort(paths, args[1], args[0]);
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

        int min = 0;
        int indexOfMin = -1;
        List<String> stringValues = new ArrayList<>();
        int[] intValues, indexesToDelete = new int[stringValues.size()];

        int tmp = 0;
        do {
            Arrays.fill(indexesToDelete, -1);

            for (Scanner scanner: scanners)
                if (scanner.hasNext()) {
                    if (indexOfMin == scanners.indexOf(scanner) || indexOfMin == -1) stringValues.add(scanner.nextLine());
                } else {
                    stringValues.add("No value" + tmp);
                    ++tmp;
                }

            tmp ^= tmp;

            for (String stringValue : stringValues)
                if (stringValue.equals("No value" + tmp)) {
                    indexesToDelete[tmp] = 1;
                    ++tmp;
                }
            for (int i = 0; i < indexesToDelete.length; ++i)
                if (indexesToDelete[i] == 1) {
                    scanners.remove(i);
                    stringValues.remove(i);
                }
            if (stringValues.isEmpty()) break;
            intValues = new int[stringValues.size()];
            for (int i = 0; i < intValues.length; ++i) intValues[i] = Integer.parseInt(stringValues.get(i));
            min = Arrays.stream(intValues).min().getAsInt();
            int finalMin = min;
            int[] finalIntValues = intValues;
            indexOfMin = IntStream.range(0, intValues.length).filter(i -> finalMin == finalIntValues[i]).findFirst().getAsInt();
            try {
                System.out.println("tried to write " + min);
                fileWriter.write(String.valueOf(min));
            } catch (IOException e) {
                System.out.println("ERROR InputOutput: couldn't write in file" + destination);
                break;
            }

        } while (!stringValues.isEmpty());

        try {
            fileWriter.close();
        } catch (IOException e) {
            System.out.println("ERROR InputOutput: couldn't close a file");
            return false;
        }
        
        return true;
    }
}
