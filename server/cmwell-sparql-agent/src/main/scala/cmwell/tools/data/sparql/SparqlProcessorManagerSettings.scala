/**
  * Copyright 2015 Thomson Reuters
  *
  * Licensed under the Apache License, Version 2.0 (the “License”); you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *   http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
  * an “AS IS” BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  *
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package cmwell.tools.data.sparql

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.scaladsl.Flow
import akka.util.ByteString
import cmwell.tools.data.utils.ArgsManipulations
import cmwell.tools.data.utils.ArgsManipulations.HttpAddress
import com.typesafe.config
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._
import scala.util.Try

class SparqlProcessorManagerSettings {
  val stpSettings: config.Config = ConfigFactory.load()
  val hostConfigFile: String = stpSettings.getString("cmwell.agents.sparql-triggered-processor.host-config-file")
  val hostUpdatesSource: String = stpSettings.getString("cmwell.agents.sparql-triggered-processor.host-updates-source")
  val hostWriteOutput: String = stpSettings.getString("cmwell.agents.sparql-triggered-processor.host-write-output")
  val materializedViewFormat: String = stpSettings.getString("cmwell.agents.sparql-triggered-processor.format")
  val pathAgentConfigs: String = stpSettings.getString("cmwell.agents.sparql-triggered-processor.path-agent-configs")
  val writeToken: String = stpSettings.getString("cmwell.agents.sparql-triggered-processor.write-token")
  val initDelay: FiniteDuration =
    stpSettings.getDuration("cmwell.agents.sparql-triggered-processor.init-delay").toMillis.millis
  val maxDelay: FiniteDuration =
    stpSettings.getDuration("cmwell.agents.sparql-triggered-processor.max-delay").toMillis.millis
  val interval: FiniteDuration =
    stpSettings.getDuration("cmwell.agents.sparql-triggered-processor.config-polling-interval").toMillis.millis

  val consumeLengthHint = stpSettings.hasPath("cmwell.agents.sparql-triggered-processor.consumer.fetch-size") match {
    case true => Some(stpSettings.getInt("cmwell.agents.sparql-triggered-processor.consumer.fetch-size"))
    case false => None
  }

  //val httpPool: Flow[(HttpRequest, ByteString), (Try[HttpResponse], ByteString), Http.HostConnectionPool] = {
  //val HttpAddress(_, host, port, _) = ArgsManipulations.extractBaseUrl(hostConfigFile)
  //Http().cachedHostConnectionPool[ByteString](host, port)
  //}
}
