package com.summer.control;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.bean.Records;
import com.summer.global.Value;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Crash;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.entity.Tip;
import com.summer.mybatis.entity.Tiplab;
import com.summer.mybatis.mapper.*;
import com.summer.util.DateFormatUtil;
import com.summer.util.GsonUtil;
import com.summer.util.NullUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by SWSD on 2018-03-26.
 */

@Controller
@RequestMapping("/record")
public class RecordControl {

    @RequestMapping(value = "/crash",method = RequestMethod.POST)

    public void crash(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req,res);
        BaseResBean baseResBean = new BaseResBean();
        Crash crash = GsonUtil.getInstance().fromJson(map.get("data"),Crash.class);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
        crash.setTimestr(format.format(new Date()));        SqlSession session = DBTools.getSession();
        CrashMapper crashMapper =  session.getMapper(CrashMapper.class);
        crashMapper.insert(crash);
        session.commit();
        session.close();
        baseResBean.setData(true);
        Tools.printOut(res,baseResBean);
    }


    @RequestMapping(value = "/test",method = RequestMethod.GET)
    public void test(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        List<Record> records = recordMapper.selectAllByAtype("image");
        session.close();
        for(int i=0;i<records.size();i++){
            System.out.println(records.get(i).getId());
            if(records.get(i).getNetpath()==null){
                continue;
            }
            File file = null;
            if(records.get(i).getNetpath().startsWith("E:\\record\\")){

            }else{
                records.get(i).setNetpath("E:"+records.get(i).getNetpath());
            }
            file  = new File(records.get(i).getNetpath());
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdirs();
            }
            if(file.exists()){
                continue;
            }

            int bytesum = 0;
            int byteread = 0;
            String str = "";
            if(records.get(i).getNetpath().startsWith("E:\\record\\")){
                str = records.get(i).getNetpath().substring("E:\\record\\".length(),records.get(i).getNetpath().length());
            }else{
                str = records.get(i).getNetpath().substring("\\record\\".length(),records.get(i).getNetpath().length());
            }
            String ss = "http://106.14.161.168:8888/record/"+str.replace("\\","/");
            //String dir = file.getPath().substring(0,"E:\\record\\20180302".length());
            String[] strs = ss.split("/");
            String head = "";
            String name = "";
            for(int j=0;j<strs.length-1;j++){
                head+=strs[j]+"/";
            }
            try {
                name = URLEncoder.encode(strs[strs.length-1],"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            try {
                URL url = new URL(head+name);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                FileOutputStream fs = new FileOutputStream(records.get(i).getNetpath());

                byte[] buffer = new byte[1204];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread;
                    fs.write(buffer, 0, byteread);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            System.out.println(records.size()+"---"+i+"------->"+(head+name)+"\n"+file.getPath());

            System.out.println("------------------------------------------------------------------------------------------------------------------");
        }


    }


    @RequestMapping(value = "/test2",method = RequestMethod.GET)
    public void test2(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TestMapper recordMapper = session.getMapper(TestMapper.class);
        List<Record> records = recordMapper.selectSome(187);
        baseResBean.setData(records);
        Tools.printOut(res,baseResBean);
        session.close();
    }

    @RequestMapping(value = "/addRecordTipsInfo",method = RequestMethod.POST)
    public void addRecordTipsInfo(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        Record record = GsonUtil.getInstance().fromJson(req.getParameter("data"),Record.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        List<Tiplab> tiplabs = tiplabMapper.selectTipLabByContent(record.getTiplabs().get(0).getContent());

        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        //默认都已经上传服务器上有记录 如果没有记录 需要改写代码
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        if(records==null||records.size()==0){
            baseResBean.setData("");
            baseResBean.setErrorMessage("未上传");
            Tools.printOut(res,baseResBean);
            session.close();
            return;
        }
        Record r =  records.get(0);
        record.setId(r.getId());

        if(tiplabs!=null&&tiplabs.size()>0){
            //判断本记录是否已经存在这个标签
            ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.isTipExist(record.getId(),tiplabs.get(0).getId());
            if(tips==null|| tips.size()==0){
                Tip tip = new Tip();
                tip.setRecordid(record.getId());
                tip.setCtime(System.currentTimeMillis());
                tip.setTipid(tiplabs.get(0).getId());
                tipMapper.insert(tip);
                session.commit();
            }
        }else{
            Tiplab tiplab = new Tiplab();
            tiplab.setContent(record.getTiplabs().get(0).getContent());
            tiplab.setCtime(System.currentTimeMillis());

            tiplabMapper.insert(tiplab);
            int tiplabid =  tiplabMapper.selectTipLabByContent(tiplab.getContent()).get(0).getId();

            Tip tip = new Tip();
            tip.setRecordid(record.getId());
            tip.setCtime(System.currentTimeMillis());
            tip.setTipid(tiplabid);

            tipMapper.insert(tip);
            session.commit();
        }

        Tools.printOut(res,baseResBean);
        session.close();

    }


    @RequestMapping(value = "/addTextTipsInfo",method = RequestMethod.POST)
    public void addTextTipsInfo(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        Record record = GsonUtil.getInstance().fromJson(req.getParameter("data"),Record.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        List<Tiplab> tiplabs = tiplabMapper.selectTipLabByContent(record.getTiplabs().get(0).getContent());

        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        //默认都已经上传服务器上有记录 如果没有记录 需要改写代码

        if(tiplabs!=null&&tiplabs.size()>0){
            //判断本记录是否已经存在这个标签
            ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.isTipExist(record.getId(),tiplabs.get(0).getId());
            if(tips==null|| tips.size()==0){
                Tip tip = new Tip();
                tip.setRecordid(record.getId());
                tip.setCtime(System.currentTimeMillis());
                tip.setTipid(tiplabs.get(0).getId());
                tipMapper.insert(tip);
                session.commit();
            }
        }else{
            Tiplab tiplab = new Tiplab();
            tiplab.setContent(record.getTiplabs().get(0).getContent());
            tiplab.setCtime(System.currentTimeMillis());

            tiplabMapper.insert(tiplab);
            int tiplabid =  tiplabMapper.selectTipLabByContent(tiplab.getContent()).get(0).getId();

            Tip tip = new Tip();
            tip.setRecordid(record.getId());
            tip.setCtime(System.currentTimeMillis());
            tip.setTipid(tiplabid);

            tipMapper.insert(tip);
            session.commit();
        }

        Tools.printOut(res,baseResBean);
        session.close();

    }


    @RequestMapping(value = "/getAllRecords",method = RequestMethod.GET)
    public void getAllRecords(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        String atype = req.getParameter("atype");
        String startTime = req.getParameter("startTime");
        if(startTime==null){
            startTime = new Date(0).getTime()+"";
        }
        String endTime = req.getParameter("endTime");
        if(endTime==null){
            endTime = System.currentTimeMillis()+"";
        }
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        baseResBean.setData(recordMapper.selectAllByAtypeWithSE(atype,startTime,endTime));
        baseResBean.setOther(recordMapper.getRecordCountWithSE(atype,startTime,endTime)+"/"+recordMapper.getUploadNumWithSE(atype,startTime,endTime));
        Tools.printOut(res,baseResBean);
        session.close();
    }


    @RequestMapping(value = "/getRecordsWithSize",method = RequestMethod.GET)
    public void getRecordsWithSize(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        String index = req.getParameter("index");

        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        baseResBean.setData(recordMapper.selectRecordsWithSizeLimit(Integer.parseInt(index)*100));
        Tools.printOut(res,baseResBean);
        session.close();
    }


    @RequestMapping(value = "/getRecordsWithTypeSize",method = RequestMethod.GET)
    public void getRecordsWithTypeSize(HttpServletRequest req, HttpServletResponse res){

        Tools.init(req,res);

        String index = req.getParameter("index");
        String type = req.getParameter("type");
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        baseResBean.setData(recordMapper.selectRecordsWithTypeSizeLimit(type,Integer.parseInt(index)*100));
        Tools.printOut(res,baseResBean);
        session.close();
    }

    @RequestMapping(value = "/getMaxMinYear",method = RequestMethod.GET)
    public void getMaxMinYear(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        String atype = req.getParameter("atype");
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);

        int ma =0,mi=0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(recordMapper.getRecordMaxDate(atype)));
        ma = calendar.get(Calendar.YEAR);
        calendar.setTime(new Date(recordMapper.getRecordMinDate(atype)));
        mi = calendar.get(Calendar.YEAR);
        int[] ints = new int[]{mi,ma};
        baseResBean.setData(ints);
        Tools.printOut(res,baseResBean);
        session.close();
    }


    @RequestMapping(value = "/getMaxMinDateStamp",method = RequestMethod.GET)
    public void getMaxMinDateStamp(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);

        long[] ints = new long[]{recordMapper.getRecordMinDateStamp(),recordMapper.getRecordMaxDateStamp()};
        baseResBean.setData(ints);
        Tools.printOut(res,baseResBean);
        session.close();
    }




    @RequestMapping(value = "/getAllRecordsStep",method = RequestMethod.GET)
    public void getAllRecordsStep(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        String atype = req.getParameter("atype");
        String index = req.getParameter("pageindex");
        String startTime = req.getParameter("startTime");
        if(startTime==null){
            startTime = new Date(0).getTime()+"";
        }
        String endTime = req.getParameter("endTime");
        if(endTime==null){
            endTime = System.currentTimeMillis()+"";
        }
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        int a = Integer.parseInt(index);
        int count = recordMapper.getRecordCount(atype);
        int offset = count-(a+1)*16;
        if(offset<0){
            offset=0;
        }
        baseResBean.setData(recordMapper.selectAllByAtypeStep(atype,offset));
        baseResBean.setOther(recordMapper.getRecordCountWithSE(atype,startTime,endTime)+"/"+recordMapper.getUploadNumWithSE(atype,startTime,endTime));
        Tools.printOut(res,baseResBean);
        session.close();
    }


    @RequestMapping(value = "/getAllRecordsStepwithLimit",method = RequestMethod.GET)
    public void getAllRecordsStepwithLimit(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        String atype = req.getParameter("atype");
        String limit = req.getParameter("limit");
        String index = req.getParameter("pageindex");
        String startTime = req.getParameter("startTime");
        if(startTime==null){
            startTime = new Date(0).getTime()+"";
        }
        String endTime = req.getParameter("endTime");
        if(endTime==null){
            endTime = System.currentTimeMillis()+"";
        }
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        int a = Integer.parseInt(index);
        int count = recordMapper.getRecordCount(atype);
        int lim = Integer.parseInt(limit);
        int offset = count-(a+1)*lim;
        if(offset<0){
            offset=0;
        }
        baseResBean.setData(recordMapper.selectAllByAtypeStepLimit(atype,lim,offset));
        baseResBean.setOther(recordMapper.getRecordCountWithSE(atype,startTime,endTime)+"/"+recordMapper.getUploadNumWithSE(atype,startTime,endTime));
        Tools.printOut(res,baseResBean);
        session.close();
    }


    @RequestMapping(value = "/getRecordInfo",method = RequestMethod.GET)
    public void getRecordInfo(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        String atype = req.getParameter("atype");
        String startTime = req.getParameter("startTime");
        if(startTime==null){
            startTime = new Date(0).getTime()+"";
        }
        String endTime = req.getParameter("endTime");
        if(endTime==null){
            endTime = System.currentTimeMillis()+"";
        }
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        Records records = new Records();
        records.setAllNum(recordMapper.getRecordCountWithSE(atype,startTime,endTime));
        records.setDoneNum(recordMapper.getUploadNumWithSE(atype,startTime,endTime));
        baseResBean.setData(records);
        Tools.printOut(res,baseResBean);
        session.close();
    }

    @RequestMapping(value = "/updateRecords",method = RequestMethod.POST)
    public void updateRecords(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        ArrayList<Record> records = GsonUtil.getInstance().fromJson(req.getParameter("data"), new TypeToken<ArrayList<Record>>(){}.getType());
        ArrayList<Record> list = new ArrayList<>();
        SqlSession session  =  DBTools.getSession();
        for(int i=0;records!=null && i<records.size();i++){
            RecordMapper recordMapper = session.getMapper(RecordMapper.class);
            ArrayList<Record> selects = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(records.get(i).getLocpath());
            if(selects!=null&&selects.size()>0){
               if(NullUtil.isStrEmpty(selects.get(0).getNetpath())){
                   list.add(records.get(i));
               }else{
                   File file = new File(selects.get(0).getNetpath());
                   if(!file.exists()){
                       list.add(records.get(i));
                   }
               }
                continue;
            }
            recordMapper.insert(records.get(i));
            list.add(records.get(i));
        }
        session.commit();
        session.close();
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(list);
        Tools.printOut(res,baseResBean);
    }


    @RequestMapping(value = "/isRecordUploaded",method = RequestMethod.POST)
    public void isRecordUploaded(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        Record record = GsonUtil.getInstance().fromJson(req.getParameter("data"),Record.class);
        SqlSession session  =  DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        BaseResBean baseResBean = new BaseResBean();
        if(record==null||records.size()==0){
            Record record1 = new Record();
            record1.setIsUploaded(0);
            baseResBean.setData(record1);
        }else{
            File typeFile = new File(Value.getRecordFile(), DateFormatUtil.getdDateStr(DateFormatUtil.YYYYMMDD,new Date(records.get(0).getCtime())));
            String[] strs = record.getLocpath().split("/");
            File file = new File(typeFile,strs[strs.length-1]);
            if(NullUtil.isStrEmpty(records.get(0).getNetpath())||!file.exists()){
                Record record1 = new Record();
                record1.setIsUploaded(0);
                baseResBean.setData(record1);
            }else{
                Record record1 = new Record();
                record1.setIsUploaded(1);
                baseResBean.setData(record1);
            }
        }
        Tools.printOut(res,baseResBean);
        session.commit();
        session.close();
    }


    @RequestMapping(value = "/uploadRecords",method = RequestMethod.POST)
    public void uploadRecords(HttpServletRequest req, HttpServletResponse rep){
        Tools.init(req,rep);
        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(Value.getTempFile());
        factory.setSizeThreshold(1024*1024);
        ServletFileUpload upload = new ServletFileUpload(factory);
        BaseResBean baseResBean = new BaseResBean();
        ArrayList<String> files = new ArrayList<String>();
        try {
            ArrayList<FileItem> list = (ArrayList<FileItem>) upload.parseRequest(req);
            String local = null;
            for(int i=0;i<list.size();i++){
                switch (list.get(i).getFieldName()){
                    case "file":

                        break;
                    case "locpath":
                        local = list.get(i).getString();
                        break;
                }

            }

            for(int i=0;i<list.size();i++){
                switch (list.get(i).getFieldName()){
                    case "file":
                        if(local==null){
                            break;
                        }
                        SqlSession session = DBTools.getSession();
                        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
                        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(local);
                        //数据库有此条记录
                        if(records!=null&&records.size()==1){
                            File typeFile = new File(Value.getRecordFile(), DateFormatUtil.getdDateStr(DateFormatUtil.YYYYMMDD,new Date(records.get(0).getCtime())));
                            if(!typeFile.exists()){
                                typeFile.mkdirs();
                            }
                            String[] strs = local.split("/");

                            File file = new File(typeFile,strs[strs.length-1]);
                            System.out.println(file.getPath());
                            files.add(file.getPath());
                            //本地没有该文件
                            if(!file.exists()){
                                list.get(i).write(file);
                                list.get(i).delete();
                                recordMapper.updateNetPath(file.getPath(),local);
                                session.commit();
                            }
                        }
                        session.close();
                        break;
                    case "locpath":

                        break;
                }
            }
            baseResBean.setData(files);

        } catch (FileUploadException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Tools.printOut(rep,baseResBean);
    }



}
