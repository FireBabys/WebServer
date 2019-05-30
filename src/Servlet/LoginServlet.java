package Servlet;

import WebServer.Request;
import WebServer.Response;

public class LoginServlet implements  IServlet{

    @Override
    public void service(Request req, Response res) {
        res.print("<html>");
        res.print("<head>");
        res.print("<title>");
        res.print("登录成功");
        res.print("</title>");
        res.print("</head>");
        res.print("<body>");
        res.print("登录成功!");
        res.print("登录人:" + req.getParams().get("name"));
        res.print("</body>");
        res.print("</html>");
    }
}
