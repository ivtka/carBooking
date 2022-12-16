package org.jadatix.carbooking.api.v1.controller;

import org.jadatix.carbooking.api.v1.request.AbstractRequest;
import org.jadatix.carbooking.api.v1.response.AbstractResponse;
import org.jadatix.carbooking.api.v1.response.PageResponse;
import org.jadatix.carbooking.exception.NotFoundException;
import org.jadatix.carbooking.model.IdentifierEntity;
import org.jadatix.carbooking.service.AbstractService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

public abstract class AbstractController<Entity extends IdentifierEntity, Request extends AbstractRequest<Entity>, Response extends AbstractResponse> {
    protected abstract AbstractService<Entity> getService();

    protected abstract Response convertToResponse(Entity entity);

    @GetMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public PageResponse<Response> getAll(@RequestParam(defaultValue = "0") Integer index,
                                         @RequestParam(defaultValue = "10") Integer size,
                                         @PathParam(value = "sort") Sort sort) {
        sort = mapSortProperties(sort);

        Pageable pageable = PageRequest.of(index, size, sort);

        Page<Entity> pagedEntities = getService().get(pageable);

        List<Response> responses = pagedEntities.getContent().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        PageResponse<Response> pageResponse = new PageResponse<>(responses,
                pagedEntities.getTotalElements(), pagedEntities.getNumber(), pagedEntities.getSize());

        return pageResponse;
    }

    @GetMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response get(@PathVariable Long id) {
        Entity entity = getService().get(id);
        if (isNull(entity)) {
            throw new NotFoundException("Not found entity with " + id);
        }
        return convertToResponse(entity);
    }

    @PostMapping
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Response create(@RequestBody Request body) {
        Entity createdEntity = getService().create(body.create());
        return convertToResponse(createdEntity);
    }

    @PutMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public Response update(@PathVariable Long id, @RequestBody Request body) {
        Entity entity = getService().get(id);
        Entity newEntity = body.update(entity);
        Entity updatedEntity = getService().update(newEntity);
        return convertToResponse(updatedEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        getService().delete(id);
    }

    protected Sort mapSortProperties(Sort sort) {
        if (isNull(sort)) {
            return Sort.unsorted();
        }

        List<Sort.Order> mappedOrders = sort.stream()
                .map(this::mapSortProperty)
                .collect(Collectors.toList());

        return Sort.by(mappedOrders);
    }

    private Sort.Order mapSortProperty(Sort.Order order) {
        return new Sort.Order(order.getDirection(), order.getProperty(), order.getNullHandling());
    }
}
