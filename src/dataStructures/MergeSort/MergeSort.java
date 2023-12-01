/*
package dataStructures.MergeSort;

import java.util.Arrays;

public class MergeSort<T extends Comparable<T>> {

    public T[] sort(T[] array) {
        if (array == null || array.length <= 1) {
            return array;
        }
        mergeSort(array, 0, array.length - 1);
        return array;
    }

    private void mergeSort(T[] array, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;

            mergeSort(array, left, mid);
            mergeSort(array, mid + 1, right);

            merge(array, left, mid, right);
        }
    }

    private void merge(T[] array, int left, int mid, int right) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        // Crear arreglos temporales
        Object[] leftArray = new Object[n1];
        Object[] rightArray = new Object[n2];

        // Copiar datos a los arreglos temporales
        System.arraycopy(array, left, leftArray, 0, n1);
        System.arraycopy(array, mid + 1, rightArray, 0, n2);

        // Índices iniciales de los subarreglos
        int i = 0, j = 0;

        // Índice inicial del subarreglo fusionado
        int k = left;

        // Fusionar los subarreglos
        while (i < n1 && j < n2) {
            System.out.println("\nleft: " + Arrays.toString(leftArray));
            System.out.println("right: " + Arrays.toString(rightArray));
            if (((T) leftArray[i]).compareTo((T) rightArray[j]) >= 0) {
                array[k] = (T) leftArray[i];
                i++;
            } else {
                array[k] = (T) rightArray[j];
                j++;
            }
            k++;
        }

        // Copiar los elementos restantes de leftArray (si los hay)
        while (i < n1) {
            array[k] = (T) leftArray[i];
            i++;
            k++;
        }

        // Copiar los elementos restantes de rightArray (si los hay)
        while (j < n2) {
            array[k] = (T) rightArray[j];
            j++;
            k++;
        }
    }
}*/