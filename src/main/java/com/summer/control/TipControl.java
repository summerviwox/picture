package com.summer.control;

import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.entity.Tip;
import com.summer.mybatis.entity.Tiplab;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.mybatis.mapper.TipMapper;
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
@RequestMapping("/tip")
public class TipControl {


    @RequestMapping(value = "/addTip", method = RequestMethod.POST)
    public void addTip(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        //复用tiplab as req conent == netpath id = tipid in tip
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        List<Record> records = recordMapper.selectRecordWhereLocalPath(tiplab.getContent());
        if(records==null||records.size()==0){
            baseResBean.setErrorCode(201);
            baseResBean.setErrorMessage("未上传");
            baseResBean.setData(null);
        }else{
            Tip tip = new Tip();
            tip.setTipid(tiplab.getId());
            tip.setRecordid(records.get(0).getId());
            tip.setCtime(System.currentTimeMillis());
            tipMapper.insert(tip);
            baseResBean.setData(tip);
        }
        session.commit();
        session.close();
        Tools.printOut(res, baseResBean);
        session.close();

    }

    @RequestMapping(value = "/deleteTipById", method = RequestMethod.POST)
    public void deleteTipById(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tip tip = GsonUtil.getInstance().fromJson(map.get("data"), Tip.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        int i = tipMapper.deleteByPrimaryKey(tip.getId());
        session.commit();
        session.close();
        baseResBean.setData(i==1);
        Tools.printOut(res, baseResBean);
    }

}
