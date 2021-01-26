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
import it.progettois.brewday.service.BrewService;
import it.progettois.brewday.service.BrewerService;
import it.progettois.brewday.service.IngredientService;
import it.progettois.brewday.service.RecipeService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
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
class BrewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrewerService brewerService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private BrewService brewService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    BrewerFatDto createBrewer(String username) throws ConversionException, AlreadyPresentException {

        BrewerDto brewer = new BrewerDto();
        brewer.setUsername(username);
        brewer.setPassword("TEST");
        brewer.setName("TEST");
        brewer.setEmail(username);
        brewer.setMaxBrew(20);

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


    RecipeIngredientDto createRecipeIngredient(Integer ingredientId, String name, Double quantity) {

        return RecipeIngredientDto.builder()
                .ingredientId(ingredientId)
                .ingredientName(name)
                .quantity(quantity)
                .build();

    }

    RecipeDto createRecipe(String username, String name, boolean shared, boolean save, List<RecipeIngredientDto> ingredients) throws BrewerNotFoundException,
            ConversionException, IngredientNotFoundException, NegativeQuantityException {

        RecipeDto recipeDto = RecipeDto.builder()
                .name(name)
                .description("TEST")
                .username(username)
                .shared(shared)
                .ingredients(ingredients)
                .build();

        return save ? this.recipeService.saveRecipe(recipeDto, username) : recipeDto;
    }

    BrewDto createBrew(Integer quantity, RecipeDto recipe,
                       String username, Boolean save) throws BrewerNotFoundException, AccessDeniedException {

        BrewDto brewDto = BrewDto.builder()
                .quantity(quantity)
                .startDate(null)
                .duration("forever and ever")
                .note("TEST")
                .recipe(recipe)
                .brewerUsername(username+"@"+username+".it")
                .build();

        return save ? this.brewService.saveBrew(brewDto, username) : brewDto;
    }

    @Test
    void getBrewsTest() throws Exception {

        BrewerFatDto brewerFatDto = createBrewer("TEST");
        String token = performLogin(brewerFatDto.getUsername());

        this.mockMvc.perform(get("/brew")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("data")));

        deleteBrewer(brewerFatDto, token);
    }

    @Test
    void getBrewTest() throws Exception {

        BrewerFatDto brewerFatDto = createBrewer("TEST1");
        String token = performLogin(brewerFatDto.getUsername());

        BrewerFatDto brewerFatDto2 = createBrewer("TEST2");
        String token2 = performLogin(brewerFatDto2.getUsername());

        //New ingredients
        IngredientDto ingredientDto1 = createIngredient(brewerFatDto.getUsername(), "TEST1", false, true, 10);
        IngredientDto ingredientDto2 = createIngredient(brewerFatDto.getUsername(), "TEST2", false, true, 10);
        IngredientDto ingredientDto3 = createIngredient(brewerFatDto.getUsername(), "TEST3", false, true, 5);

        List<RecipeIngredientDto> recipeIngredientDtoList1 = new ArrayList<>();
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto1.getIngredientId(),
                "TEST1",5.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto2.getIngredientId(),
                "TEST2",7.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto3.getIngredientId(),
                "TEST3",15.0 ));

        //New recipe
        RecipeDto recipe = createRecipe(brewerFatDto.getUsername(), "RECIPE1", false, true, recipeIngredientDtoList1);

        //New brew
        BrewDto brew = createBrew(1000, recipe, brewerFatDto.getUsername(), true);

        this.mockMvc.perform(get("/brew/" + brew.getBrewId())
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"brewId\":" + brew.getBrewId())));


        //Get non existing brew
        this.mockMvc.perform(get("/brew/-1")
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

        //Get other brewers' brew
        this.mockMvc.perform(get("/brew/" + brew.getBrewId())
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isForbidden());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);

    }


    @Test
    void createBrewTest() throws Exception {

        BrewerFatDto brewerFatDto = createBrewer("TEST1");
        String token = performLogin(brewerFatDto.getUsername());

        //New ingredients
        IngredientDto ingredientDto1 = createIngredient(brewerFatDto.getUsername(), "TEST1", false, true, 10);
        IngredientDto ingredientDto2 = createIngredient(brewerFatDto.getUsername(), "TEST2", false, true, 10);
        IngredientDto ingredientDto3 = createIngredient(brewerFatDto.getUsername(), "TEST3", false, true, 5);

        List<RecipeIngredientDto> recipeIngredientDtoList1 = new ArrayList<>();
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto1.getIngredientId(),
                "TEST1",5.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto2.getIngredientId(),
                "TEST2",7.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto3.getIngredientId(),
                "TEST3",15.0 ));

        //New recipe
        RecipeDto recipe = createRecipe(brewerFatDto.getUsername(), "RECIPE1", false, true, recipeIngredientDtoList1);

        //New brew
        BrewDto brew = createBrew(1000, recipe, brewerFatDto.getUsername(), true);

        ObjectMapper objectMapper = new ObjectMapper();

        //Create valid brew
        this.mockMvc.perform(post("/brew")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brew))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());

        deleteBrewer(brewerFatDto, token);

    }

    @Test
    void editBrewTest() throws Exception {

        BrewerFatDto brewerFatDto = createBrewer("TEST1");
        String token = performLogin(brewerFatDto.getUsername());

        BrewerFatDto brewerFatDto2 = createBrewer("TEST2");
        String token2 = performLogin(brewerFatDto2.getUsername());


        //New ingredients
        IngredientDto ingredientDto1 = createIngredient(brewerFatDto.getUsername(), "TEST1", false, true, 10);
        IngredientDto ingredientDto2 = createIngredient(brewerFatDto.getUsername(), "TEST2", false, true, 10);
        IngredientDto ingredientDto3 = createIngredient(brewerFatDto.getUsername(), "TEST3", false, true, 5);

        List<RecipeIngredientDto> recipeIngredientDtoList1 = new ArrayList<>();
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto1.getIngredientId(),
                "TEST1",5.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto2.getIngredientId(),
                "TEST2",7.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto3.getIngredientId(),
                "TEST3",15.0 ));

        //New recipe
        RecipeDto recipe = createRecipe(brewerFatDto.getUsername(), "RECIPE1", false, true, recipeIngredientDtoList1);

        //New brew
        BrewDto brew = createBrew(1000, recipe, brewerFatDto.getUsername(), true);

        brew.setNote("Migliorabile");

        ObjectMapper objectMapper = new ObjectMapper();

        this.mockMvc.perform(put("/brew/" + brew.getBrewId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brew))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());


        //Modify non existing brew
        this.mockMvc.perform(put("/brew/-1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brew))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

        //Modify other brewers' brew
        this.mockMvc.perform(put("/brew/" + brew.getBrewId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brew))
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isForbidden());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);

    }

    @Test
    void deleteBrewTest() throws Exception {
        BrewerFatDto brewerFatDto = createBrewer("TEST1");
        String token = performLogin(brewerFatDto.getUsername());

        BrewerFatDto brewerFatDto2 = createBrewer("TEST2");
        String token2 = performLogin(brewerFatDto2.getUsername());


        //New ingredients
        IngredientDto ingredientDto1 = createIngredient(brewerFatDto.getUsername(), "TEST1", false, true, 10);
        IngredientDto ingredientDto2 = createIngredient(brewerFatDto.getUsername(), "TEST2", false, true, 10);
        IngredientDto ingredientDto3 = createIngredient(brewerFatDto.getUsername(), "TEST3", false, true, 5);

        List<RecipeIngredientDto> recipeIngredientDtoList1 = new ArrayList<>();
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto1.getIngredientId(),
                "TEST1",5.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto2.getIngredientId(),
                "TEST2",7.0 ));
        recipeIngredientDtoList1.add(createRecipeIngredient(ingredientDto3.getIngredientId(),
                "TEST3",15.0 ));

        //New recipe
        RecipeDto recipe = createRecipe(brewerFatDto.getUsername(), "RECIPE1", false, true, recipeIngredientDtoList1);

        //New brew
        BrewDto brew = createBrew(1000, recipe, brewerFatDto.getUsername(), true);

        ObjectMapper objectMapper = new ObjectMapper();

        //Delete other brewers' brew
        this.mockMvc.perform(delete("/brew/" + brew.getBrewId())
                .header("Authorization", token2))
                .andDo(print())
                .andExpect(status().isForbidden());

        //Delete brew
        this.mockMvc.perform(delete("/brew/" + brew.getBrewId())
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brew))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isOk());


        //Delete non existing brew
        this.mockMvc.perform(delete("/brew/-1")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(brew))
                .header("Authorization", token))
                .andDo(print())
                .andExpect(status().isNotFound());

        deleteBrewer(brewerFatDto, token);
        deleteBrewer(brewerFatDto2, token2);

    }

}
