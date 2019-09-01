import {Injectable} from '@angular/core';
import * as FileSaver from 'file-saver';
import * as XLSX from 'xlsx';

const EXCEL_TYPE = 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=UTF-8';
const EXCEL_EXTENSION = '.xlsx';

@Injectable()
export class ExcelService {

  constructor() {
  }

  /**
   * Exports the given information as an excel file.
   * @param json - any[]; the information the has to be exported.
   * @param excelFileName - string; the name of the resulted excel file.
   *
   * */
  public exportAsExcelFile(json: any[], excelFileName: string): void {

    const worksheet: XLSX.WorkSheet = XLSX.utils.json_to_sheet(json);
    console.log(worksheet);

    worksheet['A1'].v = 'ID';
    worksheet['B1'].v = 'Title';
    worksheet['C1'].v = 'Description';
    worksheet['D1'].v = 'Version';
    worksheet['E1'].v = 'Target Date';
    worksheet['F1'].v = 'Status';
    worksheet['G1'].v = 'Fixed Version';
    worksheet['H1'].v = 'Severity';
    worksheet['I1'].v = 'Created From';
    worksheet['J1'].v = 'Assigned To';


    const workbook: XLSX.WorkBook = {Sheets: {'data': worksheet}, SheetNames: ['data']};
    const excelBuffer: any = XLSX.write(workbook, {bookType: 'xlsx', type: 'array'});
    this.saveAsExcelFile(excelBuffer, excelFileName);
  }

  /**
   * Creates a blob with the given data and saves the data as an excel file.
   * @param buffer - any; the information the has to be saved.
   * @param fileName - string; the name of the resulted excel file.
   *
   * */
  private saveAsExcelFile(buffer: any, fileName: string): void {
    const data: Blob = new Blob([buffer], {
      type: EXCEL_TYPE
    });
    FileSaver.saveAs(data, fileName + '_export_' + new Date().getTime() + EXCEL_EXTENSION);
  }
}
