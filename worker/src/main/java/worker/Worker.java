package worker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.util.*;
import java.util.concurrent.*;

import TextSorter.WorkerInterfacePrx;
import com.zeroc.Ice.*;

import TextSorter.WorkerInterface;
import threadPool.ThreadPool;

public class Worker implements WorkerInterface {

    private static String fileName;
    private static ThreadPool threadPool;
    private static Long startTime;
    private static TextSorter.MasterInterfacePrx masterInterfacePrx;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "worker.cfg")) {

            com.zeroc.Ice.ObjectPrx prx = communicator.stringToProxy("Worker:default -p 10000");

            // communicator con propiedades de callback
            communicator.getProperties().setProperty("Ice.Default.Package", "com.zeroc.demos.Ice.worker");

            // PRX del master
            masterInterfacePrx = TextSorter.MasterInterfacePrx.uncheckedCast(communicator.propertyToProxy("MasterInterface.Proxy")).ice_twoway();

            // Crear el adaptador y agregar el objeto maestro
            ObjectAdapter adapter = communicator.createObjectAdapter("Worker");
            Worker worker = new Worker();

            adapter.add(worker, com.zeroc.Ice.Util.stringToIdentity("Worker"));
            adapter.activate();

            // PRX de este worker
            WorkerInterfacePrx workerInterfacePrx =
                    WorkerInterfacePrx.uncheckedCast(adapter.createProxy(
                            com.zeroc.Ice.Util.stringToIdentity("Worker")));

            startTime = System.currentTimeMillis();

            // se suscribe
            masterInterfacePrx.attachWorker(workerInterfacePrx);

            while (true) {
                /*
                try {
                    // Verifica si el proxy implementa la interfaz del Master
                    if (masterInterfacePrx.ice_isA("::TextSorter::MasterInterface")) {
                        // El proxy implementa la interfaz del Master, por lo que puedes llamar a sus métodos
                        // Ejecuta las operaciones del Worker según sea necesario
                        // Llama a los métodos del Master según sea necesario
                    } else {
                        // El proxy no implementa la interfaz del Master, maneja esto según tus necesidades
                    }
                } catch (com.zeroc.Ice.CommunicatorDestroyedException ex) {
                    // La excepción se lanza cuando el comunicador se destruye, puedes manejar esto según tus necesidades
                    break; // Sale del bucle cuando el comunicador se destruye
                } catch (com.zeroc.Ice.LocalException ex) {
                    // Maneja otras excepciones según sea necesario
                    ex.printStackTrace();
                }*/

                Thread.sleep(1000); // Espera 1 segundo antes de volver a verificar
            }
        } catch (com.zeroc.Ice.ObjectNotExistException ex) {     
            ex.printStackTrace();
        }
    }

    // observer
    public static void notifyClient() {
        // Escribir el resultado ordenado en un nuevo archivo de texto
        // obtiene el array ordenado
        String[] sortedArr = threadPool.getSorted();

        System.out.println("Sorted!");
        masterInterfacePrx.addPartialResult(sortedArr);

        //String outputFilePath = "doc/sorted_" + fileName;
        // writeDataToFile(outputFilePath, sortedArr);

        //long endTime = System.currentTimeMillis();
        //long latency = endTime - startTime;

        //System.out.println("Latencia de dist_sorter: " + latency + " milisegundos");
   }

    @Override
    public void processTask(String task, Current current) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'processTask'");
    }

    @Override
    public void subscribe(Current current) {
        
    }

    @Override
    public void sort(String[] lines, Current current) {
        System.out.println("Sorting...");
        Worker.threadPool = new ThreadPool(lines);
        
        try {
            threadPool.execute();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
