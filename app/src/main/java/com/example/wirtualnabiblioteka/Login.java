package com.example.wirtualnabiblioteka;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

/*public class Login extends AppCompatActivity{
       EditText UsernameEt, PasswordEt;


    TextView textViewLogin;
    public boolean isLogged = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


           setContentView(R.layout.activity_login);

            UsernameEt = (EditText) findViewById(R.id.etUserName);
           PasswordEt = (EditText) findViewById(R.id.etPassword);


    }

    public void OnLogin(View view) {
        String username = UsernameEt.getText().toString();
        String password = PasswordEt.getText().toString();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(type, username, password);
    isLogged=true;


        Log.d("Login", username);
    }


    public void OpenRegister(View view) {
        startActivity(new Intent(this, Register.class));
    }
    public void OpenLibrary(View view) { startActivity(new Intent(this, MainActivity.class)); }

    private String login;





}
*/
public class Login extends AppCompatActivity {

    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;

    EditText UsernameEt, PasswordEt;

    ListView listView;
    List<UserId> libraryList;
    TextView textViewLogin;
    public boolean isLogged = false;
    Button buttonLogin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (isLogged){
            startActivity(new Intent(this, MyBooks.class));
        }
        ProgressBar progressBar;
        setContentView(R.layout.activity_login);


        listView = (ListView) findViewById(R.id.listViewUser);
        UsernameEt = (EditText) findViewById(R.id.etUserName);
        PasswordEt = (EditText) findViewById(R.id.etPassword);


        buttonLogin = (Button) findViewById(R.id.btn_Login);

        libraryList = new ArrayList<>();

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //if it is updating
                String login = UsernameEt.getText().toString().trim();
                String password = PasswordEt.getText().toString().trim();

                if (TextUtils.isEmpty(login)) {
                    UsernameEt.setError("Please enter name");
                    UsernameEt.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    PasswordEt.setError("Please enter real name");
                    PasswordEt.requestFocus();
                    return;
                }
                   login(login,password);

            }
        });

    }



    private void refreshBooksList(JSONArray uzytkownik) throws JSONException {
        libraryList.clear();

        for (int i = 0; i < uzytkownik.length(); i++) {
            JSONObject obj = uzytkownik.getJSONObject(i);

            libraryList.add(new UserId(
                    obj.getInt("id_uzytkownik")
            ));
        }

        Log.d("refresh", "dziala");

        BookAdapter adapter = new BookAdapter(libraryList);
        listView.setAdapter(adapter);
    }

    public class PerformNetworkRequest extends AsyncTask<Void, Void, String> {
        String url;
        HashMap<String, String> params;
        int requestCode;

        PerformNetworkRequest(String url, HashMap<String, String> params, int requestCode) {
            this.url = url;
            this.params = params;
            this.requestCode = requestCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         //   progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshBooksList(object.getJSONArray("uzytkownik"));
                   isLogged=true;
                }else{
                        Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public String doInBackground(Void... voids) {
            RequestHandler requestHandler = new RequestHandler();

            if (requestCode == CODE_POST_REQUEST)
                return requestHandler.sendPostRequest(url, params);


            if (requestCode == CODE_GET_REQUEST)
                return requestHandler.sendGetRequest(url);

            return null;
        }
    }

    class BookAdapter extends ArrayAdapter<UserId> {
        List<UserId> libraryList;

        public BookAdapter(List<UserId> libraryList) {
            super(Login.this, R.layout.layout_mybooks_list, libraryList);
            this.libraryList = libraryList;
            Log.d("bookAdapter", "dziala");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_mybooks_list, null, true);
            Log.d("View", "dziala");

            TextView textViewUserId = listViewItem.findViewById(R.id.textViewUserId);
            final UserId userId = libraryList.get(position);

           // TextView textViewAuthor = listViewItem.findViewById(R.id.textViewAuthor);
          //  textViewName.setText(hero.getName());

            //   progressBar = (ProgressBar) findViewById(R.id.progressBar);
            textViewUserId.setText(String.valueOf(userId.getId()));
            Log.d("userid", String.valueOf(userId.getId()));
            return listViewItem;
        }
    }

   // public void OnLogin(String login, String password){



  //  }
    private void login(String login, String password) {

        Log.d("Login", login);
        Log.d("Password", password);
        HashMap<String, String> params = new HashMap<>();
        params.put("login", login);
        params.put("password", password);

        PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_LOGIN, params,  CODE_POST_REQUEST);
        request.execute();
        Log.d("islogged", String.valueOf(isLogged));
        if (isLogged){
            Intent intent =new Intent(this, MyBooks.class);
            intent.putExtra("login",login);
            this.startActivity(intent);
        }
       // startActivity(new Intent(this, MainActivity.class));

    }


    public void OpenRegister(View view) {
        startActivity(new Intent(this, Register.class));
    }
    public void OpenLibrary(View view) {
        startActivity(new Intent(this, MainActivity.class));
    }

}

