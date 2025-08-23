package org.khasanof.backup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BackupTenantCriteriaTest {

    @Test
    void newBackupTenantCriteriaHasAllFiltersNullTest() {
        var backupTenantCriteria = new BackupTenantCriteria();
        assertThat(backupTenantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void backupTenantCriteriaFluentMethodsCreatesFiltersTest() {
        var backupTenantCriteria = new BackupTenantCriteria();

        setAllFilters(backupTenantCriteria);

        assertThat(backupTenantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void backupTenantCriteriaCopyCreatesNullFilterTest() {
        var backupTenantCriteria = new BackupTenantCriteria();
        var copy = backupTenantCriteria.copy();

        assertThat(backupTenantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(backupTenantCriteria)
        );
    }

    @Test
    void backupTenantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var backupTenantCriteria = new BackupTenantCriteria();
        setAllFilters(backupTenantCriteria);

        var copy = backupTenantCriteria.copy();

        assertThat(backupTenantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(backupTenantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var backupTenantCriteria = new BackupTenantCriteria();

        assertThat(backupTenantCriteria).hasToString("BackupTenantCriteria{}");
    }

    private static void setAllFilters(BackupTenantCriteria backupTenantCriteria) {
        backupTenantCriteria.id();
        backupTenantCriteria.tenantKey();
        backupTenantCriteria.dbName();
        backupTenantCriteria.dbHost();
        backupTenantCriteria.dbPort();
        backupTenantCriteria.dbUsername();
        backupTenantCriteria.dbPassword();
        backupTenantCriteria.settingId();
        backupTenantCriteria.backupFilesId();
        backupTenantCriteria.distinct();
    }

    private static Condition<BackupTenantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTenantKey()) &&
                condition.apply(criteria.getDbName()) &&
                condition.apply(criteria.getDbHost()) &&
                condition.apply(criteria.getDbPort()) &&
                condition.apply(criteria.getDbUsername()) &&
                condition.apply(criteria.getDbPassword()) &&
                condition.apply(criteria.getSettingId()) &&
                condition.apply(criteria.getBackupFilesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BackupTenantCriteria> copyFiltersAre(
        BackupTenantCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTenantKey(), copy.getTenantKey()) &&
                condition.apply(criteria.getDbName(), copy.getDbName()) &&
                condition.apply(criteria.getDbHost(), copy.getDbHost()) &&
                condition.apply(criteria.getDbPort(), copy.getDbPort()) &&
                condition.apply(criteria.getDbUsername(), copy.getDbUsername()) &&
                condition.apply(criteria.getDbPassword(), copy.getDbPassword()) &&
                condition.apply(criteria.getSettingId(), copy.getSettingId()) &&
                condition.apply(criteria.getBackupFilesId(), copy.getBackupFilesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
