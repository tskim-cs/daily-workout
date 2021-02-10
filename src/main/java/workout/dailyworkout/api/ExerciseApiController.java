package workout.dailyworkout.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import workout.dailyworkout.domain.Exercise;
import workout.dailyworkout.domain.ExerciseEquip;
import workout.dailyworkout.domain.ExerciseType;
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
     * Get all exercises
     * @param name name of exercise
     * @return list of exercises name
     */
    @GetMapping("/api/v1/exercises")
    public List<DefaultExerciseResponse> getExercises(@RequestParam(value = "name", required = false) String name) {
        List<DefaultExerciseResponse> exerciseResponses = new ArrayList<>();
        List<Exercise> exerciseList = name != null ?
                exerciseService.findByName(name) :
                exerciseService.findAllExercises();

        // TODO Is it right place to handle error?
        if (exerciseList.isEmpty()) {
            throw new IllegalArgumentException("No such exercise.");
        }
        else if (exerciseList.size() > 1) {
            throw new IllegalStateException("Cannot have duplicate exercise.");
        }

        for (Exercise e : exerciseList) {
            exerciseResponses.add(new DefaultExerciseResponse(e.getId(), e.getName()));
        }

        return exerciseResponses;
    }


    /**
     * Get specific exercise info with given ID
     * @return id and name of exercise
     */
    @GetMapping("/api/v1/exercises/{id}")
    public GetExerciseInfoResponse getExercises(@PathVariable("id") Long id){
        Exercise exercise = exerciseService.findOne(id);
        if (exercise == null) {
            throw new IllegalArgumentException("No such exercise.");
        }

        List<WorkoutSet> lastSets = exerciseService.findWorkoutSetsInLastSession(id);
        return GetExerciseInfoResponse.createFromExerciseAndSets(exercise, lastSets);
    }

    /**
     * Save new exercise
     * @param request name, type, equip
     * @return id and name of new exercise (exception when name already exists)
     */
    @PostMapping("/api/v1/exercises")
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

    // For now, DELETE request is not allowed.

    /**
     * Get all exercise types
     * @return list of exercise types name
     */
    @GetMapping("/api/v1/exercises/types")
    public Result<List<String>> getExerciseTypes() {
        List<String> types = ExerciseType.names();
        return new Result<>(types);
    }

    /**
     * Get all exercise equipments
     * @return list of exercise equipments name
     */
    @GetMapping("/api/v1/exercises/equips")
    public Result<List<String>> getExerciseEquips() {
        List<String> equips = ExerciseEquip.names();
        return new Result<>(equips);
    }

    @Data
    @AllArgsConstructor
    private static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    private static class DefaultExerciseResponse {
        private Long id;
        private String name;
    }

    @Data
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class GetExerciseInfoResponse {
        private final Long id;
        private final String name;
        private final ExerciseType type;
        private final ExerciseEquip equip;
        private final List<WorkoutSetResponse> lastWorkoutSets;

        public static GetExerciseInfoResponse createFromExerciseAndSets(Exercise exercise, List<WorkoutSet> sets) {
            return new GetExerciseInfoResponse(
                    exercise.getId(),
                    exercise.getName(),
                    exercise.getType(),
                    exercise.getEquip(),
                    WorkoutSetResponse.createFromWorkoutSets(sets)
            );
        }
    }

    @Data
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    private static class WorkoutSetResponse {
        private final LocalDateTime lastRecordDate;
        private final Integer lastWeight;
        private final Integer lastReps;

        public static WorkoutSetResponse createFromWorkoutSet(WorkoutSet set) {
            return new WorkoutSetResponse(set.getCreatedDate(), set.getWeight(), set.getReps());
        }

        public static List<WorkoutSetResponse> createFromWorkoutSets(List<WorkoutSet> sets) {
            List<WorkoutSetResponse> responses = new ArrayList<>();
            sets.forEach(s -> responses.add(createFromWorkoutSet(s)));
            return responses;
        }
    }

    @Data
    static class CreateExerciseRequest {
        @NotEmpty
        private String name;

        @NotEmpty
        private String type;

        @NotEmpty
        private String equip;
    }

    @Data
    static class UpdateExerciseRequest {
        @NotEmpty
        private String name;
    }
}
