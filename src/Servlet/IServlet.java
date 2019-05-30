package Servlet;

import WebServer.Request;
import WebServer.Response;

public interface IServlet {

    void service(Request req, Response res);
}
