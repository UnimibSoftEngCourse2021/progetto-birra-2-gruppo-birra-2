package it.progettois.brewday.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.progettois.brewday.common.constant.IngredientType;
import it.progettois.brewday.common.constant.IngredientUnit;
import it.progettois.brewday.common.dto.BrewerDto;
import it.progettois.brewday.common.dto.BrewerFatDto;
import it.progettois.brewday.common.dto.IngredientDto;
import it.progettois.brewday.common.exception.BrewerNotFoundException;
import it.progettois.brewday.common.exception.ConversionException;
import it.progettois.brewday.common.exception.NegativeQuantityException;
import it.progettois.brewday.common.exception.AlreadyPresentException;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.common.model.Token;
import it.progettois.brewday.controller.common.model.UsernameAndPassword;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewerService;
import it.progettois.brewday.service.IngredientService;
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
class IngredientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrewerService brewerService;

    @Autowired
    private IngredientService ingredientService;

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

    IngredientDto createIngredient(String username, boolean shared, boolean save, double quantity) throws BrewerNotFoundException, NegativeQuantityException {

        IngredientDto ingredientDto = IngredientDto.builder()
                .name("TEST")
                .description("TEST")
                .unit(IngredientUnit.L)
                .type(IngredientType.ADDITIVE)
                .quantity(quantity)
                .username(username)
                .shared(shared)
                .build();

        return save ? this.ingredientService.createIngredient(ingredientDto, username) : ingredientDto;
    }

    @Test
    void getIngredientsTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");
        this.mockMvc.perform(get("/ingredient")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data")));
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getIngredientNotFoundTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");
        this.mockMvc.perform(get("/ingredient/0")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getIngredientTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getIngredientFromAnotherUserTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token = performLogin("TEST");
        String token2 = performLogin("TEST2");

        IngredientDto ingredientDto = createIngredient("TEST2", false, true, 42.0);

        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }

    @Test
    void getIngredientFromAnotherUserSharedTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token = performLogin("TEST");
        String token2 = performLogin("TEST2");

        IngredientDto ingredientDto = createIngredient("TEST2", true, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }

    @Test
    void createIngredientTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, false, 42.0);

        ObjectMapper objectMapper = new ObjectMapper();

        MvcResult result = this.mockMvc.perform(post("/ingredient")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ingredientDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        Response response = objectMapper.readValue(result.getResponse().getContentAsString(), Response.class);

        Map<String, Object> map = (Map<String, Object>) response.getData();

        this.mockMvc.perform(get("/ingredient/" + map.get("ingredientId"))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void createIngredientNegativeQuantityTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, false, -42.0);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/ingredient")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ingredientDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest());

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void deleteIngredientTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Mi assicuro che l'ingrediente sia stato creato correttamente
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        // Elimino l'ingrediente
        this.mockMvc.perform(delete("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The ingredient was deleted successfully")));

        // Mi assicuro che l'ingrediente non esista più
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void deleteIngredientFromAnotherUserTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST");
        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token = performLogin("TEST");
        String token2 = performLogin("TEST2");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), true, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Mi assicuro che l'ingrediente sia stato creato correttamente
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        // Elimino l'ingrediente
        this.mockMvc.perform(delete("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Mi assicuro che l'ingrediente esista ancora
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }

    @Test
    void editIngredientTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Mi assicuro che l'ingrediente sia stato creato correttamente
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        ingredientDto.setDescription("DESCRIPTION MODIFICATA");

        // Modifico l'ingrediente
        this.mockMvc.perform(put("/ingredient/" + ingredientDto.getIngredientId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ingredientDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The ingredient has been updated")));

        // Mi assicuro che l'ingrediente sia stato modificato correttamente
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void editIngredientNegativeQuantityTest() throws Exception {

        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Mi assicuro che l'ingrediente sia stato creato correttamente
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        IngredientDto ingredientDto1 = new IngredientDto();
        BeanUtils.copyProperties(ingredientDto, ingredientDto1);
        Response response1 = new Response(ingredientDto1);
        ingredientDto.setQuantity(-36.0);
        ingredientDto.setQuantity(-36.0);

        // Modifico l'ingrediente
        this.mockMvc.perform(put("/ingredient/" + ingredientDto.getIngredientId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ingredientDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest());

        // Mi assicuro che l'ingrediente sia rimasto invariato
        this.mockMvc.perform(get("/ingredient/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response1))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getStorageTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");
        this.mockMvc.perform(get("/storage")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data")));
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getStorageNotFoundTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");
        this.mockMvc.perform(get("/storage/0")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getStorageIngredientTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getStorageIngredientQuantityZeroTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 0.0);


        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getStorageFromAnotherUserTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token = performLogin("TEST");
        String token2 = performLogin("TEST2");

        IngredientDto ingredientDto = createIngredient("TEST2", false, true, 42.0);

        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }

    @Test
    void getStorageFromAnotherUserSharedTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token = performLogin("TEST");
        String token2 = performLogin("TEST2");

        IngredientDto ingredientDto = createIngredient("TEST2", true, true, 42.0);

        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);
    }

    @Test
    void modifyStorageTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Mi assicuro che l'ingrediente sia stato creato correttamente
        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        ingredientDto.setQuantity(36.0);

        // Modifico l'ingrediente
        this.mockMvc.perform(put("/storage/" + ingredientDto.getIngredientId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ingredientDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The ingredient has been updated")));

        // Mi assicuro che l'ingrediente sia stato modificato correttamente
        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void modifyStorageNegativeQuantityTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Mi assicuro che l'ingrediente sia stato creato correttamente
        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        IngredientDto ingredientDto1 = new IngredientDto();
        BeanUtils.copyProperties(ingredientDto, ingredientDto1);
        Response response1 = new Response(ingredientDto1);
        ingredientDto.setQuantity(-36.0);

        // Modifico l'ingrediente
        this.mockMvc.perform(put("/storage/" + ingredientDto.getIngredientId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ingredientDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("The quantity must be greater than zero.")));

        // Mi assicuro che l'ingrediente sia rimasto invariato
        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response1))));

        deleteBrewer(brewerFatDto, token);
    }
}
