package com.alexey.homeactivitymodel;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private TextView mCurrentBalanceTextView;
    private TableLayout mDataTable;
    private List<Transaction> mTransactions = new ArrayList<>();
    private double mCurrentBalance = 0;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;

    private static final String FILENAME = "finances_csv.json";
    private static final Type TRANSACTIONS_TYPE = new TypeToken<List<Transaction>>() {}.getType();
    private static final Gson GSON = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();

    private static final String PREFERENCE_NAME = "com.alexey.homeactivitymodel.PREFERENCE_FILE_KEY";
    private static final String LANGUAGE_KEY = "com.alexey.homeactivitymodel.LANGUAGE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguage();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mCurrentBalanceTextView = findViewById(R.id.balance_btn);
        mDataTable = findViewById(R.id.tableLayout);

        try (InputStreamReader reader = new InputStreamReader(openFileInput(FILENAME))) {
            mTransactions = GSON.fromJson(reader, TRANSACTIONS_TYPE);
        } catch (IOException e) {
            // File not found, ignore
        }

        calculateCurrentBalance();
        updateDataTable();
        updateCurrentBalance();

        mCurrentBalanceTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_layout, null);
                builder.setView(dialogView);

                EditText incomeInput = dialogView.findViewById(R.id.incomeInput);
                EditText expenseNameInput = dialogView.findViewById(R.id.expenseNameInput);
                EditText expenseInput = dialogView.findViewById(R.id.expenseInput);

                AlertDialog dialog = builder.create();
                dialog.setCancelable(false);

                Button okButton = dialogView.findViewById(R.id.ok_btn);
                okButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        double income = 0;
                        if (!incomeInput.getText().toString().isEmpty()) {
                            income = Double.parseDouble(incomeInput.getText().toString());
                        }

                        double expense = 0;
                        if (!expenseInput.getText().toString().isEmpty()) {
                            expense = Double.parseDouble(expenseInput.getText().toString());
                        }

                        String expenseName = expenseNameInput.getText().toString();
                        Date date = new Date();

                        mCurrentBalance += income - expense;
                        mTransactions.add(new Transaction(date, income, expenseName, expense));

                        updateCurrentBalance();
                        updateDataTable();

                        try (OutputStreamWriter writer = new OutputStreamWriter(openFileOutput(FILENAME, MODE_PRIVATE))) {
                            GSON.toJson(mTransactions, TRANSACTIONS_TYPE, writer);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        incomeInput.setText("");
                        expenseNameInput.setText("");
                        expenseInput.setText("");
                    }
                });

                Button exitButton = dialogView.findViewById(R.id.exit_btn);
                exitButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        Button exitButton = findViewById(R.id.exit_btn);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.open, R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                if (id == R.id.nav_date) {
                    Intent intentDate = new Intent(MainActivity.this, DateActivity.class);
                    startActivity(intentDate);
                } else if (id == R.id.nav_languages) {
                    showLanguageDialog();
                } else if (id == R.id.nav_download) {
                    downloadFile();
                }

                // Close the navigation drawer when an item is selected.
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    private void updateCurrentBalance() {
        mCurrentBalanceTextView.setText(String.format(Locale.US, "Баланс: %.2f", mCurrentBalance));
    }

    private void updateDataTable() {
        mDataTable.removeAllViews();

        TableRow headerRow = new TableRow(this);
        String[] headerTexts = {"Дата", "Доход", "Статья", "Расход"};
        for (String headerText : headerTexts) {
            TextView headerView = new TextView(this);
            headerView.setText(headerText);
            headerView.setTextSize(18);
            headerView.setTextColor(Color.BLACK);
            headerRow.addView(headerView);
        }
        mDataTable.addView(headerRow);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        for (Transaction transaction : mTransactions) {
            TableRow row = new TableRow(this);

            TextView dateView = new TextView(this);
            dateView.setText(sdf.format(transaction.getDate()));
            row.addView(dateView);

            TextView incomeView = new TextView(this);
            incomeView.setText(String.format(Locale.US, "%.2f", transaction.getIncome()));
            row.addView(incomeView);

            TextView expenseNameView = new TextView(this);
            expenseNameView.setText(transaction.getExpenseName());
            row.addView(expenseNameView);

            TextView expenseView = new TextView(this);
            expenseView.setText(String.format(Locale.US, "%.2f", transaction.getExpense()));
            row.addView(expenseView);

            mDataTable.addView(row);
        }
    }

    private void calculateCurrentBalance() {
        for (Transaction transaction : mTransactions) {
            mCurrentBalance += transaction.getIncome() - transaction.getExpense();
        }
    }

    private void downloadFile() {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse("https://example.com/myfile.json"));
        request.setDescription("Downloading file...");
        request.setTitle("File Download");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "myfile.json");
        DownloadManager manager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        manager.enqueue(request);
    }

    private void loadLanguage() {
        SharedPreferences sharedPref = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        String languageToLoad = sharedPref.getString(LANGUAGE_KEY, "en");
        Locale locale = new Locale(languageToLoad);
        Locale.setDefault(locale);
        android.content.res.Configuration config = new android.content.res.Configuration();
        config.setLocale(locale);
        Context context = createConfigurationContext(config);
        Resources resources = context.getResources();
    }


    private void saveLanguage(String languageToSave) {
        SharedPreferences sharedPref = this.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(LANGUAGE_KEY, languageToSave);
        editor.apply();
    }

    private void showLanguageDialog() {
        final String[] languages = {"English", "Русский"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(languages, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        saveLanguage("en");
                        break;
                    case 1:
                        saveLanguage("ru");
                        break;
                }
                loadLanguage();
                dialog.dismiss();
            }
        });
        builder.create().show();
    }
}
