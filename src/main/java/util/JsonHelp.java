package util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import model.Point;


import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author ldd
 * @date 2019/6/26
 * @function  解析json 文件类功能封装
 * */
public class JsonHelp {
    private static final JsonHelp jsonHelp = new JsonHelp();
    private static Logger logger=Logger.getLogger("JsonHelp");
    public JsonHelp() {
        super();
    }
    public static JsonHelp getInstance() {
        if(jsonHelp!=null)
            return jsonHelp;
        else return new JsonHelp();
    }

    /**
     * @function 通过解析json file 得到HashMap<Integer, Point>points
     * @param str jsonfile中一个节点对各邻居节点 id
     * "jsonObject6":[
     *     {"1":{"distance":0,"port":3035,"name":"F"}},
     *     {"2":{"distance":2,"port":3034,"name":"E"}},
     *     {"3":{"distance":5,"port":3032,"name":"C"}}
     *   ]
     * */
    public HashMap<Integer, Point>parsePoints(String str){
        String file="D:\\IDEA\\project\\dsdv\\src\\main\\resources\\";
        String s=readJsonFile(file+"topology.json");         //java命令行运行目录存在问题
        JSONObject jobj = JSON.parseObject(s);
        logger.info("jsonFile:\n"+jobj.get(str));
        JSONArray links = jobj.getJSONArray(str);
        List<Point> tempPoints =  JSONArray.parseArray(links.toJSONString(), Point.class);
        HashMap<Integer, Point>points=new HashMap<Integer, Point>();
        for (int i = 0 ; i < links.size();i++){
            JSONObject key1 = (JSONObject)links.get(i);
            Point point=JSONObject.parseObject(key1.get(i+1).toString(),Point.class);
            points.put(i+1,point);
        }
       return points;
    }

    /**
     * 读取json文件，返回json串
     * @param fileName
     * @return
     */
    public static String readJsonFile(String fileName) {
        String jsonStr = "";
        try {
            File jsonFile = new File(fileName);
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile),"utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * 功能描述：把JSON数据转换成指定的java对象
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return 指定的java对象
     */
    public static <T> T getJsonToBean(String jsonData, Class<T> clazz) {
        return JSON.parseObject(jsonData, clazz);
    }

    /**
     * 功能描述：把java对象转换成JSON数据
     * @param object java对象
     * @return JSON数据
     */
    public static String getBeanToJson(Object object) {
        return JSON.toJSONString(object);
    }

    /**
     * 功能描述：把JSON数据转换成指定的java对象列表
     * @param jsonData JSON数据
     * @param clazz 指定的java对象
     * @return List<T>
     */
    public static <T> List<T> getJsonToList(String jsonData, Class<T> clazz) {
        return JSON.parseArray(jsonData, clazz);
    }

    /**
     * 功能描述：把JSON数据转换成较为复杂的List<Map<String, Object>>
     * @param jsonData JSON数据
     * @return List<Map<String, Object>>
     */
    public static List<Map<String, Object>> getJsonToListMap(String jsonData) {
        return JSON.parseObject(jsonData, new TypeReference<List<Map<String, Object>>>() {
        });
    }

}
