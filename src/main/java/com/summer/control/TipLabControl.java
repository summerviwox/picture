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
import java.util.HashMap;
import java.util.List;

@Controller
@RequestMapping("/tiplab")
public class TipLabControl {


    @RequestMapping(value = "/getRecordsFromTip", method = RequestMethod.POST)
    public void getRecordsFromTip(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.selectTipsByTipId(tiplab.getId());
        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; tips != null && i < tips.size(); i++) {
            if(tips.get(i).getRecordid()==null){
                continue;
            }
            Record record = recordMapper.selectByPrimaryKey(tips.get(i).getRecordid());
            if (record != null) {
                record.setParentid(tips.get(i).getId());//借用parentid 存储 tipsid
                records.add(record);
            }else{
                System.out.println(tips.get(i).getRecordid());
            }
        }
        baseResBean.setData(records);
        Tools.printOut(res, baseResBean);
        session.close();

    }



    @RequestMapping(value = "/deleteByContent", method = RequestMethod.POST)
    public void deleteByContent(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        int i = tiplabMapper.deleteByContent(tiplab.getContent());
        session.commit();
        session.close();
        baseResBean.setData(i==1);
        Tools.printOut(res, baseResBean);

    }

    @RequestMapping(value = "/getImageRecordsFromTip", method = RequestMethod.POST)
    public void getImageRecordsFromTip(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.selectTipsByTipId(tiplab.getId());
        ArrayList<Record> records = new ArrayList<>();
        for (int i = 0; tips != null && i < tips.size(); i++) {
            Record record = recordMapper.selectByPrimaryKey(tips.get(i).getRecordid());
            if (record != null && Record.ATYPE_IMAGE.equals(record.getAtype())) {
                records.add(record);
            }
        }
        baseResBean.setData(records);
        Tools.printOut(res, baseResBean);
        session.close();

    }

    @RequestMapping(value = "/getRecordTips", method = RequestMethod.POST)
    public void getRecordTips(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Record record = GsonUtil.getInstance().fromJson(map.get("data"), Record.class);

        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        RecordMapper recordMapper = session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectRecordWhereLocalPath(record.getLocpath());
        if (records != null && records.size() > 0) {
            ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.selectTipsByRecordId(records.get(0).getId());
            ArrayList<Tiplab> tiplabs = new ArrayList<>();
            for (int i = 0; tips != null && i < tips.size(); i++) {
                Tiplab tiplab = tiplabMapper.selectByPrimaryKey(tips.get(i).getTipid());
                tiplabs.add(tiplab);
            }
            baseResBean.setData(tiplabs);
        }
        Tools.printOut(res, baseResBean);
        session.close();
    }


    @RequestMapping(value = "/addTip", method = RequestMethod.POST)
    public void addRecordTip(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tip tip = GsonUtil.getInstance().fromJson(map.get("data"), Tip.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();

        TipMapper tipMapper = session.getMapper(TipMapper.class);
        tipMapper.insert(tip);
        session.commit();
        baseResBean.setData(tip);
        Tools.printOut(res, baseResBean);
        session.close();
    }


    @RequestMapping(value = "/checkTip", method = RequestMethod.POST)
    public void checkTip(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        List<Tiplab> tiplabs = tiplabMapper.selectTipLabByContent(tiplab.getContent());
        baseResBean.setData(tiplabs);
        Tools.printOut(res, baseResBean);
        session.close();
    }

    @RequestMapping(value = "/getLikeTiplab", method = RequestMethod.POST)
    public void getLikeTiplab(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        ArrayList<Tiplab> tiplabs = (ArrayList<Tiplab>) tiplabMapper.selectLikeTipLabByContent(tiplab.getContent());
        baseResBean.setData(tiplabs);
        Tools.printOut(res, baseResBean);
        session.close();
    }


    @RequestMapping(value = "/selectTipLabwithLimit", method = RequestMethod.GET)
    public void selectTipLabwithLimit(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        ArrayList<Tiplab> tiplabs = (ArrayList<Tiplab>) tiplabMapper.selectTipLabwithLimit(Integer.parseInt(req.getParameter("count")));
        baseResBean.setData(tiplabs);
        Tools.printOut(res, baseResBean);
        session.close();
    }

    @RequestMapping(value = "/addLikeTipLab", method = RequestMethod.POST)
    public void addLikeTipLab(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        List<Tiplab> tiplabs = tiplabMapper.selectLikeTipLabByContent(tiplab.getContent());
        if (tiplabs == null || tiplabs.size() == 0) {
            tiplabMapper.insert(tiplab);
        }
        baseResBean.setData(tiplab);
        Tools.printOut(res, baseResBean);
        session.commit();
        session.close();
    }

    @RequestMapping(value = "/addTipLab", method = RequestMethod.POST)
    public void addTipLab(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> map = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(map.get("data"), Tiplab.class);
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        List<Tiplab> tiplabs = tiplabMapper.selectTipLabByContent(tiplab.getContent());
        if (tiplabs == null || tiplabs.size() == 0) {
            tiplabMapper.insert(tiplab);
        }else{
            tiplab = tiplabs.get(0);
        }
        baseResBean.setData(tiplab);
        Tools.printOut(res, baseResBean);
        session.commit();
        session.close();
    }

    @RequestMapping(value = "/getAllTipLabs", method = RequestMethod.GET)
    public void getAllTipLabs(HttpServletRequest req, HttpServletResponse res) {
        Tools.init(req, res);
        SqlSession sqlSession = DBTools.getSession();
        TiplabMapper tiplabMapper = sqlSession.getMapper(TiplabMapper.class);
        ArrayList<Tiplab> tiplabs = (ArrayList<Tiplab>) tiplabMapper.selectAll();
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(tiplabs);
        Tools.printOut(res, baseResBean);
        sqlSession.commit();
        sqlSession.close();

    }


    @RequestMapping(value = "/addLikeTip", method = RequestMethod.POST)
    public void addLikeTip(HttpServletRequest req, HttpServletResponse res) {
        HashMap<String, String> maps = Tools.getStr(req, res);
        Tiplab tiplab = GsonUtil.getInstance().fromJson(maps.get("data"), Tiplab.class);//这里复用tiplab这个类 id为recordid content为tip
        SqlSession session = DBTools.getSession();
        BaseResBean baseResBean = new BaseResBean();
        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        TipMapper tipMapper = session.getMapper(TipMapper.class);
        List<Tiplab> tiplaba = tiplabMapper.selectLikeTipLabByContent(tiplab.getContent());
        List<Tiplab> tiplabb = tiplabMapper.selectTipLabByContent(tiplab.getContent());
        int recordid = tiplab.getId();
        int tiplabid = 0;
        if (tiplaba == null || tiplaba.size() == 0) {
            tiplab.setEnable(1);
            tiplab.setCtime(System.currentTimeMillis());
            tiplabMapper.insert(tiplab);
            tiplabid = tiplab.getId();
        } else {
            //如果有精确匹配的选取精确匹配的 否则选取模糊匹配的
            tiplabid = tiplaba.get(0).getId();
            if (tiplabb != null && tiplabb.size() != 0) {
                tiplabid = tiplabb.get(0).getId();
            }
        }

        Tip tip = new Tip();
        tip.setCtime(System.currentTimeMillis());
        tip.setRecordid(recordid);
        tip.setTipid(tiplabid);

        List<Tip> tips = tipMapper.checkTipIsExist(tip.getRecordid(), tip.getTipid());
        if (tips != null && tips.size() != 0) {

        } else {
            tipMapper.insert(tip);
        }
        baseResBean.setData(tip);
        Tools.printOut(res, baseResBean);
        session.commit();
        session.close();
    }
}
