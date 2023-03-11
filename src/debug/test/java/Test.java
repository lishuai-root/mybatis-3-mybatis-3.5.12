package test.java;

import net.sf.cglib.core.DebuggingClassWriter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import test.java.mybatis.MyBatisDebug;
import test.java.pojo.User;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @description:
 * @author: LISHUAI
 * @createDate: 2023/3/5 17:56
 * @version: 1.0
 */

public class Test {

    @org.junit.Test
    public void mybatisTest() throws FileNotFoundException {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "target/cglib");
        FileInputStream stream = new FileInputStream("src/debug/test/resources/MybatisConfig.xml");
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(stream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        User o = sqlSession.selectOne("test.java.mybatis.MyBatisDebug.queryOneUser", "d6feda449cea21cc");
        System.out.println("userName : " + o.getUserName());
        System.out.println("passWord : " + o.getPassWord());


        MyBatisDebug mapper = sqlSession.getMapper(MyBatisDebug.class);
        User user = mapper.queryOneUser("d6feda449cea21cc");
        System.out.println(user);
        System.out.println(user.getPassWord());
    }

    @org.junit.Test
    public void test_02() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(100000);
        System.out.println("start");
        for (int i = 0; i < 100000; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    double d = 0;
                    for (int j = 0; j < 10000000; j++) {
                        d++;
                    }
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
        System.out.println("over");
    }


    @org.junit.Test
    public void test_03() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        for (int i = 0; i < 1; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    double k = Double.MAX_VALUE;
                    while (k >= 0){
                        k = k / 1.3322;
                    }
                    latch.countDown();
                }
            }).start();
        }
        latch.await();
    }

    @org.junit.Test
    public void Test_04() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        ConcurrentSignal lockSignal = new ConcurrentSignal();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (lockSignal.getSignal() == 0){

                }
                latch.countDown();
            }
        }).start();
        Thread.sleep(1000);
        lockSignal.signal();
        System.out.println(lockSignal.getSignal());
        latch.await();
    }


    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(5);
        ConcurrentSignal lockSignal = new ConcurrentSignal();
        for (int i = 0; i < latch.getCount(); i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (lockSignal.getSignal() == 0){

                    }
                    if (lockSignal.getSignal() != 0){
                        System.out.println(Thread.currentThread().getName() + " exit.");
                    }
                    latch.countDown();
                }
            }).start();
        }
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        lockSignal.signal();
        latch.await();
        scanner.nextLine();
        System.out.println(Thread.currentThread().getName() + " exit.");
    }

    static class ConcurrentSignal {
        private volatile int signal = 0;
        public void signal(){
            this.signal = 1;
        }

        public int getSignal(){
            return this.signal;
        }
    }
}
