package lgm.cmu.spotagram.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.request.NewPasswordRequest;

public class New_password_Activity extends AppCompatActivity {

    EditText text1;
    EditText text2;
    EditText text3;
    EditText text4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_password_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        text1=(EditText)findViewById(R.id.userID);
        text2=(EditText)findViewById(R.id.old_pw);
        text3=(EditText)findViewById(R.id.new_pw1);
        text4=(EditText)findViewById(R.id.new_pw2);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void  send_newPW(View v){
        int userID=Integer.parseInt(text1.getText().toString());
        String old_password=text2.getText().toString();
        String new_password1=text3.getText().toString();
        String new_password2=text4.getText().toString();

        if(new_password1.equals(new_password2)){
            new AlertDialog.Builder(this)
                    .setTitle("Two new passwords don't match")
                    .setIcon(android.R.drawable.alert_dark_frame)
                    .setNegativeButton("Return", null).show();
            return;
        }else{
            NewPasswordRequest request= new NewPasswordRequest(userID,old_password,new_password1);

        }
    }

}
