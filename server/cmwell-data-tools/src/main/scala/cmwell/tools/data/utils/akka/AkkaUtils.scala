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
package cmwell.tools.data.utils.akka

import java.net.InetSocketAddress

import akka.http.scaladsl.ClientTransport
import akka.http.scaladsl.settings.{ClientConnectionSettings, ConnectionPoolSettings}
import com.typesafe.config.ConfigFactory

object AkkaUtils {

  def generateClientConnectionSettings(userAgent : String) ={
    val settings = ClientConnectionSettings(
        ConfigFactory
          .parseString(s"akka.http.host-connection-pool.client.user-agent-header=$userAgent")
          .withFallback(config)
      )

    val proxyHost = config.getString("akka.http.client.proxy.https.host")
    val proxyPort = config.getInt("akka.http.client.proxy.https.port")

    if (proxyHost!="")
    {
      val httpsProxyTransport = ClientTransport.httpsProxy(InetSocketAddress.createUnresolved(proxyHost, proxyPort))
      settings.withTransport(httpsProxyTransport)
    }
    else
      settings
  }

  def generateConnectionPoolSettings(userAgent : Option[String] = None) ={
    val settings = userAgent.fold{
      ConnectionPoolSettings(config)
      .withConnectionSettings(ClientConnectionSettings(config))
    }{userAgentV =>
      ConnectionPoolSettings(
        ConfigFactory
          .parseString(s"akka.http.host-connection-pool.client.user-agent-header=$userAgentV")
          .withFallback(config)
      )
    }

    val proxyHost = config.getString("akka.http.client.proxy.https.host")
    val proxyPort = config.getInt("akka.http.client.proxy.https.port")

    if (proxyHost!="")
    {
      val httpsProxyTransport = ClientTransport.httpsProxy(InetSocketAddress.createUnresolved(proxyHost, proxyPort))
      settings.withTransport(httpsProxyTransport)
    }
    else
      settings
  }
}