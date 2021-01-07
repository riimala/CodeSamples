import datetime as d
import mailer as m    
import settings as s
import mailer as m
import webbrowser as w

def luoSivu():
    sisalto = ""
    #print("Anna tiedoston nimi (ilman loppupäätettä).")
    #tiedosto = input()
    file = s.tiedosto 
    f = open(file, "a")

    #Sisältö
    style = "<head><style><table> {border-collapse: collapse;border-spacing: 0;width: 100%;border: 1px solid #ddd;}th, td {text-align: left;padding: 16px;}tr:nth-child(even) {background-color: #f2f2f2;}</style></head>"
    
    sivunAlku = "<html>" + style + "\n<body>\n<center>"
    title = "<title>" + "Jonen päiväkirja " + "</title>"
    otsikko = "<tr><th>Taskilista, " + str(d.date.today())+"</th></tr>"
    print("Syötä päivän tehtävät:")
    while True:
        keho = input().lower()
        if (keho == "end"):
            break
        else:
            sisalto += "<tr><td><li>" + keho + "</li></td></tr>" + "\n"

    sivunLoppu = "\n</center>\n</body>\n</html>"

    sivu = sivunAlku + title + "<table class = 'center' border = 1>" + otsikko + "\n" + sisalto + "</table>\n" + "<br>"+ sivunLoppu
    #print(sivu)
    f.write(sivu)   
    #avaaSivu()
    #mailinLahetys(f)

'''
def avaaSivu():
    w.get(s.browser + " %s").open("file://c:/Users/jriim/Documents/python/" + s.tiedosto)
    #w.get(s.browser + " %s").open(s.tiedosto)

def mailinLahetys(f):
    vast = 'k' #input('Maili?')
    if (vast == 'k'):
        m.mailer()
        f.close()
'''
