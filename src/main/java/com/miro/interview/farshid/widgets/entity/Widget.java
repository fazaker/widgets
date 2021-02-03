package com.miro.interview.farshid.widgets.entity;

import javax.persistence.Entity;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.UUID;

@Entity
public class Widget implements Comparable<Widget> {
    private UUID id;

    @NotNull
    private Integer x;

    @NotNull
    private Integer y;

    private Integer zIndex;

    @NotNull
    @Min(value = 0L, message = "must be positive")
    private Integer width;

    @NotNull
    @Min(value = 0L, message = "must be positive")
    private Integer height;

    private Date lastUpdate;

    public Widget(UUID id, Integer x, Integer y, Integer zIndex, Integer width, Integer height) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.zIndex = zIndex;
        this.width = width;
        this.height = height;
    }

    private Widget(Widget widget) {
        this.id = widget.getId();
        this.x = widget.getX();
        this.y = widget.getY();
        this.zIndex = widget.getZIndex();
        this.width = widget.getWidth();
        this.height = widget.getHeight();
        this.lastUpdate = widget.getLastUpdate();
    }

    public static Widget from(Widget widget) {
        Widget newWidget = new Widget(widget);
        newWidget.setId(UUID.randomUUID());
        newWidget.setLastUpdate(new Date());
        return newWidget;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Integer getZIndex() {
        return zIndex;
    }

    public void setZIndex(Integer zIndex) {
        this.zIndex = zIndex;
    }

    public Integer getWidth() {
        return width;
    }

    public void setWidth(Integer width) {
        this.width = width;
    }

    public Integer getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    @Override
    public int compareTo(Widget o) {
        return this.zIndex.compareTo(o.zIndex);
    }
}
