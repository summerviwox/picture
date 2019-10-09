package com.summer.control;

import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Welcome;
import com.summer.mybatis.entity.Will;
import com.summer.mybatis.mapper.WelcomeMapper;
import com.summer.mybatis.mapper.WillMapper;
import com.summer.util.GsonUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;

@Controller
@RequestMapping("/will")
public class WillControl  {

    @RequestMapping(value = "/addWill",method = RequestMethod.POST)
    public void addWill(HttpServletRequest req, HttpServletResponse rep){
        HashMap<String,String> data = Tools.getStr(req,rep);
        Will will = GsonUtil.getInstance().fromJson(data.get("data"),Will.class);
        SqlSession session = DBTools.getSession();
        WillMapper willMapper = session.getMapper(WillMapper.class);
        int i= willMapper.insert(will);
        session.commit();
        session.close();
        Tools.printOutData(rep,i==1);
    }


    @RequestMapping(value ="/getAllWill",method = RequestMethod.POST)
    public void getAllWill(HttpServletRequest req, HttpServletResponse rep){
        HashMap<String,String> data = Tools.getStr(req,rep);
        SqlSession session = DBTools.getSession();
        WillMapper willMapper = session.getMapper(WillMapper.class);
        int level = Integer.parseInt(data.get("level"));
        ArrayList<Will> wills = level>=0?(ArrayList<Will>) willMapper.selectAll(level):(ArrayList<Will>) willMapper.selectAllNotDelete();
        session.close();
        Tools.printOutData(rep,wills);
    }

    @RequestMapping(value ="/deleteWillById", method = RequestMethod.POST)
    public void deleteWillById(HttpServletRequest req, HttpServletResponse rep){
        HashMap<String,String> data = Tools.getStr(req,rep);
        String id = data.get("id");
        SqlSession session = DBTools.getSession();
        WillMapper willMapper = session.getMapper(WillMapper.class);
        int i = willMapper.deleteByPrimaryKey(Integer.parseInt(id));
        session.commit();
        session.close();
        Tools.printOutData(rep,i==1);
    }

    @RequestMapping(value ="/finishWillById", method = RequestMethod.POST)
    public void finishWillById(HttpServletRequest req, HttpServletResponse rep){
        HashMap<String,String> data = Tools.getStr(req,rep);
        String id = data.get("id");
        String level = data.get("level");
        SqlSession session = DBTools.getSession();
        WillMapper willMapper = session.getMapper(WillMapper.class);
        int i = willMapper.updateLevelByPrimaryKey(Integer.parseInt(id),Integer.parseInt(level));
        session.commit();
        session.close();
        Tools.printOutData(rep,i==1);
    }
}
