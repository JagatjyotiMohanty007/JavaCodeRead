import com.microsoft.azure.gateway.core.GatewayModule;

/*
 * Copyright (c) Microsoft. All rights reserved.
 * Licensed under the MIT license. See LICENSE file in the project root for full license information.
 */

import com.microsoft.azure.gateway.core.GatewayModule;
import com.microsoft.azure.gateway.core.Broker;
import com.microsoft.azure.gateway.messaging.Message;


public class FilterModule extends GatewayModule { 
	
	public FilterModule(long address, Broker broker, String configuration) {
		super(address, broker, configuration);
		//this.threadStop = false;
		System.out.println("ReadModule Senser Module Constructor.....: ");
		
	}


	@Override
	public void receive(Message message) {
		// Ignores incoming messages
		System.out.println(" Message filtered...");
		System.out.println("ReadModule Module Received.....: " + message);
	}

	@Override
	public void destroy() {
		// Causes the publishing thread to stop
		System.out.println("ReadModule Module Destroy.....: ");
		//this.threadStop = true;
	}

}
