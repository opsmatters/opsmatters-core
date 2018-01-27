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

package com.opsmatters.core.util;

import java.util.List;
import java.util.TimeZone;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.opsmatters.core.file.CommonFiles;
import com.opsmatters.core.file.OutputFile;
import com.opsmatters.core.util.DateUtilities;

/**
 * Base class for an Excel XLS or XLSX workbook.
 * 
 * @author Gerald Curley (opsmatters)
 */
public abstract class Workbook
{
    private boolean headers = true;

    /**
     * Returns an existing workbook object.
     */
    public static Workbook getWorkbook(File file) throws IOException
    {
        Workbook ret = null;
        String lowerFilename = file.getName().toLowerCase();
        if(lowerFilename.endsWith("."+CommonFiles.XLS_EXT))
        {
            ret = XlsWorkbook.getWorkbook(file);
        }
        else if(lowerFilename.endsWith("."+CommonFiles.XLSX_EXT))
        {
            XlsxWorkbook.initJAXBContexts();
            ret = XlsxWorkbook.getWorkbook(file);
        }
        return ret;
    }

    /**
     * Returns an existing workbook object.
     */
    public static Workbook getWorkbook(String filename, InputStream stream) 
        throws IOException
    {
        Workbook ret = null;
        String lowerFilename = filename.toLowerCase();
        if(lowerFilename.endsWith("."+CommonFiles.XLS_EXT))
        {
            ret = XlsWorkbook.getWorkbook(stream);
        }
        else if(lowerFilename.endsWith("."+CommonFiles.XLSX_EXT))
        {
            XlsxWorkbook.initJAXBContexts();
            ret = XlsxWorkbook.getWorkbook(stream);
        }
        return ret;
    }

    /**
     * Creates a new workbook object.
     */
    public static Workbook createWorkbook(short format,
        OutputStream os, Workbook existing)
        throws IOException
    {
        Workbook ret = null;
        if(format == OutputFile.XLS_FORMAT)
        {
            ret = XlsWorkbook.createWorkbook(os, existing);
        }
        else if(format == OutputFile.XLSX_FORMAT)
        {
            XlsxWorkbook.initJAXBContexts();
            ret = XlsxWorkbook.createWorkbook(os, existing);
        }
        return ret;
    }

    /**
     * Creates a new workbook object.
     */
    public static Workbook createWorkbook(short format, OutputStream os)
        throws IOException
    {
        return createWorkbook(format, os, null);
    }

    /**
     * Returns the internal workbook.
     */
    public abstract Object getWorkbook();

    /**
     * Returns the number of worksheets in the given workbook.
     */
    public abstract int numSheets();

    /**
     * Returns the list of worksheet names from the given workbook.
     */
    public abstract String[] getSheetNames();

    /**
     * Returns the worksheet at the given index in the workbook.
     */
    public abstract Worksheet getSheet(int i);

    /**
     * Returns the worksheet with the given name in the workbook.
     */
    public abstract Worksheet getSheet(String name);

    /**
     * Creates a sheet in the workbook with the given name and lines of data.
     */
    public abstract Worksheet createSheet(ReportColumn[] columns, List<String[]> lines, String sheetName)
        throws Exception;

    /**
     * Adds the given lines of data to an existing sheet in the workbook.
     */
    public abstract void appendToSheet(ReportColumn[] columns, List<String[]> lines, String sheetName)
        throws Exception;

    /**
     * Write the workbook.
     */
    public abstract void write() throws Exception;

    /**
     * Close the workbook.
     */
    public abstract void close();

    /**
     * Returns the current offset for the given time allowing for daylight savings.
     */
    protected int getOffset(long dt)
    {
        int ret = 0;
        TimeZone tz = DateUtilities.getCurrentTimeZone();
        if(tz != null)
            ret = tz.getOffset(dt);
        return ret;
    }

    /**
     * Returns <CODE>true</CODE> if the workbook has a header row.
     */
    public boolean hasHeaders()
    {
        return headers;
    }

    /**
     * Set to <CODE>true</CODE> if the workbook has a header row.
     */
    public void setHeaders(boolean b)
    {
        headers = b;
    }
}