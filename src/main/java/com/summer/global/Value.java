package com.summer.global;

import com.summer.util.ThumbnailUtil;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static com.summer.util.ThumbnailUtil.start2;

/**
 * Created by SWSD on 2018-04-03.
 */
public class Value {

    public static File getRecordFile() {
        File parent = new File("E://records");
        if (!parent.exists()) {
            parent.mkdirs();
        }
        return parent;
    }

    public static File getTempFile() {
        File parent = new File("E://temp");
        if (!parent.exists()) {
            parent.mkdirs();
        }
        return parent;
    }

    public static String getString(InputStream inputStream) {
        String name = null;
        try {
            byte[] bytes = new byte[1024];
            int i = 0;
            StringBuffer stringBuffer = new StringBuffer();
            while ((i = inputStream.read(bytes, 0, 1024)) != -1) {
                stringBuffer.append(new String(bytes, 0, i));
            }
            name = stringBuffer.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }
    public static File toWinddowsFileCreateParent(String ori){
        String path = "";
        if (ori.startsWith(ThumbnailUtil.start2)) {
            path = ori.substring(ThumbnailUtil.start2.length());
        } else if (ori.startsWith(ThumbnailUtil.start)) {
            path = ori.substring(ThumbnailUtil.start.length());
        }
        File newfile = new File("E:\\records" + path);
        if(!newfile.getParentFile().exists()){
            newfile.getParentFile().mkdirs();
        }
        return  newfile;
    }

    public static File toWinddowsFile(String ori){
        String path = "";
        if (ori.startsWith(ThumbnailUtil.start2)) {
            path = ori.substring(ThumbnailUtil.start2.length());
        } else if (ori.startsWith(ThumbnailUtil.start)) {
            path = ori.substring(ThumbnailUtil.start.length());
        }
        File newfile = new File("E:\\records" + path);
        return  newfile;
    }

    public static File toThumbnailPath(String type,String ori){
        String path = "";
        if (ori.startsWith(ThumbnailUtil.start2)) {
            path = ori.substring(ThumbnailUtil.start2.length());
        } else if (ori.startsWith(ThumbnailUtil.start)) {
            path = ori.substring(ThumbnailUtil.start.length());
        }
        File newfile = null;
        if(type.equals("image")){
            newfile = new File("E:\\thumbnail" + path);
        }else{
            String newstr = path.substring(0,path.lastIndexOf(".")-1);
            newfile = new File("E:\\thumbnail" + newstr+".jpg");
        }
        return  newfile;
    }

    public static File toThumbnailPathCreateParent(String type,String ori){
        String path = "";
        if (ori.startsWith(ThumbnailUtil.start2)) {
            path = ori.substring(ThumbnailUtil.start2.length());
        } else if (ori.startsWith(ThumbnailUtil.start)) {
            path = ori.substring(ThumbnailUtil.start.length());
        }
        File newfile = null;
        if(type.equals("image")){
            newfile = new File("E:\\thumbnail" + path);
        }else{
            String newstr = path.substring(0,path.lastIndexOf(".")-1);
            newfile = new File("E:\\thumbnail" + newstr+".jpg");
        }
        if(!newfile.getParentFile().exists()){
            newfile.getParentFile().mkdirs();
        }
        return  newfile;
    }
}
