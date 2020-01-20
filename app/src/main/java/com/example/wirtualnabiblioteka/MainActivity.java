package com.example.wirtualnabiblioteka;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

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

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity {


    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;


    ProgressBar progressBar;

    Button buttonAddUpdate;

    TextView tvTitle, tvName;
    ListView listView;
    List<Library> libraryList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("myTag", "This is my message");


        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewBooks);

        libraryList = new ArrayList<>();

        readBooks();
    }

    private void readBooks() {
        PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_READ_BOOKS, null, CODE_GET_REQUEST);
        request.execute();
    }


    private void readBook(int id) {
        PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_READ_BOOK + id, null, CODE_POST_REQUEST);
        request.execute();


    }


    private void refreshBooksList(JSONArray ksiazki) throws JSONException {
        libraryList.clear();

        for (int i = 0; i < ksiazki.length(); i++) {
            JSONObject obj = ksiazki.getJSONObject(i);

            libraryList.add(new Library(
                    obj.getInt("k.id_ksiazka"),
                    obj.getString("k.tytul"),
                    obj.getString("a.imie")

            ));
        }

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
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressBar.setVisibility(GONE);
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("error")) {
                    Toast.makeText(getApplicationContext(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    refreshBooksList(object.getJSONArray("ksiazki"));
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

    class BookAdapter extends ArrayAdapter<Library> {
        List<Library> libraryList;

        public BookAdapter(List<Library> libraryList) {
            super(MainActivity.this, R.layout.layout_library_list, libraryList);
            this.libraryList = libraryList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_library_list, null, true);

            Log.d("View", "dziala");
            TextView textViewTitle = listViewItem.findViewById(R.id.textViewTitle);
            TextView textViewAuthor = listViewItem.findViewById(R.id.textViewAuthor);

            //TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Library library = libraryList.get(position);

            textViewTitle.setText(library.getTitle());
            textViewAuthor.setText(library.getAuthor());



            textViewDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {



                    Intent intent =new Intent(MainActivity.this , Book.class);

                    intent.putExtra("bookId",library.getId());
                    startActivity(intent);


                }
            });



            return listViewItem;
        }
    }

    public void OpenLogin(View view) {
            startActivity(new Intent(this, Login.class));
    }

    public void BookInfo(View view) {
        startActivity(new Intent(this, Book.class));
    }


}