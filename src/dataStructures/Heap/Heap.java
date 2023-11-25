package dataStructures.Heap;

import dataStructures.Interfaces.IPriorityQueue;

import java.util.ArrayList;

public class Heap<K extends Comparable,V> implements IPriorityQueue<K,V> {

    private ArrayList<HeapNode<K, V>> list;

    private int heapSize;

    // Heap methods

    public void minHeapify(int from) {
        int left = getLeft(from);
        int right = getRigth(from);
        int smallest = from;

        if (left < heapSize) {
            if (list.get(left).getKey().compareTo(list.get(from).getKey()) < 0) smallest = left;
        }

        if (right < heapSize) {
            if (list.get(right).getKey().compareTo(list.get(smallest).getKey()) < 0) smallest = right;
        }

        if (smallest != from) {
            HeapNode temporal = list.get(from);
            list.set(from, list.get(smallest));
            list.set(smallest, temporal);
            minHeapify(smallest);
        }
    }

    public void buildHeap() {
        this.heapSize = list.size();
        for (int i = (list.size() / 2) - 1; i >= 0; i--) {
            minHeapify(i);
        }
    }

    /**
     * It´s worth to say when the heapsort is applied,  the list attribute is not a heap anymore, thus,
     * the heapSize is changed to 0.
     */
    public void heapSort() {
        buildHeap();
        for (int i = list.size() - 1; i >= 1; i--) {
            HeapNode temporal = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temporal);
            heapSize -= 1;
            minHeapify(0);
        }

    }

    public int getFather(int position) {
        position += 1;
        if (position == 0) return 0;
        else return position / 2 - 1;
    }

    public int getLeft(int position) {
        position += 1;
        return position * 2 - 1;
    }

    public int getRigth(int position) {
        position += 1;
        return position * 2;
    }

    //Priority Queue methods
    public HeapNode<K,V> searchByValue(V value){
        for (int i = 0; i < heapSize; i++) {
            if (list.get(i).getValue().equals(value)){
                return list.get(i);
            }
        }
        return null;
    }

    @Override
    public V heapExtractMin() {
        if (heapSize < 0) return null;
        V max = list.get(0).getValue();
        list.set(0, list.get(heapSize - 1));
        list.remove(heapSize - 1);
        heapSize--;
        minHeapify(0);
        return max;
    }

    /**
     * It returns the highest key as long as the buildHeap or heapSort methods have been applied.
     *
     * @return
     */
    @Override
    public K getMin() {
        return list.get(0).getKey();
    }

    @Override
    public boolean isEmpty() {
        return getList().isEmpty();
    }

    @Override
    public String decreasePriority(V value, K key) {
        Integer position = null ;

        for (int i = 0; i < getList().size(); i++) {
            if (getList().get(i).getValue().equals(value)) {
                position =i;
                break;
            }
        }

        if (key.compareTo(list.get(position).getKey()) > 0) {
            return "Not incrementing priority";
        }
        list.get(position).setKey(key);

        while (position > 0 && list.get(getFather(position)).getKey().compareTo(list.get(position).getKey()) > 0 ){

            HeapNode temporal = list.get(getFather(position));
            list.set(getFather(position), list.get(position));
            list.set(position, temporal);
            position = getFather(position);
        }
        return "Decrease done";
    }

    /**
     * This method is partially illegal, due to, it inserts a Node at last with a key not necessary lower
     * than the rest of the "tree", however, it's done in this way to continue with the generic implementation.
     * @param key
     * @param value
     */
    @Override
    public void insert(K key, V value) {
        heapSize ++;
        list.add(new HeapNode<>(key, value));
        int position = heapSize-1;

        while (position > 0 && list.get(getFather(position)).getKey().compareTo(list.get(position).getKey()) > 0 ){

            HeapNode temporal = list.get(getFather(position));
            list.set(getFather(position), list.get(position));
            list.set(position, temporal);
            position = getFather(position);
        }
    }


    //Initial methods
    //Constructor
    public Heap() {
        list = new ArrayList<>();
        heapSize = 0;
    }
    //Getters and Setters

    public ArrayList<HeapNode<K, V>> getList() {
        return list;
    }

    public void setList(ArrayList<HeapNode<K, V>> list) {
        this.list = list;
    }

    public int getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(int heapSize) {
        this.heapSize = heapSize;
    }
}
