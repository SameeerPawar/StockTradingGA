# Imports
from iexfinance.stocks import get_historical_intraday
import datetime
import pandas

tickerfile = open("tickers")
tickers = tickerfile.read().split('\n')
#tickers = ['NFX']

startdate = datetime.datetime(2019, 2, 16)
date = startdate
today = datetime.datetime(2019, 3, 13)
df = pandas.DataFrame()

done = []
for ticker in tickers:
    while date <= today:
        dr = get_historical_intraday(ticker, date, output_format='pandas')
        if dr.shape != (0,0):
            df = df.append(dr, ignore_index=True)
        date += datetime.timedelta(days=1)
    # if "marketClose" in dr.columns.values:
    #     df['marketClose'].replace(to_replace=0, value = df['close'], inplace=True)
    print(ticker)
    done.append(ticker)
    df.to_csv(path_or_buf="C:\\Users\\the1s\\PycharmProjects\\StockTrader\\StockData2\\" + ticker + ".csv", index=False, columns=["date","label","open","close","high", "low", "volume", "marketOpen","marketClose","marketHigh", "marketLow", "marketVolume"])
    date = startdate
    df = pandas.DataFrame()
