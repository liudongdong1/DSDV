package service;

import model.Point;
import model.RouteTableItem;
import util.CSVFileHelp;
import util.JsonHelp;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

public class StartService {
    private static Logger logger=Logger.getLogger("StartService");
    private HashMap<Integer, Point> points = new HashMap<Integer, Point>();
    private HashMap<Integer, RouteTableItem> routeTables = new HashMap<Integer, RouteTableItem>();


    /**
     * @param args 存储节点的一跳邻居几点文件，例如resourse 下 a.csv.. 或.json 文件下一个记录 id
     * @function
     * */
    public StartService(String[] args) {
        String file="src/main/resources/";
         if(args.length > 0) {
           initTable(args[0]);
        } else {
            initTable("a.csv");
            //initTable("a.csv");
        }
    }
    /**
     * @function 判断命令行是 a.csv 还是 a 形式
     * */
    public boolean fileChoose(String file){
        if(file.contains("."))
            return true;
        else return false;
    }

    /**
     *
     * 如果从csv:输入  a.csv , b.csv , ... ,f.csv
     * 如果从json 输入： a , b, c , ... , f
     * */
    public void initTable(String file){
        if(fileChoose(file)){
            file="D:\\IDEA\\project\\dsdv\\src\\main\\resources\\"+file;
            logger.info("Reading from file: " + file);
            points = CSVFileHelp.getInstance().importCsv(file);

        }else{
            logger.info("Reading from json: " + file);
            points = JsonHelp.getInstance().parsePoints(file);

        }
        //Building routing table
        for (Map.Entry<Integer, Point> tempPoint : points.entrySet()) {
            Point point = tempPoint.getValue();
            System.out.println("points info:"+tempPoint.getKey()+" : "+tempPoint.getValue());
            routeTables.put(routeTables.size() + 1, new RouteTableItem(point.getPort(),point.getName(),point.getPort(),point.getName(),0,point.getDistance(),new Date()));
            System.out.println("当前初始化路由表信息项："+routeTables.get(routeTables.size()).toString());
        }
    }

    public void begin(){
        //Start sending routing table
        BroadCastService broadCastService = new BroadCastService(points, routeTables);
        broadCastService.start();
        ReceiceHandleService receiceHandleService = new ReceiceHandleService(points, routeTables);
        receiceHandleService.start();
    }

    /**
     * 测试HashMap类型put 会不会更新value值，会更新
     * */
    public void testHashMap(){
        HashMap<Integer, Date> updates = new HashMap<Integer, Date>();
        updates.put(1,new Date());
        System.out.println(updates);
        for(int i=0;i<1000000;i++);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // for(int j=0;i<100;j++);
        updates.put(1,new Date());
        System.out.println(updates);
    }
    public static void main(String[] args) {
        String file="src/main/resources/";
        StartService startService = new StartService(args);
        startService.begin();

    }
}
