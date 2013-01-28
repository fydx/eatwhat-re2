package com.sssta.eatwhat_re2;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import android.util.Log;
import android.util.Xml;

/** 
 * 采用PULL  生成XML数据 
 * @author Administrator 
 * 
 */  
public class PullBuildXML 
{  
    /** 
     *  
     * @param foods 
     * @param outputStream 
     * @throws Exception 
     */  
	private static final String TAG = "XML";
    public static void buildXML(List<food> eat,Writer writer) throws Exception  
    {  
        XmlSerializer serializer=Xml.newSerializer();  
        serializer.setOutput(writer);  
          
        serializer.startDocument("UTF-8", true);  
        serializer.startTag(null, "EAT");  
          
        for(food food:eat)  
        {  
            serializer.startTag(null, "FoodList");  
            serializer.attribute(null, "id", food.getId().toString()); 
              
            serializer.startTag(null, "name");  
            serializer.text(food.getName().toString());  
            serializer.endTag(null, "name");  
              
            serializer.startTag(null, "place1");  
            serializer.text(food.getPlace1().toString());  
            serializer.endTag(null, "place1");  
            
            serializer.startTag(null, "place2");  
            serializer.text(food.getPlace2().toString());  
            serializer.endTag(null, "place2"); 
            serializer.startTag(null, "rate");  
            serializer.text(String.valueOf(food.getRate()));  
            serializer.endTag(null, "rate"); 
            serializer.startTag(null, "price");  
            serializer.text(String.valueOf(food.getPrice()));  
            serializer.endTag(null, "price"); 
            
            serializer.endTag(null, "FoodList");  
        }  
          
        serializer.endTag(null, "EAT");  
        serializer.endDocument();  
        writer.flush();  
        writer.close();  
    }  
    public static void writeXML(List<food> eat,Writer writer) throws Exception  
    {  
        XmlSerializer serializer=Xml.newSerializer();  
        serializer.setOutput(writer);  
          
        serializer.startDocument("UTF-8", true);  
        serializer.startTag(null, "EAT");  
          
        for(food food:eat)  
        {  
            serializer.startTag(null, "FoodList");  
            serializer.attribute(null, "id", food.getId().toString()); 
              
            serializer.startTag(null, "name");  
            serializer.text(food.getName().toString());  
            serializer.endTag(null, "name");  
              
            serializer.startTag(null, "place1");  
            serializer.text(food.getPlace1().toString());  
            serializer.endTag(null, "place1");  
            
            serializer.startTag(null, "place2");  
            serializer.text(food.getPlace2().toString());  
            serializer.endTag(null, "place2"); 
            serializer.startTag(null, "rate");  
            serializer.text(String.valueOf(food.getRate()));  
            serializer.endTag(null, "rate"); 
            serializer.startTag(null, "price");  
            serializer.text(String.valueOf(food.getPrice()));  
            serializer.endTag(null, "price"); 
            
            serializer.endTag(null, "FoodList");  
        }  
          
        serializer.endTag(null, "EAT");  
        serializer.endDocument();  
        writer.flush();  
        writer.close();  
    }  
    public static List<food> readXml(InputStream inStream) throws Exception{  
        
        List<food> foods = null;  
        food food = null;  
     
        //1.使用android提供的Xml类new一个XmlPullerParser，即new一个XmlPull解析器  
        XmlPullParser parser = Xml.newPullParser();  
      
        //2.然后设置需要解析的xml文件,第一个参数为输入流，第二个参数为字符编码  
        parser.setInput(inStream, "UTF-8");  
    
        //3.触发事件，当这个方法遇到某个字符符合XML语法，就会触发这个语法所代表的数字  
        int eventCode = parser.getEventType();  
   
        //4.XML文件的第一行为开始文档事件START_DOCUMENT，最后一行为结束文档事件END_DOCUMENT，我们需要不断读取xml文件的内容  
        while( XmlPullParser.END_DOCUMENT != eventCode ){  
        	
            //5.我们对这个事件进行处理,我们感兴趣的是<FoodList>这个元素  
            switch(eventCode){  
              
            case XmlPullParser.START_DOCUMENT://开始文档事件  
                foods = new ArrayList<food>();//初始化用来存放food的List  
                break;  
              
              
            case XmlPullParser.START_TAG://6.如果这个事件是开始元素（例如<foods>）事件  
                  
                if("FoodList".equals(parser.getName())){  
                    food = new food();  
                   
                    //7.使用解析器得到当前元素的属性，即id  
                    food.setId(new Integer(parser.getAttributeValue(0)));  
                      
                }else if(food!=null)
                {  
                    if("name".equals(parser.getName()))
                    {  
                        //8.如果当前元素为开始标签,下一个元素为文本，就会返回这个文本  
                        food.setName(parser.nextText());  
                    }
                    else if("place1".equals(parser.getName())){  
                        //8.如果当前元素为开始标签,下一个元素为文本，就会返回这个文本  
                        food.setPlace1(parser.nextText()); 
                    }
                        else if("place2".equals(parser.getName())){  
                            
                            food.setPlace2(parser.nextText()); 
                    }  
                        else if("rate".equals(parser.getName())){  
                            
                            food.setRate(new Integer(parser.nextText())); 
                        }
                        else if("price".equals(parser.getName())){  
                                
                            food.setPrice(new Double(parser.nextText())); 
                    }  
                }  
                break;  
                  
            case XmlPullParser.END_TAG://9.如果这个事件是结束元素（例如</EAT>）事件  
                  
                if("FoodList".equals(parser.getName()) && food != null){  
                    //往List里添加food,并把food置空，以便下次的读取  
                    foods.add(food);  
                    food = null;  
                    Log.e(TAG, "SUCCESS3");  
                }  
                  
                break;  
            }  
            //10.当我们第一次调用getEventType()方法，得到的是XML文件的第一行，即<?xml version="1.0" encoding="UTF-8"?>，所以需要再往下一行读取  
            //接着就遇到了<EAT>,触发一个事件,再往下就是空格，触发一个事件，如此类推  
            eventCode = parser.next();  
        }  
          
        //11.返回储存好的list  
        return foods;  
    }
    
}  
  
