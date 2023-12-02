package master;

import java.util.concurrent.*;

import com.zeroc.Ice.*;
import com.zeroc.Ice.Exception;

import TextSorter.MasterInterface;
import TextSorter.WorkerInterfacePrx;

import threadPool.ThreadPool;

import java.io.*;
import java.util.*;

public class Master implements MasterInterface {
    private final int numThreads;  // Número de hilos a utilizar
    private final List<Thread> threads;  // Lista para almacenar los hilos
    private static List<WorkerInterfacePrx> workers;  // Lista para almacenar las referencias a los esclavos
    private static int nodes;
    // private ArrayList<String[]> sortedArrays;
    private int counter = 0;
    // private static int counterForSub = 0;
    // private static int size;
    private static ArrayList<String[]> subArrays;
    private static TextSorter.WorkerInterfacePrx workerInterfacePrx;
    private static List<String> sorted;
    private static ThreadPool threadPool;
    private static String fileName;

    public Master(int numThreads) {
        this.numThreads = numThreads;
        this.threads = new ArrayList<>();
        this.workers = new ArrayList<>();
        this.subArrays = new ArrayList<>();
        this.sorted = new ArrayList<>();
    }

    public static void main(String[] args)  {
        Scanner reader = new Scanner(System.in);

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "master.cfg")){            
            com.zeroc.Ice.ObjectPrx prx = communicator.stringToProxy("Master:default -p 10000");

            // communicator con propiedades de callback
            communicator.getProperties().setProperty("Ice.Default.Package",
                    "com.zeroc.demos.Ice.master");

            // Crear el adaptador y agregar el objeto maestro
            ObjectAdapter adapter = communicator.createObjectAdapterWithEndpoints("Master", "default -h localhost -p 10000");
            Master master = new Master(16);
            adapter.add(master, com.zeroc.Ice.Util.stringToIdentity("master"));
            // adapter.createProxy(prx.ice_getIdentity());
            adapter.activate();

            System.out.println(TextSorter.WorkerInterfacePrx.class);
            // twoway añadido, debe esperar respuesta
            // workerInterfacePrx = TextSorter.WorkerInterfacePrx.checkedCast(communicator.propertyToProxy("WorkerInterface.Proxy")).ice_twoway();
            workerInterfacePrx = TextSorter.WorkerInterfacePrx
            .uncheckedCast(communicator.propertyToProxy("WorkerInterface.Proxy")).ice_twoway();

            // Imprimir mensaje indicando que el servidor está listo
            System.out.println("Servidor maestro listo para recibir conexiones...");
            
            System.out.println("Selecciona la cantidad de nodos que deseas utilizar para ordenar: " +
                "\na -> 1 nodo"+ 
                "\nb -> 4 nodos" +
                "\nc -> 8 nodos" +
                "\nd -> 12 nodos");
            String opt = reader.next();

            switch (opt) {
                case "a":
                    nodes = 1;
                    break;
                
                case "b":
                    nodes = 4;
                    break;

                case "c":
                    nodes = 8;
                    break;
                
                case "d":
                    nodes = 12;
                    break;

                default:
                    System.out.println("Bad option");
                    nodes = 0;
                    break;
            }


            if (nodes != 0){
                Scanner scanner = new Scanner(System.in);
                System.out.print("Ingrese el nombre del archivo de datos: ");
                fileName = scanner.nextLine();
                String[] arr = readDataFromFile("doc/" + fileName);

                // metodo que llama a los demas para crear las tareas, ejecutar workers y ordenar
                doProcess(arr);

                // Esperar a que se cierre el servidor
                communicator.waitForShutdown();

                // Apagar el Communicator cuando se cierre el servidor
                communicator.destroy();
            }
        } catch (com.zeroc.Ice.ObjectNotExistException ex) {     
            ex.printStackTrace();
        }
    
    }

    public static void doProcess(String[] arrayToSort){
        createTasks(arrayToSort);
        launchWorkers();
    }

    @Override
    public void sort(Current current) {
        String[] array = new String[sorted.size()];

        threadPool = new ThreadPool(sorted.toArray(array));
    }

    public static void notifySorted(){
        writeDataToFile(fileName, threadPool.getSorted());
    }
 
    // creamos los subarrays para los workers
    public static void createTasks(String[] bigArray) {
        int size = (int) Math.ceil((double) bigArray.length / nodes);

        for (int i = 0; i < nodes; i++) {
            int start = i * size;
            int end = Math.min(start + size, bigArray.length);
            
            if (start > end) {
                break;
            }

            String[] subArr = Arrays.copyOfRange(bigArray, start, end);
            // añado subarrays para luego mandarselos a los workers
            subArrays.add(subArr);
        }
    }  

    // enviamos a cada worker su respectivo subarray para que lo ordene
    public static void launchWorkers() {
        for (String[] stringArr : subArrays) {
            workerInterfacePrx.sort(stringArr);   
        }
    }

    @Override
    public void attachWorker(WorkerInterfacePrx subscriber, Current current) {
        workers.add(subscriber);
        System.out.println("Esclavo adjuntado: " + subscriber.toString());
    }

    @Override
    public void addPartialResult(String[] res, Current current) {
        counter++;
        //counterForSub += size;

        // añadimos la respuesta de los workers a sortedArrays
        //sortedArrays.add(res);
        sorted.addAll(Arrays.asList(res));

        if (counter == nodes){
            sort(current);
        }
    }

    @Override
    public void detachWorker(WorkerInterfacePrx subscriber, Current current) {
        workers.remove(subscriber);
        System.out.println("Esclavo desvinculado: " + subscriber.toString());
    }

    @Override
    public String getTask(Current current) {
        return null;  // Reemplazar con la tarea real
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
