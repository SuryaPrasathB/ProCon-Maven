from win32com import client
import os
import sys


# Prompt user for (optional) command line arguments, when run from IDLE:
#if sys.modules['idlelib']: sys.argv.extend(input("Args: ").split())

##path = "C:\\Reports\\YBF\\Payslip\\Serena-YBF\\April_2019\\"
##ConsolidatedPDF_FileName = "All.pdf"
path = sys.argv[1]
print ("Report path:" + path)
exit
ConsolidatedPDF_FileName = sys.argv[2]
##ConsolidatedPDF_FileName = "All.pdf"
##path = "C:\\TAS_Network\\Procal_Excel\\Output\\20210210_180854\\Chirabkip\\"
#print ("Report path:" + path)
print ("ConsolidatedPDF_FileName:" + ConsolidatedPDF_FileName)

xlApp = client.Dispatch("Excel.Application")

x = [a for a in os.listdir(path) if a.endswith(".xlsx")]

for ExcelFile in x:

    if not('~' in ExcelFile):
        FileNameWithOutExtension = os.path.splitext(path +ExcelFile)[0]
        print("FileNameWithOutExtension:"+FileNameWithOutExtension)
        print("ExcelFile:"+path +ExcelFile)
        books = xlApp.Workbooks.Open(path +ExcelFile)
        ws = books.Worksheets[0]
        ws.Visible = 1
        print("FileName:"+FileNameWithOutExtension+".pdf")
        ws.ExportAsFixedFormat(0, FileNameWithOutExtension+".pdf")
        ##Test = FileNameWithOutExtension [FileNameWithOutExtension.index('_')+1:]
        #ws.close()
        books.Close()
        os.remove(path +ExcelFile)

#Test = FileNameWithOutExtension [FileNameWithOutExtension.index('_')+1:]
RemovedEmpNameString = FileNameWithOutExtension[FileNameWithOutExtension.rindex('_')+1:]
print("RemovedEmpNameString:"+ RemovedEmpNameString)
##ConsolidatedPDF_FileName = FileNameWithOutExtension.replace(RemovedEmpNameString,"")+ConsolidatedPDF_FileName
ConsolidatedPDF_FileName = path + ConsolidatedPDF_FileName
print( "RightIndex1:"+ConsolidatedPDF_FileName )
#Test = Test [Test.index('_')+1:]
#print( "RightIndex:"+Test )
print ("Individual PDF File created - Done!!")


##from PyPDF2 import PdfFileMerger
##
##pdfs = ['C:\\Reports\\YBF\\Payslip\\Serena-YBF\\April_2019\\Output.pdf', 'C:\\Reports\\YBF\\Payslip\\Serena-YBF\\April_2019\\Output2.pdf']
##
##merger = PdfFileMerger()
##
##for pdf in pdfs:
##    merger.append(open(pdf, 'rb'))
##
##with open('C:\\Reports\\YBF\\Payslip\\Serena-YBF\\April_2019\\result.pdf', 'wb') as fout:
##    merger.write(fout)

import os
from PyPDF2 import PdfFileMerger
#import PdfFileReader


if os.path.exists(ConsolidatedPDF_FileName):
  os.remove(ConsolidatedPDF_FileName)
  print(ConsolidatedPDF_FileName + " Deleted")
else:
  print('File does not exists')
x = [a for a in os.listdir(path) if a.endswith(".pdf")]

merger = PdfFileMerger()

for pdf in x:
    ##with open(path+pdf, 'rb') as f:
       ## merger.append(f)
        ##f.close()
        ##os.remove(path +pdf)
##            reader = PdfFileReader(f)
##            merger.append(reader)
    ##f1 =open(path+pdf, 'rb')
    merger.append(open(path+pdf, 'rb'))
    
    ##merger.append(f1)
    ##close(path+pdf)
    ##f1.close()
    ##os.remove(path +pdf)

with open(ConsolidatedPDF_FileName, "wb") as fout:
    merger.write(fout)

fout.close()
merger.close()
#fout.close()


print ("Merged PDF File - Finished!!")

##delete all the pdf except consolidated

##x = [a for a in os.listdir(path) if a.endswith(".pdf")]
##
##merger = PdfFileMerger()
##
##for pdf in x:
##    if(pdf != ConsolidatedPDF_FileName):
##        os.remove(path +pdf)
