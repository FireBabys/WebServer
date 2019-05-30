package WebServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

public class Response {

    private SocketChannel channel;
    private final String BLANK = " ";
    private final String CRLF = "\r\n";
    private StringBuilder content = new StringBuilder();
    StringBuilder responseInfo;
    private int len = 0;

    public Response(SocketChannel channel){
        this.channel = channel;
    }

    public void write(int code){
        String text = getHeader(code);
        try {
            channel.write(ByteBuffer.wrap(text.getBytes()));
            channel.close();
        } catch (IOException e) {
            System.out.println("发送失败!");
        }
    }

    public void print(String s){
        content.append(s + CRLF);
    }

    private String getHeader(int code){
        String info = "";
        switch (code) {
            case 200:
                info = "成功!";
                break;
            case 404:
                info = "无法找到页面";
                content.append("无法找到页面!");
                break;
            case 500:
                info = "服务器错误!";
                content.append("Internal Server Error 服务器内部错误。演示页面抛出异常");
                break;
            default:
                info = "未知错误!";
        }
        len = content.toString().getBytes().length;
        responseInfo = new StringBuilder();
        responseInfo.append("HTTP/1.1").append(BLANK);
        responseInfo.append(code).append(BLANK);
        responseInfo.append(info).append(CRLF);
        responseInfo.append("Date:").append(new Date()).append(CRLF);
        responseInfo.append("Server:").append("MyServer").append(CRLF);
        responseInfo.append("Content-type:text/html;charset=UTF-8").append(CRLF);
        responseInfo.append("Content-length:").append(len).append(CRLF);
        responseInfo.append(CRLF);
        responseInfo.append(content.toString());
        String s = responseInfo.toString();
        return s;
    }

}
