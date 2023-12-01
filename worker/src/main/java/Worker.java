package main.java;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.util.*;
import java.util.concurrent.*;
import main.java.threadPool.*;
import com.zeroc.Ice.*;


public class Worker {
    private static String fileName;
    private static ThreadPool threadPool;
    private static Long startTime;

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try (com.zeroc.Ice.Communicator communicator = com.zeroc.Ice.Util.initialize(args, "worker.cfg")) {
            com.zeroc.Ice.ObjectPrx prx = communicator.stringToProxy("Worker:default -p 10000");
            //WorkerPrx worker = WorkerPrx.checkedCast(base);

            // callback prx (sender)
            CallbackSenderPrx callbackSenderPrx = CallbackSenderPrx.checkedCast(
                    communicator.propertyToProxy("CallbackSender.Proxy")).ice_twoway();

            // chat manager para enviar msj
            // Demo.PrinterPrx chatManagerPrx = Demo.PrinterPrx
            //        .checkedCast(communicator.propertyToProxy("Printer.Proxy")).ice_twoway();

            com.zeroc.Ice.ObjectAdapter adapter = communicator.createObjectAdapter("Callback.Client");
            adapter.add(new CallbackReceiverI(), com.zeroc.Ice.Util.stringToIdentity("callbackReceiver"));
            adapter.activate();

            // callback prx (receiver)
            //CallbackReceiverPrx receiver =
            //CallbackReceiverPrx.uncheckedCast(adapter.createProxy(com.zeroc.Ice.Util.stringToIdentity("callbackReceiver")));

            try {
                String hostname = Inet4Address.getLocalHost().getHostName();
                String msg = "";
                String username = System.getProperty("user.name");

                // inicia el callback
                //callbackSenderPrx.initiateCallback(hostname, receiver);

                while (!msg.equalsIgnoreCase("exit")) {
                    System.out.println("Escribe tu mensaje... (escribe 'shutdown' para apagar el servidor)");
                    msg = reader.readLine();

                    Long start = System.currentTimeMillis();
                    sent_request += 1;

                    // enviar string al server
                    callbackSenderPrx.message(receiver, username+":"+hostname+" ->"+msg);

                    if(msg.equalsIgnoreCase("exit") || msg.equalsIgnoreCase("shutdown")){
                        break;
                    }

                    String latency_response = "\nLatency (response | less the 1000ms of testing throughput): " + (System.currentTimeMillis() - start - 1000) + "ms";
                    String requests = "\nSent requests (by this client): " + sent_request;
                    System.out.println(latency_response + requests + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el nombre del archivo de datos: ");
        fileName = scanner.nextLine();
        String[] arr = readDataFromFile("doc/" + fileName);

        startTime = System.currentTimeMillis();
        Worker.threadPool = new ThreadPool(arr);
        threadPool.execute();
    }

    // observer
    public static void notifyClient() {
        // Escribir el resultado ordenado en un nuevo archivo de texto
        // obtiene el array ordenado
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
