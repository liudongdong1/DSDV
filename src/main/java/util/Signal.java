package util;

/**
 * @author ldd
 * @date 2019/6/26
 * @function 信号量用于同步更新控制
 * */
public  class Signal {
    private  int needUpdate=30;
    private static final Signal instance = new Signal();

    public Signal() {
        set();
    }
    public void set()
    {
        needUpdate+=1;
    }
    public void sub(){
        needUpdate-=1;
    }
    public int get(){
        return needUpdate;
    }
    public static Signal getInstance(){
        if(instance!=null)
            return instance;
        return new Signal();
    }
}
