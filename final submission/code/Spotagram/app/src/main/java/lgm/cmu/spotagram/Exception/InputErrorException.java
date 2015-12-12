package lgm.cmu.spotagram.Exception;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by MiaojunLi on 15/12/5.
 */
public class InputErrorException extends Exception{
    ErrorType errorType;
    String msg;
    public InputErrorException(ErrorType errorType, String msg){
        this.errorType=errorType;
        this.msg=msg;
    }

    public enum ErrorType{
        MISSED_INPUT,
        WRONG_PASSWORD_FORM,
        WRONG_EMAIL_FORM,
    }

    public void msgPrint(){
        Log.v(errorType.toString(),msg);
        System.out.println(msg);
    }

    public String getmsg(){
        return msg;
    }

    public void execHandle(Context mcontext){
        Toast.makeText(mcontext, this.getmsg(),
                Toast.LENGTH_SHORT).show();
    }
}
