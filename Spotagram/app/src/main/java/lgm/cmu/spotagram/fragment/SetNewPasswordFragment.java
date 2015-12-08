package lgm.cmu.spotagram.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import lgm.cmu.spotagram.Exception.InputErrorException;
import lgm.cmu.spotagram.R;
import lgm.cmu.spotagram.request.NewPasswordRequest;


public class SetNewPasswordFragment extends Fragment {
    EditText text1;
    EditText text2;
    EditText text3;
    EditText text4;
    Button button;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view=inflater.inflate(R.layout.fragment_set_new_password, container, false);

        text1=(EditText)view.findViewById(R.id.userID);
        text2=(EditText)view.findViewById(R.id.old_pw);
        text3=(EditText)view.findViewById(R.id.new_pw1);
        text4=(EditText)view.findViewById(R.id.new_pw2);
//        mContext=New_password_Activity.this;

        Button button = (Button)view.findViewById(R.id.new_psw_button);

        button.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                send_newPW();
            }
        });


        return view;
    }

    public void  send_newPW(){
        int userID;
        String old_password;
        String new_password1;
        String new_password2;
        try {
            userID = Integer.parseInt(text1.getText().toString());
        }catch (Exception e){
            text1.requestFocus();
            Toast.makeText(getActivity(), "User ID error, please retry", Toast.LENGTH_SHORT).show();
            return;
        }
        try{
            old_password = text2.getText().toString();
            if (old_password.length()<6){
                throw new InputErrorException(InputErrorException.ErrorType.WRONG_PASSWORD_FORM,"Please input an exact password");
            }
        }catch (InputErrorException e){
            text2.requestFocus();
            e.msgPrint();
            return;
        }

        try{
            new_password1=text3.getText().toString();
            new_password2=text4.getText().toString();
            if (new_password1.length()<6 || new_password2.length()<6){
                throw new InputErrorException(InputErrorException.ErrorType.WRONG_PASSWORD_FORM,"Please input a longer new password");
            }
        }catch (InputErrorException e){
            e.msgPrint();
            return;
        }

        if(!new_password1.equals(new_password2)){
            new AlertDialog.Builder(getActivity())
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
                            Toast.makeText(getActivity(), "Set new Password done", Toast.LENGTH_SHORT).show();
                        if(upload_status==-1)
                            Toast.makeText(getActivity(), "Failed setting new password", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            request.execute();
        }
    }

}
