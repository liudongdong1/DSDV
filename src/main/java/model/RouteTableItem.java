package model;

import java.io.Serializable;
import java.util.Date;

/**
 * @author ldd
 * @date 2019/6/25
 * @function 路由表项基本类
 *           destPort,destName: 目的节点端口号和名称
 *           nextPort,nextName: 转发的节点端口号和名称
 *           seqNumber  序列号，正常偶数+2 ，异常基数
 *           distance   到目的节点跳数
 * */

public class RouteTableItem implements Serializable {
    private int destPort;
    private String destName;
    private int nextPort;
    private String nextName;
    private int seqNumber;
    private int distance;
    private Date date;

    public RouteTableItem(int destPort, String destName, int nextPort, String nextName, int seqNumber, int distance, Date date) {
        this.destPort = destPort;
        this.destName = destName;
        this.nextPort = nextPort;
        this.nextName = nextName;
        this.seqNumber = seqNumber;
        this.distance = distance;
        this.date = date;
    }

    public int getDestPort() {
        return destPort;
    }

    public void setDestPort(int destPort) {
        this.destPort = destPort;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }

    public int getNextPort() {
        return nextPort;
    }

    public void setNextPort(int nextPort) {
        this.nextPort = nextPort;
    }

    public String getNextName() {
        return nextName;
    }

    public void setNextName(String nextName) {
        this.nextName = nextName;
    }

    public int getSeqNumber() {
        return seqNumber;
    }

    public void setSeqNumber(int seqNumber) {
        this.seqNumber = seqNumber;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "RouteTableItem{" +
                "destPort=" + destPort +
                ", destName='" + destName + '\'' +
                ", nextPort=" + nextPort +
                ", nextName='" + nextName + '\'' +
                ", seqNumber=" + seqNumber +
                ", distance=" + distance +
                ", date=" + date +
                '}';
    }
}
