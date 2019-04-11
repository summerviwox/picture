package com.summer.control;

import com.google.gson.reflect.TypeToken;
import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.entity.Tip;
import com.summer.mybatis.entity.Tiplab;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.mybatis.mapper.TipMapper;
import com.summer.mybatis.mapper.TiplabMapper;
import com.summer.util.GsonUtil;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/tip")
public class TipLabControl {


    @RequestMapping(value = "/getRecordsFromTip",method = RequestMethod.POST)
    public void getRecordsFromTip(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(req.getParameter("data"),Tiplab.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.selectTipsByTipId(tiplab.getId());
        ArrayList<Record> records = new ArrayList<>();
        for(int i=0;tips!=null&&i<tips.size();i++){
            Record record = recordMapper.selectByPrimaryKey(tips.get(i).getRecordid());
            if(record!=null){
                records.add(record);
            }
        }
        baseResBean.setData(records);
        Tools.printOut(res,baseResBean);
        session.close();

    }

    @RequestMapping(value = "/getImageRecordsFromTip",method = RequestMethod.POST)
    public void getImageRecordsFromTip(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(req.getParameter("data"),Tiplab.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.selectTipsByTipId(tiplab.getId());
        ArrayList<Record> records = new ArrayList<>();
        for(int i=0;tips!=null&&i<tips.size();i++){
            Record record = recordMapper.selectByPrimaryKey(tips.get(i).getRecordid());
            if(record!=null&&Record.ATYPE_IMAGE.equals(record.getAtype())){
                records.add(record);
            }
        }
        baseResBean.setData(records);
        Tools.printOut(res,baseResBean);
        session.close();

    }

    @RequestMapping(value = "/getRecordTips",method = RequestMethod.POST)
    public void getRecordTips(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req,res);
        Record record = GsonUtil.getInstance().fromJson(req.getParameter("data"),Record.class);

        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        if(records!=null&& records.size()>0){
            ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.selectTipsByRecordId(records.get(0).getId());
            ArrayList<Tiplab> tiplabs = new ArrayList<>();
            for(int i=0;tips!=null&&i<tips.size();i++){
                Tiplab tiplab = tiplabMapper.selectByPrimaryKey(tips.get(i).getTipid());
                tiplabs.add(tiplab);
            }
            baseResBean.setData(tiplabs);
        }
        Tools.printOut(res,baseResBean);
        session.close();
    }



    @RequestMapping(value = "/addTip",method = RequestMethod.POST)
    public void addRecordTip(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        Tip tip = GsonUtil.getInstance().fromJson(req.getParameter("data"),Tip.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();

        TipMapper tipMapper = session.getMapper(TipMapper.class);
        tipMapper.insert(tip);
        session.commit();
        baseResBean.setData(tip);
        Tools.printOut(res,baseResBean);
        session.close();
    }


    @RequestMapping(value = "/checkTip",method = RequestMethod.POST)
    public void checkTip(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(req.getParameter("data"),Tiplab.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        List<Tiplab> tiplabs = tiplabMapper.selectTipLabByContent(tiplab.getContent());
        baseResBean.setData(tiplabs);
        Tools.printOut(res,baseResBean);
        session.close();
    }

    @RequestMapping(value = "/getLikeTiplab",method = RequestMethod.POST)
    public void getLikeTiplab(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(req.getParameter("data"),Tiplab.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        ArrayList<Tiplab> tiplabs = (ArrayList<Tiplab>) tiplabMapper.selectLikeTipLabByContent(tiplab.getContent());
        baseResBean.setData(tiplabs);
        Tools.printOut(res,baseResBean);
        session.close();
    }

    @RequestMapping(value = "/addTipLab",method = RequestMethod.POST)
    public void addTipLab(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(req.getParameter("data"),Tiplab.class);
        SqlSession session  =  DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        tiplabMapper.insert(tiplab);
        baseResBean.setData(tiplab);
        Tools.printOut(res,baseResBean);
        session.commit();
        session.close();
    }

    @RequestMapping(value = "/getAllTipLabs",method = RequestMethod.GET)
    public void getAllTipLabs(HttpServletRequest req, HttpServletResponse res){
        Tools.init(req, res);
        SqlSession sqlSession = DBTools.getSession();
        TiplabMapper tiplabMapper = sqlSession.getMapper(TiplabMapper.class);
        ArrayList<Tiplab> tiplabs = (ArrayList<Tiplab>) tiplabMapper.selectAll();
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(tiplabs);
        Tools.printOut(res,baseResBean);
        sqlSession.commit();
        sqlSession.close();

    }


}
