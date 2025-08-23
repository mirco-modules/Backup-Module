package org.khasanof.backup.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BackupTenantSettingCriteriaTest {

    @Test
    void newBackupTenantSettingCriteriaHasAllFiltersNullTest() {
        var backupTenantSettingCriteria = new BackupTenantSettingCriteria();
        assertThat(backupTenantSettingCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void backupTenantSettingCriteriaFluentMethodsCreatesFiltersTest() {
        var backupTenantSettingCriteria = new BackupTenantSettingCriteria();

        setAllFilters(backupTenantSettingCriteria);

        assertThat(backupTenantSettingCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void backupTenantSettingCriteriaCopyCreatesNullFilterTest() {
        var backupTenantSettingCriteria = new BackupTenantSettingCriteria();
        var copy = backupTenantSettingCriteria.copy();

        assertThat(backupTenantSettingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(backupTenantSettingCriteria)
        );
    }

    @Test
    void backupTenantSettingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var backupTenantSettingCriteria = new BackupTenantSettingCriteria();
        setAllFilters(backupTenantSettingCriteria);

        var copy = backupTenantSettingCriteria.copy();

        assertThat(backupTenantSettingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(backupTenantSettingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var backupTenantSettingCriteria = new BackupTenantSettingCriteria();

        assertThat(backupTenantSettingCriteria).hasToString("BackupTenantSettingCriteria{}");
    }

    private static void setAllFilters(BackupTenantSettingCriteria backupTenantSettingCriteria) {
        backupTenantSettingCriteria.id();
        backupTenantSettingCriteria.timeUnit();
        backupTenantSettingCriteria.duration();
        backupTenantSettingCriteria.isActive();
        backupTenantSettingCriteria.tenantId();
        backupTenantSettingCriteria.distinct();
    }

    private static Condition<BackupTenantSettingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTimeUnit()) &&
                condition.apply(criteria.getDuration()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BackupTenantSettingCriteria> copyFiltersAre(
        BackupTenantSettingCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTimeUnit(), copy.getTimeUnit()) &&
                condition.apply(criteria.getDuration(), copy.getDuration()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
