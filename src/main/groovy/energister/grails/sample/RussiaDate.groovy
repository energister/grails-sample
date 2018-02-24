package energister.grails.sample

import java.time.LocalDate
import java.time.ZoneId

class RussiaDate {
    static LocalDate now() {
        LocalDate.now(ZoneId.of("Europe/Moscow"))
    }
}
