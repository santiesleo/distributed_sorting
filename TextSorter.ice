module TextSorter {
    sequence<string> StringSeq;
    sequence<StringSeq> StringSeqSeq;

    interface Slave {
        StringSeq sort(StringSeq lines);
    };

    interface Master {
        StringSeq sort(StringSeqSeq partitions);
    };
};
