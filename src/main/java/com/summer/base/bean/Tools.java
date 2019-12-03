package com.summer.base.bean;

import com.summer.util.GsonUtil;
import org.apache.commons.io.IOUtils;
import org.apache.tools.ant.taskdefs.condition.HasMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

/**
 * Created by SWSD on 2018-03-26.
 */
public class Tools {

    public static  void init(HttpServletRequest req, HttpServletResponse res){
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        res.setHeader("Access-Control-Allow-Origin", "*");//跨域
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");
    }

    public static  void init2(HttpServletRequest req, HttpServletResponse res){
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        res.setHeader("Access-Control-Allow-Origin", "*");//跨域
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/x-www-form-urlencoded");
    }

    public static void printOut(HttpServletResponse res,Object o){
        try {
            PrintWriter printWriter = res.getWriter();
            printWriter.println(GsonUtil.getInstance().toJson(o));
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void printOutData(HttpServletResponse res,Object o){
        BaseResBean baseResBean = new BaseResBean();
        baseResBean.setData(o);
        try {
            PrintWriter printWriter = res.getWriter();
            printWriter.println(GsonUtil.getInstance().toJson(baseResBean));
            printWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 请求body获取键值对
     * @param req
     * @return
     */
    public static HashMap<String,String>  getStr(HttpServletRequest req,HttpServletResponse res){
        try {
            req.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setCharacterEncoding("UTF-8");
        res.setContentType("application/json;charset=UTF-8");
        String result = null;
        HashMap<String,String> str = new HashMap<>();
        try {
            result = IOUtils.toString(req.getInputStream(), StandardCharsets.UTF_8);
            result = URLDecoder.decode(result, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        String[] strs = result.split("&");
        for(int i=0;i<strs.length;i++){
            String[] ss = strs[i].split("=");
            str.put(ss[0],ss[1]);
        }
        return str;
    }
}
