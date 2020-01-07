package com.example.wirtualnabiblioteka;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Book extends AppCompatActivity {


    public static final int CODE_GET_REQUEST = 1024;
    public static final int CODE_POST_REQUEST = 1025;


    ProgressBar progressBar;
    ListView listView;
    Button buttonAddUpdate;

    TextView tvTitle, tvName;

    List<Library> libraryList;

    boolean isUpdating = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewBooks);

        libraryList = new ArrayList<>();

        readBooks();
    }

    private void readBooks() {
        PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_READ_BOOKS, null, CODE_GET_REQUEST);
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
            super(Book.this, R.layout.layout_book_info, libraryList);
            this.libraryList = libraryList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.layout_mybooks_list, null, true);

            TextView textViewTitle = listViewItem.findViewById(R.id.textViewTitle);
            TextView textViewAuthor = listViewItem.findViewById(R.id.textViewAuthor);

            //TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            //TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final Library library = libraryList.get(position);

            textViewTitle.setText(library.getTitle());
            textViewAuthor.setText(library.getAuthor());

            return listViewItem;
        }
    }


}
