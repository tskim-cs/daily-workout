package workout.dailyworkout.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
import workout.dailyworkout.domain.workout.WorkoutSession;
import workout.dailyworkout.domain.workout.WorkoutSet;
import workout.dailyworkout.service.ExerciseService;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ExerciseApiController {
    private final ExerciseService exerciseService;

    /**
     * Get all exercise types
     * @return list of exercise types name
     */
    @GetMapping("/api/v1/exercises/types")
    public Result getExerciseTypes() {
        List<String> types = ExerciseType.names();
        return new Result(types);
    }

    /**
     * Get all exercise equipments
     * @return list of exercise equipments name
     */
    @GetMapping("/api/v1/exercises/equips")
    public Result getExerciseEquips() {
        List<String> equips = ExerciseEquip.names();
        return new Result(equips);
    }

    /**
     * Get all exercises
     * @return list of exercises name
     */
    @GetMapping("/api/v1/exercises")
    public Result getExercises() {
        List<String> exercises = new ArrayList<>();
        for (Exercise e : exerciseService.findAllExercises()) {
            exercises.add(e.getName());
        }
        return new Result(exercises);
    }

    /**
     * Find exercise by name
     * @param request name
     * @return exercise info and last relevant workout info
     */
    @PostMapping("/api/v1/exercises/find")
    public GetExerciseInfoResponse getExerciseInfo(@RequestBody @Valid GetExerciseInfoRequest request) {
        List<Exercise> exercises = exerciseService.findByName(request.getName());
        if (exercises.isEmpty()) {
            throw new IllegalArgumentException("No such exercise.");
        }
        Exercise exercise = exercises.get(0);
        List<WorkoutSet> lastWorkoutSets = exerciseService.findLastWorkoutSets(exercise.getId());

        GetExerciseInfoResponse result = new GetExerciseInfoResponse(exercise.getId(), exercise.getName(), exercise.getType(), exercise.getEquip());
        for (WorkoutSet s : lastWorkoutSets) {
            WorkoutSetResponse setResponse = new WorkoutSetResponse(s.getCreatedDate(), s.getWeight(), s.getReps());
            result.getWorkoutSetResponses().add(setResponse);
        }

        return result;
    }

    /**
     * Save new exercise
     * @param request name, type, equip
     * @return id and name of new exercise (exception when name already exists)
     */
    @PostMapping("/api/v1/exercises/add")
    public DefaultExerciseResponse saveExerciseV1(@RequestBody @Valid CreateExerciseRequest request) {
        Exercise exercise = Exercise.createExercise(request.getName(),
                ExerciseType.valueOf(request.getType().toUpperCase()),
                ExerciseEquip.valueOf(request.getEquip().toUpperCase()));

        Long id = exerciseService.addExercise(exercise);
        return new DefaultExerciseResponse(id, exercise.getName());
    }

    /**
     * Update exercise
     * @param id exercise id
     * @param request name
     * @return id and name of new exercise
     */
    @PutMapping("/api/v1/exercises/update/{id}")
    public DefaultExerciseResponse updateExerciseV1(@PathVariable("id") Long id,
                                                    @RequestBody @Valid UpdateExerciseRequest request) {
        exerciseService.updateExerciseName(id, request.getName());
        Exercise exercise = exerciseService.findOne(id);
        return new DefaultExerciseResponse(exercise.getId(), exercise.getName());
    }

    @Data
    @AllArgsConstructor
    private static class Result<T> {
        private T data;
    }

    @Data
    private static class CreateExerciseRequest {
        @NotEmpty
        private String name;

        @NotEmpty
        private String type;

        @NotEmpty
        private String equip;
    }

    @Data
    @AllArgsConstructor
    private static class DefaultExerciseResponse {
        private Long id;
        private String name;
    }

    @Data
    private static class UpdateExerciseRequest {
        @NotEmpty
        private String name;
    }

    @Data
    private static class GetExerciseInfoRequest {
        @NotEmpty
        private String name;
    }

    @Data
    @RequiredArgsConstructor
    private static class GetExerciseInfoResponse {
        private final Long id;
        private final String name;
        private final ExerciseType type;
        private final ExerciseEquip equip;
        private final List<WorkoutSetResponse> workoutSetResponses = new ArrayList<WorkoutSetResponse>();
    }

    @Data
    @RequiredArgsConstructor
    private static class WorkoutSetResponse {
        private final LocalDateTime lastRecordDate;
        private final Integer lastWeight;
        private final Integer lastReps;
    }
}
