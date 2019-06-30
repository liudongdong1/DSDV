package service;

import model.Point;
import model.RouteTableItem;
import util.RPtUtilNThread;
import util.RoutePrintUtil;
import util.Signal;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * @author ldd
 * @date 2019/6/26
 * @funtion 接受到一跳邻居节点发来的消息处理：
 *              先比较序列号，选大的，若相同，选择跳数较少的。若超过一定时间没有更新路由消息，则按节点丢失处理
 * */
public class ReceiceHandleService implements Runnable {

    private int timeLimit=10000;       //判断时间ms
    private int MaxInt=100000;
    private Thread receive;
    private HashMap<Integer, Point> points;
    private HashMap<Integer, RouteTableItem> routeTables;
    private HashMap<Integer, Date> updates = new HashMap<Integer, Date>();
    private RoutePrintUtil routePrintUtil = new RoutePrintUtil();
    private Logger logger=Logger.getLogger("ReceiveHandleService");
    private RPtUtilNThread rPtUtilNThread=new RPtUtilNThread();


    public ReceiceHandleService(HashMap<Integer, Point> points, HashMap<Integer, RouteTableItem> routeTables) {
        this.points = points;
        this.routeTables = routeTables;
    }
    
    /**
     * 获取广播的数据，并进行处理
     * */
    public void run() {
        while(true) {
            try {
                DatagramSocket clientSocket;
                clientSocket = new DatagramSocket(points.get(1).getPort());
                byte[] receiveData = new byte[1024];
                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);
                ByteArrayInputStream baos2 = new ByteArrayInputStream(receiveData);
                ObjectInputStream oos2 = new ObjectInputStream(baos2);
                HashMap<Integer, RouteTableItem> receivedRouteTable = (HashMap<Integer, RouteTableItem>) oos2.readObject();
                handleResponse(receivedRouteTable);
                clientSocket.close();
                sleep(2000);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void handleResponse(HashMap<Integer, RouteTableItem> receivedRouteTables)
    {
        int change=0;
      /*  logger.info("接受到的路由表信息\n");
        printTableTest(receivedRouteTables);
        logger.info("本地路由表信息\n");
        printTableTest(routeTables);*/
        RouteTableItem recvTableItem = receivedRouteTables.get(1);          //发送者路由表项
        //更新路由表对应表项时间
        for (Map.Entry<Integer, RouteTableItem> routerEntry : routeTables.entrySet()) {
            if(recvTableItem.getDestPort() == routerEntry.getValue().getDestPort()) {
                synchronized (routerEntry){
                    routerEntry.getValue().setSeqNumber(recvTableItem.getSeqNumber());
                    recvTableItem = routerEntry.getValue();
                }
                updates.put(recvTableItem.getDestPort(), new Date());
                break;
            }
        }
        /**
         * 路由表项更新逻辑，选取路由序列号大的路由项，如果俩这相同选择跳数少的
         * */
        for (Map.Entry<Integer, RouteTableItem> routerEntry : receivedRouteTables.entrySet()) {
            if(routerEntry.getKey() == 1)
                continue;
            boolean found = false;
            for (Map.Entry<Integer, RouteTableItem> routerEntry2 : routeTables.entrySet()) {
                if(routerEntry.getValue().getDestPort() == routerEntry2.getValue().getDestPort()) {
                    found = true;
                    //判断否跟新Sequence
                    if(routerEntry.getValue().getSeqNumber() > routerEntry2.getValue().getSeqNumber())
                    {
                            synchronized (routeTables)
                            {
                                change = 1;
                                routerEntry2.getValue().setSeqNumber(routerEntry.getValue().getSeqNumber());
                                routerEntry2.getValue().setNextName(recvTableItem.getDestName());
                                routerEntry2.getValue().setNextPort(recvTableItem.getDestPort());
                                routerEntry2.getValue().setDistance(routerEntry.getValue().getDistance() + recvTableItem.getDistance());
                            }
                            break;
                    }
                    else if((routerEntry.getValue().getSeqNumber()== routerEntry2.getValue().getSeqNumber())&&(recvTableItem.getDistance()+routerEntry.getValue().getDistance() < routerEntry2.getValue().getDistance()))
                    {
                            synchronized (routeTables){
                                change=1;
                                routerEntry2.getValue().setNextName(recvTableItem.getDestName());
                                routerEntry2.getValue().setNextPort(recvTableItem.getDestPort());
                                routerEntry2.getValue().setDistance(recvTableItem.getDistance()+routerEntry.getValue().getDistance());
                            }
                    }
                    break;
                }
            }
            /**
             * 如果该节点路由表中没有该节点，则增加路由表项
             * */
            if(!found) {
               synchronized (routeTables){
                   change=1;
                   RouteTableItem newRouteTable =routerEntry.getValue();
                   newRouteTable.setNextPort(recvTableItem.getDestPort());
                   newRouteTable.setNextName(recvTableItem.getDestName());
                   newRouteTable.setDate(new Date());
                   newRouteTable.setDistance(newRouteTable.getDistance()+recvTableItem.getDistance());
                   routeTables.put(routeTables.size()+1,newRouteTable);
                   logger.info("New router connected on port: " + routerEntry.getValue().getDestPort());
               }
            }
        }
        /**
         * 如果超过timeLimit时间没收到，则删除该节点，将其序列号置为基数
         * 具体做法：如果序列号为偶是则加一，否则不操作
         *          将目的节点的为该长时间为接受到消息置-1，并且吓一跳也置-1
         * */
        Date date = new Date();
        for (Map.Entry<Integer, Date> updatesEntry : updates.entrySet()) {
            int port = updatesEntry.getKey();
            Date updateDate = updatesEntry.getValue();
            if(updateDate.getTime() + timeLimit < date.getTime()) {
                //System.out.println(date.getTime());
                synchronized (routeTables) {
                    for (Map.Entry<Integer, RouteTableItem> routerEntry : routeTables.entrySet()) {
                        RouteTableItem router = routerEntry.getValue();
                        if (router.getDestPort() == port || router.getNextPort() == port) {
                            router.setDistance(MaxInt);
                            if (router.getSeqNumber() % 2 == 0) {
                                router.setSeqNumber(router.getSeqNumber() + 1);
                            }
                            change = 1;
                        }
                    }
                }
            }
        }
        rPtUtilNThread.printTableTest(routeTables);
        if(change==1){
            synchronized (Signal.getInstance()){
                Signal.getInstance().set();
            }
        }
    }


    public void printTable()
    {
        routePrintUtil.setRouters(routeTables);
        routePrintUtil.start();
    }

    public void start ()
    {
        logger.info("开启接受线程：");
        if (receive == null)
        {
            receive = new Thread (this, "receive");
            receive.start ();
        }
    }
}

