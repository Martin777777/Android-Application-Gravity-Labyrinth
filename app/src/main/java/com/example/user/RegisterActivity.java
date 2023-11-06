package com.example.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Looper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.entrance.EntranceActivity;
import com.example.game.R;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;


public class RegisterActivity extends AppCompatActivity {

    Button signUp;
    EditText username;
    EditText password;
    EditText rePassword;
    AutoCompleteTextView email;
    EditText phone;
    RadioButton male;
    RadioButton female;
    ArrayAdapter<String> adapter;
    DatabaseHelper databaseHelper;
    Connection connection;
    PreparedStatement statement1;
    PreparedStatement statement2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_register);

        signUp = this.findViewById(R.id.sign_up);
        username = this.findViewById(R.id.register_username);
        password = this.findViewById(R.id.register_password);
        rePassword = this.findViewById(R.id.register_repassword);
        email = this.findViewById(R.id.register_email);
        phone = this.findViewById(R.id.register_phone);
        male = this.findViewById(R.id.male);
        female = this.findViewById(R.id.female);

        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "font-style2.ttf");
        signUp.setTypeface(typeface2);

        String[] addressList = new String[]{"@gmail.com", "@qq.com", "@163.com", "@126.com", "@ucdconnect.ie"};
        String[] list = new String[5];
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                for ( int a = 0; a < 5; a++ ){
                    list[a] = email.getText().toString() + addressList[a];
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_dropdown_item_1line, list);
                email.setAdapter(adapter);
            }
        });

        databaseHelper = new DatabaseHelper(this, "account.db", null, 1);

        signUp.setOnClickListener(view -> {

            if (TextUtils.isEmpty(username.getText().toString())){
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "Please enter your username!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            else if (TextUtils.isEmpty(password.getText().toString())){
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "Please enter your password!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            else if (TextUtils.isEmpty(rePassword.getText().toString())){
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "Please enter your password again!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            else if (!password.getText().toString().equals(rePassword.getText().toString())) {
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "Please enter the same passwords!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            else if (!email.getText().toString().matches("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*")){
                Looper.prepare();
                Toast.makeText(RegisterActivity.this, "You should enter an email address!", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
            else {

                new Thread(() -> {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                                "root", "132465798Mm");
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    try {
                        String sql1 = "select username, email from account";
                        String sql2 = "insert into account (username,password,email,phone_number,gender) values (?,?,?,?,?)";
                        if (connection!=null) {

                            boolean repeat = false;
                            statement1 = connection.prepareStatement(sql1);

                            connection.setAutoCommit(false);

                            ResultSet resultSet = statement1.executeQuery();
                            while (resultSet.next()) {
                                if (resultSet.getString("username").equals(username.getText().toString())) {
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this, "The username name has been registered!", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    repeat = true;
                                    break;

                                }
                                else if (resultSet.getString("email").equals(email.getText().toString())) {
                                    Looper.prepare();
                                    Toast.makeText(RegisterActivity.this, "The email name has been registered!", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                    repeat = true;
                                    break;
                                }
                            }

                            connection.commit();
                            resultSet.close();
                            statement1.close();

                            if (!repeat){
                                statement2 = connection.prepareStatement(sql2);

                                connection.setAutoCommit(false);

                                statement2.setString(1, username.getText().toString());
                                statement2.setString(2, password.getText().toString());
                                statement2.setString(3, email.getText().toString());
                                statement2.setString(4, phone.getText().toString());
                                if (male.isChecked()) {
                                    statement2.setString(5, "male");
                                } else {
                                    statement2.setString(5, "female");
                                }

                                statement2.addBatch();
                                statement2.executeBatch();
                                connection.commit();

                                setResult(Activity.RESULT_OK);
                                finish();
                            }

                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();



//                SQLiteDatabase database = databaseHelper.getWritableDatabase();
//                Cursor cursor = database.query("account", new String[]{"username", "email"}, null, null, null, null, null);
//                boolean register = true;
//                if (cursor.moveToFirst()) {
//                    do {
//                        if (username.getText().toString().equals(cursor.getString(cursor.getColumnIndexOrThrow("username")))){
//                            Toast.makeText(RegisterActivity.this, "The username name has been registered!", Toast.LENGTH_SHORT).show();
//                            register = false;
//                            break;
//                        }
//                        else if (email.getText().toString().equals(cursor.getString(cursor.getColumnIndexOrThrow("email")))) {
//                            Toast.makeText(RegisterActivity.this, "The email name has been registered!", Toast.LENGTH_SHORT).show();
//                            register = false;
//                            break;
//                        }
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//                if (register) {
//                    ContentValues contentValues = new ContentValues();
//
//                    contentValues.put("username", username.getText().toString());
//                    contentValues.put("password", password.getText().toString());
//                    contentValues.put("email", email.getText().toString());
//                    contentValues.put("phone_number", phone.getText().toString());
//                    if (male.isChecked()){
//                        contentValues.put("gender", "male");
//                    }
//                    else {
//                        contentValues.put("gender", "female");
//                    }
//                    database.insert("account", null, contentValues);
//                    this.setResult(Activity.RESULT_OK);
//                    finish();
//                }
            }
        });



    }
}