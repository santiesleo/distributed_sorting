package client;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;
import threadPool.ThreadPool;

public class Client {
    private static String fileName;
    private static ThreadPool threadPool;
    private static Long startTime;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del archivo de datos: ");
        fileName = scanner.nextLine();
        String[] arr = readDataFromFile("doc/" + fileName);

        startTime = System.currentTimeMillis();
        ThreadPool threadPool = new ThreadPool(arr);
        threadPool.execute();
    }

    // observer
    public static void notifyClient() {
        // Escribir el resultado ordenado en un nuevo archivo de texto
        // obtiene el array ordenado
        
        System.out.println("sorted igual a " + threadPool.getSorted());
        String[] sortedArr = threadPool.getSorted();
        String outputFilePath = "doc/sorted_" + fileName;
        writeDataToFile(outputFilePath, sortedArr);

       long endTime = System.currentTimeMillis();
       long latency = endTime - startTime;

       System.out.println("Latencia de dist_sorter: " + latency + " milisegundos");
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
