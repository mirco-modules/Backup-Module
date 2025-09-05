package org.khasanof.backup.domain.common.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.temporal.ChronoUnit;

/**
 * The BackupTimeUnit enumeration.
 */
@Getter
@RequiredArgsConstructor
public enum BackupTimeUnit {
    HOUR(ChronoUnit.HOURS),
    DAY(ChronoUnit.DAYS),
    WEEK(ChronoUnit.DAYS),
    MONTH(ChronoUnit.DAYS),
    ;

    private final ChronoUnit chronoUnit;
}
