package classes;

import java.util.List;
import java.util.NoSuchElementException;
/*
Класс двусвязного кольцевого списка
 */
public class LinkedList<T> {
    private Node<T> head;
    private Node<T> tail;
    private Node<T> current;
    /*
    Конструктор
     */
    public LinkedList () {
    }
    /*
    Конструктор для создания кольцевого списка при получении массива значений
     */
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
    /*
    Метод для изменения обратного порядка списка
     */
    public LinkedList<T> reversed () {
        LinkedList<T> reversedList = new LinkedList<>();
        if (head == null) {
            return reversedList;
        }
        setCurrent(tail);
        do {
            reversedList.addTail(current.getValue());
            current = current.getPreNode();
        } while (current != tail);
        return reversedList;
    }
    /*
    Метод для проверки, содержит ли один из нодов списка данное значение
     */
    public boolean contains (T value) {
        if (head == null) {
            return false;
        }
        setCurrent(head);
        if (current.getValue().equals(value)) {
            return true;
        }
        while (current.getNextNode() != head) {
            current = current.getNextNode();
            if (current.getValue().equals(value)) {
                return true;
            }
        }
        return false;
    }
    /*
    Метод для получения нода по индексу
     */
    public Node<T> getNode (int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        setCurrent(head);
        for (int i = 0; i < index; i++) {
            turnRightCurrent();
        }
        return current;
    }
    /*
    Метод для вставки нового нода после выбранного в список
     */
    public void insert (Node<T> previousNode, Node<T> newNode) {
        if (size() == 0) {
            return;
        }
        if (previousNode == tail) {
            previousNode.setNextNode(newNode);
            newNode.setPreNode(previousNode);
            newNode.setNextNode(head);
            head.setPreNode(newNode);
            tail = newNode;
            return;
        }
        Node<T> nextNode = previousNode.getNextNode();
        previousNode.setNextNode(newNode);
        newNode.setPreNode(previousNode);
        newNode.setNextNode(nextNode);
        nextNode.setPreNode(newNode);
    }
    /*
    Метод-алиас для метода AddTail()
     */
    public void add (T value) {
        addTail(value);
    }
    /*
    Метод для добавления нового нода в качестве Head
     */
    public void addHead (T value) {
        Node<T> newNode = new Node<>(value);
        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
            head.setNextNode(tail);
            head.setPreNode(tail);
            tail.setNextNode(head);
            tail.setPreNode(head);
        } else {
            newNode.setNextNode(head);
            newNode.setPreNode(tail);
            tail.setNextNode(newNode);
            head.setPreNode(newNode);
            head = newNode;
        }
    }
    /*
    Метод для добавления нового нода в качестве Tail
     */
    public void addTail (T value) {
        Node<T> newNode = new Node<>(value);
        if (tail == null && head == null) {
            head = newNode;
            tail = newNode;
            head.setNextNode(tail);
            head.setPreNode(tail);
            tail.setNextNode(head);
            tail.setPreNode(head);
        } else {
            newNode.setPreNode(tail);
            newNode.setNextNode(head);
            tail.setNextNode(newNode);
            head.setPreNode(newNode);
            tail = newNode;
        }
    }
    /*
    Метод для получения длины списка
     */
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
    /*
    Метод-сеттер для нода Current
     */
    public void setCurrent (Node<T> node) {
        current = node;
    }
    /*
    Метод-геттер нода Current
     */
    public Node<T> getCurrent() {
        return current;
    }
    /*
    Метод-геттер нода Head
     */
    public Node<T> getHead() {
        return head;
    }
    /*
    Метод-геттер нода Tail
     */
    public Node<T> getTail() {
        return tail;
    }
    /*
    Метод для переключения current на одну позицию вправо
     */
    public void turnRightCurrent () {
        setCurrent(current.getNextNode());
    }
    /*
    Метод для переключения current на одну позицию влево
     */
    public void turnLeftCurrent () {
        setCurrent(current.getPreNode());
    }
    /*
    Метод для получения строкового представления списка
     */
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
    /*
    Метод для удаления current
     */
    public void deleteCurrent () {
        current.getPreNode().setNextNode(current.getNextNode());
        current.getNextNode().setPreNode(current.getPreNode());
        current = head;
    }
    /*
    Метод для удаления нода со значением value
     */
    public void delete(T value) {
        if (size() == 0) {
            return;
        }
        if (head.getValue().equals(value)) {
            if (size() == 1) {
                head = null;
                tail = null;
            } else {
                head.getNextNode().setPreNode(tail);
                tail.setNextNode(head.getNextNode());
                head = head.getNextNode();
            }
            return;
        }
        if (tail.getValue().equals(value)) {
            tail.getPreNode().setNextNode(head);
            head.setPreNode(tail.getPreNode());
            tail = tail.getPreNode();
            return;
        }
        setCurrent(head.getNextNode());
        while (current != head) {
            if (current.getValue().equals(value)) {
                current.getPreNode().setNextNode(current.getNextNode());
                current.getNextNode().setPreNode(current.getPreNode());
                return;
            }
            setCurrent(current.getNextNode());
        }
        throw new NoSuchElementException();
    }
    /*
    Метод для перемещения двигающегося нода после выбранного нода
     */
    public void moveAfter(Node<T> movingNode, Node<T> previousNode) {
        if (movingNode == head) {
            head = movingNode.getNextNode();
        }
        if (movingNode == tail) {
            tail = movingNode.getPreNode();
        }
        Node<T> pastPrevNode = movingNode.getPreNode();
        Node<T> pastNextNode = movingNode.getNextNode();
        pastPrevNode.setNextNode(pastNextNode);
        pastNextNode.setPreNode(pastPrevNode);
        movingNode.setPreNode(previousNode);
        if (previousNode == tail) {
            movingNode.setNextNode(head);
            previousNode.setNextNode(movingNode);
            head.setPreNode(movingNode);
            tail = movingNode;
        } else {
            Node<T> nextNode = previousNode.getNextNode();
            movingNode.setNextNode(nextNode);
            nextNode.setPreNode(movingNode);
            previousNode.setNextNode(movingNode);
        }
    }
    /*
    Метод-геттер для нода tail
     */
    public void setTail (Node<T> node) {
        tail.setNextNode(node);
        node.setNextNode(head);
        node.setPreNode(tail);
        head.setPreNode(node);
        tail = node;

    }
    /*
    Метод для перемещения двигающегося нода перед выбранным нодом
     */
    public void moveBefore(Node<T> movingNode, Node<T> nextNode) {
        if (movingNode == head) {
            head = movingNode.getNextNode();
        }
        if (movingNode == tail) {
            tail = movingNode.getPreNode();
        }
        Node<T> pastPrevNode = movingNode.getPreNode();
        Node<T> pastNextNode = movingNode.getNextNode();
        pastPrevNode.setNextNode(pastNextNode);
        pastNextNode.setPreNode(pastPrevNode);
        movingNode.setNextNode(nextNode);
        if (nextNode == head) {
            movingNode.setPreNode(tail);
            tail.setNextNode(movingNode);
            head.setPreNode(movingNode);
            head = movingNode;
        } else {
            Node<T> prevNode = nextNode.getPreNode();
            movingNode.setPreNode(prevNode);
            prevNode.setNextNode(movingNode);
            nextNode.setPreNode(movingNode);
        }
    }
    /*
    Метод для свапа двигающегося нода с выбранным нодом
     */
    public void swapNodes(Node<T> movingNode, Node<T> selectedNode) {
        if (movingNode == null || selectedNode == null || movingNode == selectedNode) {
            return;
        }
        if (movingNode.getNextNode() == selectedNode) {
            swapNeighbourNodes(movingNode, selectedNode);
            return;
        } else if (selectedNode.getNextNode() == movingNode) {
            swapNeighbourNodes(selectedNode, movingNode);
            return;
        }
        Node<T> prevMoving = movingNode.getPreNode();
        Node<T> nextMoving = movingNode.getNextNode();
        Node<T> prevSelected = selectedNode.getPreNode();
        Node<T> nextSelected = selectedNode.getNextNode();
        if (prevMoving != null) prevMoving.setNextNode(selectedNode);
        if (nextMoving != null) nextMoving.setPreNode(selectedNode);
        if (prevSelected != null) prevSelected.setNextNode(movingNode);
        if (nextSelected != null) nextSelected.setPreNode(movingNode);
        movingNode.setPreNode(prevSelected);
        movingNode.setNextNode(nextSelected);
        selectedNode.setPreNode(prevMoving);
        selectedNode.setNextNode(nextMoving);
        if (movingNode == head) {
            head = selectedNode;
        } else if (selectedNode == head) {
            head = movingNode;
        }
        if (movingNode == tail) {
            tail = selectedNode;
        } else if (selectedNode == tail) {
            tail = movingNode;
        }
    }
    /*
    Метод для свапа двигающегося нода с выбранным нодом, если ноды являются соседними
     */
    private void swapNeighbourNodes(Node<T> movingNode, Node<T> selectedNode) {
        Node<T> prevMovingNode = movingNode.getPreNode();
        Node<T> nextSelectedNode = selectedNode.getNextNode();
        if (prevMovingNode != null) {
            prevMovingNode.setNextNode(selectedNode);
        }
        selectedNode.setPreNode(prevMovingNode);
        movingNode.setNextNode(nextSelectedNode);
        selectedNode.setNextNode(movingNode);
        movingNode.setPreNode(selectedNode);
        if (nextSelectedNode != null) {
            nextSelectedNode.setPreNode(movingNode);
        }
        if (movingNode == head) {
            head = selectedNode;
        } else if (selectedNode == tail) {
            tail = movingNode;
        }
    }
}
