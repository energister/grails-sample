package energister.grails.sample

import java.time.LocalDate

/**
 * Groovy extensions for JSR-310
 */
class JavaTimeCategory {
    static load() {
        LocalDate.mixin(JavaTimeCategory)
    }

    static next(LocalDate self) { self.plusDays(1) }
    static previous(LocalDate self) { self.minusDays(1) }

    // workaround for https://issues.apache.org/jira/browse/GROOVY-3612
    String toString() {
        // invoke toString() on mixin's owner
        Object owner = this.metaClass.owner
        owner.class.getMethod('toString').invoke(owner)
    }
}
