package de.dolhs.tom.arraysorter;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

public class SortLogic {

    private LinkedList<Integer> list = new LinkedList<>();

    private final LinkedList<Pair<LinkedList<Integer>, Integer>> history = new LinkedList<>();

    private int max = 64;

    private final int min = 1;

    public void reset() {
        history.clear();
        list = new LinkedList<>();
        for (int i = min; i <= max; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
    }

    public void bubbleSort() {
        int n = list.size();
        boolean swapped = false;
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (list.get(i) > list.get(i + 1)) {
                    swap(i, i + 1);
                    history.add(new Pair<>(new LinkedList<>(list), i + 1));
                    swapped = true;
                }
            }
            n--;
        } while (swapped);
    }

    public void selectionSort() {
        for (int i = 0; i < list.size() - 1; i++) {
            int smallestIndex = i;
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(smallestIndex) > list.get(j)) {
                    smallestIndex = j;
                }
                history.add(new Pair<>(new LinkedList<>(list), j));
            }
            swap(i, smallestIndex);
            history.add(new Pair<>(new LinkedList<>(list), i));
        }
    }

    public void mergeSort() {
        mergeSort(0, list.size());
    }

    private void mergeSort(int from, int to) {
        if (to - from > 1) {
            int mid = from + (to - from) / 2;
            mergeSort(from, mid);
            mergeSort(mid, to);
            merge(from, mid, to);
        }
    }

    private void merge(int from, int mid, int to) {
        LinkedList<Integer> left = new LinkedList<>(list.subList(from, mid));
        LinkedList<Integer> right = new LinkedList<>(list.subList(mid, to));
        int current = from;
        while (!left.isEmpty() && !right.isEmpty()) {
            if (left.getFirst() <= right.getFirst()) {
                list.set(current, left.removeFirst());
            } else {
                list.set(current, right.removeFirst());
            }
            history.add(new Pair<>(new LinkedList<>(list), current));
            current++;
        }
        while (!left.isEmpty()) {
            list.set(current, left.removeFirst());
            history.add(new Pair<>(new LinkedList<>(list), current));
            current++;
        }
        while (!right.isEmpty()) {
            list.set(current, right.removeFirst());
            history.add(new Pair<>(new LinkedList<>(list), current));
            current++;
        }
    }

    private void swap(int a, int b) {
        int t = list.get(a);
        list.set(a, list.get(b));
        list.set(b, t);
    }

    public LinkedList<Integer> getList() {
        return list;
    }

    public LinkedList<Pair<LinkedList<Integer>, Integer>> getHistory() {
        return history;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

}
