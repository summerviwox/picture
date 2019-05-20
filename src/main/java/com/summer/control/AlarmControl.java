package com.summer.control;

import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Alarm;
import com.summer.mybatis.mapper.AlarmMapper;
import com.summer.mybatis.mapper.AlbumMapper;
import com.summer.util.GsonUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/alarm")
public class AlarmControl {

    @RequestMapping(value = "/selectAllAlarms",method = RequestMethod.GET)
    public void selectAllAlarms(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req,res);
        SqlSession session  = DBTools.getSession();
        AlarmMapper alarmMapper = session.getMapper(AlarmMapper.class);
        List<Alarm> data = alarmMapper.selectAll();
        Collections.sort(data);
        session.close();
        Tools.printOutData(res,data);
    }


    @RequestMapping(value = "/insertAlarm",method = RequestMethod.POST)
    public void insertAlarm(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        Alarm alarm = GsonUtil.getInstance().fromJson(map.get("data"),Alarm.class);
        SqlSession session  = DBTools.getSession();
        AlarmMapper alarmMapper = session.getMapper(AlarmMapper.class);
        int i=alarmMapper.insert(alarm);
        session.commit();
        session.close();
        Tools.printOutData(res,i==1);
    }

    @RequestMapping(value = "/deleteAlarm",method = RequestMethod.POST)
    public void deleteAlarm(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        String id = map.get("id");
        SqlSession session  = DBTools.getSession();
        AlarmMapper alarmMapper = session.getMapper(AlarmMapper.class);
        int i = alarmMapper.deleteByPrimaryKey(Integer.parseInt(id));
        session.commit();
        session.close();
        Tools.printOutData(res,i==1);
    }


    @RequestMapping(value = "/updateAlarm",method = RequestMethod.POST)
    public void updateAlarm(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String,String> map = Tools.getStr(req, res);
        Alarm alarm = GsonUtil.getInstance().fromJson(map.get("data"),Alarm.class);
        SqlSession session  = DBTools.getSession();
        AlarmMapper alarmMapper = session.getMapper(AlarmMapper.class);
        int i=alarmMapper.updateByPrimaryKey(alarm);
        session.commit();
        session.close();
        Tools.printOutData(res,i==1);
    }

}
