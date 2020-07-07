package com.company.todolist;

import com.company.todolist.datamodel.TodoData;
import com.company.todolist.datamodel.todoitem;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {
    @FXML
    private BorderPane mainborderpane;
    @FXML
    private Label datelabel;
    @FXML
    private ListView<todoitem> todolistview;
    @FXML
    private TextArea vboxtext;
    @FXML
    private ContextMenu listmenu;
    @FXML
    private ToggleButton mytoggle;

    private FilteredList<todoitem> filteredList;

    private SortedList<todoitem> sortedList;

    public void initialize() {
        listmenu  = new ContextMenu();
        MenuItem deleter = new MenuItem("Delete");
        deleter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                todoitem currentlyselected = todolistview.getSelectionModel().getSelectedItem();
                deleteItem(currentlyselected);
            }
        });
        listmenu.getItems().addAll(deleter);

        todolistview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<todoitem>() {
            @Override
            public void changed(ObservableValue<? extends todoitem> observableValue, todoitem todoitem, todoitem t1) {
                if (t1 != null) {
                    todolistview.getSelectionModel().select(t1);
                    vboxtext.setText(t1.getDetails());
                    DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM - dd - yyyy");
                    datelabel.setText(df.format(t1.getDeadline()));
                }
            }
        });
        filteredList = new FilteredList<>(TodoData.getInstance().getTodoitemList(), new Predicate<todoitem>() {
            @Override
            public boolean test(todoitem todoitem) {
                if(todoitem.getDeadline().compareTo(LocalDate.now()) == 0){
                    return true;
                }else{
                    return false;
                }
            }
        });
          sortedList = new SortedList<todoitem>(TodoData.getInstance().getTodoitemList(), new Comparator<todoitem>() {
             @Override
             public int compare(todoitem o1, todoitem o2) {
                 return o1.getDeadline().compareTo(o2.getDeadline());
             }
         });
        todolistview.setItems(sortedList);
        todolistview.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        todolistview.getSelectionModel().selectFirst();

        todolistview.setCellFactory(new Callback<ListView<todoitem>, ListCell<todoitem>>() {
            @Override
            public ListCell<todoitem> call(ListView<todoitem> todoitemListView) {
                ListCell<todoitem> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(todoitem todoitem, boolean b) {
                        super.updateItem(todoitem, b);
                        if (b) {
                            setText(null);
                        } else {
                            setText(todoitem.getName());
                            if (todoitem.getDeadline().isBefore(LocalDate.now())) {
                                setTextFill(Color.BROWN);
                            }
                            if (todoitem.getDeadline().isEqual(LocalDate.now())) {
                                setTextFill(Color.RED);
                            }
                            Calendar calendar = Calendar.getInstance();
                            calendar.add(Calendar.DAY_OF_YEAR, 1);
                            Date tomorrow = calendar.getTime();
                            LocalDate date = tomorrow.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

                            if (todoitem.getDeadline().equals(date)) {
                                setTextFill(Color.GREENYELLOW);
                            }
                        }
                    }
                };
                cell.emptyProperty().addListener(
                        (ons, wasEmpty, isNowEmpty) -> {
                            if(isNowEmpty){
                                cell.setContextMenu(null);
                            }else{
                                cell.setContextMenu(listmenu);
                            }
                        }
                );
                return cell;
            }

        });

    }

    @FXML
    public void clicklistview() {
        todoitem item = todolistview.getSelectionModel().getSelectedItem();
        vboxtext.setText(item.getDetails());
        datelabel.setText(item.getDeadline().toString());

    }

    @FXML
    public void showEditDialog() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainborderpane.getScene().getWindow());
        FXMLLoader myloader = new FXMLLoader();
        SelectionDialogController mycontroller = new SelectionDialogController();
        myloader.setController(mycontroller);
        myloader.setLocation(getClass().getResource("SelectionDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(myloader.load());
        } catch (IOException e) {
            System.out.println("couldnt load dialog");
            e.printStackTrace();
            return;
        }

        dialog.setTitle("Edit an item");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> hmm = dialog.showAndWait();
        if (hmm.isPresent() && hmm.get().equals(ButtonType.OK)) {
            Dialog<ButtonType> dialog2 = new Dialog<>();
            FXMLLoader myloader2 = new FXMLLoader();
            EditDialog edit = new EditDialog();
            myloader2.setController(edit);
            myloader2.setLocation(getClass().getResource("editDialog.fxml"));
            try {
                dialog2.getDialogPane().setContent(myloader2.load());
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }

            edit.setall(mycontroller.selectionlistviewq.getSelectionModel().getSelectedItem().getName(),mycontroller.selectionlistviewq.getSelectionModel().getSelectedItem().getDetails(),mycontroller.selectionlistviewq.getSelectionModel().getSelectedItem().getDeadline());
            dialog2.setTitle("Do your edit");
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog2.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            dialog2.showAndWait();
            Optional<ButtonType> saveorcancel = dialog.showAndWait();
            if (saveorcancel.isPresent() && saveorcancel.get().equals((ButtonType.OK))) {

                todoitem current = new todoitem(edit.getName(), edit.getDetails(),edit.datedue.getValue());
                TodoData.getInstance().addTodoItem(current);
                TodoData.getInstance().removetodoitem(mycontroller.selectionlistviewq.getSelectionModel().getSelectedItem());

            }

        }

    }

    @FXML
    public void shownewdialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainborderpane.getScene().getWindow());
        dialog.setTitle("Add new item..");
        dialog.setHeaderText("This is the header text !");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("TodoitemDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("new");
            System.out.println("couldnt load dialog");
            e.printStackTrace();
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            todoitem itemtoselect = controller.process();
            todolistview.getSelectionModel().select(itemtoselect);

        }

    }

    public void showDeletdialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainborderpane.getScene().getWindow());
        FXMLLoader myloader = new FXMLLoader();
        SelectionDialogController mycontroller = new SelectionDialogController();
        myloader.setController(mycontroller);
        myloader.setLocation(getClass().getResource("SelectionDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(myloader.load());
        } catch (IOException e) {
            System.out.println("couldnt load dialog");
            e.printStackTrace();
            return;
        }
        dialog.setTitle("Select an item to delete");
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        Optional<ButtonType> delete = dialog.showAndWait();
        if(delete.isPresent() && delete.get().equals(ButtonType.OK)){
            TodoData.getInstance().removetodoitem(mycontroller.selectionlistviewq.getSelectionModel().getSelectedItem());
        }


    }
    public void deleteItem(todoitem item){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete item");
        alert.setHeaderText("Delete item: " + item.getName() + " " + "?");
        alert.setContentText("Are you sure you want to delete that item?");
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent()&&result.get().equals(ButtonType.OK)){
            TodoData.getInstance().removetodoitem(item);
        }

    }
    @FXML
    public void handleKeyPressed(KeyEvent keyEvent) {
        if(todolistview.getSelectionModel().getSelectedItem()!=null){
           if( keyEvent.getCode().equals(KeyCode.DELETE)){
                deleteItem(todolistview.getSelectionModel().getSelectedItem());
            }
        }
    }

    public void toggleswitched() {
        todoitem currentlyselected = todolistview.getSelectionModel().getSelectedItem();
        if(mytoggle.isSelected()){
            todolistview.setItems(filteredList);
            todolistview.getSelectionModel().selectFirst();
        }else{
            todolistview.setItems(sortedList);
            todolistview.getSelectionModel().select(currentlyselected);
        }
    }

    public void exitapp(ActionEvent actionEvent) {
        Platform.exit();
    }
}
