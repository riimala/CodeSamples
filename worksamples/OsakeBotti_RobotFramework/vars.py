from datetime import date


URL  =  "https://finance.yahoo.com/"
BROWSER = "chrome"

ReportSubject = "Daily Stock Report"

SMTP_SERVER = "smtp.gmail.com"  
subject = "Daily Stock Report"  
text = ""
attach = "stockdata.txt"
D = date.today()
osakeLista = []
stockList = ["NOKIA.HE", "CAPMAN.HE"]
EXECDIR = "/home/jone"

