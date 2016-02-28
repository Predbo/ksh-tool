package de.predbo.ksh;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class CsvManager {

	private static final Logger _logger = LoggerFactory.getLogger(CsvManager.class);
	
	private static final String CSV_FILENAME = "data.csv";
	private ICsvBeanWriter _csvWriter = null;
	private CellProcessor[] _csvCellProcessors;
	private String[] _csvHeaders = new String[] { "sheetNumber", "currentNumber", "familyNumber", "price" };

	
	
	public CsvManager(KshConfig _kshConfig) throws IOException {
		try {
			boolean newFileWasCreated = openCsvFileWriter(true);
			if (newFileWasCreated) {
				_csvWriter.writeHeader(_csvHeaders);
			}
			_csvCellProcessors = new CellProcessor[] {
					new LMinMax(1L, _kshConfig.getMaxSheetNumber()),
					new LMinMax(1L, _kshConfig.getMaxCurrentNumber()),
					new LMinMax(1L, _kshConfig.getMaxFamilyNumber()),
					new DMinMax(0.5, _kshConfig.getMaxprice()) };
		} finally {
			closeCsvWriter();
		}
	}
	
	public void addEntryToCsvFile(KshEntry kshEntry) throws IOException {
		try {
			openCsvFileWriter(true);
			_csvWriter.write(kshEntry, _csvHeaders, _csvCellProcessors);
			_logger.info("Eintrag für \"Blatt=" + kshEntry.getSheetNumber() + ", Laufende Nummer=" + kshEntry.getCurrentNumber() + "\" erfolgreich gespeichert");
		} finally {
			closeCsvWriter();
		}
	}
	
	public List<KshEntry> getCsvContentAsList() throws Exception {
		List<KshEntry> list = new ArrayList<KshEntry>();
		ICsvBeanReader beanReader = null;
		try {
			beanReader = new CsvBeanReader(new FileReader(CSV_FILENAME), CsvPreference.STANDARD_PREFERENCE);
			final String[] header = beanReader.getHeader(true);

			KshEntry kshEntry;
			while ((kshEntry = beanReader.read(KshEntry.class, header, _csvCellProcessors)) != null) {
				list.add(kshEntry);
			}
		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return list;
	}
	
	public void removeEntryFromCsvFile(String entryId) throws Exception {
		List<KshEntry> csvContentAsList = getCsvContentAsList();
		KshEntry entryToBeRemoved = null;
		for (KshEntry kshEntry : csvContentAsList) {
			String entryIdOfCurrentListEntry = kshEntry.getSheetNumber() + "-" + kshEntry.getCurrentNumber(); 
			if (entryId.equals(entryIdOfCurrentListEntry)) {
				entryToBeRemoved = kshEntry;
				break;
			}
		}
		if (entryToBeRemoved != null) {
			csvContentAsList.remove(entryToBeRemoved);
		} else {
			throw new Exception("Der Datensatz ist unbekannt");
		}
		
		try {
			openCsvFileWriter(false);
			_csvWriter.writeHeader(_csvHeaders);
			for (KshEntry kshEntry : csvContentAsList) {
				_csvWriter.write(kshEntry, _csvHeaders, _csvCellProcessors);
			}
			String[] parts = entryId.split("-");
			_logger.info("Eintrag für \"Blatt=" + parts[0] + ", Laufende Nummer=" + parts[1] + "\" erfolgreich gelöscht");
		} finally {
			closeCsvWriter();
		}
	}

	
	
	

	private boolean openCsvFileWriter(boolean append) throws IOException {
		FileWriter fw = null;
		boolean newFileWasCreated = false;
		File file = new File(CSV_FILENAME);
		if (file.exists()) {
			fw = new FileWriter(file, append);
		} else {
			fw = new FileWriter(file); // If file does not exist. Create it.
			newFileWasCreated = true;
		}
		_csvWriter = new CsvBeanWriter(fw, CsvPreference.STANDARD_PREFERENCE);
		return newFileWasCreated;
	}

	private void closeCsvWriter() throws IOException {
		if (_csvWriter != null) {
			_csvWriter.close();
		}
	}




	
	
	private void createDummyData() throws IOException {
		try {
			openCsvFileWriter(true);

			KshEntry kshEntry;
			Random random = new Random();
			for (int j = 1; j <= 80; j++) {
				_logger.info("Erstelle 40 Einträge für Blatt " + j);
				for (int i = 1; i <= 40; i++) {
					kshEntry = new KshEntry();
					kshEntry.setSheetNumber(j);
					kshEntry.setCurrentNumber(i);
					kshEntry.setFamilyNumber(random.nextInt(120) + 1);
					kshEntry.setPrice(random.nextInt(20) + 1);
					_csvWriter.write(kshEntry, _csvHeaders, _csvCellProcessors);
				}
			}
			_logger.info("insgesammt " + (80*40) + " Dummy Einträge erstellt");
		} finally {
			closeCsvWriter();
		}
	}

}
