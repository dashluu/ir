package cfg;

public class CFGEdge {
    private final BasicBlock src;
    private final BasicBlock dest;

    public CFGEdge(BasicBlock src, BasicBlock dest) {
        this.src = src;
        this.dest = dest;
    }

    public BasicBlock getSrc() {
        return src;
    }

    public BasicBlock getDest() {
        return dest;
    }
}
