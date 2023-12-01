package mergeSortedArrays;

import java.util.*;

import elem.Elem;

public class MergeSortedArrays {
    public String[] mergeSortedArrays(List<String[]> lists) {
        PriorityQueue<Elem> pq = new PriorityQueue<>(new Comparator<Elem>() {
            @Override
            public int compare(Elem a, Elem b) {
                return a.getStr().compareTo(b.getStr());
            }
        });

        List<String> result = new ArrayList<>();

        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).length != 0) {
                pq.offer(new Elem(i, 0, lists.get(i)[0]));
            }
        }

        while (!pq.isEmpty()) {
            Elem currElem = pq.poll();
            result.add(currElem.getStr());

            if (currElem.getIdx() + 1 < lists.get(currElem.getArrayNum()).length) {
                pq.offer(new Elem(currElem.getArrayNum(), currElem.getIdx() + 1, lists.get(currElem.getArrayNum())[currElem.getIdx() + 1]));
            }
        }

        return result.toArray(new String[0]);
    }
}