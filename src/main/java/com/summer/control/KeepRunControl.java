package com.summer.control;

import com.summer.base.bean.BaseBean;
import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.KeepRun;
import com.summer.mybatis.mapper.KeepRunMapper;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.util.GsonUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/keeprun")
public class KeepRunControl {

    @RequestMapping(value = "/insert",method = RequestMethod.POST)
    public void getAllRecordsStepwithLimit(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);

        BaseBean baseBean = new BaseBean();

        KeepRun keepRun = GsonUtil.getInstance().fromJson(req.getParameter("data"),KeepRun.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        KeepRunMapper keepRunMapper = session.getMapper(KeepRunMapper.class);
        keepRunMapper.insert(keepRun.getTime(),keepRun.getText());

        baseResBean.setData(keepRun);
        Tools.printOut(res,baseResBean);
        session.commit();
        session.close();

    }
}
