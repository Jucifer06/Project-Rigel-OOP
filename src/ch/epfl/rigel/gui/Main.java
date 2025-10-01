package ch.epfl.rigel.gui;

import ch.epfl.rigel.astronomy.AsterismLoader;
import ch.epfl.rigel.astronomy.HygDatabaseLoader;
import ch.epfl.rigel.astronomy.ObservedSky;
import ch.epfl.rigel.astronomy.StarCatalogue;
import ch.epfl.rigel.coordinates.CartesianCoordinates;
import ch.epfl.rigel.coordinates.GeographicCoordinates;
import ch.epfl.rigel.coordinates.HorizontalCoordinates;
import ch.epfl.rigel.coordinates.StereographicProjection;
import ch.epfl.rigel.math.ClosedInterval;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.converter.LocalTimeStringConverter;
import javafx.util.converter.NumberStringConverter;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.function.UnaryOperator;

/**
 * the main program
 *
 * @author Juliette Parchet (295888)
 * @author Paola Matta (296390)
 */
public class Main extends Application {


    //public static boolean asterismsAreOn = true;
    public static SimpleObjectProperty<Boolean> asterismsAreOn = new SimpleObjectProperty<>(Boolean.TRUE);

    private static final ClosedInterval CLOSED_INTERVAL_5_TO_90 = ClosedInterval.of(5, 90);
    private static final String STYLE_ALIGNMENT_BASELINE_LEFT = "-fx-spacing: inherit;-fx-alignment: baseline-left;";
    private static final double MIN_HEIGHT = 600;
    private static final double MIN_WIDTH = 800;
    private static final double RIGEL_IMAGE_SIZE = 18;
    private static final int RIGEL_INDEX_X = 2038;
    private static final int RIGEL_INDEX_Y = 2039;
    private static final String MUSIC_ON = "musicIsON";
    private static final String MUSIC_OFF = "musicIsOFF";
    private static final String CAMERA_SOUND = "cameraSound";

    private boolean musicIsOn = true;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Start the animation
     *
     * @param stage : the top level stage to animate the sky
     * @throws IOException : IOException in case of a stream error
     */
    @Override
    public void start(Stage stage) throws IOException {
        try (InputStream starsStream = resourceStream("/hygdata_v3.csv")) {
            try (InputStream asterismStream = resourceStream("/asterisms.txt")) {
                //catalogue
                StarCatalogue catalogue = new StarCatalogue.Builder()
                        .loadFrom(starsStream, HygDatabaseLoader.INSTANCE)
                        .loadFrom(asterismStream, AsterismLoader.INSTANCE)
                        .build();

                // zone date time bean initialization
                //ZonedDateTime when = ZonedDateTime.parse("2020-02-17T20:15:00+01:00");
                ZonedDateTime when = ZonedDateTime.now();

                DateTimeBean dateTimeBean = new DateTimeBean();
                dateTimeBean.setZonedDateTime(when);

                //observer location bean initialization
                ObserverLocationBean observerLocationBean = new ObserverLocationBean();
                observerLocationBean.setCoordinates(GeographicCoordinates.ofDeg(6.57, 46.52));

                // viewing parameters bean initialization
                ViewingParametersBean viewingParametersBean = new ViewingParametersBean();
                viewingParametersBean.setCenter(HorizontalCoordinates.ofDeg(180.000000000001, 42));
                viewingParametersBean.setFieldOfViewDeg(70);

                //sky canvas manager
                SkyCanvasManager canvasManager = new SkyCanvasManager(
                        catalogue,
                        dateTimeBean,
                        observerLocationBean,
                        viewingParametersBean);

                //setting the stage with the roots, the intermediate knots and the basic knots
                BorderPane pane = new BorderPane();
                pane.setTop(controlPanel(canvasManager, stage));
                pane.setCenter(skyView(canvasManager));
                pane.setBottom(informationLine(canvasManager));
                stage.setMinHeight(MIN_HEIGHT);
                stage.setMinWidth(MIN_WIDTH);
                stage.setScene(new Scene(pane));
                stage.setTitle("Rigel =)");
                stage.show();

                canvasManager.canvas().requestFocus();
            }
        }
    }

    /**
     * Construct and return the control panel
     *
     * @param canvasManager : the sky canvas manager
     * @return : the constructed control panel
     * @throws IOException : IOException in case of a stream error
     */
    private HBox controlPanel(SkyCanvasManager canvasManager, Stage stage) throws IOException {
        TimeAccelerator accelerator = NamedTimeAccelerator.TIMES_3000.getAccelerator();
        TimeAnimator timeAnimator = new TimeAnimator(canvasManager.getDateTimeBean());
        timeAnimator.setAccelerator(accelerator);

        HBox observationPosition = observationPosition(canvasManager);
        HBox observationTime = observationTime(canvasManager, timeAnimator);
        HBox timeFlow = timeFlow(canvasManager, timeAnimator);
        HBox extensions = extensions(canvasManager, stage);

        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        Separator separator2 = new Separator();
        separator2.setOrientation(Orientation.VERTICAL);
        HBox controlPanel = new HBox(observationPosition,separator, observationTime,separator2, timeFlow, extensions);
        controlPanel.setStyle("-fx-spacing: 4; -fx-padding: 4;");
        return controlPanel;
    }

    /**
     * Construct and return the sky view
     *
     * @param canvasManager : the sky canvas manager
     * @return : the constructed sky view
     */
    private Pane skyView(SkyCanvasManager canvasManager) {
        Canvas sky = canvasManager.canvas();
        Pane root = new BorderPane(sky);
        sky.widthProperty().bind(root.widthProperty());
        sky.heightProperty().bind(root.heightProperty());
        return root;
    }

    /**
     * Construct and return the information line
     *
     * @param canvasManager : the sky canvas manager
     * @return : the constructed information line
     */
    private BorderPane informationLine(SkyCanvasManager canvasManager) {
        //left Text
        ViewingParametersBean viewingParametersBean = canvasManager.getViewingParametersBean();
        Text leftText = new Text(setFieldOfViewText(viewingParametersBean));
        viewingParametersBean.fieldOfViewDegProperty().addListener(o ->
                leftText.setText(setFieldOfViewText(viewingParametersBean)));

        //center text
        Text centerText = new Text();
        centerText.textProperty().bind(Bindings.createStringBinding(() -> {
            StringBuilder celestialObjectInfo = new StringBuilder();
            if (canvasManager.objectUnderMouse.get() != null && canvasManager.objectUnderMouse.get().isPresent()) {
                celestialObjectInfo.append(canvasManager.objectUnderMouse.get().get().info());
            }
            return celestialObjectInfo.toString();
        }, canvasManager.objectUnderMouseProperty()));

        //right Text
        Text rightText = new Text(setMousePositionText(canvasManager));
        canvasManager.mousePositionProperty().addListener(o -> rightText.setText(setMousePositionText(canvasManager)));

        BorderPane informationLine = new BorderPane();
        informationLine.setStyle("-fx-padding: 4;-fx_background-color: white;");
        informationLine.setLeft(leftText);
        informationLine.setCenter(centerText);
        informationLine.setRight(rightText);
        return informationLine;
    }

    /**
     * Return the text of the mouse position
     *
     * @param canvasManager : the sky canvas manager
     * @return : the text of the mouse position
     */
    private String setMousePositionText(SkyCanvasManager canvasManager) {
        return String.format(
                Locale.ROOT,
                "Azimut : %.2f°, hauteur : %.2f°",
                canvasManager.mouseAzDeg.get(),
                canvasManager.mouseAltDeg.get());
    }

    /**
     * Return the text of the field of view
     *
     * @param viewingParametersBean : the viewing parameter bean
     * @return : the text of the field of view
     */
    private String setFieldOfViewText(ViewingParametersBean viewingParametersBean) {
        return String.format(Locale.ROOT, "Champ de vue : %.1f°", viewingParametersBean.getFieldOfViewDeg());
    }

    /**
     * Construct and return the observation position
     *
     * @param canvasManager : the sky canvas manager
     * @return : the constructed observation position
     */
    private HBox observationPosition(SkyCanvasManager canvasManager) {
        ObserverLocationBean observerLocationBean = canvasManager.getObserverLocationBean();
        String styleValue = "-fx-pref-width: 60;-fx-alignment: baseline-right;";
        //longitude
        Label longitude = new Label("Longitude (°) : ");
        TextField longitudeTextField = new TextField();
        longitudeTextField.setStyle(styleValue);
        TextFormatter<Number> lonTextFormatter = lonAndLatTextFormatter(true);
        longitudeTextField.setTextFormatter(lonTextFormatter);
        lonTextFormatter.valueProperty().bindBidirectional(observerLocationBean.lonDegProperty());

        //latitude
        Label latitude = new Label("Latitude (°) : ");
        TextField latitudeTextField = new TextField();
        latitudeTextField.setStyle(styleValue);
        TextFormatter<Number> latTextFormatter = lonAndLatTextFormatter(false);
        latitudeTextField.setTextFormatter(latTextFormatter);
        latTextFormatter.valueProperty().bindBidirectional(observerLocationBean.latDegProperty());

        HBox observationPosition = new HBox(longitude, longitudeTextField, latitude, latitudeTextField);
        observationPosition.setStyle(STYLE_ALIGNMENT_BASELINE_LEFT);
        return observationPosition;
    }

    /**
     * Construct and return the observation time
     *
     * @param canvasManager : the sky canvas manager
     * @param timeAnimator  : the time animator
     * @return : the constructed observation time
     */
    private HBox observationTime(SkyCanvasManager canvasManager, TimeAnimator timeAnimator) {
        //date
        Label date = new Label("Date : ");
        DatePicker datePicker = new DatePicker();
        datePicker.setStyle("-fx-pref-width: 120;");
        DateTimeBean dateTimeBean = canvasManager.getDateTimeBean();
        datePicker.valueProperty().bindBidirectional(dateTimeBean.dateProperty());
        datePicker.disableProperty().bind(timeAnimator.runningProperty());

        //hour
        Label hour = new Label("Heure : ");
        TextField hourTextField = new TextField();
        TextFormatter<LocalTime> hourTextFormatter = hourTextFormatter();
        hourTextFormatter.valueProperty().bindBidirectional(dateTimeBean.timeProperty());
        hourTextField.setTextFormatter(hourTextFormatter);
        hourTextField.setStyle("-fx-pref-width: 75;-fx-alignment: baseline-right;");
        hourTextField.disableProperty().bind(timeAnimator.runningProperty());

        //sorted list of the zones
        List<String> zoneIdList = new ArrayList<>(ZoneId.getAvailableZoneIds());
        Collections.sort(zoneIdList);
        List<ZoneId> sortedZoneIdList = new ArrayList<>();
        for (String zone : zoneIdList) sortedZoneIdList.add(ZoneId.of(zone));
        ObservableList<ZoneId> observableTimeZoneList = FXCollections.observableArrayList(sortedZoneIdList);
        //combo box of the zones
        ComboBox<ZoneId> timeZoneBox = new ComboBox<>();
        timeZoneBox.setStyle("-fx-pref-width: 180;");
        timeZoneBox.setItems(observableTimeZoneList);
        timeZoneBox.valueProperty().bindBidirectional(dateTimeBean.zoneProperty());
        timeZoneBox.disableProperty().bind(timeAnimator.runningProperty());

        HBox observationTime = new HBox(date, datePicker, hour, hourTextField, timeZoneBox);
        observationTime.setStyle(STYLE_ALIGNMENT_BASELINE_LEFT);
        return observationTime;
    }

    /**
     * Construct and return the time flow
     *
     * @param canvasManager : the sky canvas manager
     * @param timeAnimator  : the time animator
     * @return : the constructed time flow
     * @throws IOException : IOException in case of a stream error
     */
    private HBox timeFlow(SkyCanvasManager canvasManager, TimeAnimator timeAnimator) throws IOException {
        try (InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {
            //accelerator choice box
            ChoiceBox<NamedTimeAccelerator> acceleratorChoice = new ChoiceBox<>();
            acceleratorChoice.setItems(FXCollections.observableArrayList(List.of(NamedTimeAccelerator.values())));
            acceleratorChoice.setValue(NamedTimeAccelerator.TIMES_300);
            timeAnimator.acceleratorProperty().bind(
                    Bindings.select(acceleratorChoice.valueProperty(), "accelerator"));

            //buttons
            Font fontAwesome = Font.loadFont(fontStream, 15);
            String reset = "\uf0e2";
            Button resetButton = new Button(reset);
            resetButton.setFont(fontAwesome);
            resetButton.setOnAction(e -> canvasManager.getDateTimeBean().setZonedDateTime(ZonedDateTime.now()));
            String play = "\uf04b";
            String pause = "\uf04c";
            Button playPauseButton = new Button(play);
            playPauseButton.setFont(fontAwesome);
            playPauseButton.setOnAction(e -> {
                if (timeAnimator.getRunning()) {
                    playPauseButton.setText(play);
                    timeAnimator.stop();
                } else {
                    playPauseButton.setText(pause);
                    timeAnimator.start();
                }
            });

            HBox timeFlow = new HBox(acceleratorChoice, resetButton, playPauseButton);
            timeFlow.setStyle("-fx-spacing: inherit;");
            return timeFlow;
        }
    }

    /**
     * Construct and return the control panel extensions
     *
     * @param canvasManager : the sky canvas manager
     * @param stage         : the stage
     * @return : the constructed extensions of the control panel
     * @throws IOException : IOException in case of a stream error
     */
    private HBox extensions(SkyCanvasManager canvasManager, Stage stage) throws IOException {
        try (InputStream fontStream = resourceStream("/Font Awesome 5 Free-Solid-900.otf")) {
            Font fontAwesome = Font.loadFont(fontStream, 15);

            //minus button
            String minus = "\uf010";
            Button minusButton = new Button(minus);
            minusButton.setFont(fontAwesome);
            minusButton.setOnAction(event -> {
                double oldFieldOfView = canvasManager.getViewingParametersBean().getFieldOfViewDeg();
                double newFieldOfView = ((oldFieldOfView >= 145) ? 150D : (oldFieldOfView + 5D));
                canvasManager.getViewingParametersBean().setFieldOfViewDeg(newFieldOfView);
            });

            //plus button
            String plus = "\uf00e";
            Button plusButton = new Button(plus);
            plusButton.setFont(fontAwesome);
            plusButton.setOnAction(event -> {
                double oldFieldOfView = canvasManager.getViewingParametersBean().getFieldOfViewDeg();
                double newFieldOfView = ((oldFieldOfView <= 35D) ? 30D : (oldFieldOfView - 5D));
                canvasManager.getViewingParametersBean().setFieldOfViewDeg(newFieldOfView);
            });

            //Save a picture button
            String camera = "\uf030";
            Button cameraButton = new Button(camera);
            cameraButton.setFont(fontAwesome);
            cameraButton.setOnAction(e -> {
                playSound(CAMERA_SOUND);
                saveImage(canvasManager, stage);
            });

            //Go to Rigel button
            Image rigelImage = new Image("/rigel.jpg");
            ImageView rigelImageView = new ImageView(rigelImage);
            rigelImageView.setFitHeight(RIGEL_IMAGE_SIZE);
            rigelImageView.setFitWidth(RIGEL_IMAGE_SIZE);
            Button rigelButton = new Button("RIGEL", rigelImageView);
            rigelButton.setOnAction(e -> {
                ObservedSky sky = canvasManager.getObservedSky();
                CartesianCoordinates rigelCartesianCoordinates = CartesianCoordinates.of(
                        sky.starsPosition()[RIGEL_INDEX_X],
                        sky.starsPosition()[RIGEL_INDEX_Y]);
                StereographicProjection projection = canvasManager.getProjection();
                HorizontalCoordinates rigelHorizontalCoordinates = projection.inverseApply(rigelCartesianCoordinates);
                double rigelAz = rigelHorizontalCoordinates.azDeg();
                double rigelAlt = rigelHorizontalCoordinates.altDeg();
                double rigelClippedAlt = CLOSED_INTERVAL_5_TO_90.clip(rigelAlt);
                canvasManager.getViewingParametersBean()
                        .setCenter(HorizontalCoordinates.ofDeg(rigelAz, rigelClippedAlt));
                if (rigelClippedAlt != rigelAlt) rigelNotInScope();
            });

            //Music button
            String musicON = "\uf028";
            String musicOFF = "\uf026";
            Button musicButton = new Button(musicON);
            musicButton.setFont(fontAwesome);
            playSound(MUSIC_ON);
            musicButton.setOnAction(e -> {
                if (musicIsOn) {
                    musicIsOn = false;
                    musicButton.setText(musicOFF);
                    playSound(MUSIC_OFF);
                } else {
                    musicIsOn = true;
                    musicButton.setText(musicON);
                    playSound(MUSIC_ON);
                }
            });

            //ASTERISM BUTTON
            String asterismON = "ASTERISM ON";
            String asterismOFF = "ASTERISM OFF";
            Button asterismButton = new Button(asterismOFF);
            asterismButton.setOnAction(e -> {
                if (asterismsAreOn.get()) {
                    asterismsAreOn.set(false);
                    asterismButton.setText(asterismON);
                } else {
                    asterismsAreOn.set(true);
                    asterismButton.setText(asterismOFF);
                }
            });


            HBox extensions = new HBox(minusButton, plusButton, cameraButton, rigelButton, musicButton, asterismButton);
            extensions.setStyle("-fx-spacing: inherit;");
            return extensions;
        }
    }

    /**
     * Construct and return a text formatter for longitude (if isLon is true) and for latitude (if isLon is false)
     *
     * @param isLon : decide whether the text formatter is for longitude or latitude
     * @return : a text formatter for longitude (if isLon is true) and for latitude (if isLon is false)
     */
    private TextFormatter<Number> lonAndLatTextFormatter(boolean isLon) {
        NumberStringConverter stringConverter = new NumberStringConverter("#0.00");
        UnaryOperator<TextFormatter.Change> filter = (change -> {
            try {
                String newText = change.getControlNewText();
                double newDegValue = stringConverter.fromString(newText).doubleValue();
                if (isLon) return GeographicCoordinates.isValidLonDeg(newDegValue) ? change : null;
                else return GeographicCoordinates.isValidLatDeg(newDegValue) ? change : null;
            } catch (Exception e) {
                return null;
            }
        });
        return new TextFormatter<>(stringConverter, 0, filter);
    }

    /**
     * Construct and return a text formatter for the hour
     *
     * @return : a constructed formatter for the hour
     */
    private TextFormatter<LocalTime> hourTextFormatter() {
        DateTimeFormatter hmsFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalTimeStringConverter stringConverter = new LocalTimeStringConverter(hmsFormatter, hmsFormatter);
        return new TextFormatter<>(stringConverter);
    }


    /**
     * Manage the music and sounds of the program
     *
     * @param soundName : the sound that is managed
     */
    private void playSound(String soundName) {
        //Snap audio
        URL pathSnap;
        AudioClip snapAudioClip;
        //Music audio
        URL pathMusic = this.getClass().getResource("/music.mp3");
        AudioClip musicAudioClip = new AudioClip(pathMusic.toString());
        musicAudioClip.setCycleCount(AudioClip.INDEFINITE);
        musicAudioClip.setVolume(0.2);
        switch (soundName) {
            case CAMERA_SOUND:
                pathSnap = this.getClass().getResource("/SnapshotSound.wav");
                snapAudioClip = new AudioClip(pathSnap.toString());
                snapAudioClip.setVolume(5D);
                snapAudioClip.play();
                break;
            case MUSIC_ON:
                musicAudioClip.play();
                break;
            case MUSIC_OFF:
                musicAudioClip.stop();
                break;
        }
    }

    /**
     * Save a screenshot of the sky in the user files
     *
     * @param canvasManager : the sky canvas manager
     * @param stage         : the stage
     */
    private void saveImage(SkyCanvasManager canvasManager, Stage stage) {
        WritableImage fxImage = canvasManager.canvas().snapshot(null, null);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Sauvegarder l'image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("png", "*.png"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(fxImage, null), "png", file);
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    /**
     * Brings up a window to alert the user that rigel is not in the scope of view
     */
    private void rigelNotInScope() {
        final Stage popUpStage = new Stage();
        popUpStage.setTitle("Attention");
        popUpStage.initModality(Modality.WINDOW_MODAL);
        VBox popUpVBox = new VBox();
        Text text = new Text("Rigel ne se trouve pas dans le champ de vu (5° à 90° d'altitude)");
        popUpVBox.getChildren().add(text);
        Scene popUpScene = new Scene(popUpVBox);
        popUpStage.setScene(popUpScene);
        popUpStage.setResizable(false);
        popUpStage.show();
    }

    /**
     * fetch and load the file named nameOfResource
     *
     * @param nameOfResource : the name of the file
     * @return : the file named nameOfResource
     */
    private InputStream resourceStream(String nameOfResource) {
        return getClass().getResourceAsStream(nameOfResource);
    }


    public static Boolean getAsterismsAreOn() {
        return asterismsAreOn.get();
    }

    public static SimpleObjectProperty<Boolean> asterismsAreOnProperty() {
        return asterismsAreOn;
    }

    public static void setAsterismsAreOn(Boolean asterismsAreOn) {
        Main.asterismsAreOn.set(asterismsAreOn);
    }
}
