import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import util.ConstantValue;

/**
 * Main.java 	Version <1.00>	ÏÂÎç8:45:34
 *
 * Copyright(C) 2015-2016  All rights reserved. 
 * Lei YU is a graduate student majoring in Electrical and Electronics Engineering, 
 * from the ECE department, Carnegie Mellon University, PA 15213, United States.
 *
 * Email: leiyu@andrew.cmu.edu
 */
public class RegisterMain {
	public static final String URL = "http://localhost/SpotagramServer/RegisterServlet";

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(URL);
            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_USER, "vip"));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_EMAIL, "jackqdyulei@gamil.com"));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_PWD, "jackqdyulei@gamil.com"));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_INFO, "jackqdyulei@gamil.com"));
            nvps.add(new BasicNameValuePair(ConstantValue.KEY_GENDER, "0"));
            
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response = httpclient.execute(httpPost);

            try {
                System.out.println(response.getStatusLine());
                HttpEntity entity2 = response.getEntity();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(entity2.getContent()));
                System.out.println(bufferedReader.readLine());
                bufferedReader.close();
            } finally {
                response.close();
            }
        } catch(Exception e) {
        	
        }	finally {
            try {
				httpclient.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}

}
