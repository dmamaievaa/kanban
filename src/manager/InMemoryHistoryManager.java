package manager;

import task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class InMemoryHistoryManager implements HistoryManager {

    public static class Node {
        public Task item;
        public Node next;
        public Node prev;

        public Node(Node prev, Task element, Node next) {
            this.prev = prev;
            this.item = element;
            this.next = next;
        }
    }

    private final Map<Integer, Node> history;
    private Node head;
    private Node tail;

    public InMemoryHistoryManager() {
        history = new HashMap<>();
    }

    @Override
    public <T extends Task> void add(T task) {
        if (history.containsKey(task.getId())) {
            remove(task.getId());
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory() {
        ArrayList<Task> tasksHistoryList = new ArrayList<>();
        Node curr = head;
        while (curr != null) {
            tasksHistoryList.add(curr.item);
            curr = curr.next;
        }
        return tasksHistoryList;
    }

    @Override
    public void remove(int id) {
        if (history.containsKey(id)) {
            removeNode(history.get(id));
        }
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newTail = new Node(oldTail, task, null);
        tail = newTail;
        if (oldTail == null) {
            head = newTail;
        } else {
            oldTail.next = newTail;
        }
        history.put(task.getId(), newTail);
    }

    private void removeNode(Node node) {
        if (node == null) return;
        history.remove(node.item.getId());
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }

        if (node == tail) {
            tail = node.prev;
        }

    }

}
