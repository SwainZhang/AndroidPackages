package com.emery.test.net;

import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/**
 * Created by MyPC on 2017/2/21.
 */

public class FileHttpUpload {
    public static String postHttp(String url, File file, Map<String,String> params) throws IOException {
        String BOUNDARY=java.util.UUID.randomUUID().toString();
        String PREFIX="--";
        String LINEEND="\r\n";
        String MULTIPART_FROM_DATA="multipart/form-data";
        String CHARSET="UTF-8";
        URL uri=new URL(url);
        HttpURLConnection connection = (HttpURLConnection) uri.openConnection();
        connection.setReadTimeout(5*1000);
        connection.setDoInput(true);
        connection.setDoOutput(true);
        connection.setUseCaches(false);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("connection","keep-alive");
        connection.setRequestProperty("charset",CHARSET);
        connection.setRequestProperty("content-type",MULTIPART_FROM_DATA+"; boundary="+BOUNDARY);

        /*--------------- 文件实体 --------begin-------*/
        DataOutputStream out=new DataOutputStream(connection.getOutputStream());
        if(!TextUtils.isEmpty(file.getAbsolutePath())){
            StringBuilder builder=new StringBuilder();
            builder.append(PREFIX);
            builder.append(BOUNDARY);
            builder.append(LINEEND);
            builder.append("content-disposition: form-data;name=\"file\";filename=\""+file.getAbsolutePath()+"\""+LINEEND);
            builder.append("content-type: application/octet-stream; charset="+CHARSET+LINEEND);
            builder.append(LINEEND);

            out.write(builder.toString().getBytes());
            FileInputStream fin=new FileInputStream(file);
            byte[] buf=new byte[1024];
            int len=0;
            while((len=fin.read(buf))!=-1){
                out.write(buf,0,len);
            }
            fin.close();
            out.write(LINEEND.getBytes());
        }
        byte[] end_data=(PREFIX+BOUNDARY+PREFIX+LINEEND).getBytes();
        out.write(end_data);
        /*--------------- 文件实体 --------end-------*/
        out.flush();
        int responseCode = connection.getResponseCode();
        String result="";
        if(responseCode==200){
            StringBuilder builder=new StringBuilder();
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line=null;
            while((line=reader.readLine())!=null){
                builder.append(line);
            }
            result=builder.toString();
        }

        return result;
    }

}
