import pandas
import glob

path = "C:\\Users\\the1s\\PycharmProjects\\StockTrader\\CleanStockData\\"
files = glob.glob(path+"*.csv")

for file in files:
    df = pandas.read_csv(file)
    df['date'] = df['date'].astype(str)+" "+df['label']
    df.drop('label', 1, inplace=True)
    df.columns = ['date','open', 'close', 'high', 'low', 'volume']
    df.to_csv(path_or_buf=file, index=False, columns=['date', 'open', 'high', 'low', 'close', 'volume'])
    print(file)