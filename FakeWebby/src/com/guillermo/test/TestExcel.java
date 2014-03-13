package com.guillermo.test;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class TestExcel {

	public static void main(String[] args) throws IOException {
		
		File inputWorkbook = new File("C:\\Temp\\SFR_Issue\\Copy of Samsung Apps - Traffic Extract - 2013-10-29 (4)_PROVIDEDBYNETSIZE.xls");
		
		Workbook w;
		
		try {
			
			w = Workbook.getWorkbook(inputWorkbook);
			
			Sheet sheet = w.getSheet(1);
			
			for (int j = 0; j < sheet.getColumns(); j++) {
				for (int i = 0; i < sheet.getRows(); i++) {
					Cell cell = sheet.getCell(j, i);
					CellType type = cell.getType();
					if (type == CellType.LABEL) {
						System.out.println("I got a label " + cell.getContents());
					}

					if (type == CellType.NUMBER) {
						System.out.println("I got a number " + cell.getContents());
					}
				}
			}
			
		} catch (BiffException e) {
			e.printStackTrace();
		}

	}

}
