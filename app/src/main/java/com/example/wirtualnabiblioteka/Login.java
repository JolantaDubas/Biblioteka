package com.example.wirtualnabiblioteka;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Login extends AppCompatActivity{
    EditText UsernameEt, PasswordEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        UsernameEt = (EditText)findViewById(R.id.etUserName);
        PasswordEt = (EditText)findViewById(R.id.etPassword);
    }

    public void OnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
    }

    public void OpenRegister(View view) {
        startActivity(new Intent(this, Register.class));
    }

}