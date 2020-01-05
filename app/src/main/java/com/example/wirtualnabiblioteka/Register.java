package com.example.wirtualnabiblioteka;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    EditText name, secondname, surname, email, phone, address, username, password;
    String str_name, str_secondname, str_surname, str_email, str_phone, str_address, str_username, str_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        name=(EditText)findViewById(R.id.et_name);
        secondname=(EditText)findViewById(R.id.et_secondname);
        surname=(EditText)findViewById(R.id.et_surname);
        email=(EditText)findViewById(R.id.et_email);
        phone=(EditText)findViewById(R.id.et_phone);
        address=(EditText)findViewById(R.id.et_address);
        username=(EditText)findViewById(R.id.et_username);
        password=(EditText)findViewById(R.id.et_password);
    }

    public void OnRegister(View view){
        str_name = name.getText().toString();
        str_secondname = secondname.getText().toString();
        str_surname = surname.getText().toString();
        str_email = email.getText().toString();
        str_phone = phone.getText().toString();
        str_address = address.getText().toString();
        str_username = username.getText().toString();
        str_password = password.getText().toString();
        String type = "register";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, str_name, str_secondname, str_surname,str_email,str_phone,str_address, str_username, str_password);
    }

    public void OpenLogin(View view) {
        startActivity(new Intent(this, Login.class));
    }
}
