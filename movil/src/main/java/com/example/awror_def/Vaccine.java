package com.example.awror_def;

public class Vaccine {

    private int id;
    private int petId;
    private String name;
    private String dateGiven;
    private String nextDate;
    private String notes;

    public Vaccine() {

    }

    public Vaccine(int petId, String name, String dateGiven, String nextDate, String notes) {
        this.petId = petId;
        this.name = name;
        this.dateGiven = dateGiven;
        this.nextDate = nextDate;
        this.notes = notes;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public int getPetId() {
        return petId;
    }
    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getDateGiven() {
        return dateGiven;
    }
    public void setDateGiven(String dateGiven) {
        this.dateGiven = dateGiven;
    }

    public String getNextDate() {
        return nextDate;
    }
    public void setNextDate(String nextDate) {
        this.nextDate = nextDate;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }
}
