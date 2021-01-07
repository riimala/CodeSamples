*** Settings ***
Library  SeleniumLibrary
Library  Collections
Library  String
Library  mailer.py
Library  OperatingSystem
Library  DateTime
Library  Dialogs
Variables    vars.py

*** Variables ***
#Käytössä kun manuaalisesti luodaan osakelistaa
@{osakeLista}  
@{autoLista}    CAPMAN.HE
${stuff}=  ""
${USER}=  ""  
${PASS}=  ""
${To}=  ""


*** Tasks ***
Hae Osake Dataa
    [Documentation]  Purpose to collect stock data based on user preferences and send output to mail box.
    [Tags]    RPA    OsakeHarjoitus    1    
    [Setup]   Credentials

    Initiate TextFile
    #LuoLista
    Alusta Yahoo    ${URL}    browser=${BROWSER}
    Sivun validointi

*** Keywords ***

Credentials
    ${us}=  Get Value From User    Sähköpostin lähetystä varten,\nAnnatko lähettäjän?
    ${psw}=  Get Value From User    Salasana?
    ${toWhom}=  Get Value From User  Kenelle?
     
    Set Global Variable   ${USER}  ${us}
    Set Global Variable   ${PASS}  ${psw}
    Set Global Variable   ${To}  ${toWhom}

Initiate TextFile
    ${D}=    Get Current Date    result_format=%d-%m-%Y
    log    {D}
    ${RS}=    Catenate  ${ReportSubject}    ${D} 
    Remove File   stockdata.txt 
    Create File   stockdata.txt    ${RS}
    Append To File   stockdata.txt   ${\n}------------------------------------

Alusta Yahoo
    [Arguments]    ${URL}    ${browser}=headlesschrome 
    Open Browser  ${URL}  ${BROWSER} 
    Maximize Browser Window

Sivun validointi
    [Documentation]  Tarkistetaan, että tarvittavat komponentit löytyvät
    Click Button  //*[@id="consent-page"]/div/div/div/div[2]/div[2]/form/button
    #${Result}=  Page Should Contain Element  //*[@id="yfin-usr-qry"]
    #Run Keyword Unless  ${Result}=='PASS'  IteroiLista
    IteroiLista

#Semimanuaalinen, luodaan lista käyttäjän antamien inputtien kautta
LuoLista
    FOR    ${i}    IN RANGE    999
        ${osake}=  Get Value From User  Osake?  .HE
        ${strOsake}=  Convert To Upper Case    ${osake}
        Run Keyword Unless    '${osake}'=='.HE'  Append To List    ${osakeLista}  ${strOsake}            
        Log To Console    ${osake}
        Exit For Loop If    '${osake}'=='.HE'
    END
   

IteroiLista
    Log To Console  IteroiLista vaihe
    FOR  ${osake}  IN  @{autoLista}
        Input Text  //*[@id="yfin-usr-qry"]  ${osake}
        Click Button  //*[@id="header-desktop-search-button"]
        ${fetchedText} =  Get Text  //*[@id="quote-header-info"]/div[2]/div[1]/div[1]/h1
        ${strUpperCases} =  Convert To Uppercase  ${fetchedText}
        Log   ${strUpperCases}
        ${check}=    Run Keyword And Return Status    Should Contain    ${strUpperCases}    ${osake}
        Run Keyword If      ${check} is ${TRUE}  OsakeLoytyi  ${osake}
        ...  ELSE  OsakettaEiLoydy
    END

OsakettaEiLoydy
    Log  osakkeen hakua epäonnistui.
    Log To Console  osakkeen hakua epäonnistui,  haetaan uudestaan.
    IteroiLista

OsakeLoytyi
    [Arguments]  ${osake}
    Log To Console  Osakelöytyi vaihe
    ${s}=    Get Text  //*[@id="quote-header-info"]/div[3]/div[1]/div/span[1]
    Log    ${s}
    Log To Console  ${s}
    ${sVal}=    Convert To Number    ${s}
    Log    ${sVal}

    ${sChange} =  Get Text  //*[@id="quote-header-info"]/div[3]/div[1]/div/span[2]
    ${valText} =  Remove String   ${sChange}  (  +  -  %  )
    ${valSubstr} =  Get Substring   ${valText}   0   3
    Log To Console  ${valSubstr}
    Log  ${valSubstr}
    ${sChangeVal} =  Convert To Number  ${valSubstr}
    Log     Osakkeen muutos ${sChangeVal}
    ${stuff}=  Catenate  ${osake}  ${sVal}  ${sChange}
    Log To Console  ${stuff}
    Append To File  ${EXECDIR}/stockdata.txt  ${\n}${\n}${stuff}${\n}
    Comment    Sending data via mail if ...
    Run Keyword Unless  ${sVal} < ${2.3}  SEND
    [Teardown]  Close Browser

SEND
    #[Arguments]  ${USER}  ${PASS}  ${To}
    Log To Console  Käyttäjänimi ${USER}
    Log To Console  Salasana ${PASS}
    Log To Console  Kenelle ${To}
    Send Mail With Attachment  ${SMTP_SERVER}  ${USER}  ${PASS}  ${To}  ${subject}  ${text}  ${attach}
    Log To Console  Osake data ${stuff} from ( ${D}) in your mail box.
    





