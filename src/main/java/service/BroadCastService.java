package service;

import model.Point;
import model.RouteTableItem;
import util.Signal;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import static java.lang.Thread.sleep;

/**
 * @author ldd
 * @date 2019/6/26
 * @function 节点广播自身路由表信息，流程：先修改自己的seqNumber,然后广播给自己的所有邻居节点,
 *              其中节点的所有一跳邻居节点记录在HashMap<Integer, Point> point中
 *
 * */
public class BroadCastService implements Runnable {

    private Thread send;
    private HashMap<Integer, Point> points;
    private HashMap<Integer, RouteTableItem> routeTable;
    private Logger logger=Logger.getLogger("BroadCast");
    private boolean flag=true;
    private int period=3000;


    public BroadCastService(HashMap<Integer, Point> points, HashMap<Integer, RouteTableItem> routeTable) {
        this.points = points;
        this.routeTable = routeTable;
    }

    public void run() {
        while(flag) {
            try {
                logger.info("开始广播线程");
                //获取自身路由项
                RouteTableItem router = routeTable.get(1);
                if(router.getSeqNumber() % 2 == 0) {
                    router.setSeqNumber(router.getSeqNumber() + 2);
                }
                else if(router.getSeqNumber() % 2 == 1) {
                    flag=false;
                }
                synchronized (Signal.getInstance()){
                    Signal.getInstance().sub();
                    if (Signal.getInstance().get()<=0)
                        flag=false;
                }
                DatagramSocket clientSocket;
                clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName("localhost");
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ObjectOutputStream oos = new ObjectOutputStream(baos);
                oos.writeObject(routeTable);
                oos.flush();
                byte[] Buf = baos.toByteArray();
                //给所有的邻近节点发送自身存储的路由表，其中读的时候自身唯一第一个，及不发给自身
                for (Map.Entry<Integer, Point> pointEntry : points.entrySet()) {
                    if(pointEntry.getKey() == 1)
                        continue;
                    Point points = pointEntry.getValue();
                    DatagramPacket sendPacket = new DatagramPacket(Buf, Buf.length, IPAddress, points.getPort());
                    clientSocket.send(sendPacket);
                }
                clientSocket.close();
                sleep(period);
                //sleep(1000 + (new Random().nextInt()%10)*100);
            } catch (SocketException e) {
                e.printStackTrace();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void start ()
    {
        System.out.println("开始广播进程");
        //date.getTime()
        if (send == null)
        {
            send = new Thread (this, "send");
            send.start ();
           /* RouteTableItem router = routeTable.get(1);
            if(router.getSeqNumber()%2==1)
                send.interrupt();
            synchronized (Signal.getInstance()){
                if(Signal.getInstance().get()<=0)
                    send.interrupt();
            }*/
        }
    }
}
