package com.miro.interview.farshid.widgets.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miro.interview.farshid.widgets.entity.Widget;
import com.miro.interview.farshid.widgets.repository.Repository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WidgetsController.class)
class WidgetsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @MockBean
    private Repository repository;

    public static final int X = 1;
    public static final int Y = 2;
    public static final int Z_INDEX = 3;
    public static final int W = 5;
    public static final int H = 6;

    @Test
    void test_all() throws Exception {
        Widget w1 = Widget.from(new Widget(null, X, Y, Z_INDEX, W, H));
        Widget w2 = Widget.from(new Widget(null, X, Y, Z_INDEX, W, H));
        List<Widget> list = Arrays.asList(w1, w2);
        when(repository.list()).thenReturn(list);

        this.mockMvc.perform(get("/widgets")).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

    }

    @Test
    void test_get() throws Exception {
        UUID uuid = UUID.randomUUID();
        Widget widget = new Widget(uuid, X, Y, Z_INDEX, W, H);
        when(repository.get(uuid)).thenReturn(widget);

        this.mockMvc.perform(get("/widgets/" + uuid)).andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(X))
                .andExpect(jsonPath("$.y").value(Y))
                .andExpect(jsonPath("$.zindex").value(Z_INDEX))
                .andExpect(jsonPath("$.id").value(uuid.toString()));
    }

    @Test
    void test_create() throws Exception {
        Widget widgetRequest = new Widget(null, X, Y, Z_INDEX, W, H);

        UUID uuid = UUID.randomUUID();
        Widget createdWidget = new Widget(uuid, X, Y, Z_INDEX, W, H);
        when(repository.create(any(Widget.class))).thenReturn(createdWidget);

        this.mockMvc.perform(
                    post("/widgets")
                    .content(mapper.writeValueAsString(widgetRequest))
                    .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()).andExpect(status().isOk())
                .andExpect(jsonPath("$.x").value(X))
                .andExpect(jsonPath("$.y").value(Y))
                .andExpect(jsonPath("$.zindex").value(Z_INDEX))
                .andExpect(jsonPath("$.width").value(W))
                .andExpect(jsonPath("$.height").value(H))
                .andExpect(jsonPath("$.id").value(uuid.toString()));
    }

    @Test
    void test_update() {
    }

    @Test
    void test_delete() {
    }
}