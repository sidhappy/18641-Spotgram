package lgm.cmu.spotagram.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import lgm.cmu.spotagram.R;

public class NewNoteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_note);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    //send a new post, then return to map view
    public void send_and_return(View v){
        Toast toast=Toast.makeText(getApplicationContext(),"Send Succeed!",Toast.LENGTH_LONG);
        toast.show();
        return_to_map();
    }
    //return to map view
    public void return_to_map(){
        Intent intent =new Intent(this,MapsActivity.class);
        startActivity(intent);
    }

}