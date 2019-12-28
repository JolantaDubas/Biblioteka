package com.example.wirtualnabiblioteka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText name, surname, age, username, password;
    String str_name, str_surname, str_age, str_username, str_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText)findViewById(R.id.et_name);
        surname=(EditText)findViewById(R.id.et_surname);
        age=(EditText)findViewById(R.id.et_age);
        username=(EditText)findViewById(R.id.et_username);
        password=(EditText)findViewById(R.id.et_password);
    }

    public void OnRegister(View view){
        str_name = name.getText().toString();
        str_surname = surname.getText().toString();
        str_age = age.getText().toString();
        str_username = username.getText().toString();
        str_password = password.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_name, str_surname,str_age, str_username, str_password);
    }

    public void OpenLogin(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }
}
