package com.company.todolist.datamodel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

public class TodoData {
    private static TodoData INSTANCE = new TodoData();
    private static String filename = "todolistitemsstored.txt";

    private ObservableList<todoitem> todoitemList;
    private DateTimeFormatter formatter;

    public static TodoData getInstance(){
        return INSTANCE;
    }

    public ObservableList<todoitem> getTodoitemList() {
        return todoitemList;
    }

    private TodoData(){
        formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
    }
    public void loaddata() throws IOException {
        todoitemList = FXCollections.observableArrayList();
        Path path = Paths.get(filename);
        BufferedReader br = Files.newBufferedReader(path);
        String input;
        try {
            while ((input = br.readLine()) != null) {

                String[] inputstring = input.split("\t");
                String name = inputstring[0];
                String details = inputstring[1];
                String date = inputstring[2];

                LocalDate inputdate = LocalDate.parse(date, formatter);
                todoitem todoItem = new todoitem(name, details, inputdate);
                todoitemList.add(todoItem);
            }
        }finally {
            if (br != null) {
                br.close();
            }
        }
    }
    public void storedata()throws IOException{
        Path path = Paths.get(filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try{
            Iterator<todoitem> myiterator= todoitemList.iterator();
            while(myiterator.hasNext()){
                todoitem current = myiterator.next();
                bw.write(String.format("%s\t%s\t%s",
                        current.getName() ,
                current.getDetails(),
                        current.getDeadline().format(formatter)));
                bw.newLine();
            }


        }finally {
        if(bw!=null){
            bw.close();
        }
        }

    }
    public void removetodoitem(todoitem todoitem){
        todoitemList.remove(todoitem);
    }
    public void addTodoItem(todoitem todoitem) {
        todoitemList.add(todoitem);
    }
}
