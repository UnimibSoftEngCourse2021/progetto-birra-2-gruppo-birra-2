package it.progettois.brewday.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.progettois.brewday.common.constant.IngredientType;
import it.progettois.brewday.common.constant.IngredientUnit;
import it.progettois.brewday.common.dto.*;
import it.progettois.brewday.common.exception.*;
import it.progettois.brewday.common.util.JwtTokenUtil;
import it.progettois.brewday.controller.common.model.Token;
import it.progettois.brewday.controller.common.model.UsernameAndPassword;
import it.progettois.brewday.controller.response.Response;
import it.progettois.brewday.service.BrewerService;
import it.progettois.brewday.service.IngredientService;
import it.progettois.brewday.service.RecipeService;
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
import java.util.ArrayList;
import java.util.List;
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

    @Autowired
    private RecipeService recipeService;

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

    IngredientDto createIngredient(String username, boolean shared, boolean save, Double quantity) throws BrewerNotFoundException {

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

    // This method saves a new ingredient which is then used to create the recipeIngredientDto
    RecipeIngredientDto createRecipeIngredient(String username, String name, boolean shared, boolean save, Double quantity) throws BrewerNotFoundException {

        IngredientDto ingredientDto = createIngredient(username, shared, save, quantity);

        return RecipeIngredientDto.builder()
                .ingredientId(ingredientDto.getIngredientId())
                .ingredientName(name)
                .quantity(quantity)
                .build();

    }

    RecipeDto createRecipe(String username, boolean shared, boolean save, List<RecipeIngredientDto> ingredients) throws BrewerNotFoundException,
            ConversionException, IngredientNotFoundException, NegativeQuantityException, RecipeIngredientNotFoundException {

        RecipeDto recipeDto = RecipeDto.builder()
                .name("TEST")
                .description("TEST")
                .username(username)
                .shared(shared)
                .ingredients(ingredients)
                .build();

        return save ? this.recipeService.saveRecipe(recipeDto, username) : recipeDto;
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


        List<RecipeIngredientDto> recipeIngredientDtoList = new ArrayList<>();
        recipeIngredientDtoList.add(createRecipeIngredient(brewerFatDto.getUsername(),
                "TEST1", false, true, 23.0));
        RecipeDto recipeDto = createRecipe(brewerFatDto.getUsername(), false, true, recipeIngredientDtoList);

        response = new Response(recipeDto);

        // Mi assicuro che la ricetta sia stata creata correttamente
        this.mockMvc.perform(get("/recipe/" + recipeDto.getRecipeId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        // Elimino l'ingrediente che è in una ricetta
        this.mockMvc.perform(delete("/ingredient/" + recipeDto.getIngredients().get(0).getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(content().string(containsString("The ingredient is part of existing recipes.")));

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
    void getStorageIngredientTest() throws Exception {
        BrewerFatDto brewerFatDto = createUser("TEST");
        String token = performLogin("TEST");
        BrewerFatDto brewerFatDto2 = createUser("TEST2");
        String token2 = performLogin("TEST2");

        IngredientDto ingredientDto = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 42.0);

        Response response = new Response(ingredientDto);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        // Other brewers' storage
        this.mockMvc.perform(get("/storage/" + ingredientDto.getIngredientId())
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Non existing ingredient
        this.mockMvc.perform(get("/storage/0")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

        IngredientDto ingredientDto2 = createIngredient("TEST2", true, true, 42.0);

        //Other brewers' shared storage ingredient
        this.mockMvc.perform(get("/storage/" + ingredientDto2.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isForbidden());

        IngredientDto ingredientDto3 = createIngredient(this.jwtTokenUtil.getUsername(token), false, true, 0.0);

        //Zero quantity
        this.mockMvc.perform(get("/storage/" + ingredientDto3.getIngredientId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

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

        // Salvo la vecchia quantità per verificare successivamente che la somma sia corretta
        double temp = ingredientDto.getQuantity();
        ingredientDto.setQuantity(36.0);
        double sum = temp + ingredientDto.getQuantity();

        // Modifico l'ingrediente
        this.mockMvc.perform(put("/storage/" + ingredientDto.getIngredientId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(ingredientDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The ingredient has been updated")));

        ingredientDto.setQuantity(sum);

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
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The ingredient has been updated")));

        deleteBrewer(brewerFatDto, token);
    }
}
