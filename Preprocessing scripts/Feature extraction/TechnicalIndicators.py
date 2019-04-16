import pandas
import glob
from finta import TA
from pandas import DataFrame, Series

path = "C:\\Users\\the1s\\OneDrive\\Documents\\NetBeansProjects\\StockTraderGA\\stockdata\\"
savepath = "C:\\Users\\the1s\\OneDrive\\Documents\\NetBeansProjects\\StockTraderGA\\candledata\\"
files = glob.glob(path+"*.csv")


def avg_volume(ohlc: DataFrame, period: int = 10, column: str = "volume") -> Series:
    return pandas.Series(
        ohlc[column]
            .rolling(center=False, window=period, min_periods=period - 1)
            .mean(),
        name="AVG Volume".format(period),
    )

def inc_volume(ohlc: DataFrame, period: int = 3, column: str = "volume") -> Series:
    func = lambda x: (x[2]-x[0])/x[0] if x[0]<x[1]<x[2] else 0
    return pandas.Series(
        ohlc[column]
            .rolling(center=False, window=period)
            .apply(func),
        name="INCREASING VOLUME".format(period),
    )


for file in files:
    df = pandas.read_csv(file)
    df["sma5"] = TA.SMA(df,5*390)
    df["sma7"] = TA.SMA(df,7*390)
    df["sma14"] = TA.SMA(df,14*390)
    df["ema5"] = TA.EMA(df,5*390)
    df["ema7"] = TA.EMA(df,7*390)
    df["ema9"] = TA.EMA(df,9*390)
    df["ema13"] = TA.EMA(df,13*390)
    df["ema20"] = TA.EMA(df,20*390)
    macd = TA.MACD(df, 12*390, 26*390, 9*390)
    df["macd"] = macd.iloc[:,0]
    df["macd_signal"] = macd.iloc[:,1]
    df["rsi"] = TA.RSI(df,14*390)
    bb = TA.BBANDS(df,20*390)
    df["ubb"] = bb.iloc[:,0]
    df["mbb"] = bb.iloc[:,1]
    df["lbb"] = bb.iloc[:,2]
    df["avgvol10"] = avg_volume(df,10*390)
    df["incvol"] = inc_volume(df,3)
    print(df.size)
    df = df.dropna()
    print(df.size)
    df["SP>SMA5"] = df["close"] > df["sma5"]
    df["SP>SMA7"] =  df["close"] > df["sma7"]
    df["SP>SMA14"] =  df["close"] > df["sma14"]
    df["SMA5>SMA14"] =  df["sma5"] > df["sma14"]
    df["SP>EMA5"] =  df["close"] > df["ema5"]
    df["SP>EMA7"] =  df["close"] > df["ema7"]
    df["SP>EMA9"] =  df["close"] > df["ema9"]
    df["SP>EMA13"] =  df["close"] > df["ema13"]
    df["SP>EMA20"] =  df["close"] > df["ema20"]
    df["EMA9>EMA20"] =  df["ema9"] > df["ema20"]
    df["MACD>MACD_SIGNAL"] =  df["macd"] > df["macd_signal"]
    df["RSI<30"] =  df["rsi"] < 30
    df["RSI>70"] =  df["rsi"] > 70
    df["SP>UBB"] =  df["close"] > df["ubb"]
    df["SP<LBB"] =  df["close"] < df["lbb"]
    df["SP>MBB"] =  df["close"] > df["mbb"]
    df["VOL>AVG_VOL10"] = (df["volume"]-df["avgvol10"])/df["avgvol10"] > 0.3
    df["INC_VOL"] = df["incvol"] > 0.2
    savefile = savepath+file.split("\\")[-1]
    df.to_csv(path_or_buf=savefile, index=False, columns=['date', 'open', 'high', 'low', 'close', 'volume',
                                                          'SP>SMA5', 'SP>SMA7', 'SP>SMA14', 'SMA5>SMA14', 'SP>EMA5',
                                                          'SP>EMA7', 'SP>EMA9', 'SP>EMA13', 'SP>EMA20', 'EMA9>EMA20',
                                                          'MACD>MACD_SIGNAL', 'RSI<30', 'RSI>70', 'SP>UBB', 'SP<LBB',
                                                          'SP>MBB', "VOL>AVG_VOL10", "INC_VOL"])
    print(file)