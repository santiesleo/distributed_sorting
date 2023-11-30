package threadPool;
import main.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import dataStructures.MergeSort.MergeSort;

public class ThreadPool {
    private String[] bigData;
    private static String[] sortedBigData;
    private static int counter = 0;
    // Crear un "thread pool" con 16 hilos
    private static int nThreads = 16;
    private static ExecutorService executorService;
    private static String[] sorted;
    private static List<String[]> arrays;

    public ThreadPool(String[] bigData){
        this.bigData = bigData;
    } 

    public String[] getSorted() {
        return sorted;
    }

    // thread notifica que ya está disponible - pasa el trabajo terminado
    public static void returnPool(String[] sorted) {
        sortedBigData[counter] = Arrays.toString(sorted).substring(1, Arrays.toString(sorted).length()-1);
        counter++;

        if (counter == arrays.size() - 1) {
            MergeSort<String> ms = new MergeSort<>();
            //ThreadPool.sorted = ms.sort(sortedBigData);
            Main.notifyMain();
            // Apagar el "thread pool" después de completar todas las tareas
            executorService.shutdown();
        }
    }

    // ejecutar 
    public void execute() {
        arrays = divideData(nThreads);
        ThreadPool.sortedBigData = new String[arrays.size()];
        System.out.println("ya seteo el sorted big data");

        executorService = Executors.newFixedThreadPool(nThreads);
        
        // thread para hacer Join
        // Runnable joinWorker = new WorkerJoinThread(nThreads - 1);
        // executorService.execute(joinWorker);

        // Agregar tareas al "thread pool"
        for (int i = 0; i < arrays.size(); i++) {
            System.out.println("entra a crear arrays pos " + i);
            Runnable worker = new WorkerThread(arrays.get(i));
            executorService.execute(worker);
        }
    }

    // divide el array grande en array de pequeños elementos
    public List<String[]> divideData(int n) {
        System.out.println("n = " + n);
        List<String[]> dividedArrays = new ArrayList<>();
        int totalElements = bigData.length;
        int chunkSize = (int) Math.ceil(totalElements / (double) n);

        for (int i = 0; i < totalElements; i += chunkSize) {
            int end = Math.min(totalElements, i + chunkSize);
            String[] chunk = new String[end - i];
            System.arraycopy(bigData, i, chunk, 0, chunk.length);
            dividedArrays.add(chunk);
        }

        return dividedArrays;
    }
}