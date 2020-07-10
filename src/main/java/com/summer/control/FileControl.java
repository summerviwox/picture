package com.summer.control;


import com.google.gson.reflect.TypeToken;
import com.summer.base.bean.Tools;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.File;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.mapper.FileMapper;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.util.GsonUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/file")
public class FileControl {




    @RequestMapping(value = "/selectAllByParentId", method = RequestMethod.POST)
    public void selectAllByParentId(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        FileMapper fileMapper = session.getMapper(FileMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        List<File> files = fileMapper.selectAllByParentId(Integer.parseInt(map.get("parentId")));
        for (int i = 0; i < files.size(); i++) {
            if(files.get(i).getHeadid()==null){
                files.get(i).setRecord(null);
            }else{
                Record record = recordMapper.selectByPrimaryKey(files.get(i).getHeadid());
                files.get(i).setRecord(record);
            }

        }
        session.commit();
        session.close();
        Tools.printOutData(res, files);
    }


    @RequestMapping(value = "/addFolder", method = RequestMethod.POST)
    public void addFolder(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        FileMapper fileMapper = session.getMapper(FileMapper.class);
        File file = GsonUtil.getInstance().fromJson(map.get("data"), File.class);
        if (file.getParentid() == null) {
            session.close();
            Tools.printOutData(res, false);
            return;
        }
        file.setId(-1);
        file.setHeadid(null);
        file.setType(1);
        file.setUtime(System.currentTimeMillis());
        file.setCtime(System.currentTimeMillis());
        file.setEnable(1);
        int a = fileMapper.insert(file);
        session.commit();
        session.close();
        Tools.printOutData(res, file.getId());
    }

    @RequestMapping(value = "/addFiles", method = RequestMethod.POST)
    public void addFiles(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        FileMapper fileMapper = session.getMapper(FileMapper.class);
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
            File file = new File();
            file.setEnable(1);
            file.setCtime(System.currentTimeMillis());
            file.setUtime(System.currentTimeMillis());
            file.setType(0);
            file.setParentid(parentId);
            file.setHeadid(record.getId());
            file.setName(record.getName());
            file.setEnable(1);
            fileMapper.insert(file);
        }
        session.commit();
        session.close();
        Tools.printOutData(res, records.size() != 0);
    }

    @RequestMapping(value = "/deleteFile", method = RequestMethod.POST)
    public void deleteFile(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        FileMapper fileMapper = session.getMapper(FileMapper.class);
        Integer id = Integer.parseInt(map.get("id"));
        int i = 0;
        fileMapper.deleteByPrimaryKey(id);
        session.commit();
        session.close();
        Tools.printOutData(res, i != 0);
    }


    @RequestMapping(value = "/setHeadImage", method = RequestMethod.POST)
    public void setHeadImage(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        FileMapper fileMapper = session.getMapper(FileMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(map.get("locpath"));
        int i = 0;
        if (records != null && records.size() != 0) {
            Record record = recordMapper.selectByPrimaryKey(Integer.parseInt(map.get("parentId")));
            i = fileMapper.updateHeadIdByPrivateKey(Integer.parseInt(map.get("parentId")), records.get(0).getId());
        }
        session.commit();
        session.close();
        Tools.printOutData(res, i != 0);
    }

    @RequestMapping(value = "/selectAllFolder", method = RequestMethod.GET)
    public void selectAllFolder(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        SqlSession session = DBTools.getSession();
        FileMapper fileMapper = session.getMapper(FileMapper.class);
        ArrayList<File> files = (ArrayList<File>) fileMapper.selectAllFolder();
        session.close();
        Tools.printOutData(res, files);
    }

    @RequestMapping(value = "/moveFile", method = RequestMethod.POST)
    public void moveFile(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        FileMapper fileMapper = session.getMapper(FileMapper.class);
        Integer id = Integer.parseInt(map.get("id"));
        Integer parentId = Integer.parseInt(map.get("parentId"));
        int i = fileMapper.updateParentByPrivateKey(id, parentId);
        session.commit();
        session.close();
        Tools.printOutData(res, i != 0);
    }


}
