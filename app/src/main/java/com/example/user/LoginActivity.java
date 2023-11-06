package com.example.user;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.entrance.EntranceActivity;
import com.example.game.R;
import com.mysql.jdbc.Blob;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    Button login;
    Button register;
    DatabaseHelper databaseHelper;
    EditText username;
    EditText password;
    SharedPreferences userData;
    CheckBox keepIn;
    Connection connection;
    PreparedStatement statement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN
                , WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        login = this.findViewById(R.id.btn_login);
        register = this.findViewById(R.id.btn_register);

        username = this.findViewById(R.id.username);
        password = this.findViewById(R.id.password);
        keepIn = this.findViewById(R.id.keep_in);

        Typeface typeface2 = Typeface.createFromAsset(getAssets(), "font-style2.ttf");
        login.setTypeface(typeface2);
        register.setTypeface(typeface2);

        userData = this.getSharedPreferences("user_data", MODE_PRIVATE);

        databaseHelper = new DatabaseHelper(this, "account.db", null, 1);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                SQLiteDatabase database = databaseHelper.getWritableDatabase();
//                Cursor cursor = database.query("account", new String[]{"username", "password"}, "username = ?", new String[]{username.getText().toString()}, null, null, null);
//                if (cursor.moveToFirst()){
//                    String psw = cursor.getString(cursor.getColumnIndexOrThrow("password"));
//                    if (psw.equals(password.getText().toString())) {
//                        SharedPreferences.Editor editor = userData.edit();
//                        if (keepIn.isChecked()){
//                            editor.putBoolean("keepIn", true);
//                            editor.putString("username", username.getText().toString());
//                        }
//                        EntranceActivity.isLogin = true;
//                        EntranceActivity.username = username.getText().toString();
//                        editor.apply();
//                        cursor.close();
//                        setResult(Activity.RESULT_OK);
//                        finish();
//                    }
//                    else {
//                        Toast.makeText(LoginActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                else {
//                    Toast.makeText(LoginActivity.this, "The username does not exist!", Toast.LENGTH_SHORT).show();
//                }
//                cursor.close();
                new Thread(() -> {
                    try {
                        Class.forName("com.mysql.jdbc.Driver");
                        connection = DriverManager.getConnection("jdbc:mysql://bj-cdb-he3zrqqs.sql.tencentcdb.com:60342/game_labyrinth?useSSL=false",
                                "root", "132465798Mm");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        String sql = "select username, password from account where username=?";
                        if (connection != null) {

                            statement = connection.prepareStatement(sql);

                            connection.setAutoCommit(false);

                            statement.setString(1, username.getText().toString());

                            ResultSet resultSet = statement.executeQuery();//创建数据对象

                            if (resultSet.next()) {
                                String user = resultSet.getString("username");
                                if (user.equals(username.getText().toString())){
                                    String psw = resultSet.getString("password");
                                    if (psw.equals(password.getText().toString())) {
                                        SharedPreferences.Editor editor = userData.edit();
                                        if (keepIn.isChecked()) {
                                            editor.putBoolean("keepIn", true);
                                            editor.putString("username", username.getText().toString());
                                        }
                                        EntranceActivity.isLogin = true;
                                        EntranceActivity.username = username.getText().toString();
                                        editor.apply();
                                        connection.commit();
                                        resultSet.close();
                                        statement.close();
                                        setResult(Activity.RESULT_OK);
                                        finish();
                                    } else {
                                        Looper.prepare();
                                        Toast.makeText(LoginActivity.this, "Wrong password!", Toast.LENGTH_SHORT).show();
                                        Looper.loop();
                                    }
                                }
                                else {
                                    Looper.prepare();
                                    Toast.makeText(LoginActivity.this, "The username does not exist!", Toast.LENGTH_SHORT).show();
                                    Looper.loop();
                                }
                            } else {
                                Looper.prepare();
                                Toast.makeText(LoginActivity.this, "The username does not exist!", Toast.LENGTH_SHORT).show();
                                Looper.loop();
                            }
                            connection.commit();
                            resultSet.close();
                            statement.close();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();

            }
        });

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Toast.makeText(LoginActivity.this, "Sign up successfully!", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                launcher.launch(intent);
            }
        });
    }
}