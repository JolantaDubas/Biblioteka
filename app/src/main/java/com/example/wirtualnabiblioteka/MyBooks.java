package com.example.wirtualnabiblioteka;

import androidx.appcompat.app.AppCompatActivity;
import com.example.wirtualnabiblioteka.MainActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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



    public class MyBooks extends AppCompatActivity {

        public static final int CODE_GET_REQUEST = 1024;
        public static final int CODE_POST_REQUEST = 1025;


        ProgressBar progressBar;
        ListView listView;


        Button buttonAddUpdate;

        TextView tvTitle, tvName;

        List<MyLibrary> libraryList;

        boolean isUpdating = false;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_my_books);


            Log.d("Login", "username");
            progressBar = (ProgressBar) findViewById(R.id.progressBar);
            listView = (ListView) findViewById(R.id.listViewBooks);

            Intent i = getIntent();
            String login = i.getStringExtra("login");

            libraryList = new ArrayList<>();
            readMyBooks(login);

        }

        private void readMyBooks(String login) {
            PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_READ_MYBOOKS + login, null, CODE_GET_REQUEST);
            request.execute();
        }
        private void readBooks() {
            PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_READ_BOOKS , null, CODE_GET_REQUEST);
            request.execute();
        }




        private void refreshBooksList(JSONArray ksiazki) throws JSONException {
            libraryList.clear();

            for (int i = 0; i < ksiazki.length(); i++) {
                JSONObject obj = ksiazki.getJSONObject(i);

                libraryList.add(new MyLibrary(
                        obj.getInt("k.id_ksiazka"),
                        obj.getString("k.tytul"),
                        obj.getString("a.imie"),
                        obj.getString("a.nazwisko"),
                        obj.getString("a.pseudonim"),
                        obj.getString("k.opis"),
                        obj.getString("kt.kategoria"),
                        obj.getString("j.jezyk"),
                        obj.getString("w.nazwa"),
                        obj.getString("w.lokalizacja"),
                        obj.getInt("kp.id_kopie"),
                        obj.getInt("kp.status"),
                        obj.getString("rez.data_wypozyczenia"),
                        obj.getString("rez.data_oddania")
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

        class BookAdapter extends ArrayAdapter<MyLibrary> {
            List<MyLibrary> libraryList;

            public BookAdapter(List<MyLibrary> libraryList) {
                super(MyBooks.this, R.layout.layout_mybooks_list, libraryList);
                this.libraryList = libraryList;
            }


            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                LayoutInflater inflater = getLayoutInflater();
                View listViewItem = inflater.inflate(R.layout.layout_mybooks_list, null, true);

                TextView textViewTitle = listViewItem.findViewById(R.id.textViewTitle);
                TextView textViewName = listViewItem.findViewById(R.id.textViewName);
                TextView textViewSurname = listViewItem.findViewById(R.id.textViewSurname);
                TextView textViewPseudonym = listViewItem.findViewById(R.id.textViewPseudonym);
                TextView textViewGenre = listViewItem.findViewById(R.id.textViewGenre);
                TextView textViewLanguage = listViewItem.findViewById(R.id.textViewLanguage);
                TextView textViewNamePubl = listViewItem.findViewById(R.id.textViewNamePubl);
                TextView textViewLocationPubl = listViewItem.findViewById(R.id.textViewLocationPubl);
                TextView textViewCopyId = listViewItem.findViewById(R.id.textViewCopyId);
                final TextView textViewStatus = listViewItem.findViewById(R.id.textViewStatus);
                final TextView textViewDate1 = listViewItem.findViewById(R.id.textViewDate1);
                final TextView textViewDate2 = listViewItem.findViewById(R.id.textViewDate2);



                final MyLibrary library = libraryList.get(position);

                textViewTitle.setText(library.getTitle());
                textViewName.setText(library.getAuthor());
                textViewSurname.setText(library.getSurname());
                textViewPseudonym.setText(library.getPseudonym());
                textViewGenre.setText(library.getGenre());
                textViewLanguage.setText(library.getLanguage());
                textViewNamePubl.setText(library.getNamePubl());
                textViewLocationPubl.setText(library.getLocationPubl());
                textViewCopyId.setText(String.valueOf(library.getCopyId()));
                textViewStatus.setText("Zarezerwowana");
                textViewDate1.setText(library.getDate1());
                textViewDate2.setText(library.getDate2());
               // checkBoxStatus.setText(library.getStatus());





                return listViewItem;
            }
        }

        public void OpenLibrary(View view) { startActivity(new Intent(this, MainActivity.class)); }

    }
