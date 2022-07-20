package ru.yandex.practicum.management.history;
import ru.yandex.practicum.domain.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> browsingHistoryTask = new HashMap<>();
    private Node head;
    private Node tail;

    @Override
    public List<Task> getHistory() {
        if (browsingHistoryTask.isEmpty()) {
            System.out.println("Просмотренных задач нет");
            return null;
        } else {
            return getTasks();
        }
    }

    @Override
    public void addHistory(Task task) {
        linkLast(task);

    }

    @Override
    public void removeNode(Integer id) {
        if(browsingHistoryTask.size() == 1) {this.clear();}
        if (browsingHistoryTask.containsKey(id)) {
            Node remoteNode = browsingHistoryTask.remove(id);
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
    }

    private void linkLast(Task task) {
        final Node oldTail = tail;
        final Node newNode = new Node(tail, task, null);
        tail = newNode;
        if (oldTail == null)
            head = newNode;
        else
            oldTail.next = newNode;

        if (browsingHistoryTask.containsKey(task.getId())) {
            removeNode(task.getId());
        }
        browsingHistoryTask.put(task.getId(), newNode);
    }

    private List<Task> getTasks() {
        List<Task> browsingHistory = new ArrayList<>();
        Node helpNextNode = head.next;
        browsingHistory.add(head.data);

        for (int i = 1; i < browsingHistoryTask.size(); i++) {
            browsingHistory.add(helpNextNode.data);
            if (helpNextNode == tail) {
                break;
            }
            helpNextNode = browsingHistoryTask.get(helpNextNode.next.data.getId());
        }
        return browsingHistory;
    }


    @Override
    public void clear() {
        browsingHistoryTask.clear();
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
}
