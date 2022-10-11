package de.dolhs.tom.arraysorter;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.sound.midi.*;

import java.io.IOException;
import java.util.LinkedList;

public class SortView extends Application {

    private final SortController controller = new SortController(this);

    private GraphicsContext gc;

    private Timeline timeline = new Timeline(new KeyFrame(Duration.millis(0), e -> draw()));

    private MidiChannel channel;

    private int width = 500;

    private int height = 500;

    @Override
    public void start(Stage stage) throws IOException {
        // Stage.
        stage.setTitle("Sorter");
        stage.setWidth(width + 15);
        stage.setHeight(height + 75);
        stage.setResizable(false);

        // Scene.
        Canvas canvas = new Canvas(width, height);
        canvas.setFocusTraversable(false);
        gc = canvas.getGraphicsContext2D();
        BorderPane borderPane = new BorderPane();
        borderPane.setTop(createToolbar());
        borderPane.setCenter(canvas);
        Scene scene = new Scene(borderPane);
        scene.setOnKeyPressed(createControls());
        stage.setScene(scene);
        stage.show();

        // Sound system.
        createSoundSystem();

        // Start.
        controller.reset();
        draw();
    }

    public void draw() {
        // Clear canvas.
        gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());

        // Reset sound.
        channel.allSoundOff();

        // Draw list.
        Pair<LinkedList<Integer>, Integer> next = controller.next();
        LinkedList<Integer> list = next.getKey();
        Integer current = next.getValue();
        for (int i = 0; i < list.size(); i++) {
            if (current != null && i == current) {
                gc.setFill(Color.rgb(255, 51, 51));
                int note = (int) (list.get(i) * ((double) 64 / controller.getSize()));
                channel.noteOn(note, 50);
            } else {
                gc.setFill(i % 2 == 0 ? Color.rgb(118, 186, 27) : Color.rgb(164, 222, 2));
            }
            gc.fillRect(i * (double) width / list.size(), gc.getCanvas().getHeight() - (double) list.get(i) / controller.getLogic().getMax() * gc.getCanvas().getHeight(), (double) width / list.size(), (double) list.get(i) / controller.getLogic().getMax() * (gc.getCanvas().getHeight()));
        }
    }

    private ToolBar createToolbar() {
        // Create toolbar and items.
        ToolBar toolBar = new ToolBar();
        ComboBox<String> algorithms = new ComboBox<>(FXCollections.observableArrayList("Bubble Sort", "Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"));
        Button play = new Button();
        Button reset = new Button();
        TextField speed = new TextField(String.valueOf(controller.getSpeed()));
        Button increaseSpeed = new Button();
        Button decreaseSpeed = new Button();
        TextField size = new TextField(String.valueOf(controller.getSize()));
        Button increaseSize = new Button();
        Button decreaseSize = new Button();

        // Set item appearance.
        int iconSize = 12;
        ImageView playIcon = new ImageView((getClass().getResource("/de/dolhs/tom/arraysorter/" + (controller.isSorting() ? "pause" : "play") + ".png").toExternalForm()));
        playIcon.setFitWidth(iconSize);
        playIcon.setFitHeight(iconSize);
        play.setGraphic(playIcon);
        play.setFocusTraversable(false);
        play.setTooltip(new Tooltip("Start sorting"));
        ImageView resetIcon = new ImageView((getClass().getResource("/de/dolhs/tom/arraysorter/exit.png").toExternalForm()));
        resetIcon.setFitWidth(iconSize);
        resetIcon.setFitHeight(iconSize);
        reset.setGraphic(resetIcon);
        reset.setFocusTraversable(false);
        reset.setTooltip(new Tooltip("Reset array"));
        speed.setPrefWidth(30);
        speed.setFocusTraversable(false);
        speed.setEditable(false);
        speed.setTooltip(new Tooltip("Sorting speed"));
        ImageView decreaseSpeedIcon = new ImageView((getClass().getResource("/de/dolhs/tom/arraysorter/minus.png").toExternalForm()));
        decreaseSpeedIcon.setFitWidth(iconSize);
        decreaseSpeedIcon.setFitHeight(iconSize);
        decreaseSpeed.setGraphic(decreaseSpeedIcon);
        decreaseSpeed.setFocusTraversable(false);
        decreaseSpeed.setTooltip(new Tooltip("Decrease Speed"));
        ImageView increaseSpeedIcon = new ImageView((getClass().getResource("/de/dolhs/tom/arraysorter/plus.png").toExternalForm()));
        increaseSpeedIcon.setFitWidth(iconSize);
        increaseSpeedIcon.setFitHeight(iconSize);
        increaseSpeed.setGraphic(increaseSpeedIcon);
        increaseSpeed.setFocusTraversable(false);
        increaseSpeed.setTooltip(new Tooltip("Increase Speed"));
        size.setPrefWidth(40);
        size.setFocusTraversable(false);
        size.setEditable(false);
        size.setTooltip(new Tooltip("Array size"));
        ImageView decreaseSizeIcon = new ImageView((getClass().getResource("/de/dolhs/tom/arraysorter/minus.png").toExternalForm()));
        decreaseSizeIcon.setFitWidth(iconSize);
        decreaseSizeIcon.setFitHeight(iconSize);
        decreaseSize.setGraphic(decreaseSizeIcon);
        decreaseSize.setFocusTraversable(false);
        decreaseSize.setTooltip(new Tooltip("Decrease array size"));
        ImageView increaseSizeIcon = new ImageView((getClass().getResource("/de/dolhs/tom/arraysorter/plus.png").toExternalForm()));
        increaseSizeIcon.setFitWidth(iconSize);
        increaseSizeIcon.setFitHeight(iconSize);
        increaseSize.setGraphic(increaseSizeIcon);
        increaseSize.setFocusTraversable(false);
        increaseSize.setTooltip(new Tooltip("Increase array size"));

        // Set item actions.
        algorithms.setOnAction(a -> {
            controller.setSort(algorithms.getSelectionModel().getSelectedItem());
            controller.reset();
        });
        algorithms.getSelectionModel().select(0);
        play.setOnAction(a -> controller.play());
        reset.setOnAction(a -> controller.reset());
        decreaseSpeed.setOnAction(a -> controller.decreaseSpeed());
        increaseSpeed.setOnAction(a -> controller.increaseSpeed());
        decreaseSize.setOnAction(a -> controller.decreaseSize());
        increaseSize.setOnAction(a -> controller.increaseSize());

        // Set change listeners.
        controller.sortingProperty().addListener((observable, oldValue, newValue) -> {
            ImageView icon = new ImageView((getClass().getResource("/de/dolhs/tom/arraysorter/" + (observable.getValue() ? "pause" : "play") + ".png").toExternalForm()));
            icon.setFitWidth(iconSize);
            icon.setFitHeight(iconSize);
            play.setGraphic(icon);
        });
        controller.speedProperty().addListener((observable, oldValue, newValue) -> speed.setText(String.valueOf(observable.getValue())));
        controller.sizeProperty().addListener((observable, oldValue, newValue) -> size.setText(String.valueOf(observable.getValue())));
        controller.sortingProperty().addListener((observable, oldValue, newValue) -> {
            decreaseSpeed.setDisable(observable.getValue());
            increaseSpeed.setDisable(observable.getValue());
            decreaseSize.setDisable(observable.getValue());
            increaseSize.setDisable(observable.getValue());
        });

        toolBar.getItems().addAll(algorithms, play, reset, new Separator(), decreaseSpeed, speed, increaseSpeed, new Separator(), decreaseSize, size, increaseSize);
        return toolBar;
    }

    private EventHandler<KeyEvent> createControls() {
        return keyEvent -> {

        };
    }

    private void createSoundSystem() {
        try {
            Synthesizer synthesizer = MidiSystem.getSynthesizer();
            synthesizer.open();
            Instrument instrument = synthesizer.getDefaultSoundbank().getInstruments()[66];
            channel = synthesizer.getChannels()[0];
            synthesizer.loadInstrument(instrument);
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        launch();
    }

    public Timeline getTimeline() {
        return timeline;
    }

    public void setTimeline(Timeline timeline) {
        this.timeline = timeline;
    }

    public MidiChannel getChannel() {
        return channel;
    }

    public void setChannel(MidiChannel channel) {
        this.channel = channel;
    }

}