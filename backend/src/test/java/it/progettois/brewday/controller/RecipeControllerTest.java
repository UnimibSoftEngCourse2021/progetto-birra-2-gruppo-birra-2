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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrewerService brewerService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    BrewerFatDto createBrewer(String username) throws ConversionException, AlreadyPresentException {

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

    IngredientDto createIngredient(String username, String name, boolean shared, boolean save, double quantity) throws BrewerNotFoundException, NegativeQuantityException {

        IngredientDto ingredientDto = IngredientDto.builder()
                .name(name)
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
    RecipeIngredientDto createRecipeIngredient(String username, String name, boolean shared, boolean save, Double quantity) throws NegativeQuantityException,
            BrewerNotFoundException {

        IngredientDto ingredientDto = createIngredient(username, name, shared, save, quantity);

        return RecipeIngredientDto.builder()
                .ingredientId(ingredientDto.getIngredientId())
                .ingredientName(name)
                .quantity(quantity)
                .build();

    }

    RecipeDto createRecipe(String username, boolean shared, boolean save, List<RecipeIngredientDto> ingredients) throws BrewerNotFoundException,
            ConversionException, IngredientNotFoundException {

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
    void getRecipesTest() throws Exception {
        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin("TEST");
        this.mockMvc.perform(get("/recipe")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data")));
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getRecipeNotFoundTest() throws Exception {
        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin("TEST");
        this.mockMvc.perform(get("/recipe/0")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());
        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getRecipeTest() throws Exception {
        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin("TEST");


        List<RecipeIngredientDto> recipeIngredientDtoList = new ArrayList<>();
        RecipeDto recipeDto = createRecipe(brewerFatDto.getUsername(), false, true, recipeIngredientDtoList);
        Response response = new Response(recipeDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Test recipe with empty ingredient list
        this.mockMvc.perform(get("/recipe/" + recipeDto.getRecipeId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        recipeIngredientDtoList.add(createRecipeIngredient(brewerFatDto.getUsername(),
                "TEST1",false,true,23.0 ));
        recipeDto = createRecipe(brewerFatDto.getUsername(), false, true, recipeIngredientDtoList);
        response = new Response(recipeDto);

        // Test recipe with 1 ingredient
        this.mockMvc.perform(get("/recipe/" + recipeDto.getRecipeId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void saveRecipe() throws Exception {
        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin("TEST");


        List<RecipeIngredientDto> recipeIngredientDtoList = new ArrayList<>();
        recipeIngredientDtoList.add(createRecipeIngredient(brewerFatDto.getUsername(),
                "TEST1",false,true,23.0 ));
        RecipeDto recipeDto = createRecipe(brewerFatDto.getUsername(), false, true, recipeIngredientDtoList);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(post("/recipe")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipeDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn();

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void deleteRecipeTest() throws Exception {
        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin("TEST");


        List<RecipeIngredientDto> recipeIngredientDtoList = new ArrayList<>();
        recipeIngredientDtoList.add(createRecipeIngredient(brewerFatDto.getUsername(),
                "TEST1",false,true,Math.ceil(Math.random())));
        RecipeDto recipeDto = createRecipe(brewerFatDto.getUsername(), false, true, recipeIngredientDtoList);

        Response response = new Response(recipeDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Tests successful recipe creation
        this.mockMvc.perform(get("/recipe/" + recipeDto.getRecipeId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        // Recipe delete
        this.mockMvc.perform(delete("/recipe/" + recipeDto.getRecipeId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The recipe was deleted successfully")));

        deleteBrewer(brewerFatDto, token);

    }

    @Test
    void editRecipe() throws Exception {
        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin("TEST");


        List<RecipeIngredientDto> recipeIngredientDtoList = new ArrayList<>();
        recipeIngredientDtoList.add(createRecipeIngredient(brewerFatDto.getUsername(),
                "TEST1",false,true,Math.ceil(Math.random())));
        RecipeDto recipeDto = createRecipe(brewerFatDto.getUsername(), false, true, recipeIngredientDtoList);

        Response response = new Response(recipeDto);

        ObjectMapper objectMapper = new ObjectMapper();

        // Tests successful recipe creation
        this.mockMvc.perform(get("/recipe/" + recipeDto.getRecipeId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        recipeDto.setDescription("DESCRIPTION NUEVA");

        //Edit recipe
        this.mockMvc.perform(put("/recipe/" + recipeDto.getRecipeId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(recipeDto))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("The recipe has been updated successfully")));

        //Tests successful recipe modification
        this.mockMvc.perform(get("/recipe/" + recipeDto.getRecipeId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(equalTo(objectMapper.writeValueAsString(response))));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getIngredientsByRecipeTest() throws Exception{
        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin("TEST");


        List<RecipeIngredientDto> recipeIngredientDtoList = new ArrayList<>();
        recipeIngredientDtoList.add(createRecipeIngredient(brewerFatDto.getUsername(),
                "TEST1",false,true,23.0 ));
        RecipeDto recipeDto = createRecipe(brewerFatDto.getUsername(), false, true, recipeIngredientDtoList);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(get("/recipe/" + recipeDto.getRecipeId()+ "/ingredient")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        deleteBrewer(brewerFatDto, token);

    }
}
