package lgm.cmu.spotagram.request;

import android.os.AsyncTask;

/**
 * Created by yulei on 2015/12/1.
 */
public abstract class BasicRequest extends AsyncTask<String, Integer, String> {
    public static final String BASE_URL = "http://108.39.226.68/SpotagramServer/";
    protected boolean mIsSuccess = false;
}
