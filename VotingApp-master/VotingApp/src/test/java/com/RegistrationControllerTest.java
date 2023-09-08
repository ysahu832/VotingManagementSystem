package com;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import com.model.Role;
import com.model.User;
import com.service.RoleService;
import com.service.UserService;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.HttpSession;
import java.io.FileInputStream;
import java.io.InputStream;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ComponentScan(basePackages = "com.controller")
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock your services (UserService and RoleService)
    @MockBean
    private UserService userService;

    @MockBean
    private RoleService roleService;

    @Test
    @Transactional
    public void testRegistrationSuccess() throws Exception {
        // Mock a successful registration
        when(userService.getUserByEmail(anyString())).thenReturn(null);
       /* when(roleService.getRoleByName("ROLE_USER")).thenReturn(new Role("ROLE_USER"));*/
        
        when(roleService.getRoleByName("ROLE_USER")).thenReturn(new Role(0, "ROLE_USER", null, null));



        // Create a mock image file
        
        InputStream resource = new FileInputStream("src/test/resources/s.jpeg");
        MockMultipartFile photo = new MockMultipartFile("photo", "s.jpeg", MediaType.IMAGE_JPEG_VALUE, resource);

        HttpSession session = mockMvc.perform(MockMvcRequestBuilders.multipart("/createuser")
                .file(photo)
                .param("name", "John Doe")
                .param("phone", "1234567890")
                .param("password", "Password@123")
                .param("confirmpass", "Password@123")
                .param("email", "john.doe@example.com")
                .sessionAttr("msg", "Registration Successful..."))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"))
                .andReturn()
                .getRequest()
                .getSession();

        
        String responseMessage = (String) session.getAttribute("msg");
        assertEquals("Registration Successful...", responseMessage);

       
        verify(userService, times(1)).addUser(any(User.class));
    }

	/*
	 * @Test
	 * 
	 * @Transactional public void testRegistrationFailure() throws Exception { //
	 * Mock a registration failure (e.g., email already exists)
	 * when(userService.getUserByEmail(anyString())).thenReturn(new User());
	 * 
	 * // Create a mock image file
	 * 
	 * 
	 * mockMvc.perform(MockMvcRequestBuilders.multipart("/createuser") .file(photo)
	 * .param("name", "John Doe") .param("phone", "1234567890") .param("password",
	 * "password123") .param("confirmpass", "password123") .param("email",
	 * "john.doe@example.com") .sessionAttr("msg",
	 * "Registration Failed, Please try a different email id"))
	 * .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
	 * .andExpect(MockMvcResultMatchers.redirectedUrl("/register"));
	 * 
	 * // Verify that the userService method was not called verify(userService,
	 * never()).addUser(any(User.class)); }
	 */
}

