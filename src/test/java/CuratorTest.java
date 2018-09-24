import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;
import org.junit.Test;

import java.util.List;

public class CuratorTest {

    /**
     * 连接zk server
     * @throws InterruptedException
     */
    @Test
    public void testStart() throws InterruptedException {

        CuratorOperator operator = new CuratorOperator();
        boolean started = operator.client.isStarted();
        System.out.println("当前状态:"+started);

        Thread.sleep(3000);

        operator.closeZKClient();
        boolean started2 = operator.client.isStarted();
        System.out.println("当前状态:"+started2);

    }

    /**
     * 创建节点
     */
    @Test
    public void testAddNode() throws Exception {

        CuratorOperator operator = new CuratorOperator();

        // 创建节点
        String nodePath = "/home/wxh";
		byte[] data = "testAdd".getBytes();
        operator.client.create()
                .creatingParentsIfNeeded()
			    .withMode(CreateMode.PERSISTENT) //持久节点
			    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE) //
			    .forPath(nodePath, data);

    }

    /**
     * 更新节点数据
     *
     */
    @Test
    public void testUpdateNode() {

        CuratorOperator operator = new CuratorOperator();

        String nodePath = "/home/wxh";
        byte[] newData = "testUpdate".getBytes();

        try {
            //根据版本号，可以实现乐观锁机制
//            operator.client.setData()
//                    .withVersion(2)
//                    .forPath(nodePath, newData);

            operator.client.setData()
                    .withVersion(0)
                    .forPath(nodePath, newData);

        } catch (Exception e) {
            System.out.println("更新失败");
            e.printStackTrace();
        } finally {
            operator.closeZKClient();
        }

    }

    @Test
    public void testDel() {

        CuratorOperator operator = new CuratorOperator();

        String nodePath = "/home/wxh";

        // 删除节点
        try {
            operator.client.delete()
                      .guaranteed()
                      .deletingChildrenIfNeeded()	// 如果有子节点，也删除
                      .withVersion(1) //此处版本号不正确，会抛出异常
                      .forPath(nodePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            operator.closeZKClient();
        }
    }

    /**
     * 读取节点数据
     */
    @Test
    public void testRead() {

        CuratorOperator operator = new CuratorOperator();

        String nodePath = "/home/wxh";

		Stat stat = new Stat();
        byte[] data = new byte[0];

        try {
            data = operator.client.getData()
                    .storingStatIn(stat)
                    .forPath(nodePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            operator.closeZKClient();
        }

        System.out.println("节点:" + nodePath + "的数据为: " + new String(data));
		System.out.println("版本号: " + stat.getVersion());

    }

    /**
     * 查询子节点
     */
    @Test
    public void testReadchildNodes() {

        CuratorOperator operator = new CuratorOperator();

        String nodePath = "/home";

        List<String> childNodes = null;
        try {
            childNodes = operator.client
                    .getChildren()
                    .forPath(nodePath);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            operator.closeZKClient();
        }

		for (String s : childNodes) {
			System.out.println(s);
		}


    }




}
