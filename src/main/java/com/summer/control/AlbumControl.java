package com.summer.control;

import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.bean.album.AlbumReq;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Album;
import com.summer.mybatis.entity.Albumitem;
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


    @RequestMapping(value = "/addNewAlbum",method = RequestMethod.POST)
    public void addNewAlbum(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> strs = Tools.getStr(req, res);
        AlbumReq albumReq = GsonUtil.getInstance().fromJson(strs.get("data"),AlbumReq.class);
        SqlSession session  = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        AlbumitemMapper albumitemMapper = session.getMapper(AlbumitemMapper.class);
        Album album = new Album();
        album.setName(albumReq.getName());
        album.setCtime(System.currentTimeMillis());
        album.setHead(recordMapper.selectRecordWhereLocalPath(albumReq.getAlbumItems().get(0)).get(0).getLocpath());
        album.setUtime(System.currentTimeMillis());
        albumMapper.insert(album);
        for(int i=0;i<albumReq.getAlbumItems().size();i++){
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
        Tools.printOut(res,baseResBean);

    }

    @RequestMapping(value = "/getAllAlbums",method = RequestMethod.GET)
    public void getAllAlbums(HttpServletRequest req, HttpServletResponse res) {
         Tools.init(req, res);
        SqlSession session  = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        List<Album> albums =  albumMapper.selectAll();
        session.close();
        Tools.printOutData(res,albums);
    }


    @RequestMapping(value = "/deleteAlbum",method = RequestMethod.POST)
    public void deleteAlbum(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        SqlSession session  = DBTools.getSession();
        AlbumMapper albumMapper = session.getMapper(AlbumMapper.class);
        albumMapper.deleteByPrimaryKey(Integer.parseInt(map.get("id")));
        session.commit();
        session.close();
        Tools.printOutData(res,true);
    }


}
