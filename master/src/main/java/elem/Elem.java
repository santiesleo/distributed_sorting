package elem;

public class Elem {
    private int arrayNum;
    private int idx;
    private String str;

    public Elem(int arrayNum, int idx, String str) {    
        this.arrayNum = arrayNum;
        this.idx = idx;
        this.str = str;
    }

    public int getArrayNum() {
        return this.arrayNum;
    }

    public int getIdx() {
        return this.idx;
    }

    public String getStr() {
        return this.str;
    }
}

