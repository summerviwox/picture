package com.summer.mybatis;

import java.io.*;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class DBTools {

    public static SqlSessionFactory sessionFactory;

    //创建能执行映射文件中sql的sqlSession
    public static SqlSession getSession(){
        if(sessionFactory==null){
            try {
                //使用MyBatis提供的Resources类加载mybatis的配置文件
                File classpath = Resources.getResourceAsFile("");
                File root = classpath.getParentFile();
                File file = new File(root,"mybatis.xml");
                Reader reader = new FileReader(file);
                //构建sqlSession的工厂
                sessionFactory = new SqlSessionFactoryBuilder().build(reader);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        SqlSession session = null;
        try {
            session = sessionFactory.openSession();
        } catch (Exception e) {
            e.printStackTrace();
            sessionFactory=null;
        }
        return session;
    }

    public static  Properties getMysqlProperties(){
        Properties properties = new Properties();
        try {
            File classpath = Resources.getResourceAsFile("");
            File root = classpath.getParentFile();
            File file = new File(root,"mybatis.xml");
            InputStream in = new FileInputStream(file);
            properties.load(in);
            // 获取驱动名称、url、用户名以及密码
            String driverName = properties.getProperty("driverName");
            String url = properties.getProperty("url");
            String user = properties.getProperty("user");
            String password = properties.getProperty("password");

        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    private static void execSqlFileByMysql(String driver,String url,String userName,String pwd,String sqlFilePath) throws Exception{


        Exception error = null;
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, userName, pwd);
            ScriptRunner runner = new ScriptRunner(conn);
            //下面配置不要随意更改，否则会出现各种问题
            runner.setAutoCommit(true);//自动提交
            runner.setFullLineDelimiter(false);
            runner.setDelimiter(";");////每条命令间的分隔符
            runner.setSendFullScript(false);
            runner.setStopOnError(false);
            //  runner.setLogWriter(null);//设置是否输出日志
            //如果又多个sql文件，可以写多个runner.runScript(xxx),
            runner.runScript(new InputStreamReader(new FileInputStream(sqlFilePath),"utf-8"));
            close(conn);
        } catch (Exception e) {
            System.out.println("执行sql文件进行数据库创建失败...."+e.getMessage());
            error = e;
        }finally{
            close(conn);
        }
        if(error != null){
            throw error;
        }
    }

    private static void close(Connection conn){
        try {
            if(conn != null){
                conn.close();
            }
        } catch (Exception e) {
            if(conn != null){
                conn = null;
            }
        }
    }

}