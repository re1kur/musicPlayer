package classes;

import java.util.List;

public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private Node<T> current;

    public LinkedList() {
    }

    public LinkedList (List<T> values) {
        head = new Node<>(values.get(0));
        tail = head;
        head.setNextNode(head);
        head.setPreNode(head);
        for (int i = 1; i < values.size(); i++) {
            Node<T> newNode = new Node<>(values.get(i));
            newNode.setPreNode(tail);
            newNode.setNextNode(head);
            tail.setNextNode(newNode);
            head.setPreNode(newNode);
            tail = newNode;
        }
        setCurrent(head);
    }


    public void addHead(T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
        } else {
            head.setPreNode(newNode);
            newNode.setNextNode(head);
            newNode.setPreNode(tail);
            tail.setNextNode(newNode);
            head = newNode;
        }
    }

    public void addTail(T value) {
        Node<T> newNode = new Node<>(value);
        if (tail == null && head == null) {
            head = newNode;
            tail = newNode;
        } else {
            tail.setNextNode(newNode);
            newNode.setPreNode(tail);
            newNode.setNextNode(head);
            head.setPreNode(newNode);
            tail = newNode;
        }
    }

    public int size() {
        if (head == null || tail == null) {
            return 0;
        }
        if (head == tail) {
            return 1;
        }
        int size = 0;
        current = head;
        do {
            size++;
            turnRightCurrent();
        } while (current != head);
        return size;
    }

    public void setCurrent (Node<T> node) {
        current = node;
    }

    public Node<T> getCurrent() {
        return current;
    }

    public Node<T> getHead() {
        return head;
    }

    public Node<T> getTail() {
        return tail;
    }

    public void turnRightCurrent () {
        setCurrent(current.getNextNode());
    }

    public void turnLeftCurrent () {
        setCurrent(current.getPreNode());
    }

    public String toString () {
        setCurrent(head);
        StringBuilder str = new StringBuilder();
        while (current.getNextNode() != head) {
            str.append(current.getValue().toString()).append(",");
            turnRightCurrent();
        }
        str.append(current.getValue().toString());
        return str.toString();
    }

    public void deleteCurrent () {
        current.getPreNode().setNextNode(current.getNextNode());
        current.getNextNode().setPreNode(current.getPreNode());
        current = head;
    }

}
