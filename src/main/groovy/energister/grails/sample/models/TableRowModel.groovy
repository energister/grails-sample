package energister.grails.sample.models

import java.time.LocalDate

/**
 * Row of the table with currency rates which is rendered by view
 */
class TableRowModel {
    LocalDate date
    double usdRate
    double eurRate
}
