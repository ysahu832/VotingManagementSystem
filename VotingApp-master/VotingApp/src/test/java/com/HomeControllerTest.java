package com;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;

import com.controller.HomeController;
import com.service.RoleService;
import com.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.controller")
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock any dependencies that HomeController uses
    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Test
    public void testIndex() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(view().name("home"))
               .andExpect(model().attribute("title", "Home"));
    }

    // Add more test methods for other controller methods
}

