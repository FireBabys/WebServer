package WebContext;

import Servlet.IServlet;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebContext {

    //一个Servlet只能对应一个class
    //一个Servlet可以有多个 mapping 进行访问
    //因为map的key是不能重复的,所以,可以通过这个特性创建下面两个map

    //存储servlet标签 Key:WebContext-name value:WebContext-class
    static Map<String, String> servletMap = new HashMap<>();
    //存储mapping标签 key:url-mapping value = WebContext-name
    static Map<String, String> mappingMap = new HashMap<>();

    public WebContext() throws DocumentException {

        //解析XML进行存储
        SAXReader saxReader = new SAXReader();
        Document doc = null;
        doc = saxReader.read(new File("src/WebContext/Web.xml"));
        Element root = doc.getRootElement();
        List<Element> elements = root.elements();
        for (Element element : elements) {
            if (element.getName().equals("servlet")){
                servletMap.put(element.element("servlet-name").getText(),element.element("servlet-class").getText());
            }
            if (element.getName().equals("servlet-mapping")){
                List<Element> urlPattern = element.elements("url-pattern");
                for (Element pattern : urlPattern) {
                    mappingMap.put(pattern.getText(),element.element("servlet-name").getText());
                }
            }
        }

    }

    //根据传入的URL获取class地址
    private static String getServletClass(String url){
        //根据URL在mappingMap中查找出servlet的class
        String serlvetName = mappingMap.get(url);
        //根据URL来获取serlvetClass
        String serlvetClass = servletMap.get(serlvetName);
        //返回
        return serlvetClass;
    }

    //反射获取Servlet
    public static IServlet getServlet(String url) throws Exception {
        IServlet servlet = (IServlet) Class.forName(getServletClass(url)).getConstructor().newInstance();
        return servlet;
    }

}
