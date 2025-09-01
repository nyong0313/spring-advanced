package org.example.expert.domain.todo.service;

import org.example.expert.client.WeatherClient;
import org.example.expert.domain.common.exception.InvalidRequestException;
import org.example.expert.domain.todo.dto.response.TodoResponse;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @InjectMocks
    private TodoService todoService;

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private WeatherClient weatherClient;

    @Test
    void getTodos_Success() {
        // given
        int page = 1;
        int size = 10;
        Pageable pageable = PageRequest.of(0, size);
        User user = new User("test@test.com", "password", UserRole.USER);
        List<Todo> todoList = List.of(new Todo("제목1", "내용1", "맑음", user));
        Page<Todo> todoPage = new PageImpl<>(todoList, pageable, todoList.size());

        given(todoRepository.findAllByOrderByModifiedAtDesc(pageable)).willReturn(todoPage);

        // when
        Page<TodoResponse> responsePage = todoService.getTodos(page, size);

        // then
        assertEquals(1, responsePage.getTotalElements());
        assertEquals("제목1", responsePage.getContent().get(0).getTitle());
    }

    @Test
    void getTodo_Fail_NotFound() {
        // given
        long nonExistentTodoId = 99L;
        given(todoRepository.findByIdWithUser(nonExistentTodoId)).willReturn(Optional.empty());

        // when & then
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            todoService.getTodo(nonExistentTodoId);
        });

        assertEquals("Todo not found", exception.getMessage());
    }
}