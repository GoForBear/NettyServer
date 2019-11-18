package task;

public interface CallBackTask<T> {
    T execute() throws Exception;
    void onBack(T t);
    void onException(Throwable t);
}
