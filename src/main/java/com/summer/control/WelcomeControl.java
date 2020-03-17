package com.summer.control;

import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.global.Value;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.entity.Welcome;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.mybatis.mapper.WelcomeMapper;
import com.summer.util.DateFormatUtil;
import com.summer.util.GsonUtil;
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
@RequestMapping("/welcome")
public class WelcomeControl {


    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public void isPictureUploaded(HttpServletRequest req, HttpServletResponse rep) {
        HashMap<String, String> data = Tools.getStr(req, rep);
        Welcome welcome = GsonUtil.getInstance().fromJson(data.get("data"), Welcome.class);
        SqlSession session = DBTools.getSession();
        WelcomeMapper welcomeMapper = session.getMapper(WelcomeMapper.class);
        List<Welcome> datas = welcomeMapper.selectAll();
        //没有则插入
        if (datas == null || datas.size() == 0) {
            welcomeMapper.insert(welcome);
        } else {
            //有则更新
            welcomeMapper.deleteByPrimaryKey(datas.get(0).getId());
            welcomeMapper.insert(welcome);
        }
        session.commit();
        session.close();
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(true);
        Tools.printOut(rep, baseResBean);
    }

    @RequestMapping(value = "/selectAll", method = RequestMethod.GET)
    public void selectAll(HttpServletRequest req, HttpServletResponse rep) {
        Tools.init(req, rep);
        SqlSession session = DBTools.getSession();
        WelcomeMapper welcomeMapper = session.getMapper(WelcomeMapper.class);
        List<Welcome> datas = welcomeMapper.selectAll();
        session.commit();
        session.close();
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(datas.size() == 0 ? null : datas.get(0));
        Tools.printOut(rep, baseResBean);
    }


}
