package com.summer.control;

import com.google.gson.reflect.TypeToken;
import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.bean.album.AlbumReq;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Album;
import com.summer.mybatis.entity.Albumitem;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.mapper.AlbumMapper;
import com.summer.mybatis.mapper.AlbumitemMapper;
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
@RequestMapping("/albumitem")
public class AlbumItemControl {


    @RequestMapping(value = "/addAlbums",method = RequestMethod.POST)
    public void addAlbums(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        ArrayList<String> records = GsonUtil.getInstance().fromJson(map.get("datas"),new TypeToken<ArrayList<String>>(){}.getType());
        String albumid = map.get("id");
        SqlSession session  = DBTools.getSession();
        AlbumitemMapper albumitemMapper = session.getMapper(AlbumitemMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        for(int i=0;i<records.size();i++){
            Albumitem albumitem = new Albumitem();
            albumitem.setAlbumid(Integer.parseInt(albumid));
            ArrayList<Record> rs = (ArrayList<Record>) recordMapper.selectRecordIdWhereLocalPath(records.get(i));
            if(rs!=null&&rs.size()!=0){
                albumitem.setRecordid(rs.get(0).getId());
                albumitem.setAlbumid(Integer.parseInt(albumid));
                albumitemMapper.insert(albumitem);
            }
        }
        session.commit();
        Tools.printOutData(res,true);

    }

    @RequestMapping(value = "/getAllAlbumItemsById",method = RequestMethod.POST)
    public void getAllAlbumItemsById(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        AlbumitemMapper albumitemMapper = session.getMapper(AlbumitemMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        List<Albumitem> albumitems =  albumitemMapper.selectByAlbumid(Integer.parseInt(map.get("id")));
        ArrayList<Record> records = new ArrayList<>();
        for(int i=0;i<albumitems.size();i++){
            Record record = recordMapper.selectByPrimaryKey(albumitems.get(i).getRecordid());
            if(record!=null){
                records.add(record);
            }
        }
        session.close();
        Tools.printOutData(res,records);
    }


    @RequestMapping(value = "/deleteAlbumItem",method = RequestMethod.POST)
    public void deleteAlbumItem(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        AlbumitemMapper albumitemMapper = session.getMapper(AlbumitemMapper.class);
        ArrayList<Albumitem> albumitems = (ArrayList<Albumitem>) albumitemMapper.selectByAlbumidAndRecordid(Integer.parseInt(map.get("albumid")),Integer.parseInt(map.get("recordid")));
        if(albumitems!=null&&albumitems.size()!=0){
            albumitemMapper.deleteByPrimaryKey(albumitems.get(0).getId());
        }
        session.commit();
        session.close();
        Tools.printOutData(res,true);
    }


}
