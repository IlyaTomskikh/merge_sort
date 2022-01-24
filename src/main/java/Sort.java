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
                if (stringValue.equals("No value" + tmp)) indexesToDelete[tmp] = 1; //iTD == 1 means: this value is not correct, delete this scanner and string
                else indexesToDelete[tmp] = -1;                                     //iTD == -1 means: this value is good, don't corrupt it
                ++tmp;
            }

            for (int i = 0; i < indexesToDelete.length; ++i)
                if (indexesToDelete[i] == 1) {
                    scanners.remove(i);
                    stringValues.remove(i);
                }

            if (stringValues.isEmpty()) break;

            for (int i = 0; i < stringValues.size(); ++i)
                if (indexOfMin != i) intValues.add(Integer.parseInt(stringValues.get(i)));
                else intValues.set(i, Integer.parseInt(stringValues.get(i)));

            min = Collections.min(intValues);

            for (indexOfMin = 0; indexOfMin < intValues.size(); ++indexOfMin) if (intValues.get(indexOfMin) == min) break;

            indexesToDelete[indexOfMin] = 2;    //iTD == 2 means: we have already written this value, get next string

            try {
                final String out = "iter " + iter + ": tried to write " + min;
                System.out.println(out);
                fileWriter.write(String.valueOf(min));
            } catch (IOException e) {
                System.out.println("ERROR InputOutput: couldn't write in file" + destination);
                break;
            }

            ++iter;
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
