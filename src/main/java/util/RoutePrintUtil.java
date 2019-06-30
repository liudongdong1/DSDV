package util;


import model.RouteTableItem;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ldd
 * @date 2019/6/26
 * @function 打印路由表信息线程
 * */
public class RoutePrintUtil implements Runnable {

    private Thread print;
    private int period=2000;        //打印周期 ms
    private HashMap<Integer, RouteTableItem> routeTable;

    public HashMap<Integer, RouteTableItem> getRouters() {
        return routeTable;
    }

    public void setRouters(HashMap<Integer, RouteTableItem> routeTable) {
        this.routeTable = routeTable;
    }

    public void run() {
        while (true) {
            printTable();
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印路由表信息
     * */
    public void printTable() {
        System.out.println("########  Routing table"+routeTable.get(1).getDestName()+"  ############");
        for (Map.Entry<Integer, RouteTableItem> routerEntry : routeTable.entrySet()) {
            RouteTableItem router = routerEntry.getValue();
            System.out.println(router.getDestName() + ": " + router.getDestPort()+ " "+ router.getNextName()+" : "+router.getNextPort()+" distance: " +
                    (router.getSeqNumber() % 2 != 0 ? "infinity" : router.getDistance()) + " seqNumber: " +
                    router.getSeqNumber()+" "+router.getDate());
        }
    }

    public void start() {
        if (print == null) {
            System.out.println("开启打印路由表线程");
            print = new Thread(this, "print");
            print.start();
        }
    }
}