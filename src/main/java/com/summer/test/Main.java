package com.summer.test;

import com.summer.base.bean.Tools;
import com.summer.global.Value;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.mapper.RecordMapper;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    //IMG_20171022_202348
    public static void main() throws ParseException, IOException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        //IMG_20171022_202348
        List<Record> records = recordMapper.getLikeRecordsIMG_X_X();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            if(records.get(i).getId()==9956){
                System.out.println("yes");
            }
            long time = simpleDateFormat.parse(records.get(i).getName().split("\\.")[0].replace("IMG_","")).getTime();
            if(time!=0){
                int line = recordMapper.updateCtime(records.get(i).getId(),time);
                if(line==1){
                    System.out.println("更新ctime完成--->"+records.get(i).getName());
                    File oldf = Value.toWinddowsFile(records.get(i).getNetpath());
                    records.get(i).setCtime(time);
                    File newf = Value.getRecordFile(records.get(i));//新的根据ctime实际的文件
                    if(oldf.getPath().equals(newf.getPath())){
                        System.out.println("时间格式正确 无需更新");
                    }else{
                        FileUtils.copyFile(oldf,newf);
                        System.out.println("拷贝完成"+oldf.getPath() +"--->"+newf.getPath());
                        boolean de = oldf.delete();
                        if(de){
                            System.out.println("删除完成");
                        }else{
                            System.out.println("删除错误");
                        }
                        int line2 = recordMapper.updateNetPath(newf.getPath(),records.get(i).getLocpath());
                        if(line2==1){
                            System.out.println("更新netpath完成");
                        }else{
                            System.out.println("updateNetPath错误");
                        }
                    }
                }else{
                    System.out.println("updateCtime错误");
                }
            }else{
                System.out.println("格式时间错误");
            }
            sqlSession.commit();
            System.out.println("------------------------------------------------------------------");
        }
        sqlSession.close();
    }


    //mmexport1508133142029
    public static void main2() throws ParseException, IOException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        //IMG_20171022_202348
        List<Record> records = recordMapper.getLikemmexportX();
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            if(records.get(i).getId()==9956){
                System.out.println("yes");
            }
            long time =Long.parseLong(records.get(i).getName().split("\\.")[0].replace("mmexport",""));
            if(time!=0){
                int line = recordMapper.updateCtime(records.get(i).getId(),time);
                if(line==1){
                   // System.out.println("更新ctime完成--->"+records.get(i).getName());
                    File oldf = Value.toWinddowsFile(records.get(i).getNetpath());
                    records.get(i).setCtime(time);
                    File newf = Value.getRecordFile(records.get(i));//新的根据ctime实际的文件
                    if(oldf.getPath().equals(newf.getPath())){
                       // System.out.println("时间格式正确 无需更新");
                    }else{
                        FileUtils.copyFile(oldf,newf);
                      // System.out.println("拷贝完成"+oldf.getPath() +"--->"+newf.getPath());
                        boolean de = oldf.delete();
                        if(de){
                          //  System.out.println("删除完成");
                        }else{
                            System.out.println("删除错误");
                        }
                        int line2 = recordMapper.updateNetPath(newf.getPath(),records.get(i).getLocpath());
                        if(line2==1){
                         //   System.out.println("更新netpath完成");
                        }else{
                            System.out.println("updateNetPath错误");
                        }
                    }
                }else{
                    System.out.println("updateCtime错误");
                }
            }else{
                System.out.println("格式时间错误");
            }
            sqlSession.commit();
            System.out.println("------------------------------------------------------------------");
        }
        sqlSession.close();
    }


    //VID_20190325_214936.mp4
    public static void main3() throws ParseException, IOException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        List<Record> records = recordMapper.getLikeRecordsVID_X_X();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            long time = simpleDateFormat.parse(records.get(i).getName().split("\\.")[0].replace("VID_","")).getTime();
            if(time!=0){
                int line = recordMapper.updateCtime(records.get(i).getId(),time);
                if(line==1){
                    //System.out.println("更新ctime完成--->"+records.get(i).getName());
                    File oldf = Value.toWinddowsFile(records.get(i).getNetpath());
                    records.get(i).setCtime(time);
                    File newf = Value.getRecordFile(records.get(i));//新的根据ctime实际的文件
                    if(oldf.getPath().equals(newf.getPath())){
                        //System.out.println("时间格式正确 无需更新");
                    }else{
                        FileUtils.copyFile(oldf,newf);
                        System.out.println("拷贝完成"+oldf.getPath() +"--->"+newf.getPath());
                        boolean de = oldf.delete();
                        if(de){
                            //System.out.println("删除完成");
                        }else{
                            System.out.println("删除错误");
                        }
                        int line2 = recordMapper.updateNetPath(newf.getPath(),records.get(i).getLocpath());
                        if(line2==1){
                            //System.out.println("更新netpath完成");
                        }else{
                            System.out.println("updateNetPath错误");
                        }
                    }
                }else{
                    System.out.println("updateCtime错误");
                }
            }else{
                System.out.println("格式时间错误");
            }
            sqlSession.commit();
            System.out.println("------------------------------------------------------------------");
        }
        sqlSession.close();
    }


    //  wx_camera_1552819550345.mp4
    public static void main4() throws ParseException, IOException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        //IMG_20171022_202348
        List<Record> records = recordMapper.getLikeRecordswx_camera_();
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            if(records.get(i).getId()==9956){
                System.out.println("yes");
            }
            long time =Long.parseLong(records.get(i).getName().split("\\.")[0].replace("wx_camera_",""));
            if(time!=0){
                int line = recordMapper.updateCtime(records.get(i).getId(),time);
                if(line==1){
                    // System.out.println("更新ctime完成--->"+records.get(i).getName());
                    File oldf = Value.toWinddowsFile(records.get(i).getNetpath());
                    records.get(i).setCtime(time);
                    File newf = Value.getRecordFile(records.get(i));//新的根据ctime实际的文件
                    if(oldf.getPath().equals(newf.getPath())){
                        // System.out.println("时间格式正确 无需更新");
                    }else{
                        FileUtils.copyFile(oldf,newf);
                        // System.out.println("拷贝完成"+oldf.getPath() +"--->"+newf.getPath());
                        boolean de = oldf.delete();
                        if(de){
                            //  System.out.println("删除完成");
                        }else{
                            System.out.println("删除错误");
                        }
                        int line2 = recordMapper.updateNetPath(newf.getPath(),records.get(i).getLocpath());
                        if(line2==1){
                            //   System.out.println("更新netpath完成");
                        }else{
                            System.out.println("updateNetPath错误");
                        }
                    }
                }else{
                    System.out.println("updateCtime错误");
                }
            }else{
                System.out.println("格式时间错误");
            }
            sqlSession.commit();
            System.out.println("------------------------------------------------------------------");
        }
        sqlSession.close();
    }


    //  1553584966468.jpg
    public static void main5() throws ParseException, IOException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        //IMG_20171022_202348
        List<Record> records = recordMapper.getLikeRecordsnum();
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            long time = 0;
            try {
                time = Long.parseLong(records.get(i).getName().split("\\.")[0]);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if(time!=0){
                int line = recordMapper.updateCtime(records.get(i).getId(),time);
                if(line==1){
                    // System.out.println("更新ctime完成--->"+records.get(i).getName());
                    File oldf = Value.toWinddowsFile(records.get(i).getNetpath());
                    records.get(i).setCtime(time);
                    File newf = Value.getRecordFile(records.get(i));//新的根据ctime实际的文件
                    if(oldf.getPath().equals(newf.getPath())){
                        // System.out.println("时间格式正确 无需更新");
                    }else{
                        FileUtils.copyFile(oldf,newf);
                        // System.out.println("拷贝完成"+oldf.getPath() +"--->"+newf.getPath());
                        boolean de = oldf.delete();
                        if(de){
                            //  System.out.println("删除完成");
                        }else{
                            System.out.println("删除错误");
                        }
                        int line2 = recordMapper.updateNetPath(newf.getPath(),records.get(i).getLocpath());
                        if(line2==1){
                            //   System.out.println("更新netpath完成");
                        }else{
                            System.out.println("updateNetPath错误");
                        }
                    }
                }else{
                    System.out.println("updateCtime错误");
                }
            }else{
                System.out.println("格式时间错误");
            }
            sqlSession.commit();
            System.out.println("------------------------------------------------------------------");
        }
        sqlSession.close();
    }


    //PANO_20190323_173548.jpg
    public static void main6() throws ParseException, IOException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        List<Record> records = recordMapper.getLikeRecordspano();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            long time = simpleDateFormat.parse(records.get(i).getName().split("\\.")[0].replace("PANO_","")).getTime();
            if(time!=0){
                int line = recordMapper.updateCtime(records.get(i).getId(),time);
                if(line==1){
                    //System.out.println("更新ctime完成--->"+records.get(i).getName());
                    File oldf = Value.toWinddowsFile(records.get(i).getNetpath());
                    records.get(i).setCtime(time);
                    File newf = Value.getRecordFile(records.get(i));//新的根据ctime实际的文件
                    if(oldf.getPath().equals(newf.getPath())){
                        //System.out.println("时间格式正确 无需更新");
                    }else{
                        FileUtils.copyFile(oldf,newf);
                        System.out.println("拷贝完成"+oldf.getPath() +"--->"+newf.getPath());
                        boolean de = oldf.delete();
                        if(de){
                            //System.out.println("删除完成");
                        }else{
                            System.out.println("删除错误");
                        }
                        int line2 = recordMapper.updateNetPath(newf.getPath(),records.get(i).getLocpath());
                        if(line2==1){
                            //System.out.println("更新netpath完成");
                        }else{
                            System.out.println("updateNetPath错误");
                        }
                    }
                }else{
                    System.out.println("updateCtime错误");
                }
            }else{
                System.out.println("格式时间错误");
            }
            sqlSession.commit();
            System.out.println("------------------------------------------------------------------");
        }
        sqlSession.close();
    }

    //PANO_20190323_173548.jpg
    public static void main7() throws ParseException, IOException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        List<Record> records = recordMapper.getLikeRecordsScreenshot();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            long time = simpleDateFormat.parse(records.get(i).getName().substring("Screenshot_".length(),"Screenshot_2019-03-27-20-20-44".length())).getTime();
            if(time!=0){
                int line = recordMapper.updateCtime(records.get(i).getId(),time);
                if(line==1){
                    //System.out.println("更新ctime完成--->"+records.get(i).getName());
                    File oldf = Value.toWinddowsFile(records.get(i).getNetpath());
                    records.get(i).setCtime(time);
                    File newf = Value.getRecordFile(records.get(i));//新的根据ctime实际的文件
                    if(oldf.getPath().equals(newf.getPath())){
                        //System.out.println("时间格式正确 无需更新");
                    }else{
                        FileUtils.copyFile(oldf,newf);
                        System.out.println("拷贝完成"+oldf.getPath() +"--->"+newf.getPath());
                        boolean de = oldf.delete();
                        if(de){
                            //System.out.println("删除完成");
                        }else{
                            System.out.println("删除错误");
                        }
                        int line2 = recordMapper.updateNetPath(newf.getPath(),records.get(i).getLocpath());
                        if(line2==1){
                            //System.out.println("更新netpath完成");
                        }else{
                            System.out.println("updateNetPath错误");
                        }
                    }
                }else{
                    System.out.println("updateCtime错误");
                }
            }else{
                System.out.println("格式时间错误");
            }
            sqlSession.commit();
            System.out.println("------------------------------------------------------------------");
        }
        sqlSession.close();
    }
}
