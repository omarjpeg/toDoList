package com.company.todolist.datamodel;

import java.time.LocalDate;

public class todoitem {
    private String name;
    private String details;
    private LocalDate deadline;

    public todoitem(String name, String details, LocalDate deadline) {
        this.name = name;
        this.details = details;
        this.deadline = deadline;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }
}
