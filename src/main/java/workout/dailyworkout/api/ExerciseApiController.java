package workout.dailyworkout.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
import workout.dailyworkout.service.ExerciseService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExerciseApiController {
    private final ExerciseService exerciseService;

    @GetMapping("/api/v1/exercise/types")
    public GetExerciseTypeResponse getExerciseTypes() {
        List<String> types = ExerciseType.names();
        return new GetExerciseTypeResponse(types);
    }

    @GetMapping("/api/v1/exercise/parts")
    public GetExerciseTypeResponse getExerciseParts() {
        List<String> parts = ExerciseType.names();
        return new GetExerciseTypeResponse(parts);
    }

    // Using DTO
    @PostMapping("/api/v1/exercise/add")
    public CreateExerciseResponse saveExerciseV1(@RequestBody @Valid CreateExerciseRequest request) {
        Exercise exercise = Exercise.createExercise(request.getName(), request.getType(), request.getEquip());

        Long id = exerciseService.addExercise(exercise);
        return new CreateExerciseResponse(id);
    }

    @Data
    @RequiredArgsConstructor
    private static class GetExerciseTypeResponse {
        final List<String> types;
    }

    @Data
    private static class CreateExerciseRequest {
        @NotEmpty
        private String name;

        @NotEmpty
        private ExerciseType type;

        @NotEmpty
        private ExerciseEquip equip;
    }

    @Data
    @AllArgsConstructor
    private static class CreateExerciseResponse {
        private Long id;
    }

}
