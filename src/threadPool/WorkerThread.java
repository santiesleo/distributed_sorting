package threadPool;

import dataStructures.MergeSort.MergeSort;

public class WorkerThread implements Runnable {

    private String[] data;

    public WorkerThread(String[] data) {
        this.data = data;
    }

    @Override
    public void run() {
        MergeSort<String> ms = new MergeSort<>();

        // Simular una tarea que lleva algún tiempo
        try {
            // devolvemos lo que ordenó este Thread al pool para que se encargue de juntarlo
            ThreadPool.returnPool(ms.sort(data));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }
}