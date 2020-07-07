package com.company.todolist;

import com.company.todolist.datamodel.TodoData;
import com.company.todolist.datamodel.todoitem;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

import java.time.LocalDate;

public class DialogController {
    @FXML
    private TextArea name;
    @FXML
    private TextArea details;
    @FXML
    private DatePicker datedue;

    public todoitem process(){
        String name1 = name.getText().trim();
        String details1 = details.getText().trim();
        LocalDate deadline = datedue.getValue();
        todoitem temp = new todoitem(name1,details1,deadline);
        TodoData.getInstance().addTodoItem(temp);
        return temp;
    }

}
