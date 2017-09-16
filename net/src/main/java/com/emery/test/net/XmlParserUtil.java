package com.emery.test.net;

import android.content.Context;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by MyPC on 2017/2/22.
 */

public class XmlParserUtil {
    public static ArrayList<CityInfo> domParseXml(Context context,String xml){
        ArrayList<CityInfo> cityInfos=new ArrayList<>();
        Document document =null;
        DocumentBuilder builder=null;
        DocumentBuilderFactory factory=null;
        InputStream inputStream=null;
        factory=DocumentBuilderFactory.newInstance();
        try {
            builder=factory.newDocumentBuilder();
            inputStream = context.getAssets().open(xml);
            document=builder.parse(inputStream);
            //获取根节点,如citys
            Element rootElement = document.getDocumentElement();//
            //获得所有的子节点,如city
            NodeList citys = rootElement.getElementsByTagName("city");
            CityInfo cityInfo=null;
            for (int i = 0; i < citys.getLength(); i++) {
                cityInfo=new CityInfo();
                //拿到其中一个节点,如 北京
                Element city = (Element) citys.item(i);
                cityInfo.setId(city.getAttribute("id"));
                Element cityNameElement = (Element) city.getElementsByTagName("name").item(0);
                cityInfo.setName(cityNameElement.getFirstChild().getNodeValue());
                Element codeElement = (Element) city.getElementsByTagName("code").item(0);
                cityInfo.setCode(codeElement.getFirstChild().getNodeValue());
                System.out.println(cityInfo.toString());
                cityInfos.add(cityInfo);

            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return cityInfos;
    }
}
