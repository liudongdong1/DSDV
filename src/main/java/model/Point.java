package model;

import java.io.Serializable;

/**
 * @author ldd
 * @date 2019/6/25
 * @function 基本节点信息类
 *            目的节点名字，目的节点通信端口地址，距离
 *            后面用于一条邻居
 * */
public class Point implements Serializable {
    private String name;
    private int distance;
    private int port;

    public Point(String name, int distance, int port) {
        this.name = name;
        this.distance = distance;
        this.port = port;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Point{" +
                "name='" + name + '\'' +
                ", distance=" + distance +
                ", port=" + port +
                '}';
    }
}
