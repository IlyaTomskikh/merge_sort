import java.io.*;
import java.util.*;

public class Sort
{
    public static void main(String[] args)
    {
        String[] paths = new String[args.length - 2];
        System.arraycopy(args, 2, paths, 0, paths.length);
        boolean flag = mergeSort(paths, args[1], args[0]);
        if (flag) System.out.println("Files have been sorted");
        else System.out.println("Some error appeared");
    }


    public static boolean mergeSort(String[] paths, String destination, String mode)
    {
        List<File> files = new ArrayList<>();
        List<Scanner> scanners = new ArrayList<>();
        FileWriter fileWriter;
        try
        {
            fileWriter = new FileWriter(destination, false);
        }
        catch (IOException e)
        {
            System.out.println("ERROR: InputOutput");
            return false;
        }
        for (int ix = 0; ix < paths.length; ++ix)
        {
            files.add(ix, new File(paths[ix]));
            try
            {
                scanners.add((new Scanner(files.get(ix))));
            }
            catch (FileNotFoundException e)
            {
                System.out.println("ERROR: FileNotFound");
                return false;
            }
        }

        ArrayList<Integer> elements = new ArrayList<>();
        boolean checker = false;
        int min = 0;
        do
        {
            for (int ix = 0; ix < scanners.size(); ++ix)
                if(!scanners.get(ix).hasNextLine())
                {
                    files.set(ix, null);
                    scanners.set(ix, null);
                }
            // FIXME: 21.01.2022 
            if (!checker)
                for (Scanner scanner : scanners) elements.add(Integer.parseInt(scanner.nextLine()));
            else
            {
                int indexOf = elements.indexOf(min);
                if (scanners.get(indexOf) == null) elements.remove(indexOf);
                else elements.set(indexOf, Integer.parseInt(scanners.get(indexOf).nextLine()));
            }
            // FIXME: 21.01.2022 

            min = Collections.min(elements);
            checker = true;

            try
            {
                final String toWrite;
                toWrite = Integer.toString(min);
                fileWriter.write(toWrite + System.lineSeparator());
                System.out.println(toWrite + " has been written");
            }
            catch (IOException e)
            {
                System.out.println("ERROR: InputOutput");
            }
        } while (!elements.isEmpty());
        
        try
        {
            fileWriter.close();
        }
        catch (IOException e)
        {
            return false;
        }
        
        return true;
    }
}
