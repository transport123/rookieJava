package com.sannity.servlets;



import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(urlPatterns = "/")
public class HelloServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //设置响应类型
        resp.setContentType("text/html");

        //获取输出流
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello head!</h1>");

        //强制输出
        pw.flush();

    }
    //tomcat 10 运行需要jdk11及更高的版本
    // <failOnMissingWebXml>false</failOnMissingWebXml> 忽略编译期间webxml信息的缺失
}
