package it.progettois.brewday.controller.response;

import lombok.Data;

@Data
public class Response {

    private Object data;

    public Response(Object data) {
        this.data = data;
    }
}
