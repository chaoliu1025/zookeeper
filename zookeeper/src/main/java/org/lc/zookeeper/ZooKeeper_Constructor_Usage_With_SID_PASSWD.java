package org.lc.zookeeper;

import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooKeeper;

/**
 * 创建一个复用sessionId和sessionPasswd的zookeeper对象实例
 * @author lc
 *
 */
public class ZooKeeper_Constructor_Usage_With_SID_PASSWD implements Watcher {
    private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    public static void main(String[] args) throws Exception{
        ZooKeeper zookeeper = new ZooKeeper("192.168.100.201:2181", 
				5000, //
				new ZooKeeper_Constructor_Usage_With_SID_PASSWD());
        connectedSemaphore.await();
        long sessionId = zookeeper.getSessionId();
        byte[] passwd  = zookeeper.getSessionPasswd();
        
        //Use illegal sessionId and sessionPassWd
        zookeeper = new ZooKeeper("192.168.100.201:2181", 
				5000, //
				new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),//
				1l,//
				"test".getBytes());
        //Use correct sessionId and sessionPassWd
        zookeeper = new ZooKeeper("192.168.100.201:2181", 
				5000, //
				new ZooKeeper_Constructor_Usage_With_SID_PASSWD(),//
				sessionId,//
				passwd);
        Thread.sleep( Integer.MAX_VALUE );
    }
    public void process(WatchedEvent event) {
        System.out.println("Receive watched event：" + event);
        if (KeeperState.SyncConnected == event.getState()) {
            connectedSemaphore.countDown();
        }
    }
}