package com.alexey.homeactivitymodel;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class DateActivity extends AppCompatActivity {
    private Spinner yearSpinner;
    private GridView monthGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);

        yearSpinner = findViewById(R.id.year_spinner);
        monthGridView = findViewById(R.id.month_grid_view);

        // Инициализация Spinner и GridView
        initializeYearSpinner();
        initializeMonthGridView();
    }

    private void initializeYearSpinner() {
        // Создайте список годов для Spinner
        List<String> years = new ArrayList<>();
        for (int i = 2000; i <= 2050; i++) {
            years.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(adapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = (String) parent.getItemAtPosition(position);
                // Обновите GridView в соответствии с выбранным годом
                updateMonthGridView(selectedYear);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void initializeMonthGridView() {
        // Заполните GridView месяцами
        List<String> months = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, months);
        monthGridView.setAdapter(adapter);

        monthGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedMonth = (String) parent.getItemAtPosition(position);
                String selectedYear = (String) yearSpinner.getSelectedItem();
                // Откройте MonthActivity с выбранным годом и месяцем
                openMonthActivity(selectedYear, selectedMonth);
            }
        });
    }

    private void updateMonthGridView(String year) {
        // Здесь вы можете обновить GridView в соответствии с выбранным годом, если это необходимо
    }

    private void openMonthActivity(String year, String month) {
        Intent intent = new Intent(this, MonthActivity.class);
        intent.putExtra("year", year);
        intent.putExtra("month", month);
        startActivity(intent);
    }
}



