/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.github.ranrosenzweig.jobs;

import com.github.ranrosenzweig.common.Utils;
import com.github.ranrosenzweig.model.NationalNames;
import com.github.ranrosenzweig.model.StateNames;

import com.github.ranrosenzweig.sink.RichNationalNamesSink;
import com.github.ranrosenzweig.sink.RichStateNamesSink;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.io.TextInputFormat;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FileProcessingMode;

public class BabyNamesJob {

    public static void main(String[] args)  {
        try {

            /* ***************************************************************************
             *                 Setup Flink environment.
             ****************************************************************************/

            final StreamExecutionEnvironment streamEnv =
                    StreamExecutionEnvironment.getExecutionEnvironment();
            streamEnv.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

            /* ***************************************************************************
             *                  Read CSV file into a DataStream
             ****************************************************************************/
            //Define the data directory to monitor new files
            String stateNamesDataDir = "my/path/to/file1";
            String nationalNamesDataDir = "my/path/to/file2";
            if (args.length > 0) {
                try {
                    stateNamesDataDir = args[0];
                    nationalNamesDataDir = args[1];
                    if (args[2].equalsIgnoreCase("truncatetables")){
                        Utils.truncateTable("public.national_names");
                        Utils.truncateTable("public.state_names");
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            else{
                stateNamesDataDir = "src/main/resources/StateNames.csv";
                nationalNamesDataDir = "src/main/resources/NationalNames.csv";
            }


            //Define the text input format based on the directory
            TextInputFormat stateNamesFormat = new TextInputFormat(
                    new org.apache.flink.core.fs.Path(stateNamesDataDir));

            TextInputFormat nationalNamesFormat = new TextInputFormat(
            		new org.apache.flink.core.fs.Path(nationalNamesDataDir));


            //Create a DataStream based on the directory
            DataStream<StateNames> states =
                            streamEnv.readFile(
                                    stateNamesFormat,
                                    stateNamesDataDir,    //Director to monitor
                                    FileProcessingMode.PROCESS_CONTINUOUSLY,
                                    1000)//monitor interval
                            .map(
                                    (MapFunction<String, StateNames>) stateStr -> {
                                        StateNames stateNames = new StateNames(stateStr);
                                        return stateNames;
                                    }
               ).name("Loading " + stateNamesDataDir + " file");


            DataStream<NationalNames> nationals
                    = streamEnv.readFile(
                            nationalNamesFormat,
                            nationalNamesDataDir,    //Director to monitor
                            FileProcessingMode.PROCESS_CONTINUOUSLY,
                        1000)//monitor interval
                    .map(
                            (MapFunction<String, NationalNames>) nationalStr -> {
                                NationalNames nationalNames = new NationalNames(nationalStr);
                                return nationalNames;
                            }
                    )
                    .name("Loading " + stateNamesDataDir + " file");

            /* ***************************************************************************
             *                Sink to Postgresql
             ****************************************************************************/

            //Define DataStreamSink
            //states.print();
            states.addSink(new RichStateNamesSink());

            //nationals.print();
            nationals.addSink(new RichNationalNamesSink());

            /* ***************************************************************************
             *                  Setup data source and execute the Flink pipeline
             ****************************************************************************/

            // execute the streaming pipeline
            streamEnv.execute("Flink Stream USA Birth Names pipeline");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
