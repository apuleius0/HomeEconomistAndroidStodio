package com.alexey.homeactivitymodel;

import java.util.Date;

public class Transaction {
    private Date date;
    private double income;
    private String expenseName;
    private double expense;

    public Transaction(Date date, double income, String expenseName, double expense) {
        this.date = date;
        this.income = income;
        this.expenseName = expenseName;
        this.expense = expense;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
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

