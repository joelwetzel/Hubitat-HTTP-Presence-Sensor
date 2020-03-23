/**
 *  HTTP Presence Sensor
 *
 *  Copyright 2019 Joel Wetzel
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

	
metadata {
	definition (name: "HTTP Presence Sensor", namespace: "joelwetzel", author: "Joel Wetzel") {
		capability "Refresh"
		capability "Sensor"
        capability "Presence Sensor"
	}

	preferences {
		section {
			input (
				type: "string",
				name: "endpointUrl",
				title: "Endpoint URL",
				required: true				
			)
			input (
				type: "bool",
				name: "enableDebugLogging",
				title: "Enable Debug Logging?",
				required: true,
				defaultValue: false
			)
		}
	}
}


def log(msg) {
	if (enableDebugLogging) {
		log.debug msg
	}
}


def installed () {
	log.info "${device.displayName}.installed()"
    updated()
}


def updated () {
	log.info "${device.displayName}.updated()"
    
    state.tryCount = 0
    
    runEvery1Minute(refresh)
    runIn(2, refresh)
}


def refresh() {
	log "${device.displayName}.refresh()"

	state.tryCount = state.tryCount + 1
    
    if (state.tryCount > 3 && device.currentValue('presence') != "not present") {
        def descriptionText = "${device.displayName} is OFFLINE";
        log descriptionText
        sendEvent(name: "presence", value: "not present", linkText: deviceName, descriptionText: descriptionText)
    }
    
	asynchttpGet("httpGetCallback", [
		uri: endpointUrl,
		timeout: 10
	]);
}


def httpGetCallback(response, data) {
	//log.debug "${device.displayName}: httpGetCallback(response, data)"
	
	if (response == null || response.class != hubitat.scheduling.AsyncResponse) {
		return
	}
		
	if (response.getStatus() == 200) {
		state.tryCount = 0
		
		if (device.currentValue('presence') != "present") {
			def descriptionText = "${device.displayName} is ONLINE";
			log descriptionText
			sendEvent(name: "presence", value: "present", linkText: deviceName, descriptionText: descriptionText)
		}
	}
}


