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
import lgm.cmu.spotagram.model2.Comment;
import lgm.cmu.spotagram.request.NewNoteRequest;
import lgm.cmu.spotagram.request.ReplyNoteRequest;
import lgm.cmu.spotagram.utils.ConstantValue;
import lgm.cmu.spotagram.utils.ParameterUtils;

public class NewNoteActivity extends AppCompatActivity {

    private TextView locationTV;
    private String latitudeStr;
    private String longitudeStr;
    private double mlat = 0.0;
    private double mlon = 0.0;
    private EditText noteET;
    private String targetURL = "noteservlet";
    private int userid;
    private String username;
    private int mNoteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ParameterUtils.initPreference(this);

        userid = ParameterUtils.getIntValue(ConstantValue.KEY_USER_ID);
        username = ParameterUtils.getStringValue(ConstantValue.KEY_USERNAME);


        Intent intent1 = getIntent();

        latitudeStr = intent1.getStringExtra("latitude");
        longitudeStr = intent1.getStringExtra("longitude");

        mlat = Double.valueOf(latitudeStr);
        mlon = Double.valueOf(longitudeStr);

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

        if (content != null && content.length() != 0) {

            NewNoteRequest request = new NewNoteRequest(userid, username, content, mlat, mlon);

            request.setOnNewNoteListener(new NewNoteRequest.OnNewNoteListener() {
                @Override
                public void onNewNote(boolean isSuccess, int noteId) {
                    if (isSuccess) {
                        Toast.makeText(getApplicationContext(), "Post success", Toast.LENGTH_SHORT).show();
                        mNoteId = noteId;
                    } else {
                        Toast.makeText(getApplicationContext(), "Internet err", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            request.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Note is null or no input", Toast.LENGTH_SHORT).show();
        }


        Toast toast=Toast.makeText(getApplicationContext(),"Send Succeed!",Toast.LENGTH_LONG);
        toast.show();
        return_to_map();
    }




    //return to map view
    public void return_to_map(){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
