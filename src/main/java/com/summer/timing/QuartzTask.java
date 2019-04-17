package com.summer.timing;

import com.summer.base.bean.BaseResBean;
import com.summer.base.bean.Tools;
import com.summer.imageclassfy.*;
import com.summer.mybatis.DBTools;
import com.summer.mybatis.entity.Record;
import com.summer.mybatis.entity.Tip;
import com.summer.mybatis.entity.Tiplab;
import com.summer.mybatis.mapper.RecordMapper;
import com.summer.mybatis.mapper.TipMapper;
import com.summer.mybatis.mapper.TiplabMapper;
import com.summer.util.DateFormatUtil;
import com.summer.util.GsonUtil;
import com.summer.util.HttpRequest;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.summer.util.HttpRequest.sendPost;


@Component("taskJob")
public class QuartzTask {

    /**
     * CRON表达式                含义 
    "0 0 12 * * ?"            每天中午十二点触发 
    "0 15 10 ? * *"            每天早上10：15触发 
    "0 15 10 * * ?"            每天早上10：15触发 
    "0 15 10 * * ? *"        每天早上10：15触发 
    "0 15 10 * * ? 2005"    2005年的每天早上10：15触发 
    "0 * 14 * * ?"            每天从下午2点开始到2点59分每分钟一次触发 
    "0 0/5 14 * * ?"        每天从下午2点开始到2：55分结束每5分钟一次触发 
    "0 0/5 14,18 * * ?"        每天的下午2点至2：55和6点至6点55分两个时间段内每5分钟一次触发 
    "0 0-5 14 * * ?"        每天14:00至14:05每分钟一次触发 
    "0 10,44 14 ? 3 WED"    三月的每周三的14：10和14：44触发 
    "0 15 10 ? * MON-FRI"   每个周一、周二、周三、周四、周五的10：15触发
     */
    
    /**
     * 每天4点触发（清空验证码表t_captcha中的数据）
     */
    @Scheduled(cron = "0 10 4 * * ?")
    public void testTask(){
        System.out.println("test quarttask "+DateFormatUtil.getdDateStr(DateFormatUtil.SECOND+"-"+DateFormatUtil.MINUTE+"-"+DateFormatUtil.HOUR,new Date()));

        init();
    }

    @Scheduled(cron = "*/30 * * * * ?")
    public void testTask2(){
        System.out.println("testTask2--"+DateFormatUtil.getNowStr(DateFormatUtil.YYYY_MM_DD_HH_MM_SS));
    }


    public void init(){
        BaseResBean baseResBean = new BaseResBean();
        SqlSession session = DBTools.getSession();
        RecordMapper recordMapper =  session.getMapper(RecordMapper.class);
        ArrayList<Record> records = (ArrayList<Record>) recordMapper.selectAllNotImageCheckWithLimit(500);

        TiplabMapper tiplabMapper = session.getMapper(TiplabMapper.class);
        TipMapper tipMapper = session.getMapper(TipMapper.class);

        BaseResBean baseResBean1 = new BaseResBean();
        AccessTokenBean accessTokenBean = new AccessTokenBean();
        accessTokenBean.setClient_id("jTQgrGjed1ZoOPpTT1xrb4A5");
        accessTokenBean.setClient_secret("7NFC3NLAYwaV8SkO3SQzdgNNv7Gt5585");
        accessTokenBean.setGrant_type("client_credentials");
        baseResBean1.setData(accessTokenBean);
        String str = HttpRequest.sendPost("https://aip.baidubce.com/oauth/2.0/token",baseResBean1,null);

        System.out.println("str:"+str+"--"+records.size());
        AccessRes access_token = GsonUtil.getInstance().fromJson(str,AccessRes.class);


        for(int i=0;i<records.size();i++){

            //改记录已经识别过一次
            if(records.get(i).getClassify()==1||records.get(i).getClassify()==2){
                continue;
            }

            ImageClassifyBean imageClassifyBean = new ImageClassifyBean();
            imageClassifyBean.setAccess_token(access_token.getAccess_token());
            imageClassifyBean.setBaike_num("0");
            String image ="";
            try {
                image = URLEncoder.encode(Base64Image.getImageStr(records.get(i).getNetpath()),"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                continue;
            }
            imageClassifyBean.setImage(image);
            BaseResBean bean = new BaseResBean();
            bean.setData(imageClassifyBean);

            String out = HttpRequest.sendPost("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general",bean,null);
            System.out.println(records.get(i).getId()+" -- 第"+i+"："+out);
            ImageClassifyRes imageClassifyRes = GsonUtil.getInstance().fromJson(out,ImageClassifyRes.class);

            //返回的识别特征为0或识别错误 继续下一个
            if(imageClassifyRes==null||imageClassifyRes.getError_code()!=null||imageClassifyRes.getResult_num()==0){
                //标记改record已经识别过一次 并且无法识别
                recordMapper.updateClassify(records.get(i).getId(),2);
                continue;
            }

            //依次将特征与record关联
            for(int j=0;j<imageClassifyRes.getResult_num();j++){
                float score = Float.parseFloat(imageClassifyRes.getResult().get(j).getScore());
                //去除掉匹配度低的特征
                if(score<0.5){
                    continue;
                }
                //检查特征库中是否存在该特征
                List<Tiplab> tiplabs = tiplabMapper.selectTipLabByContent(imageClassifyRes.getResult().get(j).getKeyword());
                //有该特征
                if(tiplabs!=null&&tiplabs.size()>0){
                    //判断是否存在record 特征记录
                    ArrayList<Tip> tips = (ArrayList<Tip>) tipMapper.isTipExist(records.get(i).getId(),tiplabs.get(0).getId());
                    //没有就添加
                    if(tips==null|| tips.size()==0){
                        Tip tip = new Tip();
                        tip.setRecordid(records.get(i).getId());
                        tip.setCtime(System.currentTimeMillis());
                        tip.setTipid(tiplabs.get(0).getId());
                        tipMapper.insert(tip);
                        session.commit();
                    }
                }else{
                    //没有改特征 添加该特征到特征库中 并关联record
                    Tiplab tiplab = new Tiplab();
                    tiplab.setContent(imageClassifyRes.getResult().get(j).getKeyword());
                    tiplab.setCtime(System.currentTimeMillis());

                    tiplabMapper.insert(tiplab);
                    int tiplabid =  tiplabMapper.selectTipLabByContent(tiplab.getContent()).get(0).getId();

                    Tip tip = new Tip();
                    tip.setRecordid(records.get(i).getId());
                    tip.setCtime(System.currentTimeMillis());
                    tip.setTipid(tiplabid);
                    tipMapper.insert(tip);
                }
            }
            //标记改record已经识别过一次
            recordMapper.updateClassify(records.get(i).getId(),1);
        }

        session.commit();
        session.close();
        baseResBean.setData(true);
    }

}