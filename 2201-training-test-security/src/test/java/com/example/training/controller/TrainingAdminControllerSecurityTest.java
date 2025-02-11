package com.example.training.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.example.training.config.SecurityConfig;
import com.example.training.service.TrainingAdminService;

//MockMvc와 연계된 스프링 시큐리티의 테스트 지원 기능을 사용하여
//모든 테스트가 통과되도록 수정
@WebMvcTest(TrainingAdminController.class)
@Import(SecurityConfig.class)
class TrainingAdminControllerSecurityTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrainingAdminService trainingAdminService;

    @MockBean
    TrainingAdminSession trainingAdminSession;

    @Test
    void test_displayList_GUEST유저접근불가() throws Exception {
        mockMvc.perform(
                        get("/admin/training/display-list")
                                .with(user("foo").roles("GUEST")) //아이디, 권한 설정
                )
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    void test_displayList_STAFF유저접근가능() throws Exception {
        mockMvc.perform(
                        get("/admin/training/display-list")
                                .with(user("foo").roles("STAFF")) //아이디, 권한 설정
                )
                .andExpect(status().isOk())
        ;
    }

    @Test
    void test_validateUpdateInput_STAFF유저접근불가() throws Exception {
        mockMvc.perform(
                        post("/admin/training/validate-update-input")
                                .with(user("foo").roles("STAFF"))
                            .with(csrf()) //CSRF 토큰 지정
                )
                .andExpect(status().isForbidden())
        ;
    }

    @Test
    void test_validateUpdateInput_ADMIN유저접근가능() throws Exception {
        mockMvc.perform(
                        post("/admin/training/validate-update-input")
                                .with(user("foo").roles("ADMIN"))
                        .with(csrf())
                )
                .andExpect(status().isOk())
        ;
    }
}
