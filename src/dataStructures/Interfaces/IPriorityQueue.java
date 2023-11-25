package dataStructures.Interfaces;

public interface IPriorityQueue<K extends Comparable,V> {

    V heapExtractMin ();
    K getMin ();
    String decreasePriority(V value, K key);
    void insert (K key,V value);

    boolean isEmpty();
}