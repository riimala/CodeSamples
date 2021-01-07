''' Ajatuksena luoda web tiedosto eli html tiedosto ja avata se browserissa

'''
import os
import tuhoaFileet as t
import settings as s
import luoSivu as l
import mailer as m

print("\nPäivän taskilistan luonti (end kun lopetat) + lähetys sähköpostiin")
print("\n")
t.tuhoa()
l.luoSivu()
m.mailer()
