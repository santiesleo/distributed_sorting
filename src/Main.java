import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import sort.SortTask;

public class Main {
    private static final int THREAD_POOL_SIZE = 16;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del archivo de datos: ");
        String fileName = scanner.nextLine();
        String[] arr = readDataFromFile("doc/" + fileName);

        Long startTime = System.currentTimeMillis();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
        List<Future<String[]>> futures = new ArrayList<>();
        int size = (int) Math.ceil((double) arr.length / THREAD_POOL_SIZE);
        for (int i = 0; i < THREAD_POOL_SIZE; i++) {
            int start = i * size;
            int end = Math.min(start + size, arr.length);
            
            if (start > end) {
                break;
            }

            String[] subArr = Arrays.copyOfRange(arr, start, end);
            futures.add(executor.submit(new SortTask(subArr)));
        }

        List<String[]> sortedSubArrs = new ArrayList<>();
        
        for (Future<String[]> future : futures) {
            sortedSubArrs.add(future.get());
        }

        executor.shutdown();
        String[] sortedArr = SortTask.mergeSortedArrays(sortedSubArrs);
        String outputFilePath = "doc/sorted_" + fileName;
        writeDataToFile(outputFilePath, sortedArr);

        Long finalTime = System.currentTimeMillis();
        System.out.println(finalTime - startTime);
    }


    private static String[] readDataFromFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            StringBuilder content = new StringBuilder();

            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }

            return content.toString().trim().split("\\s+");
        } catch (IOException e) {
            e.printStackTrace();
            return new String[0];
        }
    }

    private static void writeDataToFile(String filePath, String[] data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Write each element of the sorted data on a new line
            for (String element : data) {
                writer.write(element + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
