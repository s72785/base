/**
 * Copyright (C) 2014 Karlsruhe Institute of Technology
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package edu.kit.dama.staging.entities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This entity contains all properties needed to setup a transfer client access
 * to staging disk space. The main parts are:
 *
 * <ul>
 *
 * <li>clientHandlerId: The ID of the handler responsible for setting up client
 * access. Available IDs can be obtained via
 * StagingConfigurationManager.getSingleton().getAccessHandlerIDs()</li>
 *
 * <li>transferClientUrl: The final URL needed to access the transfer client
 * (NOT the transfer destination!) This might be for example a link to a Java
 * Webstart application accessible via Web application. </li>
 *
 * <li>stagingUrl: The URL to the staging location, which is the actual
 * destination of the transfer</li>
 *
 * <li> stagingAccessPoint: AccessPoint used to access the staging disk space.
 * Basically, this method will influence how 'stagingUrl' is build up. Available
 * AccessPoints can be obtained by calling
 * StagingConfigurationManager.getSingleton().getAccessPointNames()</li>
 *
 * <li>customProperties: This map can be used to add additional properties which
 * are only relevant for specific transfer client handlers.</li>
 *
 * </ul>
 *
 * @author jejkal
 */
public class TransferClientProperties {

  /**
   * The URL of the transfer client generated by the handler class
   */
  private String transferClientUrl = null;
  /**
   * The URL of the data within the cache area
   */
  private String stagingUrl = null;
  /**
   * The access point to access 'stagingUrl' via 'handlerClass'
   */
  private String stagingAccessPointId = null;
  /**
   * Send mail notification if transfer was prepared successfully
   */
  private boolean sendMailNotification = false;
  /**
   * Mail receiver adress if mail notification is enabled.
   */
  private String receiverMail = null;
  /**
   * The key for accessing KIT Data Manager APIs (e.g. REST interfaces).
   */
  private String apiKey = null;
  /**
   * The secret for accessing KIT Data Manager APIs (e.g. REST interfaces).
   */
  private String apiSecret = null;
  //registered staging processors
  private final List<StagingProcessor> processors = new LinkedList<StagingProcessor>();
  /**
   * Custom properties which might be used by 'handlerClass'
   */
  private Map<String, String> customProperties = null;

  /**
   * Default constructor
   */
  public TransferClientProperties() {
    customProperties = new HashMap<>();
  }

  /**
   * Add a custom property. Custom properties might be used by the handler
   * class. If a property is unknown, it should be ignored.
   *
   * @param pKey The property key
   * @param pValue The property value
   */
  public void addCustomProperty(String pKey, String pValue) {
    customProperties.put(pKey, pValue);
  }

  /**
   * Get a custom property
   *
   * @param pKey The key of the property
   *
   * @return The value of the custom property.
   */
  public String getCustomProperty(String pKey) {
    return customProperties.get(pKey);
  }

  /**
   * Get the keys of all registered custom properties
   *
   * @return An array of all keys
   */
  public String[] getPropertyKeys() {
    return customProperties.keySet().toArray(new String[customProperties.size()]);
  }

  /**
   * Get the transfer client URL
   *
   * @return the transferClientUrl
   */
  public String getTransferClientUrl() {
    return transferClientUrl;
  }

  /**
   * Set the transfer client URL
   *
   * @param transferClientUrl the transferClientUrl to set
   */
  public void setTransferClientUrl(String transferClientUrl) {
    this.transferClientUrl = transferClientUrl;
  }

  /**
   * Get the staging URL
   *
   * @return the stagingUrl
   */
  public String getStagingUrl() {
    return stagingUrl;
  }

  /**
   * Set the staging URL
   *
   * @param stagingUrl the stagingUrl to set
   */
  public void setStagingUrl(String stagingUrl) {
    this.stagingUrl = stagingUrl;
  }

  /**
   * Get the staging AccessPoint.
   *
   * @return the stagingAccessPointId
   */
  public String getStagingAccessPointId() {
    return stagingAccessPointId;
  }

  /**
   * Set the staging AccessPoint.
   *
   * @param stagingAccessPointId the stagingAccessPointId to set
   */
  public void setStagingAccessPointId(String stagingAccessPointId) {
    this.stagingAccessPointId = stagingAccessPointId;
  }

  /**
   * Enable/disable mail notification.
   *
   * @param sendMailNotification TRUE = Enable mail notification.
   */
  public void setSendMailNotification(boolean sendMailNotification) {
    this.sendMailNotification = sendMailNotification;
  }

  /**
   * Check if mail notification is enabled.
   *
   * @return TRUE = Mail notification is enabled.
   */
  public boolean isSendMailNotification() {
    return sendMailNotification;
  }

  /**
   * Set the reseiver mail address.
   *
   * @param receiverMail The receiver mail address.
   */
  public void setReceiverMail(String receiverMail) {
    this.receiverMail = receiverMail;
  }

  /**
   * Get the reseiver mail address.
   *
   * @return The receiver mail address.
   */
  public String getReceiverMail() {
    return receiverMail;
  }

  /**
   * Set the API key for accessing KIT Data Manager services.
   *
   * @param pApiKey The API key for accessing KIT Data Manager services.
   */
  public void setApiKey(String pApiKey) {
    this.apiKey = pApiKey;
  }

  /**
   * Get the API key for accessing KIT Data Manager services.
   *
   * @return The API key for accessing KIT Data Manager services.
   */
  public String getApiKey() {
    return apiKey;
  }

  /**
   * Set the API secret for accessing KIT Data Manager services.
   *
   * @param pApiSecret The API secret for accessing KIT Data Manager services.
   */
  public void setApiSecret(String pApiSecret) {
    this.apiSecret = pApiSecret;
  }

  /**
   * Get the API secret for accessing KIT Data Manager services.
   *
   * @return The API secret for accessing KIT Data Manager services.
   */
  public String getApiSecret() {
    return apiSecret;
  }

  /**
   * Add a staging processor.
   *
   * @param pProcessor A staging processor.
   */
  public void addProcessor(StagingProcessor pProcessor) {
    processors.add(pProcessor);
  }

  /**
   * Get the list of staging processors.
   *
   * @return A list of staging processors.
   */
  public List<StagingProcessor> getProcessors() {
    return processors;
  }
}
