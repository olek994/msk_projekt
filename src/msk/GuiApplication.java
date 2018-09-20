package msk;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import msk.federate.GuiFederate;

import java.net.URL;

/**
 * Created by Aleksander Małkowicz, Date: 13.09.2018
 * Copyright by RelayOnIT, Warszawa 2018
 */
public class GuiApplication extends Application {


    GuiFederate guiFederate;

    @FXML public MenuItem startButton;
    @FXML public MenuItem endButton;
    @FXML public Label simTime;
    @FXML public Label liczbaPasazerowPierwszej;
    @FXML public Label liczbaSamochodowPierwszej;
    @FXML public Label liczbaPasazerowDrugiej;
    @FXML public Label liczbaSamochodowDrugiej;
    @FXML public Label liczbaPasazerowTrzeciej;
    @FXML public Label liczbaSamochodowTrzeciej;
    @FXML public Label liczbaPasazerowCzwartej;
    @FXML public Label liczbaSamochodowCzwartej;
    @FXML public Label liczbaPasazerowPiatej;
    @FXML public Label liczbaSamochodowPiatej;
    @FXML public Label liczbaPasazerowSzostej;
    @FXML public Label liczbaSamochodowSzostej;

    @FXML public ImageView imagePierwszaStacja;
    @FXML public ImageView imageDrugaStacja;
    @FXML public ImageView imageTrzeciaStacja;
    @FXML public ImageView imageCzwartaStacja;
    @FXML public ImageView imagePiataStacja;
    @FXML public ImageView imageSzostaStacja;

    @FXML public Label liczbaWolnychMiejscNaPromie;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();

        fxmlLoader.setLocation(getClass().getClassLoader().getResource("gui.fxml"));

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root,1300,500);

        primaryStage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        primaryStage.setTitle("PrimaryStage");
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main (String[] args){
        Application.launch(args);
    }

    @FXML
    public void startSimBtn(ActionEvent actionEvent) {
        this.startButton.setDisable(true);
        this.endButton.setDisable(false);
        this.guiFederate = new GuiFederate(this);

        new Thread(() -> {
            try {
                this.guiFederate.runFederate("GUI");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    public void stopSimBtn(ActionEvent actionEvent) {
        this.startButton.setDisable(false);
        this.endButton.setDisable(true);
        this.guiFederate.stopSimulation();
        this.guiFederate = null;
    }
}
