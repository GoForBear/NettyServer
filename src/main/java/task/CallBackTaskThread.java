package task;

import com.google.common.util.concurrent.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CallBackTaskThread extends Thread {
    private ConcurrentLinkedQueue<CallBackTask> callBackTasks  = new ConcurrentLinkedQueue<CallBackTask>();
    private long sleepTime = 200;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    ListeningExecutorService pool = MoreExecutors.listeningDecorator(executorService);
    private static CallBackTaskThread  callBackTaskThread = new CallBackTaskThread();

    private CallBackTaskThread(){this.start();}

    public static <T> void add(CallBackTask<T> callBackTask){
        callBackTaskThread.callBackTasks.add(callBackTask);
    }

    private void threadSleep(long time){
        try{
            sleep(time);
        }catch (Exception e){
            System.out.println("线程休眠出现异常");
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(true){
            handleTask();
            threadSleep(sleepTime);
        }
    }

    private void handleTask(){
        try{
            CallBackTask callBackTask = null;
            while(null != callBackTasks.peek()){
                callBackTask = callBackTasks.poll();
                startTask(callBackTask);
            }

        }catch (Exception e){
            System.out.println("线程池启动出现异常");
            e.printStackTrace();
        }
    }

    private <T> void  startTask(CallBackTask<T> callBackTask){
        ListenableFuture<T> future = pool.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                T t = callBackTask.execute();
                return t;
            }
        });

        Futures.addCallback(future, new FutureCallback<T>() {
            @Override
            public void onSuccess(T t) {
                callBackTask.onBack(t);
            }

            @Override
            public void onFailure(Throwable throwable) {
                callBackTask.onException(throwable);
            }
        });


    }
}
