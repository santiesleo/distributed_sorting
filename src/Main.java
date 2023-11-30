import dataStructures.MergeSort.MergeSort;

import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import threadPool.*;

public class Main {
    private static String fileName;
    private static ThreadPool threadPool;
    private static long startTime;

    public static void main(String[] args) {
        // Crea un objeto de la clase Mergesort
        // MergeSort<String> ms = new MergeSort<>();

        // Solicitar al usuario el nombre del archivo de datos
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del archivo de datos: ");
        fileName = scanner.nextLine();
        String[] data = readDataFromFile("doc/" + fileName);

        // Medir la latencia del método dist_sorter
        startTime = System.currentTimeMillis();

        // Invocar al método dist_sorter (reemplaza esto con tu lógica específica)
        // Aquí simulamos una llamada ficticia
        threadPool = new ThreadPool(data);
        threadPool.execute();
    }

    // observer
    private static void notifyMain() {
         // Escribir el resultado ordenado en un nuevo archivo de texto
        String outputFilePath = "doc/sorted_" + fileName;
        writeDataToFile(outputFilePath, threadPool.getSorted());

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
