package classes;

public class Node<T> {
    private Node<T> preNode;
    private Node<T> nextNode;
    private T value;

    public Node (T value) {
        this.value = value;
    }

    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }

    public Node<T> getNextNode() {
        return nextNode;
    }

    public void setPreNode(Node<T> preNode) {
        this.preNode = preNode;
    }

    public Node<T> getPreNode() {
        return preNode;
    }

    public T getValue() {
        return value;
    }
}
