package com.summer.util;

import com.google.gson.reflect.TypeToken;
import com.summer.base.bean.BaseBean;
import com.summer.base.bean.BaseResBean;
import com.summer.imageclassfy.AccessTokenBean;
import com.summer.imageclassfy.Base64Image;
import com.summer.imageclassfy.ImageClassifyBean;
import com.summer.mybatis.entity.Crash;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    public static String access_token = "24.f9ef8262da652b3b60f524a7bac60f0d.2592000.1542354359.282335-14458572";

    public static void main(String[] arg) {
//        BaseResBean baseResBean = new BaseResBean();
//        Crash crash = new Crash();
//        crash.setTimestr("201810171421");
//        crash.setCreatedtime(System.currentTimeMillis());
//        crash.setError("test");
//        crash.setUser("summer");
//        baseResBean.setData(GsonUtil.getInstance().toJson(crash));
//        String str = sendPost("http://www.summernecro.com:8888/recordtest/record/crash",baseResBean);
//        System.out.println(str);

        BaseResBean baseResBean = new BaseResBean();
        AccessTokenBean accessTokenBean = new AccessTokenBean();
        accessTokenBean.setClient_id("jTQgrGjed1ZoOPpTT1xrb4A5");
        accessTokenBean.setClient_secret("7NFC3NLAYwaV8SkO3SQzdgNNv7Gt5585");
        accessTokenBean.setGrant_type("client_credentials");
        baseResBean.setData(accessTokenBean);
        String str = sendPost("https://aip.baidubce.com/oauth/2.0/token", baseResBean, null);
        System.out.println(str);


        ImageClassifyBean imageClassifyBean = new ImageClassifyBean();
        imageClassifyBean.setAccess_token(access_token);
        imageClassifyBean.setBaike_num("0");
        String image = "";
        try {
            image = URLEncoder.encode(Base64Image.getImageStr("E:\\record\\20180329\\Screenshot_2018-03-29-16-39-22-181_com.tencent.mm.png"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        imageClassifyBean.setImage(image);
        BaseResBean bean = new BaseResBean();
        bean.setData(imageClassifyBean);

        HashMap<String, String> map = new HashMap<>();
        map.put("access_token", access_token);

        String out = sendPost("https://aip.baidubce.com/rest/2.0/image-classify/v2/advanced_general", bean, map);
        System.out.println(out);

    }

    public static String sendGet(String url, BaseResBean baseBean, HashMap<String, String> header) {
        return sendGet(url, getParams(baseBean), header);
    }

    public static String sendPost(String url, BaseResBean baseBean, HashMap<String, String> header) {
        return sendPost(url, getParams(baseBean), header);
    }

    public static String getParams(BaseResBean baseBean) {
        Map<String, String> map = (Map) GsonUtil.getInstance().fromJson(GsonUtil.getInstance().toJson(baseBean.getData()), (new TypeToken<Map<String, String>>() {
        }).getType());
        String req = "";
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            String param = entry.getKey() + "=" + entry.getValue();
            req = req + "&" + param;
        }
        if (req.startsWith("&")) {
            req = req.substring(1, req.length());
        }
        return req;
    }

    /**
     * 向指定URL发送GET方法的请求
     *
     * @param url   发送请求的URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, HashMap<String, String> header) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (header != null) {
                Iterator<Map.Entry<String, String>> iterator = header.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    connection.setRequestProperty(entry.getKey(), entry.getValue());
                }

            }
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url   发送请求的 URL
     * @param param 请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param, HashMap<String, String> header) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            if (header != null) {
                Iterator<Map.Entry<String, String>> iterator = header.entrySet().iterator();
                while (iterator.hasNext()) {
                    Map.Entry<String, String> entry = iterator.next();
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }

            }

            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
