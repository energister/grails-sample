package energister.grails.sample

import grails.testing.services.ServiceUnitTest
import spock.lang.Specification

import java.time.LocalDate

class RatesDownloaderServiceSpec extends Specification implements ServiceUnitTest<RatesDownloaderService> {

    void "Rates for 1.01.2018"() {
        given:
        def date = new LocalDate(2018, 01, 01)

        when:
        CentralBankDateCurrencyRatesTable ratesTable = service.download(date)

        then:
        ratesTable.rates.size() == 34

        and: "there are USD and EUR among them"
        CurrencyRate usdRate = RatesExtractor.extract(Currencies.USD, ratesTable)
        CurrencyRate eurRate = RatesExtractor.extract(Currencies.EUR, ratesTable)

        usdRate.rate == 57.6002
        eurRate.rate == 68.8668
    }

    void "Latest rates are available (format haven't been changed)"() {
        given:
        // not sure whether rates are available from 12:00 AM MSK
        def yesterday = RussiaDate.now().minusDays(1)

        when:
        CentralBankDateCurrencyRatesTable ratesTable = service.download(yesterday)

        then:
        ratesTable.rates.size() == 34

        and: "there are USD and EUR among them"
        RatesExtractor.extract(Currencies.USD, ratesTable)
        RatesExtractor.extract(Currencies.EUR, ratesTable)
    }
}
