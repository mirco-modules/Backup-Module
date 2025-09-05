package org.khasanof.backup.service.criteria;

import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static org.assertj.core.api.Assertions.assertThat;

class BackupServerCriteriaTest {

    @Test
    void newBackupServerCriteriaHasAllFiltersNullTest() {
        var backupServerCriteria = new BackupServerCriteria();
        assertThat(backupServerCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void backupServerCriteriaFluentMethodsCreatesFiltersTest() {
        var backupServerCriteria = new BackupServerCriteria();

        setAllFilters(backupServerCriteria);

        assertThat(backupServerCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void backupServerCriteriaCopyCreatesNullFilterTest() {
        var backupServerCriteria = new BackupServerCriteria();
        var copy = backupServerCriteria.copy();

        assertThat(backupServerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(backupServerCriteria)
        );
    }

    @Test
    void backupServerCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var backupServerCriteria = new BackupServerCriteria();
        setAllFilters(backupServerCriteria);

        var copy = backupServerCriteria.copy();

        assertThat(backupServerCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(backupServerCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var backupServerCriteria = new BackupServerCriteria();

        assertThat(backupServerCriteria).hasToString("BackupServerCriteria{}");
    }

    private static void setAllFilters(BackupServerCriteria backupServerCriteria) {
        backupServerCriteria.id();
        backupServerCriteria.port();
        backupServerCriteria.status();
        backupServerCriteria.host();
        backupServerCriteria.username();
        backupServerCriteria.password();
        backupServerCriteria.distinct();
    }

    private static Condition<BackupServerCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPort()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getHost()) &&
                condition.apply(criteria.getUsername()) &&
                condition.apply(criteria.getPassword()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BackupServerCriteria> copyFiltersAre(
        BackupServerCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPort(), copy.getPort()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getHost(), copy.getHost()) &&
                condition.apply(criteria.getUsername(), copy.getUsername()) &&
                condition.apply(criteria.getPassword(), copy.getPassword()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
