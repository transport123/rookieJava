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
        //������Ӧ����
        resp.setContentType("text/html");

        //��ȡ�����
        PrintWriter pw = resp.getWriter();
        pw.write("<h1>Hello head!</h1>");

        //ǿ�����
        pw.flush();

    }
    //tomcat 10 ������Ҫjdk11�����ߵİ汾
    // <failOnMissingWebXml>false</failOnMissingWebXml> ���Ա����ڼ�webxml��Ϣ��ȱʧ
}
