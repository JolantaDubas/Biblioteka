package com.example.wirtualnabiblioteka;

import androidx.appcompat.app.AppCompatActivity;
import com.example.wirtualnabiblioteka.MainActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    List<MyLibrary> libraryList;

EditText textViewDate1,textViewDate2,textViewLogin, textViewCopyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent i = getIntent();


        setContentView(R.layout.activity_book);
        Log.d("Book", "tytul");
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        listView = (ListView) findViewById(R.id.listViewBooks);


        //textViewDate1 =(EditText) findViewById(R.id.textViewDate1);
        //textViewDate2 =(EditText) findViewById(R.id.textViewDate2);



        int bookId = i.getIntExtra("bookId", 1);
      //  String login= i.getStringExtra("login");

     //   textViewLogin.setText(login);


        textViewLogin = findViewById(R.id.textViewLogin);

        //String login="";
        //login = i.getStringExtra("login");

        //Log.d("moj ksiazka",);
       // Log.d("moj login", login);


       /*if(login.isEmpty()){
        }else{
            textViewLogin.setText(login);
        }*/
        libraryList = new ArrayList<>();
        readBook(bookId);

    }

    private void readBook(int bookId) {
        PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_READ_BOOK + bookId, null, CODE_GET_REQUEST);
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
            super(Book.this, R.layout.layout_mybooks_list, libraryList);
            this.libraryList = libraryList;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            final View listViewItem = inflater.inflate(R.layout.layout_mybooks_list, null, true);

            TextView textViewBookId = listViewItem.findViewById(R.id.textViewBookId);
            final TextView textViewUserId = listViewItem.findViewById(R.id.textViewUserId);
            TextView textViewTitle = listViewItem.findViewById(R.id.textViewTitle);
            TextView textViewName = listViewItem.findViewById(R.id.textViewName);
            TextView textViewSurname = listViewItem.findViewById(R.id.textViewSurname);
            TextView textViewPseudonym = listViewItem.findViewById(R.id.textViewPseudonym);
            TextView textViewDescr = listViewItem.findViewById(R.id.textViewDescr);
            TextView textViewGenre = listViewItem.findViewById(R.id.textViewGenre);
            TextView textViewLanguage = listViewItem.findViewById(R.id.textViewLanguage);
            TextView textViewNamePubl = listViewItem.findViewById(R.id.textViewNamePubl);
            TextView textViewLocationPubl = listViewItem.findViewById(R.id.textViewLocationPubl);
            final EditText textViewCopyId = listViewItem.findViewById(R.id.textViewCopyId);
            final TextView textViewStatus = listViewItem.findViewById(R.id.textViewStatus);
            final TextView textViewDate1 = listViewItem.findViewById(R.id.textViewDate1);
            final TextView textViewDate2 = listViewItem.findViewById(R.id.textViewDate2);
            TextView textView3 = listViewItem.findViewById(R.id.textView3);

            //TextView textViewUpdate = listViewItem.findViewById(R.id.textViewUpdate);
            //TextView textViewDelete = listViewItem.findViewById(R.id.textViewDelete);

            final MyLibrary library = libraryList.get(position);

            textViewBookId.setText(String.valueOf(library.getId()));
            textViewTitle.setText(library.getTitle());
            textViewName.setText(library.getAuthor());
            textViewSurname.setText(library.getSurname());
            textViewPseudonym.setText(library.getPseudonym());
            textViewDescr.setText(library.getDescr());
            textViewGenre.setText(library.getGenre());
            textViewLanguage.setText(library.getLanguage());
            textViewNamePubl.setText(library.getNamePubl());
            textViewLocationPubl.setText(library.getLocationPubl());
            textViewDate1.setText(library.getDate1());
            textViewDate2.setText(library.getDate2());
            textViewCopyId.setText(String.valueOf(library.getCopyId()));
            if(library.getStatus()==1){
                textViewStatus.setText("Zarezerwowana");
                textViewDate1.setText(library.getDate1());
                textViewDate2.setText(library.getDate2());
            }else{
                textViewStatus.setText("Dostepna, Zarezerwuj");
                textView3.setVisibility(GONE);
                textViewDate1.setText("Wybierz date wypożyczenia");
                textViewDate2.setText("Wybierz date oddania");
                textViewStatus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {



                        String date1 = textViewDate1.getText().toString();
                        String date2 = textViewDate2.getText().toString();
                        String copyId=textViewCopyId.getText().toString();
                        String userId = textViewUserId.getText().toString();
                        String login="admin";
                      //  String login=textViewLogin.getText().toString();


                        if (TextUtils.isEmpty(date1)) {
                            textViewDate1.setError("Wprowadz date wypozyczenia");
                            textViewDate1.requestFocus();
                            return;
                        }
                        if (TextUtils.isEmpty(userId)) {
                            textViewDate1.setError("Zaloguj sie");
                            textViewDate1.requestFocus();
                            return;
                        }
                       // if (TextUtils.isEmpty(login)) {
                      //      textViewDate1.setError("Zaloguj się");
                       //     textViewDate1.requestFocus();
                      //      return;
                      //  }

                        if (TextUtils.isEmpty(date2)) {
                            textViewDate2.setError("Wprowadz date oddania");
                            textViewDate2.requestFocus();
                            return;
                        }
                        updateStatus(date1,date2,copyId,userId);
                    }
                });
                textViewDate1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowDateTimeDialog(textViewDate1);
                    }
                });

                textViewDate2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ShowDateTimeDialog(textViewDate2);
                    }
                });
            }
            return listViewItem;
        }

    }

    private void ShowDateTimeDialog(final TextView date_time){
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH-mm");

                        date_time.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(Book.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
            }
        };
        new DatePickerDialog(Book.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();


    }
    private void updateStatus(String date1, String date2, String copyId, String userId) {

      //  String login = textViewLogin.getText().toString().trim();
     //   String copyId = textViewCopyId.getText().toString().trim();




        //if (TextUtils.isEmpty(login)) {
          //  textViewLogin.setError("Zaloguj się");
            //textViewLogin.requestFocus();
           // return;
       // }

        Log.d("Copyid", copyId);
        Log.d("Date1", date1);
        Log.d("Date2", date2);
        Log.d("UserId", userId);
       // if (TextUtils.isEmpty(copyId)) {
         //   textViewLogin.setError("Zaloguj się");
           // textViewLogin.requestFocus();
         //   return;
        //}



        HashMap<String, String> params = new HashMap<>();
        params.put("id_uzytkownik", userId);
        params.put("data_wypozyczenia", date1);
        params.put("data_oddania", date2);
        params.put("id_kopie", copyId);

        PerformNetworkRequest request = new PerformNetworkRequest(URL.URL_UPDATE_STATUS, params, CODE_POST_REQUEST);
        request.execute();
        startActivity(new Intent(this, MainActivity.class));

    }


    public void OpenLibrary(View view) {
        startActivity(new Intent(this, MainActivity.class)); }

}
