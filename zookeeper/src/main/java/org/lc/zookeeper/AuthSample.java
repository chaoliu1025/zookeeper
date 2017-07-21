package org.lc.zookeeper;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
//使用含权限信息的ZooKeeper会话创建数据节点
public class AuthSample implements Watcher{

	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
    final static String PATH = "/zk-book-auth_test";
    public static void main(String[] args) throws Exception {

        ZooKeeper zookeeper = new ZooKeeper("192.168.100.201:2181",50000,new AuthSample());
        zookeeper.addAuthInfo("digest", "foo:true".getBytes());
        
        connectedSemaphore.await();
        
        zookeeper.create( PATH, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL );
//        Thread.sleep( Integer.MAX_VALUE );
    }
	@Override
	public void process(WatchedEvent event) {
		 if (KeeperState.SyncConnected == event.getState()) {
			 connectedSemaphore.countDown();
		}
	}
}