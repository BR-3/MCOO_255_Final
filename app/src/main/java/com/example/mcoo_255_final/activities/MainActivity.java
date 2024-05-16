package com.example.mcoo_255_final.activities;

import static android.preference.PreferenceManager.getDefaultSharedPreferences;

import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.mcoo_255_final.R;
import com.example.mcoo_255_final.lib.Utils;
import com.example.mcoo_255_final.model.RandomNumber;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;

import com.example.mcoo_255_final.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private RandomNumber mRandomNumber;
    private ArrayList<Integer> mNumberHistory;
    private EditText et_to_input;
    private EditText et_from_input;
    private TextView tv_result_text;


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("history", Utils.getJSONStringFromNumberList(mNumberHistory));;
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initializeHistoryList(savedInstanceState, "history");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupContentView();
        setupToolbar();
        setupFields(savedInstanceState);
        setupFAB();
    }

    private void setupContentView() {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    private void setupFAB() {
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String to_input = et_to_input.getText().toString();
                String from_input = et_from_input.getText().toString();

                if(to_input.length() == 0 || from_input.length() == 0) {
                    Toast.makeText(getApplicationContext(),
                            R.string.empty_input_text,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                mRandomNumber.setFromTo(Integer.parseInt(from_input), Integer.parseInt(to_input));
                int currRandomNumber = mRandomNumber.getCurrentRandomNumber();
                mNumberHistory.add(currRandomNumber);
                tv_result_text.setText(String.valueOf(currRandomNumber));
            }
        });
    }

    private void setupFields(Bundle savedInstanceState) {
        mRandomNumber = new RandomNumber();
        et_to_input = findViewById(R.id.to_input);
        et_from_input = findViewById(R.id.from_input);
        tv_result_text = findViewById(R.id.result_text);
        initializeHistoryList(savedInstanceState, "history");
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

//    private void initializeHistoryList (Bundle savedInstanceState, String key)
//    {
//        if (savedInstanceState != null) {
//            mNumberHistory = savedInstanceState.getIntegerArrayList (key);
//        }
//        else {
//            String history = getDefaultSharedPreferences (this).getString (key, null);
//            mNumberHistory = history == null ?
//                    new ArrayList<> () : Utils.getNumberListFromJSONString (history);
//        }
//    }

    private void initializeHistoryList(Bundle savedInstanceState, String key) {
        String history = getDefaultSharedPreferences(this).getString(key, null);
        mNumberHistory = history == null ?
                new ArrayList<>() : Utils.getNumberListFromJSONString(history);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_show_history) {
            showHistory();
            return true;
        } else if (id == R.id.action_clear_history) {
            mNumberHistory.clear();
            return true;
        } else if (id == R.id.action_about) {
            showAbout();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showHistory() {
        if(!mNumberHistory.isEmpty()){
            Utils.showInfoDialog(MainActivity.this, "History", mNumberHistory.toString());
        }else {
            Utils.showInfoDialog(MainActivity.this, "History","No numbers saved.");

        }
    }

    private void showAbout() {
        Toast.makeText(getApplicationContext(),
                R.string.about_text,
                Toast.LENGTH_SHORT).show();
    }

}