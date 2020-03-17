package com.summer.control;

import com.summer.base.bean.Tools;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Alarm;
import com.summer.mybatis.entity.Crash;
import com.summer.mybatis.mapper.AlarmMapper;
import com.summer.mybatis.mapper.CrashMapper;
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
@RequestMapping("/crash")
public class CrashControl {

    @RequestMapping(value = "/sendCrash", method = RequestMethod.POST)
    public void sendCrash(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Crash crash = GsonUtil.getInstance().fromJson(map.get("data"), Crash.class);
        SqlSession session = DBTools.getSession();
        CrashMapper crashMapper = session.getMapper(CrashMapper.class);
        int i = crashMapper.insert(crash);
        session.commit();
        session.close();
        Tools.printOutData(res, i == 1);
    }

}
