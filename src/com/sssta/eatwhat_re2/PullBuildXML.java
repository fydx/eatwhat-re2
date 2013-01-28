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
 * ����PULL  ����XML���� 
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
     
        //1.ʹ��android�ṩ��Xml��newһ��XmlPullerParser����newһ��XmlPull������  
        XmlPullParser parser = Xml.newPullParser();  
      
        //2.Ȼ��������Ҫ������xml�ļ�,��һ������Ϊ���������ڶ�������Ϊ�ַ�����  
        parser.setInput(inStream, "UTF-8");  
    
        //3.�����¼����������������ĳ���ַ�����XML�﷨���ͻᴥ������﷨�����������  
        int eventCode = parser.getEventType();  
   
        //4.XML�ļ��ĵ�һ��Ϊ��ʼ�ĵ��¼�START_DOCUMENT�����һ��Ϊ�����ĵ��¼�END_DOCUMENT��������Ҫ���϶�ȡxml�ļ�������  
        while( XmlPullParser.END_DOCUMENT != eventCode ){  
        	
            //5.���Ƕ�����¼����д���,���Ǹ���Ȥ����<FoodList>���Ԫ��  
            switch(eventCode){  
              
            case XmlPullParser.START_DOCUMENT://��ʼ�ĵ��¼�  
                foods = new ArrayList<food>();//��ʼ���������food��List  
                break;  
              
              
            case XmlPullParser.START_TAG://6.�������¼��ǿ�ʼԪ�أ�����<foods>���¼�  
                  
                if("FoodList".equals(parser.getName())){  
                    food = new food();  
                   
                    //7.ʹ�ý������õ���ǰԪ�ص����ԣ���id  
                    food.setId(new Integer(parser.getAttributeValue(0)));  
                      
                }else if(food!=null)
                {  
                    if("name".equals(parser.getName()))
                    {  
                        //8.�����ǰԪ��Ϊ��ʼ��ǩ,��һ��Ԫ��Ϊ�ı����ͻ᷵������ı�  
                        food.setName(parser.nextText());  
                    }
                    else if("place1".equals(parser.getName())){  
                        //8.�����ǰԪ��Ϊ��ʼ��ǩ,��һ��Ԫ��Ϊ�ı����ͻ᷵������ı�  
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
                  
            case XmlPullParser.END_TAG://9.�������¼��ǽ���Ԫ�أ�����</EAT>���¼�  
                  
                if("FoodList".equals(parser.getName()) && food != null){  
                    //��List�����food,����food�ÿգ��Ա��´εĶ�ȡ  
                    foods.add(food);  
                    food = null;  
                    Log.e(TAG, "SUCCESS3");  
                }  
                  
                break;  
            }  
            //10.�����ǵ�һ�ε���getEventType()�������õ�����XML�ļ��ĵ�һ�У���<?xml version="1.0" encoding="UTF-8"?>��������Ҫ������һ�ж�ȡ  
            //���ž�������<EAT>,����һ���¼�,�����¾��ǿո񣬴���һ���¼����������  
            eventCode = parser.next();  
        }  
          
        //11.���ش���õ�list  
        return foods;  
    }
    
}  
  
