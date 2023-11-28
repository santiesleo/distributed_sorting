import dataStructures.MergeSort.MergeSort;

public class Main {
    public static void main(String[] args) {
        // Ejemplo de uso con enteros
        String[] array = {"12", "6", "12", "5", "13", "7", "11"};
        MergeSort<String> mergeSort = new MergeSort<>();
        mergeSort.sort(array);

        System.out.println("Arreglo ordenado:");
        for (String element : array) {
            System.out.print(element + " ");
        }
    }

}
