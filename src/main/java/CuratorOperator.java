import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.RetryNTimes;

import java.util.List;

/**
 * Curator操作类
 * @author wxh
 */
public class CuratorOperator {

    public CuratorFramework client = null;

    public static final String zkPath = "127.0.0.1:2181";


    public CuratorOperator() {

        RetryPolicy retryPolicy = new RetryNTimes(3, 5000);

        client = CuratorFrameworkFactory.builder()
                .connectString(zkPath)
                .sessionTimeoutMs(10000)
                .retryPolicy(retryPolicy)
                .namespace("workspace")
                .build();

        client.start();

    }

    /**
     * 关闭zkClient
     */
    public void closeZKClient() {
        if (client != null) {
            this.client.close();
        }
    }




}
