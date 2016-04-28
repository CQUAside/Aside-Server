package com.round.aside.server.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.round.aside.server.entity.RegisterResultEntity;
import com.round.aside.server.module.ModuleObjectPool;
import com.round.aside.server.module.accountmanager.IAccountManager;

import static com.round.aside.server.constant.StatusCode.*;

/**
 * 
 * 
 * @author A Shuai
 * @date 2016-4-20
 *
 */
public class RegisteSimpleServlet extends HttpServlet {

    /**
     * 
     */
    private static final long serialVersionUID = -370069543404251212L;

    /**
     * Constructor of the object.
     */
    public RegisteSimpleServlet() {
        super();
    }

    /**
     * Destruction of the servlet. <br>
     */
    public void destroy() {
        super.destroy(); // Just puts "destroy" string in log
        // Put your code here
    }

    /**
     * The doGet method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to get.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
        out.println("<HTML>");
        out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
        out.println("  <BODY>");
        out.print("    This is ");
        out.print(this.getClass());
        out.println(", using the GET method");
        out.println("  </BODY>");
        out.println("</HTML>");
        out.flush();
        out.close();
    }

    /**
     * The doPost method of the servlet. <br>
     *
     * This method is called when a form has its tag value method equals to post.
     * 
     * @param request the request send by the client to the server
     * @param response the response send by the server to the client
     * @throws ServletException if an error occurred
     * @throws IOException if an error occurred
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String mAccount = request.getParameter("account");
        String mPassword = request.getParameter("password");
        
        IAccountManager mAccountManager = ModuleObjectPool.getModuleObject(IAccountManager.class, null);
        RegisterResultEntity mRegisterResult = mAccountManager.registerAccount(mAccount, mPassword);
        
        if(mRegisterResult.getStatusCode() == S1000){
            response.sendRedirect("/Aside-Server/WEB-INF/jsp/hello.jsp");
        } else {
            
        }
        
    }

    /**
     * Initialization of the servlet. <br>
     *
     * @throws ServletException if an error occurs
     */
    public void init() throws ServletException {
        // Put your code here
    }

}
