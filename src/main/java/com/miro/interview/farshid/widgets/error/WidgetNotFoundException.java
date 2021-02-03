package com.miro.interview.farshid.widgets.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Widget not found")
public class WidgetNotFoundException extends RuntimeException {
}
