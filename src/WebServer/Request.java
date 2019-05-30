package WebServer;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.*;

public class Request {

    //请求内容
    private String requsetText;
    //请求方式
    private String method;
    //请求的URL
    private String url;
    //请求参数
    private String parameter = "";
    //分解参数
    private Map<String, List<String>> paramsMap = new HashMap<String, List<String>>();  //接收多参数
    private Map<String, String> prarmMap = new HashMap<>();     //接收单个参数


    public Request(SocketChannel channel) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        String msg = "";
        try {
            int read = channel.read(byteBuffer);
            if (read > 0) {
                byteBuffer.flip();
                while (byteBuffer.hasRemaining())
                    msg += new String(byteBuffer.get(new byte[byteBuffer.limit()]).array());
                byteBuffer.clear();
                this.requsetText = msg;
                handleMethod();
                handleUrl();
                handleParameter();
                if(parameter != "") handleParamsMap();
            } else {
                channel.close();
            }
        } catch (IOException e) {
            System.out.println("获取数据时异常!");
        }
    }
    //解析方法
    public void handleMethod() {
        if (requsetText != null) {
            this.method = requsetText.substring(0, requsetText.indexOf("/")).trim().toLowerCase();
        }
    }
    //解析URL
    public void handleUrl() {
        if (requsetText != null) {
            this.url = requsetText.substring(requsetText.indexOf("/"), requsetText.indexOf("HTTP/")).trim();
            int i = this.url.indexOf("?");
            if (i >= 0) {
                String[] split = this.url.split("\\?");
                url = split[0];
                parameter = decode(split[1],"utf-8");
            }


        }
    }
    //解析参数
    public void handleParameter() {
        if (method.equals("post")) {
            String trim = requsetText.substring(requsetText.lastIndexOf("\r\n")).trim();
            if (parameter != "") {
                parameter += "&" + trim;
            } else {
                parameter += trim;
            }
        }
    }
    //解析每个参数
    public void handleParamsMap(){
        String[] split = parameter.split("&");  //分割字符串
        for (String s : split) {
            String[] split1 = s.split("=");
            split1 = Arrays.copyOf(split1, 2);
            if (!paramsMap.containsKey(split1[0])){
                paramsMap.put(split1[0], new ArrayList<>());
            }
            paramsMap.get(split1[0]).add(decode(split1[1],"utf-8"));
            prarmMap.put(split1[0],decode(split1[1],"utf-8"));
        }
    }

    public Map<String, List<String>> getParams() {
        return paramsMap;
    }

    public Map<String, String> getParam() {
        return prarmMap;
    }

    public String getUrl() {
        return url;
    }

    public String decode(String value,String charset){

        try {
            String decode = URLDecoder.decode(value, charset);
            return decode;
        } catch (Exception e) {
            System.err.println("参数转换异常");
            return value;
        }

    }
}
