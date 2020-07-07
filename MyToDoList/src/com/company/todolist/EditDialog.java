package com.company.todolist;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;

public class EditDialog  {
    @FXML
    public TextArea details;
    @FXML
    public TextArea name;
    @FXML
    public DatePicker datedue;


    public void initialize(){
        System.out.println("runs");


    }
    public String getDetails() {
        return details.getText().trim();
    }

    public  void setDetails(TextArea details) {
        this.details = details;
    }

    public String getName() {
        return name.getText().trim();
    }

    public void setName(TextArea name) {
        this.name = name;
    }

    public LocalDate getDatedue() {
        return datedue.getValue();
    }

    public void setDatedue(DatePicker datedue) {
        this.datedue = datedue;
    }
    public void setall(String name, String details, LocalDate datedue){
        this.name.setText(name);
        this.details.setText(details);
        this.datedue.setValue(datedue);
    }
}
