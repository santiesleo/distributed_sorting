package threadPool;
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

    public ThreadPool(String[] bigData){
        this.bigData = bigData;
        ThreadPool.sortedBigData = new String[nThreads];
    } 

    public String[] getSorted() {
        return sorted;
    }

    // thread notifica que ya está disponible - pasa el trabajo terminado
    public static void returnPool(String[] sorted) {
        sortedBigData[counter] = Arrays.toString(sorted);
        counter++;

        if (counter == nThreads - 1) {
            MergeSort<String> ms = new MergeSort<>();
            ThreadPool.sorted = ms.sort(sortedBigData);
            // Apagar el "thread pool" después de completar todas las tareas
            executorService.shutdown();
        }
    }

    // ejecutar 
    public void execute() {
        List<String[]> arrays = divideData(nThreads);

        executorService = Executors.newFixedThreadPool(nThreads);
        
        // thread para hacer Join
        // Runnable joinWorker = new WorkerJoinThread(nThreads - 1);
        // executorService.execute(joinWorker);

        // Agregar tareas al "thread pool"
        for (int i = 0; i < arrays.size(); i++) {
            Runnable worker = new WorkerThread(arrays.get(i));
            executorService.execute(worker);
        }
    }

    // divide el array grande en array de pequeños elementos
    private List<String[]> divideData(int n) {
        // divide el array en tantas posiciones diga 'n'
        List<String[]> resultArrays = new ArrayList<>();
        
        for (int i = 0; i < bigData.length; i += n) {
            int end = Math.min(bigData.length, i + n);
            String[] chunk = new String[end - i];
            System.arraycopy(bigData, i, chunk, 0, chunk.length);
            resultArrays.add(chunk);
        }

        return resultArrays;
    }
}