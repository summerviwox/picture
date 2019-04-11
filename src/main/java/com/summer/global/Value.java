package com.summer.global;

import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by SWSD on 2018-04-03.
 */
public class Value {

    public static File getRecordFile(){
        File parent = new File("E://records");
        if(!parent.exists()){
            parent.mkdirs();
        }
        return parent;
    }

    public static File getTempFile(){
        File parent = new File("E://temp");
        if(!parent.exists()){
            parent.mkdirs();
        }
        return parent;
    }

    public static String  getString(InputStream inputStream ){
        String name = null;
        try {
            byte[] bytes = new byte[1024];
            int i=0;
            StringBuffer stringBuffer = new StringBuffer();
            while ((i=inputStream.read(bytes,0,1024))!=-1){
                stringBuffer.append(new String(bytes,0,i));
            }
            name = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

}
