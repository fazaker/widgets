package com.miro.interview.farshid.widgets.repository;

import com.miro.interview.farshid.widgets.entity.Widget;
import com.miro.interview.farshid.widgets.error.WidgetNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class InMemoryRepository implements Repository {

    private final List<Widget> widgets;
    private final Map<UUID, Widget> widgetsMap;
    private int maxZ = 0;
    private final ReadWriteLock lock;

    public InMemoryRepository() {
        this.widgets = new ArrayList<>();
        this.widgetsMap = new HashMap<>();
        lock = new ReentrantReadWriteLock();
    }

    @Override
    public Widget create(Widget widget) {
        try {
            lock.writeLock().lock();
            if (widget.getZIndex() == null) {
                widget.setZIndex(maxZ++);
            }
            assert widget.getId() == null;
            assert widget.getLastUpdate() == null;
            Widget newWidget = Widget.from(widget);
            int index = findInsertionPoint(newWidget, 0, widgets.size());
            widgets.add(index, newWidget);
            updateZIndexes(newWidget.getZIndex(), index + 1);
            widgetsMap.put(newWidget.getId(), newWidget);
            return newWidget;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void updateZIndexes(int z_index, int start) {
        int baseZ = z_index;
        for (int i = start; i < widgets.size(); i++) {
            Widget w = widgets.get(i);
            assert w.getZIndex() >= baseZ;
            if (w.getZIndex() == baseZ) {
                w.setZIndex(++baseZ);
            } else {
                break;
            }
        }
    }

    private int findInsertionPoint(Widget widget, int start, int length) {
        if (length == 0) {
            return start;
        }
        int mid = start + length / 2;
        int midZIndex = widgets.get(mid).getZIndex();
        if (midZIndex == widget.getZIndex()) {
            return mid;
        }
        if (midZIndex < widget.getZIndex()) {
            return findInsertionPoint(widget, mid + 1, length + start - mid - 1);
        }
        return findInsertionPoint(widget, start, mid - start);
    }

    @Override
    public Widget update(Widget widget) {
        try {
            lock.writeLock().lock();
            Widget existing = widgetsMap.get(widget.getId());
            if (existing == null) {
                throw new WidgetNotFoundException();
            }
            existing.setX(widget.getX());
            existing.setY(widget.getY());
            existing.setWidth(widget.getWidth());
            existing.setHeight(widget.getHeight());

            if (widget.getZIndex() == null) {
                widget.setZIndex(maxZ++);
            }
            if (!widget.getZIndex().equals(existing.getZIndex())) {
                widgets.remove(existing);
                existing.setZIndex(widget.getZIndex());
                int index = findInsertionPoint(existing, 0, widgets.size());
                widgets.add(index, existing);
                updateZIndexes(existing.getZIndex(), index + 1);
            }
            existing.setLastUpdate(new Date());
            return existing;
        } finally {
            lock.writeLock().unlock();
        }
    }

    @Override
    public void delete(UUID id) {
        try {
            lock.writeLock().lock();
            Widget widget = widgetsMap.get(id);
            if (widget == null) {
                throw new WidgetNotFoundException();
            }
            widgets.remove(widget);
            widgetsMap.remove(id);
        } finally {
            lock.writeLock().unlock();
        }

    }

    @Override
    public Widget get(UUID id) {
        try {
            lock.readLock().lock();
            Widget widget = widgetsMap.get(id);
            if (widget == null) {
                throw new WidgetNotFoundException();
            }
            return widget;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public List<Widget> list() {
        try {
            lock.readLock().lock();
            return widgets;
        } finally {
            lock.readLock().unlock();
        }
    }
}
