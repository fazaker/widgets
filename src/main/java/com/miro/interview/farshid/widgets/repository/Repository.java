package com.miro.interview.farshid.widgets.repository;

import com.miro.interview.farshid.widgets.entity.Widget;

import java.util.List;
import java.util.UUID;

public interface Repository {
    Widget create(Widget widget);
    Widget update(Widget widget);
    void delete(UUID id);
    Widget get(UUID id);
    List<Widget> list();
}
