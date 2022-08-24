package ru.yandex.practicum.management.history;
import ru.yandex.practicum.domain.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> browsingHistoryTask = new HashMap<>();
    private Node head;
    private Node tail;


    @Override
    public List<Task> getHistory() {
            return getTasks();
    }

    @Override
    public void addHistory(Task task) {
        linkLast(task);
    }

    @Override
    public void remove(Integer id) {
        if (browsingHistoryTask.size() == 1) {
            this.clear();
        } else if (browsingHistoryTask.containsKey(id)) {
            Node remoteNode = browsingHistoryTask.remove(id);
            if (remoteNode != null) {
                removeNode(remoteNode);
            }
        }
    }

    private void removeNode(Node remoteNode) {

        if (remoteNode == head) {
            remoteNode.next.prev = null;
            head = remoteNode.next;
        } else if (remoteNode == tail) {
            remoteNode.prev.next = null;
            tail = remoteNode.prev;
        } else {
            remoteNode.prev.next = remoteNode.next;
            remoteNode.next.prev = remoteNode.prev;
        }
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(tail, task, null);
        tail = newNode;
        if (oldTail == null) {
            head = newNode;
        } else {
            oldTail.next = newNode;
        }
        if (browsingHistoryTask.containsKey(task.getId())) {
            remove(task.getId());
        }
        browsingHistoryTask.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> browsingHistory = new ArrayList<>();
        Node node = head;
        while (node != null) {
            browsingHistory.add(node.data);
            node = node.next;
        }
        return browsingHistory;
    }


    @Override
    public void clear() {
        browsingHistoryTask.clear();
        head = null;
        tail = null;
    }


    private static class Node {
        public Task data;
        public Node next;
        public Node prev;

        public Node(Node prev, Task data, Node next) {
            this.data = data;
            this.next = next;
            this.prev = prev;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InMemoryHistoryManager that = (InMemoryHistoryManager) o;

        return browsingHistoryTask.equals(that.browsingHistoryTask);
    }

    @Override
    public int hashCode() {
        return browsingHistoryTask.hashCode();
    }
}
