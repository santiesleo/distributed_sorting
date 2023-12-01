module TextSorter {
    sequence<string> StringSeq;
    sequence<StringSeq> StringSeqSeq;

    interface WorkerInterface {
        void processTask(string task);
        void subscribe();
        StringSeq sort(StringSeq lines);
    };

    sequence<string> result;
    interface MasterInterface{
        void attachWorker(WorkerInterface* subscriber);
        void addPartialResult(["java:type:java.util.ArrayList<String>"]result res);
        void detachWorker(WorkerInterface* subscriber);
        string getTask();
        StringSeq sort(StringSeqSeq partitions);
    }
}