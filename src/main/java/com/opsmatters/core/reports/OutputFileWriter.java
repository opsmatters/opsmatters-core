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

package com.opsmatters.core.reports;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import com.opencsv.CSVWriter;
import java.util.logging.Logger;
import com.opsmatters.core.CommonFiles;
import com.opsmatters.core.type.FileDelimiterType;
import com.opsmatters.core.util.StringUtilities;

/**
 * Object that encapsulates an output file in XLS, XLSX or CSV format.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class OutputFileWriter
{
    private static final Logger logger = Logger.getLogger(OutputFileWriter.class.getName());

    private String name = "";
    private short format = CSV_FORMAT;
    private String delimiter = FileDelimiterType.COMMA;
    private String worksheet = "";
    private boolean append = true;
    private boolean headers = true;
    private boolean quotes = true;
    private OutputStream stream;
    private CSVWriter csv = null;
    private Workbook existing = null;
    private Workbook workbook = null;
    private ByteArrayOutputStream baos;

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
    public OutputFileWriter()
    {
    }

    /**
     * Sets the name of the output file.
     * @param name The name of the output file
     */
    public void setName(String name)
    {
        this.name = name;
        this.format = getFileType(name);
    }

    /**
     * Returns the name of the file.
     * @return The name of the file
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the delimiter for the file (CSV only).
     * @param delimiter The delimiter for the file (CSV only)
     */
    public void setDelimiter(String delimiter)
    {
        this.delimiter = delimiter;
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
     * @return The delimiter character for the file (CSV only)
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
     * Returns the worksheet to be opened in the output file (XLS, XLSX only).
     * @return The worksheet to be opened in the output file (XLS, XLSX only)
     */
    public String getWorksheet()
    {
        return worksheet;
    }

    /**
     * Sets the worksheet to be opened in the output file (XLS, XLSX only).
     * @param worksheet The worksheet to be opened in the output file (XLS, XLSX only)
     */
    public void setWorksheet(String worksheet)
    {
        this.worksheet = worksheet;
    }

    /**
     * Returns <CODE>true</CODE> if the output file has quotes around each field.
     * @return <CODE>true</CODE> if the output file has quotes around each field
     */
    public boolean hasQuotes()
    {
        return quotes;
    }

    /**
     * Set to <CODE>true</CODE> if the output file has quotes around each field.
     * @param quotes <CODE>true</CODE> if the output file has quotes around each field
     */
    public void setQuotes(boolean quotes)
    {
        this.quotes = quotes;
    }

    /**
     * Returns <CODE>true</CODE> if the output file has a header row.
     * @return <CODE>true</CODE> if the output file has a header row
     */
    public boolean hasHeaders()
    {
        return headers;
    }

    /**
     * Set to <CODE>true</CODE> if the output file has a header row.
     * @param headers <CODE>true</CODE> if the output file has a header row
     */
    public void setHeaders(boolean headers)
    {
        this.headers = headers;
    }

    /**
     * Set to <CODE>true</CODE> if the lines should be appended to the output file.
     * @param append <CODE>true</CODE> if the lines should be appended instead of replacing the data in the sheet
     */
    public void setAppend(boolean append)
    {
        this.append = append;
    }

    /**
     * Returns <CODE>true</CODE> if the lines should be appended to the output file.
     * @return <CODE>true</CODE> if the lines should be appended instead of replacing the data in the sheet
     */
    public boolean append()
    {
        return append;
    }

    /**
     * Sets the output stream for the output file.
     * @param stream The output stream for the output file
     */
    public void setOutputStream(OutputStream stream)
    {
        this.stream = stream;
    }

    /**
     * Returns the output stream for the output file.
     * @return The output stream for the output file
     */
    public OutputStream getOutputStream()
    {
        return stream;
    }

    /**
     * Returns the file type based on the file name.
     * @param filename The filename to check
     * @return The file type based on the file name
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
     * @param lines The lines to add to the output file
     * @return The formatted output file contents
     * @throws IOException if the file cannot be opened
     */
    public byte[] getContents(List<String[]> lines) throws IOException
    {
        return getContents(null, lines);
    }

    /**
     * Returns the formatted output file contents.
     * @param columns The column definitions for the output file
     * @param lines The lines to add to the output file
     * @return The formatted output file contents
     * @throws IOException if the file cannot be opened
     */
    public byte[] getContents(ReportColumn[] columns, List<String[]> lines)
        throws IOException
    {
        byte[] ret;

        // Excel spreadsheet
        if(format == XLS_FORMAT || format == XLSX_FORMAT)
        {
            ret = getExcelOutput(columns, lines);
        }
        else // Assume it's a CSV file
        {
            ret = getCSVOutput(lines);
        }

        return ret;
    }

    /**
     * Returns the CSV output file data.
     * @param lines The lines to add to the output file
     * @return The byte array representing the CSV output file data
     */
    private byte[] getCSVOutput(List<String[]> lines)
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
     * @param columns The column definitions for the output file
     * @param lines The lines to add to the output file
     * @return The XLS or XLSX output file data
     */
    private byte[] getExcelOutput(ReportColumn[] columns, List<String[]> lines) throws IOException
    {
        // Create the workbook
        baos = new ByteArrayOutputStream(1024);
        if(existing != null)
            workbook = Workbook.createWorkbook(format, baos, existing);
        else
            workbook = Workbook.createWorkbook(format, baos);

        workbook.setHeaders(hasHeaders());

        if(append && workbook.getSheet(worksheet) != null)
            workbook.appendToSheet(columns, lines, worksheet);
        else
            workbook.createSheet(columns, lines, worksheet);

        // Write out the workbook to the stream
        workbook.write();
        workbook.close();

        // The contents returned is the byte array
        return baos.toByteArray();
    }

    /**
     * Write the contents of the output file to the output stream.
     * @param lines The lines to add to the output file
     * @throws IOException if there was an error writing to the stream
     */
    public void write(List<String[]> lines) throws IOException
    {
        write(null, lines);
    }

    /**
     * Write the contents of the output file to the output stream.
     * @param columns The column definitions for the output file
     * @param lines The lines to add to the output file
     * @throws IOException if there was an error writing to the stream
     */
    public void write(ReportColumn[] columns, List<String[]> lines) throws IOException
    {
        Object contents = getContents(columns, lines);
        if(stream != null && contents instanceof byte[])
        {
            byte[] bytes = (byte[])contents;
            stream.write(bytes);
        }
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
     * @param w The existing Workbook to which the sheet should be added
     */
    public void setExistingWorkbook(Workbook w)
    {
        existing = w;
    }

    /**
     * Returns an existing Workbook to which the sheet should be added.
     * @return The existing Workbook to which the sheet should be added
     */
    public Workbook getExistingWorkbook()
    {
        return existing;
    }

    /**
     * Returns the workbook created.
     * @return The workbook created
     */
    public Workbook getWorkbook()
    {
        return workbook;
    }

    /**
     * Returns the workbook at the given file.
     * @param file The file containing the name for the workbook
     * @return The workbook created
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
     * @param filename The name of the workbook
     * @param stream The input stream for the workbook
     * @return The workbook at the given file
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

    /**
     * Returns a builder for the writer.
     * @return The builder instance.
     */
    public static Builder builder()
    {
        return new Builder();
    }

    /**
     * Builder to make writer construction easier.
     */
    public static class Builder
    {
        private OutputFileWriter writer = new OutputFileWriter();

        /**
         * Sets the name of the output file.
         * @param name The name of the output file
         * @return This object
         */
        public Builder name(String name)
        {
            writer.setName(name);
            return this;
        }

        /**
         * Sets the worksheet to be opened in the output file (XLS, XLSX only).
         * @param worksheet The worksheet to be opened in the output file (XLS, XLSX only)
         * @return This object
         */
        public Builder worksheet(String worksheet)
        {
            writer.setWorksheet(worksheet);
            return this;
        }

        /**
         * Sets the delimiter used in the output file (CSV only).
         * @param delimiter The delimiter used in the output file (CSV only)
         * @return This object
         */
        public Builder delimiter(String delimiter)
        {
            writer.setDelimiter(delimiter);
            return this;
        }

        /**
         * Set to <CODE>true</CODE> if the output file has quotes around each field.
         * @param quotes <CODE>true</CODE> if the output file has quotes around each field
         * @return This object
         */
        public Builder quotes(boolean quotes)
        {
            writer.setQuotes(quotes);
            return this;
        }

        /**
         * Set to <CODE>true</CODE> if the output file has a header row.
         * @param headers <CODE>true</CODE> if the output file has a header row
         * @return This object
         */
        public Builder headers(boolean headers)
        {
            writer.setHeaders(headers);
            return this;
        }

        /**
         * Set to <CODE>true</CODE> if the lines should be appended to the output file.
         * @param append <CODE>true</CODE> if the lines should be appended instead of replacing the data in the sheet
         * @return This object
         */
        public Builder append(boolean append)
        {
            writer.setAppend(append);
            return this;
        }

        /**
         * Sets the output stream for the output file.
         * @param stream The output stream for the output file
         * @return This object
         */
        public Builder withOutputStream(OutputStream stream)
        {
            writer.setOutputStream(stream);
            return this;
        }

        /**
         * Returns the configured writer instance
         * @return The writer instance
         */
        public OutputFileWriter build()
        {
            return writer;
        }
    }
}