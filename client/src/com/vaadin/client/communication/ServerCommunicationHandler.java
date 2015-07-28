/*
 * Copyright 2000-2014 Vaadin Ltd.
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
package com.vaadin.client.communication;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.Command;
import com.vaadin.client.ApplicationConfiguration;
import com.vaadin.client.ApplicationConnection;
import com.vaadin.client.ApplicationConnection.RequestStartingEvent;
import com.vaadin.client.ApplicationConnection.ResponseHandlingEndedEvent;
import com.vaadin.client.Util;
import com.vaadin.client.VLoadingIndicator;
import com.vaadin.shared.ApplicationConstants;
import com.vaadin.shared.Version;
import com.vaadin.shared.ui.ui.UIState.PushConfigurationState;

import elemental.json.Json;
import elemental.json.JsonArray;
import elemental.json.JsonObject;

/**
 * ServerCommunicationHandler is responsible for communicating (sending and
 * receiving messages) with the servlet.
 * 
 * It will internally use either XHR or websockets for communicating, depending
 * on how the application is configured.
 * 
 * Uses {@link ServerMessageHandler} for processing received messages
 * 
 * @since
 * @author Vaadin Ltd
 */
public class ServerCommunicationHandler {

    public static final String JSON_COMMUNICATION_PREFIX = "for(;;);[";
    public static final String JSON_COMMUNICATION_SUFFIX = "]";

    private ApplicationConnection connection;
    private boolean hasActiveRequest = false;

    /**
     * Counter for the messages send to the server. First sent message has id 0.
     */
    private int clientToServerMessageId = 0;
    private XhrConnection xhrConnection;
    private PushConnection push;

    public ServerCommunicationHandler() {
        xhrConnection = GWT.create(XhrConnection.class);
    }

    /**
     * Sets the application connection this handler is connected to
     *
     * @param connection
     *            the application connection this handler is connected to
     */
    public void setConnection(ApplicationConnection connection) {
        this.connection = connection;
        xhrConnection.setConnection(connection);
    }

    public static Logger getLogger() {
        return Logger.getLogger(ServerCommunicationHandler.class.getName());
    }

    public void sendInvocationsToServer() {
        if (!connection.isApplicationRunning()) {
            getLogger()
                    .warning(
                            "Trying to send RPC from not yet started or stopped application");
            return;
        }

        if (hasActiveRequest() || (push != null && !push.isActive())) {
            // There is an active request or push is enabled but not active
            // -> send when current request completes or push becomes active
        } else {
            doSendInvocationsToServer();
        }
    }

    /**
     * Sends all pending method invocations (server RPC and legacy variable
     * changes) to the server.
     * 
     */
    private void doSendInvocationsToServer() {
        ServerRpcQueue serverRpcQueue = getServerRpcQueue();
        if (serverRpcQueue.isEmpty()) {
            return;
        }

        if (ApplicationConfiguration.isDebugMode()) {
            Util.logMethodInvocations(connection, serverRpcQueue.getAll());
        }

        boolean showLoadingIndicator = serverRpcQueue.showLoadingIndicator();
        JsonArray reqJson = serverRpcQueue.toJson();
        serverRpcQueue.clear();

        if (reqJson.length() == 0) {
            // Nothing to send, all invocations were filtered out (for
            // non-existing connectors)
            getLogger()
                    .warning(
                            "All RPCs filtered out, not sending anything to the server");
            return;
        }

        JsonObject extraJson = Json.createObject();
        if (!connection.getConfiguration().isWidgetsetVersionSent()) {
            extraJson.put(ApplicationConstants.WIDGETSET_VERSION_ID,
                    Version.getFullVersion());
            connection.getConfiguration().setWidgetsetVersionSent();
        }
        if (showLoadingIndicator) {
            connection.getLoadingIndicator().trigger();
        }
        send(reqJson, extraJson);
    }

    private ServerRpcQueue getServerRpcQueue() {
        return connection.getServerRpcQueue();
    }

    /**
     * Makes an UIDL request to the server.
     * 
     * @param reqInvocations
     *            Data containing RPC invocations and all related information.
     * @param extraParams
     *            Parameters that are added to the payload
     */
    protected void send(final JsonArray reqInvocations,
            final JsonObject extraJson) {
        startRequest();

        JsonObject payload = Json.createObject();
        String csrfToken = getServerMessageHandler().getCsrfToken();
        if (!csrfToken.equals(ApplicationConstants.CSRF_TOKEN_DEFAULT_VALUE)) {
            payload.put(ApplicationConstants.CSRF_TOKEN, csrfToken);
        }
        payload.put(ApplicationConstants.RPC_INVOCATIONS, reqInvocations);
        payload.put(ApplicationConstants.SERVER_SYNC_ID,
                getServerMessageHandler().getLastSeenServerSyncId());
        payload.put(ApplicationConstants.CLIENT_TO_SERVER_ID,
                clientToServerMessageId++);

        if (extraJson != null) {
            for (String key : extraJson.keys()) {
                payload.put(key, extraJson.get(key));
            }
        }

        send(payload);

    }

    /**
     * Sends an asynchronous or synchronous UIDL request to the server using the
     * given URI.
     * 
     * @param uri
     *            The URI to use for the request. May includes GET parameters
     * @param payload
     *            The contents of the request to send
     */
    public void send(final JsonObject payload) {
        if (push != null && push.isBidirectional()) {
            push.push(payload);
        } else {
            xhrConnection.send(payload);
        }
    }

    /**
     * Sets the status for the push connection.
     * 
     * @param enabled
     *            <code>true</code> to enable the push connection;
     *            <code>false</code> to disable the push connection.
     */
    public void setPushEnabled(boolean enabled) {
        final PushConfigurationState pushState = connection.getUIConnector()
                .getState().pushConfiguration;

        if (enabled && push == null) {
            push = GWT.create(PushConnection.class);
            push.init(connection, pushState);
        } else if (!enabled && push != null && push.isActive()) {
            push.disconnect(new Command() {
                @Override
                public void execute() {
                    push = null;
                    /*
                     * If push has been enabled again while we were waiting for
                     * the old connection to disconnect, now is the right time
                     * to open a new connection
                     */
                    if (pushState.mode.isEnabled()) {
                        setPushEnabled(true);
                    }

                    /*
                     * Send anything that was enqueued while we waited for the
                     * connection to close
                     */
                    if (getServerRpcQueue().isFlushPending()) {
                        getServerRpcQueue().flush();
                    }
                }
            });
        }
    }

    public void startRequest() {
        if (hasActiveRequest) {
            getLogger().severe(
                    "Trying to start a new request while another is active");
        }
        hasActiveRequest = true;
        connection.fireEvent(new RequestStartingEvent(connection));
    }

    public void endRequest() {
        if (!hasActiveRequest) {
            getLogger().severe("No active request");
        }
        // After sendInvocationsToServer() there may be a new active
        // request, so we must set hasActiveRequest to false before, not after,
        // the call.
        hasActiveRequest = false;

        if (connection.isApplicationRunning()) {
            if (getServerRpcQueue().isFlushPending()) {
                sendInvocationsToServer();
            }
            ApplicationConnection.runPostRequestHooks(connection
                    .getConfiguration().getRootPanelId());
        }

        // deferring to avoid flickering
        Scheduler.get().scheduleDeferred(new Command() {
            @Override
            public void execute() {
                if (!connection.isApplicationRunning()
                        || !(hasActiveRequest() || getServerRpcQueue()
                                .isFlushPending())) {
                    getLoadingIndicator().hide();

                    // If on Liferay and session expiration management is in
                    // use, extend session duration on each request.
                    // Doing it here rather than before the request to improve
                    // responsiveness.
                    // Postponed until the end of the next request if other
                    // requests still pending.
                    ApplicationConnection.extendLiferaySession();
                }
            }
        });
        connection.fireEvent(new ResponseHandlingEndedEvent(connection));
    }

    /**
     * Indicates whether or not there are currently active UIDL requests. Used
     * internally to sequence requests properly, seldom needed in Widgets.
     * 
     * @return true if there are active requests
     */
    public boolean hasActiveRequest() {
        return hasActiveRequest;
    }

    /**
     * Returns a human readable string representation of the method used to
     * communicate with the server.
     * 
     * @return A string representation of the current transport type
     */
    public String getCommunicationMethodName() {
        if (push != null) {
            return "Push (" + push.getTransportType() + ")";
        } else {
            return "XHR";
        }
    }

    private CommunicationProblemHandler getCommunicationProblemHandler() {
        return connection.getCommunicationProblemHandler();
    }

    private ServerMessageHandler getServerMessageHandler() {
        return connection.getServerMessageHandler();
    }

    private VLoadingIndicator getLoadingIndicator() {
        return connection.getLoadingIndicator();
    }

    /**
     * Resynchronize the client side, i.e. reload all component hierarchy and
     * state from the server
     */
    public void resynchronize() {
        getLogger().info("Resynchronizing from server");
        JsonObject resyncParam = Json.createObject();
        resyncParam.put(ApplicationConstants.RESYNCHRONIZE_ID, true);
        send(Json.createArray(), resyncParam);
    }

    /**
     * Used internally to update what the server expects
     * 
     * @param clientToServerMessageId
     *            the new client id to set
     * @param force
     *            true if the id must be updated, false otherwise
     */
    public void setClientToServerMessageId(int nextExpectedId, boolean force) {
        if (nextExpectedId == clientToServerMessageId) {
            // No op as everything matches they way it should
            return;
        }
        if (force) {
            getLogger().info(
                    "Forced update of clientId to " + clientToServerMessageId);
            clientToServerMessageId = nextExpectedId;
            return;
        }

        if (nextExpectedId > clientToServerMessageId) {
            if (clientToServerMessageId == 0) {
                // We have never sent a message to the server, so likely the
                // server knows better (typical case is that we refreshed a
                // @PreserveOnRefresh UI)
                getLogger().info(
                        "Updating client-to-server id to " + nextExpectedId
                                + " based on server");
            } else {
                getLogger().warning(
                        "Server expects next client-to-server id to be "
                                + nextExpectedId + " but we were going to use "
                                + clientToServerMessageId + ". Will use "
                                + nextExpectedId + ".");
            }
            clientToServerMessageId = nextExpectedId;
        } else {
            // Server has not yet seen all our messages
            // Do nothing as they will arrive eventually
        }
    }

    /**
     * Strips the JSON wrapping from the given json string with wrapping.
     * 
     * If the given string is not wrapped as expected, returns null
     * 
     * @since
     * @param jsonWithWrapping
     *            the JSON received from the server
     * @return an unwrapped JSON string or null if the given string was not
     *         wrapped
     */
    public static String stripJSONWrapping(String jsonWithWrapping) {
        if (!jsonWithWrapping
                .startsWith(ServerCommunicationHandler.JSON_COMMUNICATION_PREFIX)
                || !jsonWithWrapping
                        .endsWith(ServerCommunicationHandler.JSON_COMMUNICATION_SUFFIX)) {
            return null;
        }
        return jsonWithWrapping.substring(
                ServerCommunicationHandler.JSON_COMMUNICATION_PREFIX.length(),
                jsonWithWrapping.length()
                        - ServerCommunicationHandler.JSON_COMMUNICATION_SUFFIX
                                .length());
    }
}
