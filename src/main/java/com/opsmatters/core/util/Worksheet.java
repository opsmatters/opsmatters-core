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

import java.text.SimpleDateFormat;

/**
 * Base class for an Excel XLS or XLSX worksheet.
 * 
 * @author Gerald Curley (opsmatters)
 */
public class Worksheet
{
    /**
     * Returns the number of columns in this worksheet.
     */
    public int getColumns()
    {
        return 0;
    }

    /**
     * Returns the number of rows in this worksheet.
     */
    public int getRows()
    {
        return 0;
    }

    /**
     * Returns the value of the cells in the given row in this worksheet.
     */
    public String[] getRow(int i, SimpleDateFormat df)
    {
        return new String[0];
    }

    protected SimpleDateFormat df;
}
