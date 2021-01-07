import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart
from email.mime.base import MIMEBase
from email import encoders
import settings as s

mail_content = "Päivän taski ym rapsa"
sender_address = ''
sender_pass = ''
receiver_address = ''

#Target to send created file as an attachment.
def mailer():
    #Setup the MIME
    message = MIMEMultipart()
    sender_address = input('\nTaskilistan lähetys sähköpostilla. Annatko lähettäjän sähköpostin (oikea..):\n')
    message['From'] = sender_address
    receiver_address = input('\nVastaanottaja (default) jriimala@gmail.com: ')
    if (receiver_address == ""):
        recipients = "jriimala@gmail.com"
    elif (receiver_address != ""):
        recipients = receiver_address + ",jriimala@gmail.com"
    sender_pass = input('Salasana? ')    
    message['To'] = recipients 
    message['Subject'] = 'Päivän repsa (html tiedosto)'

    #The body and the attachments for the mail
    message.attach(MIMEText(mail_content, 'plain'))
    attach_file_name = s.tiedosto
    print(attach_file_name)

    tiedostoPolku = s.curPath +"\\"+ s.tiedosto
    print(tiedostoPolku)

    attach_file = open(tiedostoPolku, 'rb') # Open the file as binary mode
    if (attach_file.fileno() > 0):
        payload = MIMEBase('application', 'octate-stream', Name=attach_file_name)
        payload.set_payload((attach_file).read())
        encoders.encode_base64(payload) #encode the attachment
        payload.add_header('Content-Decomposition', 'attachment; filename=%s' % attach_file_name)
        message.attach(payload)
        
        #Create SMTP session for sending the mail
        session = smtplib.SMTP('smtp.gmail.com', 587) #use gmail with port
        session.starttls() #enable security
        session.login(sender_address, sender_pass) #login with mail_id and password
        text = message.as_string()
        session.sendmail(sender_address, recipients.split(','), text)
        session.quit()
        print('Sähköposti lähetetty')
    else:
        print("File was not available")