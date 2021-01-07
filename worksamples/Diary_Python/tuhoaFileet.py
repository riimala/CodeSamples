import settings as s

def tuhoa():
    import os
    try:
        files_in_directory = os.listdir(s.curPath)
        filtered_files = [file for file in files_in_directory if file.endswith(".html")]
        for file in filtered_files:
            path_to_file = os.path.join(s.curPath, file)
            os.remove(path_to_file)
    except OSError as error:
        print("Virhe tiedostojen tuhoamisessa: " + error.errno)
        exit()    

    '''
    print("Tuhotaanko kaikki hakemiston html fileet (k) ?")
    vast = 'k'#input()
    if (vast == "k"):
        try:
            files_in_directory = os.listdir(s.curPath)
            filtered_files = [file for file in files_in_directory if file.endswith(".html")]
            for file in filtered_files:
                path_to_file = os.path.join(s.curPath, file)
                os.remove(path_to_file)
        except OSError as error:
            print("Virhe tiedostojen tuhoamisessa: " + error.errno)
            exit()    
    '''