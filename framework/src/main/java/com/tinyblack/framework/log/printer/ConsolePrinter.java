/*
 * Copyright 2016 Elvis Hew
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tinyblack.framework.log.printer;


import com.tinyblack.framework.log.flattener.Flattener2;
import com.tinyblack.framework.log.internal.DefaultsFactory;

/**
 * Log {@link Printer} using {@code System.out.println(String)}.
 *
 * @author yubiao
 * @since 1.3.0
 */
public class ConsolePrinter implements Printer {

    /**
     * The log flattener when print a log.
     */
    private Flattener2 flattener;

    /**
     * Constructor.
     */
    public ConsolePrinter() {
        this.flattener = DefaultsFactory.createFlattener2();
    }

    /**
     * Constructor.
     *
     * @param flattener the log flattener when print a log
     */
    public ConsolePrinter(Flattener2 flattener) {
        this.flattener = flattener;
    }

    @Override
    public void println(int logLevel, String tag, String msg) {
        long timeMillis = System.currentTimeMillis();
        String flattenedLog = flattener.flatten(timeMillis, logLevel, tag, msg).toString();
        System.out.println(flattenedLog);
    }
}
