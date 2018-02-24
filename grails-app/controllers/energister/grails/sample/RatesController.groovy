package energister.grails.sample

import energister.grails.sample.models.TableRowModel

class RatesController {

    def ratesDownloaderService

    def index() {
        def today = RussiaDate.now()
        (today..today.minusMonths(1)).collect {
            def ratesTable = ratesDownloaderService.download(it)
            new TableRowModel(date: it,
                              usdRate: RatesExtractor.extract(Currencies.USD, ratesTable),
                              eurRate: RatesExtractor.extract(Currencies.USD, ratesTable))
        }
    }
}
