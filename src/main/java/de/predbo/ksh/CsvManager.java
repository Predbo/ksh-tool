package de.predbo.ksh;

import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.supercsv.cellprocessor.constraint.DMinMax;
import org.supercsv.cellprocessor.constraint.LMinMax;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

public class CsvManager {

	private static final Logger _logger = LoggerFactory
			.getLogger(CsvManager.class);
	private static final String CSV_FILENAME = "all.csv";
	private ICsvBeanWriter _csvWriter = null;
	private CellProcessor[] _csvCellProcessors = new CellProcessor[] {
			new LMinMax(1L, 1000L), // _sheetNumber
			new LMinMax(1L, 100L), // _currentNumber
			new LMinMax(0L, 120L), // _familiyNumber
			new DMinMax(0.5, 1000.0) }; // _price
	private String[] _csvHeaders = new String[] { "sheetNumber",
			"currentNumber", "familyNumber", "price" };

	public CsvManager() {
		try {
			boolean newFileWasCreated = openCsvFileWriter();
			if (newFileWasCreated) {
				_csvWriter.writeHeader(_csvHeaders);
			}
		} catch (Exception e) {
			_logger.error("csv file could not be init.", e);
		} finally {
			closeCsvWriter();
		}
	}

	public void addEntryToCsvFile(KshEntry kshEntry) {
		try {
			openCsvFileWriter();
			_csvWriter.write(kshEntry, _csvHeaders, _csvCellProcessors);
		} catch (Exception e) {
			_logger.error("csv file could not be init.", e);
		} finally {
			closeCsvWriter();
		}
	}

	private boolean openCsvFileWriter() {
		FileWriter fw = null;
		boolean newFileWasCreated = false;
		try {
			File file = new File(CSV_FILENAME);
			if (file.exists()) {
				fw = new FileWriter(file, true);// if file exists append to
												// file.
			} else {
				fw = new FileWriter(file); // If file does not exist. Create it.
				newFileWasCreated = true;
			}
			_csvWriter = new CsvBeanWriter(fw,
					CsvPreference.STANDARD_PREFERENCE);
		} catch (Exception e) {
			_logger.error("error during csv handling", e);
			closeCsvWriter();
		}
		return newFileWasCreated;
	}

	private void closeCsvWriter() {
		if (_csvWriter != null) {
			try {
				_csvWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
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

}
