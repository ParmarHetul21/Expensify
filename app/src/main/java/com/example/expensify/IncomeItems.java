package com.example.expensify;

public class IncomeItems {
    private int id;
    private int spinnerimage;
    private String spinnertext;

    public IncomeItems(int id,int spinnerimage, String spinnertext) {
        this.id = id;
        this.spinnerimage = spinnerimage;
        this.spinnertext = spinnertext;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSpinnerimage() {
        return spinnerimage;
    }
    public void setSpinnerimage(int spinnerimage) {
        this.spinnerimage = spinnerimage;
    }
    public String getSpinnertext() {
        return spinnertext;
    }
    public void setSpinnertext(String spinnertext) {
        this.spinnertext = spinnertext;
    }
}
