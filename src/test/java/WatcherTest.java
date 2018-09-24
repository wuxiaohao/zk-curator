import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.junit.Test;

public class WatcherTest {

    /**
     * 监听节点：只会触发一次，监听完毕后就销毁了
     */
    @Test
    public void test() throws InterruptedException {

        CuratorOperator operator = new CuratorOperator();

        String nodePath = "/home/wxh";

        try {
            operator.client.getData().usingWatcher(new MyCuratorWatcher()).forPath(nodePath);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            operator.closeZKClient();
        }

        Thread.sleep(Integer.MAX_VALUE);

    }

    /**
     * 监听节点：一直监听
     */
    @Test
    public void test2() throws Exception {

        CuratorOperator operator = new CuratorOperator();
        String nodePath = "/home/wxh";

        //为节点添加watcher
        final NodeCache nodeCache = new NodeCache(operator.client, nodePath);
		nodeCache.start(true);

		if (nodeCache.getCurrentData() != null) {
			System.out.println("node init data：" + new String(nodeCache.getCurrentData().getData()));
		} else {
			System.out.println("node init data is null");
		}

		nodeCache.getListenable().addListener(new NodeCacheListener() {
			public void nodeChanged() throws Exception {
				if (nodeCache.getCurrentData() == null) {
					System.out.println("data is null");
					return;
				}
				String data = new String(nodeCache.getCurrentData().getData());
				System.out.println("data path：" + nodeCache.getCurrentData().getPath() + "data：" + data);
			}
		});

        Thread.sleep(Integer.MAX_VALUE);
    }

}
