package energister.grails.sample

import groovy.transform.TupleConstructor
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

import java.time.LocalDate
import java.time.format.DateTimeFormatter

/**
 * All currencies rates at the specific {@link #date}
 * published by The Central Bank of the Russian Federation
 */
@TupleConstructor
class CentralBankDateCurrencyRatesTable {
    LocalDate date
    Collection<CentralBankCurrencyRate> rates
}

class CentralBankCurrencyRate {
    String numericCode
    String alphabeticCode
    int units
    String name
    double rate
}

/**
 * Fetch rates published by The Central Bank (download & parse)
 */
class RatesDownloaderService {

    private static final pageWithDataUrl = 'https://www.cbr.ru/eng/currency_base/daily.aspx?date_req='

    /**
     * @return rates at the {@code date} or throws an exception
     */
    CentralBankDateCurrencyRatesTable download(LocalDate date) {
        log.info "Downloading rates for the $date"

        String dateParameter = date.format(DateTimeFormatter.ISO_LOCAL_DATE)
        String url = pageWithDataUrl + dateParameter

        log.debug "Parsing $url"
        Document doc = Jsoup.connect(url).get()

        def rates = new ArrayList<CentralBankCurrencyRate>()

        Elements rows = doc.select("table[class=data] tr")  // all <tr> inside <table class="data">
        rows.each { Element row ->
            // skip row with headers
            if (row.html().toLowerCase().contains("<th>")) {
                return
            }

            List<String> data = row.children().collect { Element column -> column.text() }

            final int expectedRowsCount = 5
            if (data.size() != expectedRowsCount) {
                log.warn("There are ${data.size()} columns in the data table at $url ($expectedRowsCount expected)")
                log.debug("Actual row content is ${row.html()}")
            }

            rates.add(new CentralBankCurrencyRate(numericCode: data[0],
                                                  alphabeticCode: data[1],
                                                  units: data[2] as int,
                                                  name: data[3],
                                                  rate: data[4] as double))
        }

        log.info "Rates for ${rates.size()} currencies has been downloaded"

        new CentralBankDateCurrencyRatesTable(date, rates)
    }
}
