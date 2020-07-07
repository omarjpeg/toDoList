package com.company.todolist;

import com.company.todolist.datamodel.TodoData;
import com.company.todolist.datamodel.todoitem;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

public class SelectionDialogController {

    @FXML
    public ListView<todoitem> selectionlistviewq;

    public void initialize(){
        selectionlistviewq.setItems(TodoData.getInstance().getTodoitemList());
        selectionlistviewq.getSelectionModel().selectFirst();
        selectionlistviewq.setCellFactory(new Callback<ListView<todoitem>, ListCell<todoitem>>() {
            @Override
            public ListCell<todoitem> call(ListView<todoitem> todoitemListView) {
               ListCell<todoitem> cell = new ListCell<>(){
                   @Override
                   protected void updateItem(todoitem todoitem, boolean b) {
                       super.updateItem(todoitem, b);
                       if(b){
                           setText(null);
                       }else{
                           setText(todoitem.getName());
                       }
                   }
               };
               return cell;
            }
        });


    }
    public  ListView<todoitem> getlist(){
        return selectionlistviewq;
    }
















}
