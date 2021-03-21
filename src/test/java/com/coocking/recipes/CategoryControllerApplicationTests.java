package com.coocking.recipes;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerApplicationTests {
    @Autowired
    private MockMvc mockMvc;

    private static final String CATEGORIES = "/categories";
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getAllCategories() throws Exception {
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get(CATEGORIES)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        String content = result.getResponse().getContentAsString();
        List<String> categories = objectMapper.readValue(content, new TypeReference<>() {
        });
        Assertions.assertEquals(7, categories.size(), "Unexpected number of categories");
    }
}
