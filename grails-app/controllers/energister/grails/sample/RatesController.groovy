package energister.grails.sample

import energister.grails.sample.models.TableRowModel
import grails.converters.JSON

import java.time.LocalDate

class RatesController {

    def ratesDownloaderService

    def index() {
        LocalDate date = params.date
        if (date == null) {
            date = RussiaDate.now()
        }

        render getRatesForTheLastMonth(date) as JSON
    }

    private Collection<TableRowModel> getRatesForTheLastMonth(LocalDate till) {
        (till..<till.minusMonths(1)).collect {
            def dateRatesTable = ratesDownloaderService.download(it)
            new TableRowModel(date: it,
                              usdRate: RatesExtractor.extract(Currencies.USD, dateRatesTable).rate,
                              eurRate: RatesExtractor.extract(Currencies.EUR, dateRatesTable).rate)
        }
    }
}
