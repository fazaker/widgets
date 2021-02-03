package com.miro.interview.farshid.widgets.controller;

import com.miro.interview.farshid.widgets.entity.Widget;
import com.miro.interview.farshid.widgets.repository.Repository;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class WidgetsController {

    private final Repository repository;

    public WidgetsController(Repository repository) {
        this.repository = repository;
    }

    @GetMapping("/widgets")
    List<Widget> all() {
        return repository.list();
    }

    @GetMapping("/widgets/{id}")
    Widget get(@PathVariable UUID id) {
        return repository.get(id);
    }

    @PostMapping("/widgets")
    Widget create(@Valid @RequestBody Widget newWidget) {
        return repository.create(newWidget);
    }

    @PutMapping("/widgets")
    Widget update(@RequestBody Widget widget) {
        return repository.update(widget);
    }

    @DeleteMapping("/widgets/{id}")
    void delete(@PathVariable UUID id) {
        repository.delete(id);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
