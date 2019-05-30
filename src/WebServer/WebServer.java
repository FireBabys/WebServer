package WebServer;

import WebContext.WebContext;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

public class WebServer {

    Selector selector;
    ServerSocketChannel channel;
    WebContext webContext;
    private Boolean flag = false;

    public static void main(String[] args) {

        WebServer webServer = new WebServer();
        webServer.start();
        webServer.handler();
        webServer.close();
    }


    public void start(){
        try {
            selector = Selector.open();
            channel = ServerSocketChannel.open();
            channel.socket().bind(new InetSocketAddress(7777));
            channel.configureBlocking(false);
            channel.register(selector, SelectionKey.OP_ACCEPT);
            webContext = new WebContext(); //加载配置文件
            System.out.println("服务器启动成功!");
        } catch (Exception e) {
            System.out.println("服务器启动失败!");
        }
    }


    public void handler(){
        while (!flag) {
            try {
                int select = selector.select();
                if (select == 0 ) continue;
            } catch (IOException e) {
                System.out.println("接收异常!");
                return;
            }
            Iterator<SelectionKey> itKey = selector.selectedKeys().iterator();
            while (itKey.hasNext()) {
                SelectionKey key = itKey.next();
                itKey.remove();
                if (key.isAcceptable()) {
                    handlerAccept(key);
                }else if (key.isReadable()) {
                    handlerReader(key);
                }
            }
        }
    }

    //处理读数据
    private void handlerReader(SelectionKey key) {
        SocketChannel channel = (SocketChannel)key.channel();
        new Dispatcher(channel);
    }


    public void sendMsg(SocketChannel channel,String s){
        try {
            channel.write(ByteBuffer.wrap(s.getBytes()));
        } catch (IOException e) {
            System.out.println("发送响应异常!");
            try {
                channel.close();
            } catch (IOException ex) {
                System.out.println("关闭通道异常!");
            }
        }
    }

    //处理请求
    private void handlerAccept(SelectionKey key) {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel)key.channel();
        SocketChannel accept = null;
        try {
            accept = serverSocketChannel.accept();
            if (accept.isConnectionPending()) {
                accept.finishConnect();
            }
            accept.configureBlocking(false);
            accept.register(selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            System.out.println("处理客户端链接请求时异常!");
        }

    }


    public void close(){
        flag = true;
        try {
            channel.close();
            selector.close();
        } catch (IOException e) {
            System.out.println("关闭服务器异常!");
        }

    }
}
