import java.util.List;

import com.zeroc.Ice.*;
import TextSorter.MasterInterface;
import TextSorter.WorkerInterfacePrx;
import java.util.ArrayList;
import java.util.Arrays;
import com.zeroc.Ice.Exception;

public class Master implements MasterInterface {

    private final int numThreads;  // Número de hilos a utilizar
    private final List<Thread> threads;  // Lista para almacenar los hilos
    private final List<WorkerInterfacePrx> workers;  // Lista para almacenar las referencias a los esclavos

    public Master(int numThreads) {
        this.numThreads = numThreads;
        this.threads = new ArrayList<>();
        this.workers = new ArrayList<>();
    }

    public static void main(String[] args)  {
        try {
            // Inicializar el Communicator
            Communicator communicator = Util.initialize(args);

            // Crear el adaptador y agregar el objeto maestro
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Master", "default -h localhost -p 10000");
            Master master = new Master(16);
            adapter.add(master, new Identity("master", "master"));
            adapter.activate();

            // Imprimir mensaje indicando que el servidor está listo
            System.out.println("Servidor maestro listo para recibir conexiones...");

            // Esperar a que se cierre el servidor
            communicator.waitForShutdown();

            // Apagar el Communicator cuando se cierre el servidor
            communicator.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

     @Override
    public String[] sort(String[][] partitions, Current current) {
        int partitionSize = partitions.length / numThreads;

        // Crear hilos y asignarles tareas
        for (int i = 0; i < numThreads; i++) {
            int start = i * partitionSize;
            int end = (i == numThreads - 1) ? partitions.length : (i + 1) * partitionSize;

            String[][] task = Arrays.copyOfRange(partitions, start, end);
            Thread thread = new Thread(() -> {
                // Lógica del esclavo para procesar la tarea
                processTask(task);
            });

            threads.add(thread);
            thread.start();
        }

        // Esperar a que todos los hilos terminen
        joinThreads();

        // Combinar resultados de los esclavos si es necesario
        // ...

        return null;  // Reemplazar con el resultado real
    }

    @Override
    public void attachWorker(WorkerInterfacePrx subscriber, Current current) {
        workers.add(subscriber);
        System.out.println("Esclavo adjuntado: " + subscriber.toString());
    }

    @Override
    public void addPartialResult(List<String> res, Current current) {
        // Lógica para combinar resultados parciales si es necesario
        // ...
    }

    @Override
    public void detachWorker(WorkerInterfacePrx subscriber, Current current) {
        workers.remove(subscriber);
        System.out.println("Esclavo desvinculado: " + subscriber.toString());
    }

    @Override
    public String getTask(Current current) {
        // Lógica para proporcionar una tarea a los esclavos
        // ...

        return null;  // Reemplazar con la tarea real
    }

    private void processTask(String[][] task) {
        // Lógica del esclavo para procesar la tarea
        // ...

        System.out.println("Esclavo procesando tarea: " + Arrays.deepToString(task));
    }

    private void joinThreads() {
        // Esperar a que todos los hilos terminen
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
