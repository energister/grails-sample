package energister.grails.sample

import java.time.LocalDate

class CurrencyRate {

    LocalDate date
    Currencies currency
    double rate

    static constraints = {
    }
}
