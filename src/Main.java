import dataStructures.Heap.Heap;
import dataStructures.Heap.HeapNode;

public class Main {
    public static void main(String[] args) {
        // Crear una instancia de Heap
        Heap<Integer, String> heap = new Heap<>();

        // Insertar elementos en el heap
        heap.insert(5, "Five");
        heap.insert(3, "Three");
        heap.insert(8, "Eight");
        heap.insert(1, "One");
        heap.insert(7, "Seven");

        // Imprimir el heap antes de ordenar
        System.out.println("Heap antes del ordenamiento:");
        printHeap(heap);

        // Aplicar el método de ordenamiento
        heap.heapSort();

        // Imprimir el heap después de ordenar
        System.out.println("\nHeap después del ordenamiento:");
        printHeap(heap);
    }

    private static void printHeap(Heap<Integer, String> heap) {
        for (int i = heap.getList().size() - 1; i >= 0; i--) {
            HeapNode<Integer, String> node = heap.getList().get(i);
            System.out.println("Key: " + node.getKey() + ", Value: " + node.getValue());
        }
    }

}
