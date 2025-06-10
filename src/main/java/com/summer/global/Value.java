package com.summer.global;

import com.summer.mybatis.entity.Record;
import com.summer.util.DateFormatUtil;
import com.summer.util.ThumbnailUtil;
import org.apache.commons.fileupload.servlet.ServletRequestContext;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;


/**
 * Created by SWSD on 2018-04-03.
 */
public class Value {

    public static File getRecordFile() {
        File parent = new File("X://records");
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
//        String path = "";
//        if (ori.startsWith(ThumbnailUtil.start2)) {
//            path = ori.substring(ThumbnailUtil.start2.length());
//        } else if (ori.startsWith(ThumbnailUtil.start)) {
//            path = ori.substring(ThumbnailUtil.start.length());
//        }
        String path = ori.substring(ori.indexOf("\\",ori.indexOf("\\")+1));
        File newfile = new File("X:\\records" + path);
        if(!newfile.getParentFile().exists()){
            newfile.getParentFile().mkdirs();
        }
        return  newfile;
    }

    public static File toWinddowsFile(String ori){
//        String path = "";
//        if (ori.startsWith(ThumbnailUtil.start2)) {
//            path = ori.substring(ThumbnailUtil.start2.length());
//        } else if (ori.startsWith(ThumbnailUtil.start)) {
//            path = ori.substring(ThumbnailUtil.start.length());
//        }
        String path = ori.substring(ori.indexOf("\\",ori.indexOf("\\")+1));
        File newfile = new File("X:\\records" + path);
        return  newfile;
    }

    public static File toThumbnailPath(String type,String ori){
//        String path = "";
//        if (ori.startsWith(ThumbnailUtil.start2)) {
//            path = ori.substring(ThumbnailUtil.start2.length());
//        } else if (ori.startsWith(ThumbnailUtil.start)) {
//            path = ori.substring(ThumbnailUtil.start.length());
//        }
        String path = ori.substring(ori.indexOf("\\",ori.indexOf("\\")+1));
        File newfile = null;
        if(type.equals("image")){
            newfile = new File("E:\\thumbnail" + path);
        }else{
            String newstr = path.substring(0,path.lastIndexOf("."));
            newfile = new File("E:\\thumbnail" + newstr+".jpg");
        }
        return  newfile;
    }

    public static File toThumbnailPathCreateParent(String type,String ori){
//        String path = "";
//        if (ori.startsWith(ThumbnailUtil.start2)) {
//            path = ori.substring(ThumbnailUtil.start2.length());
//        } else if (ori.startsWith(ThumbnailUtil.start)) {
//            path = ori.substring(ThumbnailUtil.start.length());
//        }
        String path = ori.substring(ori.indexOf("\\",ori.indexOf("\\")+1));
        File newfile = null;
        if(type.equals("image")){
            newfile = new File("E:\\thumbnail" + path);
        }else{
            String newstr = path.substring(0,path.lastIndexOf("."));
            newfile = new File("E:\\thumbnail" + newstr+".jpg");
        }
        if(!newfile.getParentFile().exists()){
            newfile.getParentFile().mkdirs();
        }
        return  newfile;
    }

    public static boolean isRecordFileExit(Record record){
        //再检查本地是否有文件 根据ctime
        File typeFile = new File(Value.getRecordFile(), DateFormatUtil.getdDateStr(DateFormatUtil.YYYYMMDD, new Date(record.getCtime())));
        if (!typeFile.exists()) {
            typeFile.mkdirs();
        }
        String[] strs = record.getLocpath().split("/");
        File file = new File(typeFile, strs[strs.length - 1]);
        return file.exists();
    }

    public static File getRecordFile(Record record){
        //再检查本地是否有文件 根据ctime
        File typeFile = new File(Value.getRecordFile(), DateFormatUtil.getdDateStr(DateFormatUtil.YYYYMMDD, new Date(record.getCtime())));
        if (!typeFile.exists()) {
            typeFile.mkdirs();
        }
        String[] strs = record.getLocpath().split("/");
        File file = new File(typeFile, strs[strs.length - 1]);
        return file;
    }
}
