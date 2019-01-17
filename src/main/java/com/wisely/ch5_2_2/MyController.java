package com.wisely.ch5_2_2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Auther: HeQin
 * @Date: 2018/10/26 10:01
 * @Description:
 * <a>https://www.toutiao.com/a6615820492282528264/?tt_from=mobile_qq&utm_campaign=client_share&timestamp=1540381998&app=news_article&utm_source=mobile_qq&iid=46532347499&utm_medium=toutiao_ios&group_id=6615820492282528264</a>
 */

@RestController
public class MyController {

    @Autowired
    private QueryService queryService;

    @RequestMapping("/mytest1")
    public String myTestCommonHandle() {
        //只有一个主线程执行任务
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();
        commonHandle(list);
        String retMsg = "全部执行耗时ms: " + (System.currentTimeMillis() - start);
        System.out.println(retMsg);
        return retMsg;
    }

    @RequestMapping("/mytest2")
    public String mytestThreadHandle() {
        //http://127.0.0.1:9090/helloboot/mytest2
        //利用线程池启用多个线程进行任务执行
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            list.add(i);
        }
        long start = System.currentTimeMillis();
        threadHandle(list);
        String retMsg = "全部执行耗时: " + (System.currentTimeMillis() - start + "(ms)");
        System.out.println(retMsg);
        return retMsg;
    }

    @RequestMapping("/mytest3")
    public String testMainThreadRunSonThread() {
        //http://127.0.0.1:9090/helloboot/mytest3
        //验证主线程处理完页面的请求时，主线程中启动的子线程，是不是不干扰到主线程的处理时长；-- 结果是不干扰
        String mainThreadName = Thread.currentThread().getName();
        long begin = System.currentTimeMillis();
        new Thread(new Runnable() {
            @Override
            public void run() {
                long begin = System.currentTimeMillis();
                String sonThreadName = Thread.currentThread().getName();
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("我是由主线程: " + mainThreadName + "启动的子线程：" + sonThreadName + ",耗时：" + (System.currentTimeMillis() - begin));
            }
        }).start();
        String retMsg = "我是主线程，叫" + mainThreadName + "：耗时：" + (System.currentTimeMillis() - begin);
        return retMsg;
    }

    private List<ItemInfoMode> commonHandle(List<Integer> itemIds) {
        List<ItemInfoMode> list = new ArrayList<>();
        for (Integer itemId : itemIds) {
            List<ItemInfoMode> retList = queryService.queryItemById(itemId);
            list.addAll(retList);
        }
        System.out.println("返回1：" + list);
        return list;
    }

    private List<ItemInfoMode> threadHandle(List<Integer> itemIds) {
        List<ItemInfoMode> list = new ArrayList<>();
        List<Future<List<ItemInfoMode>>> futures = new ArrayList<Future<List<ItemInfoMode>>>();
        for (Integer itemId : itemIds) {
            callSubmit(itemId,futures);
            //callExecute(itemId,futures);
        }
        getFutureResult(futures,list);//等待计算完成，然后获取计算结果
        ThreadPoolFactory.getInstance().shutdown();
        System.out.println("返回2：" + list);
        return list;
    }

    private void callSubmit(Integer itemId,List<Future<List<ItemInfoMode>>> futures){
        Callable<List<ItemInfoMode>> callable = new Callable<List<ItemInfoMode>>() {
            @Override
            public List<ItemInfoMode> call() throws Exception {
                long start = System.currentTimeMillis();
                try {
                    return queryService.queryItemById(itemId);
                } finally {
                    //finally此处的代码执行时机是：return 语句执行完之后，return返回之前时，执行下面这段代码的；
                    System.out.println(Thread.currentThread().getName() + "单个线程执行耗时ms: " + (System.currentTimeMillis() - start));
                }
            }
        };
        ThreadPoolExecutor threadPoolExecutor =  ThreadPoolFactory.getInstance();
        Future<List<ItemInfoMode>> future = null;
        future = threadPoolExecutor.submit(callable);
        futures.add(future);
    }

    private void callExecute(Integer itemId,List<Future<List<ItemInfoMode>>> futures){
        Callable<List<ItemInfoMode>> callable = new Callable<List<ItemInfoMode>>() {
            @Override
            public List<ItemInfoMode> call() throws Exception {
                long start = System.currentTimeMillis();
                try {
                    return queryService.queryItemById(itemId);
                } finally {
                    //finally此处的代码执行时机是：return 语句执行完之后，return返回之前时，执行下面这段代码的；
                    System.out.println(Thread.currentThread().getName() + "单个线程执行耗时ms: " + (System.currentTimeMillis() - start));
                }
            }
        };
        ThreadPoolExecutor threadPoolExecutor =  ThreadPoolFactory.getInstance();
        FutureTask<List<ItemInfoMode>> futureTask = new FutureTask<List<ItemInfoMode>>(callable);
        threadPoolExecutor.execute(futureTask);
        futures.add(futureTask);
    }
    /**
     * Waits if necessary for the computation to complete,and then retrieves its result
     * @param futures
     * @param list
     */
    private void getFutureResult(List<Future<List<ItemInfoMode>>> futures,List<ItemInfoMode> list){
        for (Future<List<ItemInfoMode>> future : futures) {
            try {
                list.addAll(future.get());//调用future.get的线程则阻塞等待计算结果
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}
