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
import com.summer.test.Main;
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
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/picture")
public class PictureControl {

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public void test(HttpServletRequest req, HttpServletResponse rep) throws IOException, ParseException {
        Main.mainmain();
    }


    @RequestMapping(value = "/isPictureUploaded", method = RequestMethod.POST)
    public void isPictureUploaded(HttpServletRequest req, HttpServletResponse rep) {
        System.out.println("------------------------------------------------------------------------------------");
        HashMap<String, String> data = Tools.getStr(req, rep);
        Tools.init(req, rep);
        Record record = GsonUtil.getInstance().fromJson(data.get("data"), Record.class);
        Record.setMyAType(record);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        //从数据库中查找是否有这条记录
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        Record record1 = null;//最终处理的数据
        //没有此条记录 则插入这条记录
        if (records == null || records.size() == 0) {
            record.setCtype(0);
            recordMapper.insert(record);
            System.out.println("插入数据"+record.getLocpath()+";"+record.getCtime());
            record1 = record;
        } else {
            //数据库查询到一条或多条类似数据
            //多条
            if(records.size()>1){
                System.out.println("关联删除"+records.get(0).getId()+":"+records.get(0).getNetpath());
                for(int i=1;i<records.size();i++){
                    recordMapper.deleteByPrimaryKey(records.get(i).getId());
                    File file = Value.getRecordFile(records.get(i));
                    if(file.exists()){
                        file.delete();
                    }
                    System.out.println(records.get(i).getId()+":"+records.get(i).getNetpath()+"已删除");
                }
                System.out.println("");
            }
            //111
            //处理完成一条
            if(record.getCtime()>records.get(0).getCtime()){
                //当前记录较新
                record1 = records.get(0);
            }else  if(record.getCtime()<records.get(0).getCtime()){
                //当前记录较早 则替换 先删掉已存在的 然后在插入改条
                recordMapper.deleteByPrimaryKey(records.get(0).getId());
                File file = Value.getRecordFile(records.get(0));
                if(file.exists()){
                    file.delete();
                }
                System.out.println(records.get(0).getId()+":"+records.get(0).getNetpath()+"已替换删除");
                File file1 = Value.getRecordFile(record);
                if(file1.exists()){
                    record.setNetpath(file1.getPath());
                }
                //插入当前记录
                record.setCtype(0);
                recordMapper.insert(record);
                record1 = record;
            }else{
                //相同数据不用处理
                record1 = records.get(0);
            }
            //111
        }
        //以上处理可能会影响到tiplab等相关表数据
        session.commit();
        session.close();
        Tools.printOutData(rep, (record1.getNetpath()!=null&&Value.isRecordFileExit(record1))?record1.getNetpath():null);
        System.out.println("------------------------------------------------------------------------------------");
    }


    @RequestMapping(value = "/uploadPicture", method = RequestMethod.POST)
    public void uploadRecords(HttpServletRequest req, HttpServletResponse rep) {
        Tools.init(req, rep);
        Record record = GsonUtil.getInstance().fromJson(req.getParameter("data"), Record.class);
        Record.setMyAType(record);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        //从数据库中查找是否有这条记录
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        //没有此条记录 则插入这条记录
        if (records == null || records.size() == 0) {
            record.setCtype(0);
            recordMapper.insert(record);
        }
        //再检查本地是否有文件
        File file = Value.getRecordFile(record);
        System.out.println(file.getPath());
        //本地没有该文件
//        if (!file.exists()) {// java.net.SocketException: Broken pipe 防止 客户端在传服务端因为文件已经存在不读取存储 保存 统一全部在读写一遍
            if(1==1){
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setRepository(Value.getTempFile());
            factory.setSizeThreshold(1024 * 1024);
            ServletFileUpload upload = new ServletFileUpload(factory);
            BaseResBean baseResBean = new BaseResBean();
            ArrayList<FileItem> list = null;
            try {
                list = (ArrayList<FileItem>) upload.parseRequest(req);
                list.get(0).write(file);
                list.get(0).delete();
                recordMapper.updateNetPath(file.getPath(), record.getLocpath());
            } catch (FileUploadException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        record.setNetpath(file.getPath());
        //生成缩略图
        try {
            ThumbnailUtil.simglezoomImageScale(record);
        } catch (IOException e) {
            e.printStackTrace();
        }
        session.commit();
        session.close();
        ArrayList<String> files = new ArrayList<>();
        files.add(file.getPath());
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(files);
        Tools.printOut(rep, baseResBean);
    }


    @RequestMapping(value = "/getAllPictures", method = RequestMethod.GET)
    public void getAllPictures(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        String startTime = req.getParameter("startTime");
        if (startTime == null) {
            startTime = new Date(0).getTime() + "";
        }
        String endTime = req.getParameter("endTime");
        if (endTime == null) {
            endTime = System.currentTimeMillis() + "";
        }
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        baseResBean.setData(recordMapper.selectAllWithSE(startTime, endTime));
        Tools.printOut(res, baseResBean);
        session.close();
    }


    @RequestMapping(value = "/thumbnail", method = RequestMethod.GET)
    public void thumbnail(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        String startTime = req.getParameter("startTime");
        if (startTime == null) {
            startTime = new Date(0).getTime() + "";
        }
        String endTime = req.getParameter("endTime");
        if (endTime == null) {
            endTime = System.currentTimeMillis() + "";
        }
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        try {
            ThumbnailUtil.zoomImagesScale((ArrayList<Record>) recordMapper.selectAllByAtypeWithSE("image",startTime,endTime));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Tools.printOut(res, "");
        session.close();
    }


    @RequestMapping(value = "/selectAllByParentId", method = RequestMethod.POST)
    public void selectAllByParentId(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        List<Record> files = recordMapper.selectRecordsByParentId(Integer.parseInt(map.get("parentId")));
        session.commit();
        session.close();
        Tools.printOutData(res, files);
    }


    @RequestMapping(value = "/addFolder", method = RequestMethod.POST)
    public void addFolder(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        Record record = GsonUtil.getInstance().fromJson(map.get("data"), Record.class);
        Record.setMyAType(record);
        if (record.getParentid() == null) {
            session.close();
            Tools.printOutData(res, false);
            return;
        }
        Record r = recordMapper.selectByPrimaryKey(7360);
        if (r != null) {
            record.setLocpath(r.getLocpath());
            record.setNetpath(r.getNetpath());
        }
        record.setIsUploaded(0);
        record.setCtype(1);
        record.setEnable(1);
        record.setUtime(System.currentTimeMillis());
        record.setCtime(System.currentTimeMillis());
        int a = recordMapper.insert(record);
        session.commit();
        session.close();
        Tools.printOutData(res, a == 1);
    }

    @RequestMapping(value = "/addFiles", method = RequestMethod.POST)
    public void addFiles(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = GsonUtil.getInstance().fromJson(map.get("data"), new TypeToken<ArrayList<Record>>() {
        }.getType());
        Integer parentId = Integer.parseInt(map.get("parentId"));
        for (int i = 0; i < records.size(); i++) {
            ArrayList<Record> list = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(records.get(i).getLocpath());
            if (list == null || list.size() == 0) {
                continue;
            }
            Record record = list.get(0);
            recordMapper.updateParentIdById(parentId, record.getId());
        }
        session.commit();
        session.close();
        Tools.printOutData(res, records.size() != 0);
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public void deleteFile(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        Integer id = Integer.parseInt(map.get("id"));
        Record record = recordMapper.selectByPrimaryKey(id);
        int i = 0;
        if (record.getCtype() == 1) {
            //文件夹
            recordMapper.deleteByPrimaryKey(id);
        } else {
            //文件
            i = recordMapper.updateParentIdById(null, id);
        }

        session.commit();
        session.close();
        Tools.printOutData(res, i != 0);
    }

    @RequestMapping(value = "/setHeadImage", method = RequestMethod.POST)
    public void setHeadImage(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(map.get("locpath"));
        int i = 0;
        if (records != null && records.size() != 0) {
            Record record = recordMapper.selectByPrimaryKey(Integer.parseInt(map.get("parentId")));
            record.setNetpath(records.get(0).getNetpath());
            record.setLocpath(records.get(0).getLocpath());
            i = recordMapper.updatePathById(record);
        }
        session.commit();
        session.close();
        Tools.printOutData(res, i != 0);
    }

    @RequestMapping(value = "/selectAllFolder", method = RequestMethod.GET)
    public void selectAllFolder(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectAllFolder();
        session.close();
        Tools.printOutData(res, records);
    }

}
