package de.dolhs.tom.arraysorter;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.util.Duration;
import javafx.util.Pair;

import java.util.LinkedList;

public class SortController {

    private final SortView view;

    private final SortLogic logic = new SortLogic();

    private IntegerProperty speed = new SimpleIntegerProperty(2);

    private IntegerProperty size = new SimpleIntegerProperty(logic.getMax());

    private BooleanProperty sorting = new SimpleBooleanProperty(false);

    private String sort = "Bubble Sort";

    public SortController(SortView view) {
        this.view = view;
    }

    public SortLogic getLogic() {
        return logic;
    }

    public void play() {
        switch (view.getTimeline().getStatus()) {
            case RUNNING:
                setSorting(false);
                view.getTimeline().pause();
                view.getChannel().allSoundOff();
                break;
            case PAUSED:
                setSorting(true);
                view.setTimeline(new Timeline(new KeyFrame(Duration.millis(Math.max(1, 100 - speed.get() * 10)), e -> view.draw())));
                view.getTimeline().setCycleCount(Animation.INDEFINITE);
                view.getTimeline().play();
                break;
            default:
                logic.reset();
                switch (sort) {
                    case "Bubble Sort":
                        logic.bubbleSort();
                        break;
                    case "Selection Sort":
                        logic.selectionSort();
                        break;
                    case "Insertion Sort":
                        logic.insertionSort();
                        break;
                    case "Merge Sort":
                        logic.mergeSort();
                        break;
                    case "Quick Sort":
                        logic.quickSort();
                        break;
                }
                setSorting(true);
                view.setTimeline(new Timeline(new KeyFrame(Duration.millis(Math.max(1, 100 - speed.get() * 10)), e -> view.draw())));
                view.getTimeline().setCycleCount(Animation.INDEFINITE);
                view.getTimeline().play();
                break;
        }
    }

    public Pair<LinkedList<Integer>, LinkedList<Integer>> next() {
        Pair<LinkedList<Integer>, LinkedList<Integer>> next;
        if (!isSorting()) {
            next = new Pair<>(logic.getList(), null);
        } else {
            next = logic.getHistory().removeFirst();
            if (logic.getHistory().isEmpty()) {
                setSorting(false);
                view.getTimeline().stop();
                view.getChannel().allNotesOff();
            }
        }
        return next;
    }

    public void reset() {
        setSorting(false);
        view.getTimeline().stop();
        view.getChannel().allNotesOff();
        logic.reset();
        view.draw();
    }

    public int getSpeed() {
        return speed.get();
    }

    public void setSpeed(int speed) {
        this.speed.set(speed);
    }

    public IntegerProperty speedProperty() {
        return speed;
    }

    public void increaseSpeed() {
        setSpeed(Math.min(10, getSpeed() + 1));
    }

    public void decreaseSpeed() {
        setSpeed(Math.max(1, getSpeed() - 1));
    }

    public BooleanProperty sortingProperty() {
        return sorting;
    }

    public boolean isSorting() {
        return sorting.get();
    }

    public void setSorting(boolean sorting) {
        this.sorting.set(sorting);
    }

    public void setSort(String algorithm) {
        sort = algorithm;
    }

    public int getSize() {
        return size.get();
    }

    public void setSize(int size) {
        this.size.set(size);
    }

    public void increaseSize() {
        setSize(Math.min(256, getSize() * 2));
        logic.setMax(getSize());
        reset();
    }

    public void decreaseSize() {
        setSize(Math.max(8, getSize() / 2));
        logic.setMax(getSize());
        reset();
    }

    public IntegerProperty sizeProperty() {
        return size;
    }

}
