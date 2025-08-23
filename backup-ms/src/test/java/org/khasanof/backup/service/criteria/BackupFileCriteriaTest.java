package org.khasanof.backup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BackupFileCriteriaTest {

    @Test
    void newBackupFileCriteriaHasAllFiltersNullTest() {
        var backupFileCriteria = new BackupFileCriteria();
        assertThat(backupFileCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void backupFileCriteriaFluentMethodsCreatesFiltersTest() {
        var backupFileCriteria = new BackupFileCriteria();

        setAllFilters(backupFileCriteria);

        assertThat(backupFileCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void backupFileCriteriaCopyCreatesNullFilterTest() {
        var backupFileCriteria = new BackupFileCriteria();
        var copy = backupFileCriteria.copy();

        assertThat(backupFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(backupFileCriteria)
        );
    }

    @Test
    void backupFileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var backupFileCriteria = new BackupFileCriteria();
        setAllFilters(backupFileCriteria);

        var copy = backupFileCriteria.copy();

        assertThat(backupFileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(backupFileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var backupFileCriteria = new BackupFileCriteria();

        assertThat(backupFileCriteria).hasToString("BackupFileCriteria{}");
    }

    private static void setAllFilters(BackupFileCriteria backupFileCriteria) {
        backupFileCriteria.id();
        backupFileCriteria.filePath();
        backupFileCriteria.fileSize();
        backupFileCriteria.createdAt();
        backupFileCriteria.backupJobId();
        backupFileCriteria.tenantId();
        backupFileCriteria.distinct();
    }

    private static Condition<BackupFileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFilePath()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getCreatedAt()) &&
                condition.apply(criteria.getBackupJobId()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BackupFileCriteria> copyFiltersAre(BackupFileCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFilePath(), copy.getFilePath()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getCreatedAt(), copy.getCreatedAt()) &&
                condition.apply(criteria.getBackupJobId(), copy.getBackupJobId()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
