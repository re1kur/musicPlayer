package classes;
/*
Класс Node для функциональной работы LinkedList
 */
public class Node<T> {
    private Node<T> preNode;
    private Node<T> nextNode;
    private T value;
    /*
    Конструктор
     */
    public Node(T value) {
        this.value = value;
    }
    /*
    Метод-сеттер для nextNode
     */
    public void setNextNode(Node<T> nextNode) {
        this.nextNode = nextNode;
    }
    /*
    Метод-геттер для nextNode
     */
    public Node<T> getNextNode() {
        return nextNode;
    }
    /*
    Метод-сеттер для preNode
     */
    public void setPreNode(Node<T> preNode) {
        this.preNode = preNode;
    }
    /*
    Метод-геттер для preNode
     */
    public Node<T> getPreNode() {
        return preNode;
    }
    /*
    Метод-геттер для значения Node
     */
    public T getValue() {
        return value;
    }
    /*
    Метод для получения строкового представления Node
     */
    public String toString() {
        return value.toString();
    }
}
