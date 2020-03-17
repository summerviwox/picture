package com.summer.control;

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
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/album")
public class AlbumControl {


    @RequestMapping(value = "/addNewAlbum", method = RequestMethod.POST)
    public void addNewAlbum(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> strs = Tools.getStr(req, res);
        AlbumReq albumReq = GsonUtil.getInstance().fromJson(strs.get("data"), AlbumReq.class);
        SqlSession session = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        AlbumitemMapper albumitemMapper = session.getMapper(AlbumitemMapper.class);
        Album album = new Album();
        album.setName(albumReq.getName());
        album.setDetail(albumReq.getDetail());
        album.setCtime(System.currentTimeMillis());
        if (albumReq.getAlbumItems() != null && albumReq.getAlbumItems().size() != 0) {
            album.setHead(recordMapper.selectRecordWhereLocalPath(albumReq.getAlbumItems().get(0)).get(0).getLocpath());
        }
        album.setUtime(System.currentTimeMillis());
        album.setHeadid(7360);
        if (albumReq.getAlbumItems().size() != 0) {
            int ida = recordMapper.selectRecordWhereLocalPath(albumReq.getAlbumItems().get(0)).get(0).getId();
            album.setHeadid(ida);
        }
        albumMapper.insert(album);
        for (int i = 0; albumReq.getAlbumItems() != null && i < albumReq.getAlbumItems().size(); i++) {
            int ida = recordMapper.selectRecordWhereLocalPath(albumReq.getAlbumItems().get(i)).get(0).getId();
            Albumitem albumitem = new Albumitem();
            albumitem.setAlbumid(album.getId());
            albumitem.setRecordid(ida);
            albumitemMapper.insert(albumitem);
        }
        session.commit();
        session.close();
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(true);
        Tools.printOut(res, baseResBean);

    }

    @RequestMapping(value = "/getAllAlbums", method = RequestMethod.GET)
    public void getAllAlbums(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        SqlSession session = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        List<Album> albums = albumMapper.selectAll();
        for (int i = 0; i < albums.size(); i++) {
            Record record = recordMapper.selectByPrimaryKey(albums.get(i).getHeadid());
            albums.get(i).setRecord(record);

        }
        session.close();
        Tools.printOutData(res, albums);
    }


    @RequestMapping(value = "/deleteAlbum", method = RequestMethod.POST)
    public void deleteAlbum(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        albumMapper.deleteByPrimaryKey(Integer.parseInt(map.get("id")));
        session.commit();
        session.close();
        Tools.printOutData(res, true);
    }


    @RequestMapping(value = "/setAlbumHead", method = RequestMethod.POST)
    public void setAlbumHead(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        Album album = new Album();
        album.setId(Integer.parseInt(map.get("id")));
        album.setHeadid(Integer.parseInt(map.get("headid")));
        albumMapper.updateHeadByPrimaryKey(album);
        session.commit();
        session.close();
        Tools.printOutData(res, true);
    }

    @RequestMapping(value = "/updateNameOrDetailById", method = RequestMethod.POST)
    public void updateNameOrDetailById(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        SqlSession session = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        Album album = new Album();
        album.setId(Integer.parseInt(map.get("id")));
        album.setName(map.get("name"));
        album.setDetail(map.get("detail"));
        albumMapper.updateNameOrDetailById(map.get("name"), map.get("detail"), Integer.parseInt(map.get("id")));
        session.commit();
        session.close();
        Tools.printOutData(res, true);
    }


}
