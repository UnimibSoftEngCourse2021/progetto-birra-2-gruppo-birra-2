package it.progettois.brewday.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.progettois.brewday.common.constant.ToolUnit;
import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.dto.ToolDto;
import it.progettois.brewday.common.exception.AlreadyPresentException;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ConversionException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.common.model.Token;
import it.progettois.brewday.controller.common.model.UsernameAndPassword;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewerService;
import it.progettois.brewday.service.ToolService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.AccessDeniedException;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class ToolControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrewerService brewerService;

    @Autowired
    private ToolService toolService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    BrewerFatDto createUser(String username) throws ConversionException, AlreadyPresentException {

        BrewerDto brewer = new BrewerDto();
        brewer.setUsername(username);
        brewer.setPassword("TEST92");
        brewer.setName("TEST92");
        brewer.setEmail(username);
        brewer.setMaxBrew(42);

        return this.brewerService.saveBrewer(brewer);
    }

    void deleteBrewer(BrewerFatDto brewer, String token) throws AccessDeniedException, BrewerNotFoundException {
        this.brewerService.deleteBrewer(brewer.getUsername(), this.jwtTokenUtil.getUsername(token));
    }

    String performLogin(String username) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        UsernameAndPassword usernameAndPassword = new UsernameAndPassword();
        usernameAndPassword.setUsername(username);
        usernameAndPassword.setPassword("TEST92");

        MvcResult result = this.mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(usernameAndPassword)))
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), Token.class).getToken();
    }

    ToolDto createTool(String username, int capacity, int quantity, boolean save)
            throws BrewerNotFoundException {

        ToolDto toolDto = ToolDto.builder()
                .name("TEST92")
                .description("TEST92")
                .capacity(capacity)
                .unit(ToolUnit.PIECE)
                .quantity(quantity)
                .username(username)
                .build();

        return save ? this.toolService.createTool(toolDto, username) : toolDto;
    }

    @Test
    void getToolsTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST92");
        String token = performLogin("TEST92");
        this.mockMvc.perform(get("/tool")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data")));
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getToolNotFoundTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST92");
        String token = performLogin("TEST92");
        this.mockMvc.perform(get("/tool/0")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getToolTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST92");
        String token = performLogin("TEST92");

        ToolDto toolDto = createTool(this.jwtTokenUtil.getUsername(token), 1, 10, true);

        Response response = new Response(toolDto);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getToolFromAnotherUserTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST92A");
        BrewerFatDto brewerFatDto2 = createUser("TEST92B");
        String token = performLogin("TEST92A");
        String token2 = performLogin("TEST92B");

        ToolDto toolDto = createTool("TEST92B", 100, 3, false);

        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }


    @Test
    void createToolTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST92");
        String token = performLogin("TEST92");

        ToolDto toolDto = createTool(this.jwtTokenUtil.getUsername(token), 1, 3, true);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = this.mockMvc.perform(post("/tool")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toolDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);

        Map<String, Object> map = (Map<String, Object>) response.getData();

        this.mockMvc.perform(get("/tool/" + map.get("toolId"))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void deleteToolTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST92");
        String token = performLogin("TEST92");

        ToolDto toolDto = createTool(this.jwtTokenUtil.getUsername(token), 1, 3, true);

        Response response = new Response(toolDto);

        ObjectMapper objectMapper = new ObjectMapper();

        //Test per la corretta creazione tool
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        //Eliminazione tool
        this.mockMvc.perform(delete("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The tool was deleted successfully")));

        // Mi assicuro che la tool non esista più
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void deleteToolFromAnotherUserTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST92A");
        BrewerFatDto brewerFatDto2 = createUser("TEST92B");
        String token = performLogin("TEST92A");
        String token2 = performLogin("TEST292B");

        ToolDto toolDto = createTool(this.jwtTokenUtil.getUsername(token), 1, 3, true);

        Response response = new Response(toolDto);

        ObjectMapper objectMapper = new ObjectMapper();

        //Test per la corretta creazione tool
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        //Eliminazione tool
        this.mockMvc.perform(delete("/tool/" + toolDto.getToolId())
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isForbidden());

        //Guardo se la tool esiste
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }

    @Test
    void editToolTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST92");
        String token = performLogin("TEST92");

        ToolDto toolDto = createTool(this.jwtTokenUtil.getUsername(token), 1, 3, true);

        Response response = new Response(toolDto);

        ObjectMapper objectMapper = new ObjectMapper();

        //Test per la corretta modifica del tool
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        toolDto.setDescription("DESCRIPTION MODIFICATA");

        //Modificazione tool
        this.mockMvc.perform(put("/tool/" + toolDto.getToolId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toolDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The tool has been updated")));

        //Test per la corretta modifica della tool
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void editToolNegativeQuantityTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST92");
        String token = performLogin("TEST92");

        ToolDto toolDto = createTool(this.jwtTokenUtil.getUsername(token), 1, 3, true);

        Response response = new Response(toolDto);

        ObjectMapper objectMapper = new ObjectMapper();

        //Test per la coretta modifica tool
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        ToolDto toolDto1 = new ToolDto();
        BeanUtils.copyProperties(toolDto, toolDto1);
        Response response1 = new Response(toolDto1);
        toolDto.setQuantity(-20);

        // Modificazione tool
        this.mockMvc.perform(put("/tool/" + toolDto.getToolId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(toolDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest());

        //Test affinchè la tool rimane invariate
        this.mockMvc.perform(get("/tool/" + toolDto.getToolId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response1))));

        deleteBrewer(brewerFatDto, token);
    }

}