import datetime
import pandas
import glob
import numpy as np

path = "C:\\Users\\the1s\\PycharmProjects\\StockTrader\\StockData2\\"
cleanpath = "C:\\Users\\the1s\\PycharmProjects\\StockTrader\\CleanStockData2\\"
files = glob.glob(path+"*.csv")

for file in files:
    df = pandas.read_csv(file)
    df['marketClose'].replace(to_replace=-1, value=df['close'], inplace=True)
    df['marketOpen'].replace(to_replace=-1, value=df['open'], inplace=True)
    df['marketHigh'].replace(to_replace=-1, value=df['high'], inplace=True)
    df['marketLow'].replace(to_replace=-1, value=df['low'], inplace=True)
    df.drop(['open','close','high','low','volume'], 1, inplace=True)
    df['marketOpen'].replace(to_replace='', value=np.nan)
    df = df.dropna()
    df.to_csv(path_or_buf=cleanpath+file.split("\\")[-1], index=False)
    print(file)