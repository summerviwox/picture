package com.summer.base.bean;

import com.summer.util.GsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

/**
 * Created by SWSD on 2018-03-26.
 */
public class Tools {

    public static  void init(HttpServletRequest req, HttpServletResponse rep){
        try {
            req.setCharacterEncoding("UTF-8");
            rep.setHeader("Access-Control-Allow-Origin", "*");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        rep.setCharacterEncoding("UTF-8");
        rep.setContentType("application/json;charset=UTF-8");
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
}
