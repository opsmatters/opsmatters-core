/*
 * Copyright 2018 Gerald Curley
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opsmatters.core.file;

import java.io.*;
import java.util.*;
import com.opencsv.*;
import java.util.logging.Logger;
import com.opsmatters.core.type.FileDelimiterType;
import com.opsmatters.core.util.ReportColumn;
import com.opsmatters.core.util.Workbook;
import com.opsmatters.core.util.StringUtilities;

/**
 * Object that encapsulates an output file in XLS, XLSX or CSV format.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class OutputFile
{
    private static final Logger logger = Logger.getLogger(OutputFile.class.getName());

    private short format = CSV_FORMAT;
    private CSVWriter csv = null;
    private Workbook existing = null;
    private Workbook workbook = null;
    private ByteArrayOutputStream baos;
    private String delimiter = FileDelimiterType.COMMA;
    private boolean headers = true;
    private boolean quotes = true;

    /**

     * Indicates a CSV output file format.
     */
    public static final short CSV_FORMAT = 0;

    /**
     * Indicates an XLS output file format.
     */
    public static final short XLS_FORMAT = 1;

    /**
     * Indicates an XLSX output file format.
     */
    public static final short XLSX_FORMAT = 2;

    /**
     * Default constructor.
     */
    public OutputFile(String filename)
    {
        this.format = getFileType(filename);
    }

    /**
     * Default constructor.
     */
    public OutputFile(short format)
    {
        this.format = format;
    }

    /**
     * Sets the delimiter for the file (CSV only).
     * @param delimiter The delimiter for the file (CSV only)
     */
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter != null ? new String(delimiter) : "";
    }

    /**
     * Returns the delimiter for the file (CSV only).
     * @return The delimiter for the file (CSV only)
     */
    public String getDelimiter()
    {
        return delimiter;
    }

    /**
     * Returns the delimiter character for the file (CSV only).
     */
    private char getDelimiterChar()
    {
        char ret = ',';
        if(delimiter != null && delimiter.length() > 0)
        {
            String str = FileDelimiterType.getDelimiterByCode(delimiter);
            if(str.length() > 0)
                ret = str.charAt(0);
        }
        return ret;
    }

    /**
     * Returns <CODE>true</CODE> if the workbook has quotes around each field.
     * @return <CODE>true</CODE> if the workbook has quotes around each field
     */
    public boolean hasQuotes()
    {
        return quotes;
    }

    /**
     * Set to <CODE>true</CODE> if the workbook has quotes around each field.
     * @param quotes <CODE>true</CODE> if the workbook has quotes around each field
     */
    public void setQuotes(boolean quotes)
    {
        this.quotes = quotes;
    }

    /**
     * Returns <CODE>true</CODE> if the workbook has a header row.
     * @return <CODE>true</CODE> if the workbook has a header row
     */
    public boolean hasHeaders()
    {
        return headers;
    }

    /**
     * Set to <CODE>true</CODE> if the workbook has a header row.
     * @param headers <CODE>true</CODE> if the workbook has a header row
     */
    public void setHeaders(boolean headers)
    {
        this.headers = headers;
    }

    /**
     * Returns the file type based on the file name.
     */
    public short getFileType(String filename)
    {
        short ret = 0;
        filename = filename.toLowerCase();
        if(filename.endsWith("."+CommonFiles.XLS_EXT))
            ret = XLS_FORMAT;
        else if(filename.endsWith("."+CommonFiles.XLSX_EXT))
            ret = XLSX_FORMAT;
        else if(filename.endsWith("."+CommonFiles.CSV_EXT))
            ret = CSV_FORMAT;
        return ret;
    }

    /**
     * Returns the formatted output file contents.
     */
    public byte[] getContents(List<String[]> lines, String sheetName, boolean append) throws Exception
    {
        return getContents(null, lines, sheetName, append);
    }

    /**
     * Returns the formatted output file contents.
     */
    public byte[] getContents(ReportColumn[] columns, 
        List<String[]> lines, String sheetName, boolean append) throws Exception
    {
        byte[] ret;

        // Excel spreadsheet
        if(format == XLS_FORMAT || format == XLSX_FORMAT)
        {
            ret = getExcelOutput(columns, lines, sheetName, append);
        }
        else // Assume it's a CSV file
        {
            ret = getCSVOutput(lines, sheetName);
        }

        return ret;
    }

    /**
     * Returns the CSV output file data.
     */
    private byte[] getCSVOutput(List<String[]> lines, String sheetName) throws Exception
    {
        StringWriter writer = new StringWriter();
        csv = new CSVWriter(writer, getDelimiterChar());
        for(int i = 0; i < lines.size(); i++)
        {
            csv.writeNext((String[])lines.get(i), quotes);
        }

        // The contents returned is the CSV string
        return writer.toString().getBytes();
    }

    /**
     * Returns the XLS or XLSX output file data.
     */
    private byte[] getExcelOutput(ReportColumn[] columns, 
        List<String[]> lines, String sheetName, boolean append) throws Exception
    {
        // Create the workbook
        baos = new ByteArrayOutputStream(1024);
        if(existing != null)
            workbook = Workbook.createWorkbook(format, baos, existing);
        else
            workbook = Workbook.createWorkbook(format, baos);

        workbook.setHeaders(hasHeaders());

        if(append && workbook.getSheet(sheetName) != null)
            workbook.appendToSheet(columns, lines, sheetName);
        else
            workbook.createSheet(columns, lines, sheetName);

        // Write out the workbook to the stream
        workbook.write();
        workbook.close();

        // The contents returned is the byte array
        return baos.toByteArray();
    }

    /**
     * Close the output file objects.
     */
    public void close()
    {
        try
        {
            // Close the workbook
            if(workbook != null)
                workbook.close();
        }
        catch(Exception e)
        {
        }

        try
        {
            // Flush and close the byte stream
            if(baos != null)
            {
                baos.flush();
                baos.close();
            }
        }
        catch(Exception e)
        {
        }

        try
        {
            // Close the CSV writer
            if(csv != null)
                csv.close();
        }
        catch(IOException e)
        {
        }
    }

    /**
     * Sets an existing Workbook to which the sheet should be added.
     */
    public void setExistingWorkbook(Workbook w)
    {
        existing = w;
    }

    /**
     * Sets an existing Workbook to which the sheet should be added.
     */
    public Workbook getExistingWorkbook()
    {
        return existing;
    }

    /**
     * Returns the workbook created.
     */
    public Workbook getWorkbook()
    {
        return workbook;
    }

    /**
     * Returns the workbook at the given file.
     */
    public Workbook getWorkbook(File file)
    {
        Workbook ret = null;

        String filename = file.getAbsolutePath().toLowerCase();
        if(filename.endsWith("."+CommonFiles.XLS_EXT)
            || filename.endsWith("."+CommonFiles.XLSX_EXT))
        {
            try
            {
                ret = Workbook.getWorkbook(file);
            }
            catch(FileNotFoundException e)
            {
                // Do nothing
            }
            catch(Exception e)
            {
                logger.severe(StringUtilities.serialize(e));
            }
        }

        return ret;
    }

    /**
     * Returns the workbook at the given file.
     */
    public Workbook getWorkbook(String filename, InputStream stream)
    {
        Workbook ret = null;

        filename = filename.toLowerCase();
        if(filename.endsWith("."+CommonFiles.XLS_EXT)
            || filename.endsWith("."+CommonFiles.XLSX_EXT))
        {
            try
            {
                ret = Workbook.getWorkbook(filename, stream);
            }
            catch(Exception e)
            {
                logger.severe(StringUtilities.serialize(e));
            }
        }

        return ret;
    }
}
