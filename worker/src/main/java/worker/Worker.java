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
            communicator.getProperties().setProperty("Ice.Default.Package",
            "com.zeroc.demos.Ice.worker");
            
            // Crear el adaptador y agregar el objeto maestro
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Worker", "default -h localhost -p 10000");
            Worker master = new Worker();
            adapter.add(master, com.zeroc.Ice.Util.stringToIdentity("Worker"));
            adapter.createProxy(prx.ice_getIdentity());
            adapter.activate();
            
            // twoway añadido, debe esperar respuesta
            Worker.masterInterfacePrx = TextSorter.MasterInterfacePrx.checkedCast(communicator.propertyToProxy("Master.Proxy")).ice_twoway();

            startTime = System.currentTimeMillis();
        } catch (com.zeroc.Ice.ObjectNotExistException ex) {     
            ex.printStackTrace();
        }
    }

    // observer
    public static void notifyClient() {
        // Escribir el resultado ordenado en un nuevo archivo de texto
        // obtiene el array ordenado
        String[] sortedArr = threadPool.getSorted();

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
        Worker.threadPool = new ThreadPool(lines);
        
        try {
            threadPool.execute();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
