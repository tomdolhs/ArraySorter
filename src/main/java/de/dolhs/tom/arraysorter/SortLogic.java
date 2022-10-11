package de.dolhs.tom.arraysorter;

import javafx.util.Pair;

import java.util.*;

public class SortLogic {

    private LinkedList<Integer> list = new LinkedList<>();

    private final LinkedList<Pair<LinkedList<Integer>, LinkedList<Integer>>> history = new LinkedList<>();

    private int max = 64;

    private final int min = 1;

    public void reset() {
        history.clear();
        list = new LinkedList<>();
        for (int i = min; i <= max; i++) {
            list.add(min + (int)(Math.random() * ((max - min) + 1)));
        }
    }

    public void bubbleSort() {
        int n = list.size();
        boolean swapped;
        do {
            swapped = false;
            for (int i = 0; i < n - 1; i++) {
                if (list.get(i) > list.get(i + 1)) {
                    swap(i, i + 1);
                    history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(i + 1))));
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
                history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(j))));
            }
            swap(i, smallestIndex);
            history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(i))));
        }
    }

    public void insertionSort() {
        for (int i = 1; i < list.size(); i++) {
            int value = list.get(i);
            int j;
            for (j = i; j > 0 && list.get(j - 1) > value; j--) {
                list.set(j, list.get(j - 1));
                history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(j))));
            }
            list.set(j, value);
            history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(j))));
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
            history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(current))));
            current++;
        }
        while (!left.isEmpty()) {
            list.set(current, left.removeFirst());
            history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(current))));
            current++;
        }
        while (!right.isEmpty()) {
            list.set(current, right.removeFirst());
            history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(List.of(current))));
            current++;
        }
    }

    public void quickSort() {
        quickSort(0, list.size() - 1);
    }

    public void quickSort(int from, int to) {
        if (from < to) {
            int mid = split(from, to);
            quickSort(from, mid - 1);
            quickSort(mid + 1, to);
        }
    }

    public int split(int from, int to) {
        int left = from;
        int right = to - 1;
        int pivot = list.get(to);
        while (left < right) {
            for (; left < right && list.get(left) <= pivot; left++) {
                history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(Arrays.asList(left, right))));
            }
            for (; right > left && list.get(right) > pivot; right--) {
                history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(Arrays.asList(left, right))));
            }
            if (list.get(left) > list.get(right)) {
                swap(left, right);
                history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(Arrays.asList(left, right))));
            }
        }
        if (list.get(left) > pivot) {
            swap(left, to);
            history.add(new Pair<>(new LinkedList<>(list), new LinkedList<>(Arrays.asList(left, to))));
        } else {
            left = to;
        }
        return left;
    }

    public void countingSort() {

    }

    private void swap(int a, int b) {
        int t = list.get(a);
        list.set(a, list.get(b));
        list.set(b, t);
    }

    public LinkedList<Integer> getList() {
        return list;
    }

    public LinkedList<Pair<LinkedList<Integer>, LinkedList<Integer>>> getHistory() {
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
