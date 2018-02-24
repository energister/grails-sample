package energister.grails.sample

import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

import java.time.LocalDate

class RatesControllerSpec extends Specification implements ControllerUnitTest<RatesController> {

    def setupSpec() {
        JavaTimeCategory.load()
    }

    def downloadsCount = 0

    def setup() {
        def dateRates = [new CentralBankCurrencyRate(numericCode: 849,
                                                     alphabeticCode: "USD",
                                                     units: 1,
                                                     name: "US Dollar",
                                                     rate: 56),
                         new CentralBankCurrencyRate(numericCode: 978,
                                                     alphabeticCode: "EUR",
                                                     units: 1,
                                                     name: "Euro",
                                                     rate: 70)]
        controller.ratesDownloaderService = Mock(RatesDownloaderService) {
            download(_) >> { LocalDate date ->
                downloadsCount++
                new CentralBankDateCurrencyRatesTable(date, dateRates)
            }
        }
    }

    void "form rates table for the last month"() {
        when:
        controller.index()

        then:
        response.text.size() > 0
        downloadsCount > 28
    }
}
