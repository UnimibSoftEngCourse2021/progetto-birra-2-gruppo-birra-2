package it.progettois.brewday.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.exception.AlreadyPresentException;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ConversionException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.common.model.Token;
import it.progettois.brewday.controller.common.model.UsernameAndPassword;
import it.progettois.brewday.service.BrewerService;
import it.progettois.brewday.service.IngredientService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.AccessDeniedException;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class BrewerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrewerService brewerService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    BrewerFatDto createUser(String username) throws ConversionException, AlreadyPresentException {

        BrewerDto brewer = new BrewerDto();
        brewer.setUsername(username);
        brewer.setPassword("TEST");
        brewer.setName("TEST");
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
        usernameAndPassword.setPassword("TEST");

        MvcResult result = this.mockMvc.perform(post("/login")
                .content(objectMapper.writeValueAsString(usernameAndPassword)))
                .andReturn();

        return objectMapper.readValue(result.getResponse().getContentAsString(), Token.class).getToken();
    }
    @Test
    void getBrewersTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        this.mockMvc.perform(get("/brewer")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data")));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getBrewerByUsernameTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token2 = performLogin("TEST2");

        this.mockMvc.perform(get("/brewer/" + brewerFatDto2.getUsername())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data")));

        //Get non existing brewer
        this.mockMvc.perform(get("/brewer/-1")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);

    }

    @Test
    void editBrewer() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token2 = performLogin("TEST2");

        brewerFatDto.setMaxBrew(98);

        ObjectMapper objectMapper = new ObjectMapper();

        // Edit brewer
        this.mockMvc.perform(put("/brewer/" + brewerFatDto.getUsername())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brewerFatDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        // Edit other brewer
        this.mockMvc.perform(put("/brewer/" + brewerFatDto2.getUsername())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brewerFatDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }

    @Test
    void deleteBrewer() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token2 = performLogin("TEST2");

        // Delete other brewer
        this.mockMvc.perform(delete("/brewer/" + brewerFatDto2.getUsername())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Delete brewer
        this.mockMvc.perform(delete("/brewer/" + brewerFatDto.getUsername())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        deleteBrewer(brewerFatDto2, token2);
    }
}
