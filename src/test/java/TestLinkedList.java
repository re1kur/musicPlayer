import project.classes.LinkedList;
import project.classes.Node;
import junit.framework.TestCase;

import java.util.NoSuchElementException;

public class TestLinkedList extends TestCase {
    private static final int[] TEST_SIZE = {
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10
    };

    private static final int[] TEST_LAST = TEST_SIZE;

    public void testSize() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int value : TEST_SIZE) {
            linkedList.add(value);
        }
        assertEquals(TEST_SIZE.length, linkedList.size());
    }

    private Node<Integer> last_Test;

    public void testLast() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int value : TEST_LAST) {
            linkedList.add(value);
            if (value == 10) {
                last_Test = new Node<>(value);
                linkedList.setTail(last_Test);
            }
        }
        assertEquals(linkedList.getHead().getPreNode(), last_Test);
        assertEquals(linkedList.getTail(), last_Test);

    }

    public void testAddHead() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int value : TEST_LAST) {
            linkedList.add(value);
        }
        Node<Integer> lastHead = linkedList.getHead();
        linkedList.addHead(42);
        Node<Integer> currentHead = linkedList.getHead();
        assertNotSame(currentHead, lastHead);
        assertNotSame(currentHead.getPreNode(), currentHead);
        assertNotSame(currentHead.getNextNode(), currentHead);
    }

    public void testAddHeadWithOneValue() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.addHead(42);
        Node<Integer> currentHead = linkedList.getHead();
        assertSame(currentHead.getNextNode(), currentHead);
    }

    public void testAddTail() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int value : TEST_LAST) {
            linkedList.add(value);
        }
        Node<Integer> lastTail = linkedList.getTail();
        linkedList.addTail(42);
        Node<Integer> currentTail = linkedList.getTail();
        assertNotSame(currentTail, lastTail);
        assertNotSame(currentTail.getPreNode(), currentTail);
        assertNotSame(currentTail.getNextNode(), currentTail);
    }

    public void testAddTailWithOneValue() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.addTail(42);
        Node<Integer> currentTail = linkedList.getTail();
        assertSame(currentTail.getNextNode(), currentTail);
    }

    public void testAdd() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int value : TEST_LAST) {
            linkedList.add(value);
        }
        linkedList.add(42);
        Node<Integer> addedNode = linkedList.getTail();
        Node<Integer> head = linkedList.getHead();
        assertSame(head.getPreNode(), addedNode);
        assertSame(addedNode.getNextNode(), head);
    }

    public void testAddWithOneValue() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(42);
        Node<Integer> addedNode = linkedList.getTail();
        assertEquals(42, (int) addedNode.getValue());
        assertSame(addedNode.getNextNode(), addedNode);
    }

    public void testDelete() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        int[] values = {10, 20, 30};
        for (int value : values) {
            linkedList.add(value);
        }
        linkedList.delete(10);
        assertEquals(20, (int) linkedList.getHead().getValue());
        assertEquals(30, (int) linkedList.getTail().getValue());
    }

    public void testDeleteWithOneValue() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(10);
        linkedList.delete(10);
        assertEquals(0, linkedList.size());
    }

    public void testDeleteFailed() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        for (int value : TEST_LAST) {
            linkedList.add(value);
        }
        try {
            linkedList.delete(100);
        } catch (NoSuchElementException e) {
            System.err.println(e.getMessage());
        }
    }

    public void testInsert() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(3);
        Node<Integer> previousNode = linkedList.getNode(0);
        Node<Integer> newNode = new Node<>(2);
        linkedList.insert(previousNode, newNode);
        assertEquals(2, (int) linkedList.getNode(1).getValue());
        assertEquals(3, (int) linkedList.getNode(2).getValue());
    }

    public void testInsertIntoEmpyList() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        Node<Integer> newNode = new Node<>(1);
        linkedList.insert(null, newNode);
        assertEquals(0, linkedList.size());
    }

    public void testInsertAtTail() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        Node<Integer> previousNode = linkedList.getNode(1);
        Node<Integer> newNode = new Node<>(3);
        linkedList.insert(previousNode, newNode);
        assertEquals(3, (int) linkedList.getTail().getValue());
        assertEquals(1, (int) linkedList.getTail().getNextNode().getValue());
    }

    public void testReversed() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        LinkedList<Integer> reversedList = linkedList.reversed();
        assertEquals(3, (int) reversedList.getNode(0).getValue());
        assertEquals(2, (int) reversedList.getNode(1).getValue());
        assertEquals(1, (int) reversedList.getNode(2).getValue());
    }

    public void testContains() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        assertTrue(linkedList.contains(2));  // The list should contain the element 2
    }

    public void testContainsFailed() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        assertFalse(linkedList.contains(4));
    }

    public void testContainsEmptyList() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        assertFalse(linkedList.contains(1));
    }

    public void testGetNode() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        Node<Integer> node = linkedList.getNode(1);
        assertEquals(2, (int) node.getValue());
    }

    public void testGetNodeFailed() {
        LinkedList<Integer> linkedList = new LinkedList<>();
        linkedList.add(1);
        linkedList.add(2);
        linkedList.add(3);
        try {
            linkedList.getNode(5);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
        try {
            linkedList.getNode(-12);
        } catch (IndexOutOfBoundsException e) {
            System.err.println(e.getMessage());
        }
    }
}

