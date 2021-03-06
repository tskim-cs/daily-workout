package workout.dailyworkout.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
import workout.dailyworkout.service.ExerciseService;

import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ExerciseApiController.class)
public class ExerciseApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExerciseService exerciseService;

    @Test
    public void contextLoads() throws Exception {
        assertNotNull(mockMvc);
        assertNotNull(exerciseService);
    }

    private String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void createNewExercise(String name, ExerciseType type, ExerciseEquip equip) {
        Exercise exercise = Exercise.createExercise(name, type, equip);
        when(exerciseService.findByName(name))
                .thenReturn(Collections.singletonList(exercise));
        when(exerciseService.findOne(1L))
                .thenReturn(exercise);
    }

    @Test
    public void getExercises_givenParamName() throws Exception {
        createNewExercise("Barbell Squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);

        mockMvc.perform(get("/api/v1/exercises").param("name", "Barbell Squat"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0]").exists())
                .andExpect(jsonPath("$[0].name").value("Barbell Squat"));
    }

    @Test
    public void getExercises_givenPathId() throws Exception {
        createNewExercise("Barbell Squat", ExerciseType.LEGS, ExerciseEquip.BARBELL);

        mockMvc.perform(get("/api/v1/exercises/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.name").value("Barbell Squat"));
    }

    @Test
    public void saveExercise_postRequest() throws Exception {
        // Given
        when(exerciseService.addExercise(any(Exercise.class))).thenReturn(1L);

        // When
        ExerciseApiController.CreateExerciseRequest request = new ExerciseApiController.CreateExerciseRequest();
        request.setName("Barbell squat");
        request.setEquip("BARBELL");
        request.setType("LEGS");

        mockMvc.perform(post("/api/v1/exercises")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(request))
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Barbell Squat"));
    }

    // TODO - Get Wrong Param Name Exception Test
    // TODO - Post Duplicated Exercise Name Exception Test
}