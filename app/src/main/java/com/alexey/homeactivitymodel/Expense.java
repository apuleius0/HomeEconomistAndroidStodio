package com.alexey.homeactivitymodel;

public class Expense {
    private int id;
    private String date;
    private double income;
    private String expenseName;
    private double expense;

    public Expense() {}

    public Expense(int id, String date, double income, String expenseName, double expense) {
        this.id = id;
        this.date = date;
        this.income = income;
        this.expenseName = expenseName;
        this.expense = expense;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getIncome() {
        return income;
    }

    public void setIncome(double income) {
        this.income = income;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public double getExpense() {
        return expense;
    }

    public void setExpense(double expense) {
        this.expense = expense;
    }
}

