package com.company.todolist;

import com.company.todolist.datamodel.TodoData;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Arrays;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainwin.fxml"));
        primaryStage.setTitle("To do list");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
    }


    @Override
    public void stop() throws Exception {
        try{
            TodoData.getInstance().storedata();
        }catch (IOException e){
            System.out.println(Arrays.toString(e.getStackTrace()) + " " + e.getMessage());
        }

    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws IOException {
        try{
            TodoData.getInstance().loaddata();
        }catch (IOException e){
            System.out.println(e.getStackTrace());
        }
    }
}
