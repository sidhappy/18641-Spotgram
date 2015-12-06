package lgm.cmu.spotagram.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import lgm.cmu.spotagram.Exception.InputErrorException;
import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.model2.Note;
import lgm.cmu.spotagram.request.NearByRequest;
import lgm.cmu.spotagram.request.NewPasswordRequest;

public class New_password_Activity extends AppCompatActivity {

    EditText text1;
    EditText text2;
    EditText text3;
    EditText text4;
    private Context mContext;
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
        mContext=New_password_Activity.this;

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
        int userID;
        String old_password;
        String new_password1;
        String new_password2;
        try {
            userID = Integer.parseInt(text1.getText().toString());
        }catch (Exception e){
            text1.requestFocus();
            Toast.makeText(mContext, "User ID error, please retry", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            old_password = text2.getText().toString();
            if (old_password.length()<6){
                throw new InputErrorException(InputErrorException.ErrorType.WRONG_PASSWORD_FORM,"Please input an exact password");
            }
        }catch (InputErrorException e){
            text2.requestFocus();
            e.execHandle(getApplicationContext());
            return;
        }

        try{
            new_password1=text3.getText().toString();
            new_password2=text4.getText().toString();
            if (new_password1.length()<6 || new_password2.length()<6){
                throw new InputErrorException(InputErrorException.ErrorType.WRONG_PASSWORD_FORM,"Please input a longer new password");
            }
        }catch (InputErrorException e){
            e.execHandle(getApplicationContext());
            return;
        }

        if(!new_password1.equals(new_password2)){
            new AlertDialog.Builder(this)
                    .setTitle("Two new passwords don't match")
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setNegativeButton("Reset", null).show();
            return;
        }else{
            NewPasswordRequest request= new NewPasswordRequest(userID,old_password,new_password1);
                request.setOnNewPwReadyListener(new NewPasswordRequest.OnNewPassWordListener() {
                    @Override
                    public void onNewPwDone(boolean isSuccess, int upload_status) {
                        if (isSuccess) {
                            if(upload_status==0)
                                Toast.makeText(mContext, "Set new Password done", Toast.LENGTH_SHORT).show();
                            if(upload_status==-1)
                                Toast.makeText(mContext, "Failed setting new password", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(mContext, "Network error", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            request.execute();
        }
    }

}
