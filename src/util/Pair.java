package util;

public class Pair<L, R> {

    private final L left;
    private final R right;
    private final int hashCode;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
        this.hashCode = (int)((((2166136261L * 16777619) ^ left.hashCode()) * 16777619) ^ right.hashCode());
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return hashCode; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Pair)) return false;
        Pair pairo = (Pair) o;
        return this.left.equals(pairo.getLeft()) &&
                this.right.equals(pairo.getRight());
    }

    @Override
    public String toString() {
        return left.toString() + " : " + right.toString();
    }

    public static <L, R> Pair<L,R> pairOf(L l, R r) {
        return new Pair<>(l, r);
    }

}
