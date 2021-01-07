import datetime as d
import os as o
prompt = ''
trans = 0 
man = 0
lunch = 0
monthTrans = 0 
monthLunch = 0
transMonth = (180.6 + 9.4) / 2
lunchCost = 8
lunchInMonth = lunchCost * 21 
totalCostMonth = transMonth + lunchInMonth
teMoneyMonth = 9 * 21
showFlag = 0
yritys = "Sogetti"
browser = "C:/Program Files (x86)/Google/Chrome/Application/chrome.exe"
curPath = o.getcwd()#"C:\\Users\\jriim\\Documents\\python\\worksamples\\diary"

tiedosto = "repsa_" + str(d.date.today()) + ".html"