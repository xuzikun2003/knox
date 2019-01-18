/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.apache.knox.gateway.topology.discovery.cm.model.ranger;

import com.cloudera.api.swagger.model.ApiConfigList;
import com.cloudera.api.swagger.model.ApiRole;
import com.cloudera.api.swagger.model.ApiService;
import com.cloudera.api.swagger.model.ApiServiceConfig;
import org.apache.knox.gateway.topology.discovery.cm.ServiceModel;
import org.apache.knox.gateway.topology.discovery.cm.model.AbstractServiceModelGenerator;

import java.util.Locale;

public class RangerServiceModelGenerator extends AbstractServiceModelGenerator {
  private static final String SERVICE = "RANGER";
  private static final String SERVICE_TYPE = "RANGER";
  private static final String ROLE_TYPE = "RANGER_ADMIN";

  @Override
  public boolean handles(ApiService service, ApiServiceConfig serviceConfig, ApiRole role, ApiConfigList roleConfig) {
    return SERVICE_TYPE.equals(service.getType()) && ROLE_TYPE.equals(role.getType());
  }

  @Override
  public ServiceModel generateService(ApiService       service,
                                      ApiServiceConfig serviceConfig,
                                      ApiRole          role,
                                      ApiConfigList    roleConfig) {
    String hostname = role.getHostRef().getHostname();
    String scheme;
    String port;
    boolean sslEnabled = Boolean.parseBoolean(getRoleConfigValue(roleConfig, "ssl_enabled"));
    if(sslEnabled) {
      scheme = "https";
      port = getServiceConfigValue(serviceConfig, "ranger_service_https_port");
    } else {
      scheme = "http";
      port = getServiceConfigValue(serviceConfig, "ranger_service_http_port");
    }
    return new ServiceModel(ServiceModel.Type.API,
                            SERVICE,
                            String.format(Locale.getDefault(), "%s://%s:%s", scheme, hostname, port));
  }

}
