package cfg;

public class CFGEdge {
    private final CFGNode src;
    private final CFGNode dest;

    public CFGEdge(CFGNode src, CFGNode dest) {
        this.src = src;
        this.dest = dest;
    }

    public CFGNode getSrc() {
        return src;
    }

    public CFGNode getDest() {
        return dest;
    }
}
