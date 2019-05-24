package com.summer.control;

import com.google.gson.reflect.TypeToken;
import com.summer.base.OnFinishI;
import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.global.Value;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.mapper.FileMapper;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.util.DateFormatUtil;
import com.summer.util.GsonUtil;
import com.summer.util.ThumbnailUtil;
import com.sun.prism.impl.Disposer;
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
import java.util.List;

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
        String netpath = null;
        //没有此条记录 则插入这条记录
        if(records==null||records.size()==0){
            record.setCtype(0);
            recordMapper.insert(record);
        }else{
            netpath = records.get(0).getNetpath();
        }
        //再检查本地是否有文件
        File typeFile = new File(Value.getRecordFile(), DateFormatUtil.getdDateStr(DateFormatUtil.YYYYMMDD,new Date(record.getCtime())));
        if(!typeFile.exists()){
            typeFile.mkdirs();
        }
        String[] strs = record.getLocpath().split("/");
        File file = new File(typeFile,strs[strs.length-1]);
        if(!file.exists()){
            netpath = null;
        }
        session.commit();
        session.close();
        Tools.printOutData(rep,netpath);
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
            record.setCtype(0);
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




    @RequestMapping(value = "/selectAllByParentId",method = RequestMethod.POST)
    public void selectAllByParentId(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        List<Record> files = recordMapper.selectRecordsByParentId(Integer.parseInt(map.get("parentId")));
        session.commit();
        session.close();
        Tools.printOutData(res,files);
    }


    @RequestMapping(value = "/addFolder",method = RequestMethod.POST)
    public void addFolder(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        Record record = GsonUtil.getInstance().fromJson(map.get("data"), Record.class);
        if(record.getParentid()==null){
            session.close();
            Tools.printOutData(res,false);
            return;
        }
        Record r = recordMapper.selectByPrimaryKey(7360);
        if(r!=null){
            record.setLocpath(r.getLocpath());
            record.setNetpath(r.getNetpath());
        }
        record.setIsUploaded(0);
        record.setCtype(1);
        record.setEnable(1);
        record.setUtime(System.currentTimeMillis());
        record.setCtime(System.currentTimeMillis());
        int a= recordMapper.insert(record);
        session.commit();
        session.close();
        Tools.printOutData(res,a==1);
    }

    @RequestMapping(value = "/addFiles",method = RequestMethod.POST)
    public void addFiles(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records  = GsonUtil.getInstance().fromJson(map.get("data"),new TypeToken<ArrayList<Record>>(){}.getType());
        Integer parentId = Integer.parseInt(map.get("parentId"));
        for(int i=0;i<records.size();i++){
            ArrayList<Record> list = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(records.get(i).getLocpath());
            if(list==null||list.size()==0){
                continue;
            }
            Record record = list.get(0);
            recordMapper.updateParentIdById(parentId,record.getId());
        }
        session.commit();
        session.close();
        Tools.printOutData(res,records.size()!=0);
    }

    @RequestMapping(value = "/deleteFile",method = RequestMethod.POST)
    public void deleteFile(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        Integer id = Integer.parseInt(map.get("id"));
        Record record = recordMapper.selectByPrimaryKey(id);
        int i = 0;
        if(record.getCtype()==1){
            //文件夹
            recordMapper.deleteByPrimaryKey(id);
        }else{
            //文件
            i = recordMapper.updateParentIdById(null,id);
        }

        session.commit();
        session.close();
        Tools.printOutData(res,i!=0);
    }

    @RequestMapping(value = "/setHeadImage",method = RequestMethod.POST)
    public void setHeadImage(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(map.get("locpath"));
        int i = 0;
        if(records!=null&&records.size()!=0){
            Record record = recordMapper.selectByPrimaryKey(Integer.parseInt(map.get("parentId")));
            record.setNetpath(records.get(0).getNetpath());
            record.setLocpath(records.get(0).getLocpath());
            i = recordMapper.updatePathById(record);
        }
        session.commit();
        session.close();
        Tools.printOutData(res,i!=0);
    }

    @RequestMapping(value = "/selectAllFolder",method = RequestMethod.GET)
    public void selectAllFolder(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        SqlSession session  = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectAllFolder();
        session.close();
        Tools.printOutData(res,records);
    }

}
