package WebServer;

import Servlet.IServlet;
import WebContext.WebContext;

import java.nio.channels.SocketChannel;

public class Dispatcher {

    public Dispatcher(SocketChannel channel){
        Request request = new Request(channel);
        if (!channel.isOpen()) return;
        Response response = new Response(channel);
        String url = request.getUrl();
        if ("/".equals(url) || "".equals(url)){
            response.print("首页");
            response.write(202);
        }else{
            IServlet servlet;
            try {
                servlet = WebContext.getServlet(url);
                try {
                    servlet.service(request,response);
                }catch (Exception e){
                    response.write(505);
                    return;
                }
                response.write(202);
            } catch (Exception e) {
                response.write(404);
            }


        }
    }
}
