package lgm.cmu.spotagram.request;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import lgm.cmu.spotagram.request.util.RequestUtil;
import lgm.cmu.spotagram.utils.ConstantValue;

/**
 * Created by yulei on 2015/12/1.
 */
public class NewPasswordRequest extends  BasicRequest {
    public final static String TAG = "NewPasswordRequest";
    public final static String SUB_URL = "ChangePwdServlet";

    private int mUserId;
    private String old_password;
    private String new_password;


    public NewPasswordRequest(int mUserId,String old_pw, String new_pw) {
        this.mUserId = mUserId;
        this.old_password = old_pw;
        this.new_password = new_pw;

    }

    private OnNewPassWordListener mOnNewPwListener;

    @Override
    protected String doInBackground(String... params) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        String responce = "";
        try {
            HttpPost httpPost = new HttpPost(BASE_URL+SUB_URL);
            Log.e(TAG, BASE_URL + SUB_URL);
            List <NameValuePair> nvps = new ArrayList<NameValuePair>();

            nvps.add(new BasicNameValuePair(ConstantValue.KEY_USER_ID, mUserId+""));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_PWD, old_password+""));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_NEW_PWD, new_password));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            Log.v(mUserId + "", "AAAAAA");
            Log.v(old_password + "", "AAAAAA");
            Log.v(new_password + "", "AAAAAA");

            try {
                Log.e(TAG, "code: " + response.getStatusLine().getStatusCode()+"");
                if (response.getStatusLine().getStatusCode() == 200) {
                    mIsSuccess = true;
                }
                //System.out.println(response.getStatusLine());
                HttpEntity entity2 = response.getEntity();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity2.getContent()));
                responce = bufferedReader.readLine();
                bufferedReader.close();

                return responce;
            } finally {
                response.close();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }	finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return responce;
    }

    public void setOnNewPwReadyListener(OnNewPassWordListener lsn) {
        mOnNewPwListener = lsn;
    }

    @Override
    protected void onPostExecute(String s) {
        if (s == null || s.length() == 0) {
            mOnNewPwListener.onNewPwDone(false, -1);
        }
        String uplodeStatus=s;
        if (uplodeStatus.equals("{\"result\":0}")) {
            mOnNewPwListener.onNewPwDone(mIsSuccess, 0);
        } else {
            mOnNewPwListener.onNewPwDone(mIsSuccess, -1);
        }

    }

    public interface OnNewPassWordListener {
        void onNewPwDone(boolean isSuccess, int commentId);
    }
}
