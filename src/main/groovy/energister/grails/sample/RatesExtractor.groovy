package energister.grails.sample

class RatesExtractor {

    /**
     * Performs conversion from the data downloaded by {@link helloworld.RatesDownloaderService}
     * to the domain model (along with the consistency validation)
     * @return requested {@code currency}
     * @throws IllegalStateException if something goes wrong
     */
    static CurrencyRate extract(Currencies currency, CentralBankDateCurrencyRatesTable ratesTable) {
        def currencyCode = currency.name()

        def centralBankRate = ratesTable.rates.find { it.alphabeticCode == currencyCode }
        if (centralBankRate == null) {
            throw new IllegalStateException("No rate for $currencyCode in currency rates table for the ${ratesTable.date}")
        }

        // validate that nothing has changed in format
        if (centralBankRate.units != getExpectedUnits(currency)) {
            throw new IllegalStateException("Unusual unit=${centralBankRate.units} for the $currency in currency rates table")
        }

        new CurrencyRate(date: ratesTable.date, currency: currency, rate: centralBankRate.rate)
    }

    private static int getExpectedUnits(Currencies currency) {
        switch (currency) {
            case Currencies.USD:
            case Currencies.EUR:
                return 1

            default:
                throw new IllegalArgumentException("Currency rate extraction is not implemented for $currency yet")
        }
    }
}