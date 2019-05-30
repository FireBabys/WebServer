package Servlet;

import WebServer.Request;
import WebServer.Response;

import java.util.Arrays;
import java.util.List;

public class RegisterServlet implements IServlet {


    @Override
    public void service(Request req, Response res) {
        String name = req.getParam().get("username");
        List<String> aihao = req.getParams().get("aihao");
        String s = Arrays.toString(aihao.toArray());
        res.print("注册成功");
        res.print("用户名: " + name);
        res.print("爱好: " + s);
    }

}
