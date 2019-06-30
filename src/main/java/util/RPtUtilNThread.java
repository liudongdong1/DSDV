package util;

import model.RouteTableItem;
import sun.rmi.runtime.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class RPtUtilNThread {
    private int MaxInt=100000;
    private  Logger logger=Logger.getLogger("RPtUtilNThread");
    public  void printTableTest(HashMap<Integer, RouteTableItem> routeTable)
    {
        StringBuffer stringBuffer=new StringBuffer().append("\n########  Routing table"+routeTable.get(1).getDestName()+"  ############\n");
        stringBuffer.append(" | DestName |  DPort | NextName | NextPort | Distance | Sequence |  time \n");
        for (Map.Entry<Integer, RouteTableItem> routerEntry : routeTable.entrySet()) {
            RouteTableItem router = routerEntry.getValue();
            stringBuffer.append("|").append(router.getDestName() + " | " + router.getDestPort()+ " | "+ router.getNextName()+" | "+router.getNextPort()+" | " +
                    (router.getSeqNumber() % 2 != 0 ? "infinity" : router.getDistance()) + " | " +
                    router.getSeqNumber()+" | "+router.getDate()).append(" |\n");
        }
        logger.info(stringBuffer.toString());
    }
}
