package com.summer.control;

import com.summer.base.OnFinishI;
import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.global.Value;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.util.DateFormatUtil;
import com.summer.util.GsonUtil;
import com.summer.util.ThumbnailUtil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

@Controller
@RequestMapping("/picture")
public class PictureControl {


    @RequestMapping(value = "/isPictureUploaded",method = RequestMethod.POST)
    public void isPictureUploaded(HttpServletRequest req, HttpServletResponse rep){
        HashMap<String,String> data =Tools.getStr(req,rep);
        Record record = GsonUtil.getInstance().fromJson(data.get("data"),Record.class);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        //从数据库中查找是否有这条记录
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        //没有此条记录 则插入这条记录
        if(records==null||records.size()==0){
            recordMapper.insert(record);
        }
        //再检查本地是否有文件
        File typeFile = new File(Value.getRecordFile(), DateFormatUtil.getdDateStr(DateFormatUtil.YYYYMMDD,new Date(record.getCtime())));
        if(!typeFile.exists()){
            typeFile.mkdirs();
        }
        String[] strs = record.getLocpath().split("/");
        File file = new File(typeFile,strs[strs.length-1]);
        session.commit();
        session.close();
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(file.exists());
        Tools.printOut(rep,baseResBean);
    }


    @RequestMapping(value = "/uploadPicture",method = RequestMethod.POST)
    public void uploadRecords(HttpServletRequest req, HttpServletResponse rep){
        Tools.init(req,rep);
        Record record = GsonUtil.getInstance().fromJson(req.getParameter("data"),Record.class);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        //从数据库中查找是否有这条记录
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        //没有此条记录 则插入这条记录
        if(records==null||records.size()==0){
            recordMapper.insert(record);
        }
        //再检查本地是否有文件
        File typeFile = new File(Value.getRecordFile(), DateFormatUtil.getdDateStr(DateFormatUtil.YYYYMMDD,new Date(record.getCtime())));
        if(!typeFile.exists()){
            typeFile.mkdirs();
        }
        String[] strs = record.getLocpath().split("/");
        File file = new File(typeFile,strs[strs.length-1]);
        ArrayList<String> files = new ArrayList<String>();
        System.out.println(file.getPath());
        //本地没有该文件
        if(!file.exists()){
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(Value.getTempFile());
            factory.setSizeThreshold(1024*1024);
            ServletFileUpload upload = new ServletFileUpload(factory);
            BaseResBean baseResBean = new BaseResBean();
            ArrayList<FileItem> list = null;
            try {
                list = (ArrayList<FileItem>) upload.parseRequest(req);
                list.get(0).write(file);
                list.get(0).delete();
                recordMapper.updateNetPath(file.getPath(),record.getLocpath());
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        if(records.get(0).getNetpath()==null){
            recordMapper.updateNetPath(file.getPath(),record.getLocpath());
        }
        record.setNetpath(file.getPath());
        //生成缩略图
        ThumbnailUtil.simglezoomImageScale(record);
        session.commit();
        session.close();
        files.add(file.getPath());
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(files);
        Tools.printOut(rep,baseResBean);
    }


    @RequestMapping(value = "/getAllPictures",method = RequestMethod.GET)
    public void getAllPictures(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
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
        baseResBean.setData(recordMapper.selectAllWithSE(startTime,endTime));
        Tools.printOut(res,baseResBean);
        session.close();
    }


    @RequestMapping(value = "/thumbnail",method = RequestMethod.GET)
    public void thumbnail(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        String startTime = req.getParameter("startTime");
        if(startTime==null){
            startTime = new Date(0).getTime()+"";
        }
        String endTime = req.getParameter("endTime");
        if(endTime==null){
            endTime = System.currentTimeMillis()+"";
        }
        SqlSession session  =  DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ThumbnailUtil.zoomImagesScale((ArrayList<Record>) recordMapper.selectAll());
        Tools.printOut(res,"");
        session.close();
    }
}
