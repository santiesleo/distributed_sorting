module TextSorter {
    sequence<string> StringSeq;
    sequence<StringSeq> StringSeqSeq;

    interface WorkerInterface {
        void processTask(string task);
        void subscribe();
        void sort(StringSeq lines);
    };

    sequence<string> result;
    interface MasterInterface{
        void attachWorker(WorkerInterface* subscriber);
        void addPartialResult(StringSeq res);
        void detachWorker(WorkerInterface* subscriber);
        string getTask();
        void sort();
    }
}