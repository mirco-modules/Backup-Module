package org.khasanof.backup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BackupJobCriteriaTest {

    @Test
    void newBackupJobCriteriaHasAllFiltersNullTest() {
        var backupJobCriteria = new BackupJobCriteria();
        assertThat(backupJobCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void backupJobCriteriaFluentMethodsCreatesFiltersTest() {
        var backupJobCriteria = new BackupJobCriteria();

        setAllFilters(backupJobCriteria);

        assertThat(backupJobCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void backupJobCriteriaCopyCreatesNullFilterTest() {
        var backupJobCriteria = new BackupJobCriteria();
        var copy = backupJobCriteria.copy();

        assertThat(backupJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(backupJobCriteria)
        );
    }

    @Test
    void backupJobCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var backupJobCriteria = new BackupJobCriteria();
        setAllFilters(backupJobCriteria);

        var copy = backupJobCriteria.copy();

        assertThat(backupJobCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(backupJobCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var backupJobCriteria = new BackupJobCriteria();

        assertThat(backupJobCriteria).hasToString("BackupJobCriteria{}");
    }

    private static void setAllFilters(BackupJobCriteria backupJobCriteria) {
        backupJobCriteria.id();
        backupJobCriteria.startedAt();
        backupJobCriteria.finishedAt();
        backupJobCriteria.status();
        backupJobCriteria.message();
        backupJobCriteria.backupFileId();
        backupJobCriteria.distinct();
    }

    private static Condition<BackupJobCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartedAt()) &&
                condition.apply(criteria.getFinishedAt()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getMessage()) &&
                condition.apply(criteria.getBackupFileId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BackupJobCriteria> copyFiltersAre(BackupJobCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartedAt(), copy.getStartedAt()) &&
                condition.apply(criteria.getFinishedAt(), copy.getFinishedAt()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getMessage(), copy.getMessage()) &&
                condition.apply(criteria.getBackupFileId(), copy.getBackupFileId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
