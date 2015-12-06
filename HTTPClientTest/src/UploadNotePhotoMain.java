import java.io.BufferedReader;
import java.io.File;
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
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
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
public class UploadNotePhotoMain {
	public static final String URL = "http://localhost/SpotagramServer/UploadNewPhotoServlet";
	public static String imageName = "tmp.png";
	public static String imagePath = "D:\\" + imageName;

	// First, you should have a picture whose path is D:\\tmp.png!!
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            HttpPost httpPost = new HttpPost(URL);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            
            File file = new File(imagePath);
            if (file.exists()) {
            	System.out.println("exist");
            } else {
            	return;
            }
            
            builder.addPart(ConstantValue.KEY_NOTE_PHOTO, new FileBody(new File(imagePath)));
            builder.addPart(ConstantValue.KEY_NOTE_ID, new StringBody(1+"", ContentType.TEXT_PLAIN));
            builder.addPart(ConstantValue.KEY_FILE_NAME, new StringBody(imageName, ContentType.TEXT_PLAIN));
            
            HttpEntity httpEntity = builder.build();
            
            httpPost.setEntity(httpEntity);
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
