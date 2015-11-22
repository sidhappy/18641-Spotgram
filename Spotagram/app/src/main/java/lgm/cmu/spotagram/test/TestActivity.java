package lgm.cmu.spotagram.test;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.ui.AboutMeActivity;
import lgm.cmu.spotagram.ui.DetailActivity;
import lgm.cmu.spotagram.ui.LoginActivity;
import lgm.cmu.spotagram.ui.MainActivity;
import lgm.cmu.spotagram.ui.MapsActivity;
import lgm.cmu.spotagram.ui.NearByActivity;
import lgm.cmu.spotagram.ui.NewNoteActivity;
import lgm.cmu.spotagram.ui.SettingsActivity;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }

    public void tologin(View view){
        Intent intent = new Intent(TestActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    public void tomap(View view){
        Intent intent = new Intent(TestActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void tonewnote(View view){
        Intent intent = new Intent(TestActivity.this, NewNoteActivity.class);
        startActivity(intent);
    }

    public void tonearby(View view){
        Intent intent = new Intent(TestActivity.this, NearByActivity.class);
        startActivity(intent);
    }

    public void todetail(View view){
        Intent intent = new Intent(TestActivity.this, DetailActivity.class);
        startActivity(intent);
    }

    public void tosetting(View view){
        Intent intent = new Intent(TestActivity.this, SettingsActivity.class);
        startActivity(intent);
    }

    public void toabout(View view){
        Intent intent = new Intent(TestActivity.this, AboutMeActivity.class);
        startActivity(intent);
    }
}
