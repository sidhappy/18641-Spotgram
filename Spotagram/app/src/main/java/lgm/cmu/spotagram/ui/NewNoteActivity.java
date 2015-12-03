package lgm.cmu.spotagram.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import lgm.cmu.spotagram.R;

public class NewNoteActivity extends AppCompatActivity {

    private TextView locationTV;
    private String latitudeStr;
    private String longitudeStr;
    private EditText noteET;
    private String targetURL = "noteservlet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent1 = getIntent();

        latitudeStr = intent1.getStringExtra("latitude");
        longitudeStr = intent1.getStringExtra("longitude");

        locationTV = (TextView)findViewById(R.id.textView);
        noteET = (EditText)findViewById(R.id.editText);

        locationTV.setText(latitudeStr + "," + longitudeStr);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //send a new post, then return to map view
    public void send_and_return(View v){

        String content = noteET.getText().toString();
        if (content.equals("")){
            content = "What a Great Day!";
        }



        Toast toast=Toast.makeText(getApplicationContext(),"Send Succeed!",Toast.LENGTH_LONG);
        toast.show();
        return_to_map();
    }

    public String sendRequest(String content){
        URL url;
        HttpURLConnection connection = null;
        try {
            String urlParameters = "content=" + URLEncoder.encode(content, "UTF-8") +
                            "&latitude=" + URLEncoder.encode(latitudeStr, "UTF-8") +
                            "&longitude=" + URLEncoder.encode(longitudeStr, "UTF-8");

            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }

    }


    //return to map view
    public void return_to_map(){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
