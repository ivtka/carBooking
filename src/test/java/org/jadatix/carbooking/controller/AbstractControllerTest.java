package org.jadatix.carbooking.controller;

import org.jadatix.carbooking.api.v1.request.AbstractRequest;
import org.jadatix.carbooking.api.v1.response.AbstractResponse;
import org.jadatix.carbooking.api.v1.response.PageResponse;
import org.jadatix.carbooking.exception.NotFoundException;
import org.jadatix.carbooking.model.IdentifierEntity;
import org.jadatix.carbooking.service.AbstractService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public abstract class AbstractControllerTest<Entity extends IdentifierEntity,
        Request extends AbstractRequest<Entity>,
        Response extends AbstractResponse> extends SpringBasedControllerTest {
    protected ResultCaptor<Entity> resultCaptor;

    @BeforeEach
    public void setup() {
        resultCaptor = new ResultCaptor<>();
    }

    @Test
    public void testCreateItem() throws Exception {
        mockServiceCreateOrUpdateMethod(resultCaptor, whenCreateInService(any(getEntityClass())));

        ResultActions resultActions = mvc.perform(post(getControllerPath())
                        .content(getJson(getNewRequest()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        assertNotNull(resultCaptor.getResult());

        Response expectedResponse = convertToResponse(resultCaptor.getResult());
        resultActions.andExpect(content().json(getJson(expectedResponse)));
    }

    protected <T> void mockServiceCreateOrUpdateMethod(ResultCaptor<Entity> resultCaptor, OngoingStubbing<T> ongoingStubbing) {
        ongoingStubbing.thenAnswer(invocation -> {
            Entity entity = invocation.getArgument(0, getEntityClass());
            entity.setId(getRandomId());
            resultCaptor.setResult(entity);
            return entity;
        });
    }

    @Test
    public void testUpdateItem() throws Exception {
        Entity entity = getNewEntity();
        entity.setId(getRandomId());

        Entity newEntity = getNewEntity();
        newEntity.setId(entity.getId());

        Request request = convertToRequest(newEntity);
        List<Function<Entity, Object>> valuesGetters = getValueToBeUpdated(request);

        mockServiceGetEntity(entity);
        mockServiceCreateOrUpdateMethod(resultCaptor, whenUpdateInService(any(getEntityClass())));

        ResultActions resultActions = mvc.perform(put(getControllerPath() + "/{id}", entity.getId())
                .content(getJson(request))
                .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());

        Entity updatedEntity = resultCaptor.getResult();
        Response expectedResponse = convertToResponse(updatedEntity);
        resultActions.andExpect(content().json(getJson(expectedResponse)));

        valuesGetters.forEach(getter -> {
            assertEquals(getter.apply(newEntity), getter.apply(updatedEntity));
        });
    }

    @Test
    @Disabled("Until implemented patch method")
    public void testPatchItem() throws Exception {
        String path = getControllerPath() + "/{id}";
    }

    @Test
    public void testSuccessOnGetById() throws Exception {
        Entity entity = getNewEntity();
        entity.setId(getRandomId());

        mockServiceGetEntity(entity);

        Response expectedResponse = convertToResponse(entity);

        mvc.perform(get(getControllerPath() + "/{id}", entity.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json(getJson(expectedResponse)));
    }

    @Test
    public void testNotFoundOnGetById() throws Exception {
        when(getService().get(any(Long.class))).thenReturn(null);

        mvc.perform(get(getControllerPath() + "/{id}", 0L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testFindAll() throws Exception {
        List<Entity> entities = new LinkedList<>(Arrays.asList(getNewEntity(), getNewEntity()));

        Page<Entity> page = mockPage(entities);

        when(getService().get(any(Pageable.class))).thenReturn(page);

        List<Response> expectedResponses = entities.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        PageResponse<Response> expectedPageResponse = new PageResponse<>(expectedResponses,
                page.getTotalElements(), page.getNumber(), page.getSize());

        mvc.perform(get(getControllerPath())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(getJson(expectedPageResponse)));
    }

    @Test
    public void testDefaultPageValuesForFindAll() throws Exception {
        List<Entity> entities = new LinkedList<>(Arrays.asList(getNewEntity(), getNewEntity()));

        Page<Entity> page = getMockedServicePage();

        when(getService().get(any(Pageable.class))).thenReturn(page);

        mvc.perform(get(getControllerPath())
                        .accept(MediaType.APPLICATION_JSON)
                        .param("index", "0"))
                .andExpect(status().isOk());

        Pageable lastPageRequest = getLastPageRequest();

        assertEquals(10, lastPageRequest.getPageSize());
    }

    @Test
    public void testCustomPageValuesForFindAll() throws Exception {
        Page<Entity> page = getMockedServicePage();

        when(getService().get(any(Pageable.class))).thenReturn(page);

        mvc.perform(get(getControllerPath())
                        .accept(MediaType.APPLICATION_JSON)
                        .param("index", "50")
                        .param("size", "100"))
                .andExpect(status().isOk());

        Pageable lastPageRequest = getLastPageRequest();
        assertEquals(50, lastPageRequest.getPageNumber());
        assertEquals(100, lastPageRequest.getPageSize());
    }

    @Test
    public void testSortForFindAll() {
        Page<Entity> page = getMockedServicePage();

        when(getService().get(any(Pageable.class))).thenReturn(page);

        Map<List<String>, Sort> sortingTestParameters = getSortingTestParameters();
        sortingTestParameters.forEach(this::doSortingTest);
    }

    protected void doSortingTest(List<String> sortingValues, Sort expectedResult) {
        MockHttpServletRequestBuilder mockHttpServletRequestBuilder = get(getControllerPath());
        sortingValues.forEach(sortingValue -> mockHttpServletRequestBuilder.param("sort", sortingValue));
        try {
            mvc.perform(mockHttpServletRequestBuilder).andExpect(status().isOk());
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    public void testDeleteItem() throws Exception {
        Entity entity = getNewEntity();
        entity.setId(getRandomId());

        mockServiceGetEntity(entity);

        mvc.perform(delete(getControllerPath() + "/{id}", entity.getId()))
                .andExpect(status().isNoContent());

        verify(getService(), times(1)).delete(eq(entity.getId()));
    }

    @Test
    public void testCanNotDelete() throws Exception {
        Entity entity = getNewEntity();
        entity.setId(getRandomId());

        mockServiceGetEntity(entity);

        doThrow(NotFoundException.class).when(getService()).delete(entity.getId());

        mvc.perform(delete(getControllerPath() + "/{id}", entity.getId()))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCanNotUpdate() throws Exception {
        Entity entity = getNewEntity();
        entity.setId(getRandomId());
        Request request = convertToRequest(entity);

        mockServiceGetEntity(entity);

        whenUpdateInService(any(getEntityClass())).thenThrow(NotFoundException.class);

        mvc.perform(put(getControllerPath() + "/{id}", entity.getId())
                        .content(getJson(request))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    protected void mockServiceGetEntity(Entity entity) {
        when(getService().get(entity.getId())).thenReturn(entity);
    }

    private Page<Entity> getMockedServicePage() {
        return mockPage(new ArrayList<>());
    }

    protected Page<Entity> mockPage(List<Entity> entities) {
        Page<Entity> page = mock(Page.class);
        when(page.getContent()).thenReturn(entities);
        when(page.getTotalElements()).thenReturn((long) entities.size());
        when(page.getNumber()).thenReturn(1);
        when(page.getSize()).thenReturn(15);
        when(page.iterator()).thenReturn(entities.iterator());

        when(getService().get(any(Pageable.class))).thenReturn(page);

        return page;
    }

    protected OngoingStubbing<Entity> whenCreateInService(Entity entity) {
        return when(getService().create(entity));
    }

    protected OngoingStubbing<Entity> whenUpdateInService(Entity entity) {
        return when(getService().update(entity));
    }

    protected Long getRandomId() {
        return ThreadLocalRandom.current().nextLong(Long.MAX_VALUE - 1);
    }

    protected Pageable getLastPageRequest() {
        ArgumentCaptor<Pageable> argumentCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(getService(), atLeastOnce()).get(argumentCaptor.capture());
        return argumentCaptor.getValue();
    }

    protected abstract String getControllerPath();

    protected abstract Class<Entity> getEntityClass();

    protected abstract AbstractService<Entity> getService();

    protected abstract List<Function<Entity, Object>> getValueToBeUpdated(Request request);

    protected abstract Map<List<String>, Sort> getSortingTestParameters();

    protected abstract Entity getNewEntity();

    protected abstract Request getNewRequest();

    protected abstract Request convertToRequest(Entity entity);

    protected abstract Response convertToResponse(Entity entity);
}
