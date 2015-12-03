package lgm.cmu.spotagram.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by MiaojunLi on 15/11/21.
 */
public class HttpUtil {
    public InputStream sendPost(String url, String params)throws IOException{
        URL realurl = null;
        InputStream in = null;
        HttpURLConnection conn = null;
        try {

            realurl = new URL(url);
            conn = (HttpURLConnection) realurl.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            PrintWriter pw = new PrintWriter(conn.getOutputStream());
            pw.print(params);
            pw.flush();
            pw.close();
            in = conn.getInputStream();
        } catch (MalformedURLException eio) {

        }
        return in;
    }
}
