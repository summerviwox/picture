package com.summer.test;

import com.summer.base.bean.Tools;
import com.summer.global.Value;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.util.ThumbnailUtil;
import org.apache.commons.io.FileUtils;
import org.apache.ibatis.session.SqlSession;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    public static void mainmain() throws IOException, ParseException {
        SqlSession sqlSession = DBTools.getSession();
        RecordMapper recordMapper = sqlSession.getMapper(RecordMapper.class);
        main1(sqlSession,recordMapper);
        main2(sqlSession,recordMapper);
        main3(sqlSession,recordMapper);
        main4(sqlSession,recordMapper);
        main5(sqlSession,recordMapper);


//        List<Record> records = recordMapper.selectAll();
//        for(int i=0;i<records.size();i++){
//            try {
//                ThumbnailUtil.simglezoomImageScale(records.get(i));
//            } catch (IOException e) {
//                e.printStackTrace();
//                continue;
//            }
//        }
        sqlSession.close();
    }



    // wx_camera_1552819550345.mp4
    //粘贴图片(5)_1543773874053.png
    public static void main1(SqlSession sqlSession,RecordMapper recordMapper) throws ParseException, IOException {
        List<Record> records = recordMapper.getLikeRecordsX_Timestem();
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            String[] strs = records.get(i).getName().split("\\.")[0].split("_");
            String tt = "0";
            for(int j=0;j<strs.length;j++){
                if(strs[j].length()==13&&strs[j].startsWith("15")){
                    tt = strs[j];
                    break;
                }
            }
            long time =Long.parseLong(tt);
            deal(time,recordMapper,records.get(i),sqlSession);
            System.out.println("------------------------------------------------------------------");
        }
    }


    //JPEG_20181205_133352.jpg
    //PANO_20190323_173548.jpg
    //IMG_20171022_202348.jpg
    //VID_20190325_214936.mp4
    public static void main2(SqlSession sqlSession,RecordMapper recordMapper) throws ParseException, IOException {
        List<Record> records = recordMapper.getLikeRecordsX_X_X();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            String head = records.get(i).getName().split("\\.")[0];
            long time = simpleDateFormat.parse(head.substring(head.length()-15,head.length())).getTime();
            deal(time,recordMapper,records.get(i),sqlSession);
            System.out.println("------------------------------------------------------------------");
        }
    }

    //Screenshot_2019-03-27-20-20-44-381_com.tencent.mm.png
    //Screenrecorder-2019-01-19-10-07-51-183.mp4
    public static void main3(SqlSession sqlSession,RecordMapper recordMapper) throws ParseException, IOException {
        List<Record> records = recordMapper.getLikeRecordsScreenshot();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            int a = records.get(i).getName().indexOf("201");
            long time = simpleDateFormat.parse(records.get(i).getName().substring(a,a+"2019-01-19-10-07-51".length())).getTime();
            deal(time,recordMapper,records.get(i),sqlSession);
            System.out.println("------------------------------------------------------------------");
        }
    }


    //  1553584966468.jpg
    public static void main4(SqlSession sqlSession,RecordMapper recordMapper) throws ParseException, IOException {
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
            deal(time,recordMapper,records.get(i),sqlSession);
            System.out.println("------------------------------------------------------------------");
        }
    }


    //mmexport1508133142029
    public static void main5(SqlSession sqlSession,RecordMapper recordMapper) throws ParseException, IOException {
        //IMG_20171022_202348
        List<Record> records = recordMapper.getLikemmexportX();
        for(int i=0;i<records.size();i++){
            System.out.println("------------------------------------------------------------------");
            System.out.println("处理"+records.get(i).getId());
            if(records.get(i).getId()==9956){
                System.out.println("yes");
            }
            long time =Long.parseLong(records.get(i).getName().split("\\.")[0].replace("mmexport",""));
            deal(time,recordMapper,records.get(i),sqlSession);
            System.out.println("------------------------------------------------------------------");
        }
    }












    public static void deal(long time,RecordMapper recordMapper,Record record,SqlSession sqlSession) throws IOException {
        if(time!=0){
            int line = recordMapper.updateCtime(record.getId(),time);
            if(line==1){
                //System.out.println("更新ctime完成--->"+records.get(i).getName());
                File oldf = Value.toWinddowsFile(record.getNetpath());
                record.setCtime(time);
                File newf = Value.getRecordFile(record);//新的根据ctime实际的文件
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
                    int line2 = recordMapper.updateNetPath(newf.getPath(),record.getLocpath());
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
    }
}
