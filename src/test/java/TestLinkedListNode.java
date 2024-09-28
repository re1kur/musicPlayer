import classes.Node;
import junit.framework.TestCase;

public class TestLinkedListNode extends TestCase {

    public void testNextNode() {
        Node<Integer> node_a = new Node<>(42);
        Node<Integer> node_b = new Node<>(196);
        node_a.setNextNode(node_b);
        assertSame(node_a.getNextNode(), node_b);
        assertNull(node_a.getPreNode());
        assertNull(node_b.getNextNode());
        assertNotSame(node_b.getPreNode(), node_a);
    }

    public void testPreNode() {
        Node<Integer> node_a = new Node<>(42);
        Node<Integer> node_b = new Node<>(196);
        node_b.setPreNode(node_a);
        assertNotSame(node_a.getPreNode(), node_b);
        assertNull(node_a.getPreNode());
        assertNull(node_b.getNextNode());
        assertSame(node_b.getPreNode(), node_a);
    }
}
