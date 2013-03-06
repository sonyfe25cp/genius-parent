package com.genius.admin.helper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.config.AbstractFactoryBean;

import com.mongodb.Mongo;
import com.mongodb.MongoOptions;
import com.mongodb.ServerAddress;

/**
* Copyright (C) 2010 SarathOnline.com.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

public class MongoFactoryBean extends AbstractFactoryBean<Mongo> {

	private List<ServerAddress> replicaSetSeeds = new ArrayList<ServerAddress>();
	private MongoOptions mongoOptions;

	@Override
	public Class<?> getObjectType() {
		return Mongo.class;
	}

	@Override
	protected Mongo createInstance() throws Exception {
		if (replicaSetSeeds.size() > 0) {
			if (mongoOptions != null) {
				return new Mongo(replicaSetSeeds, mongoOptions);
			}
			return new Mongo(replicaSetSeeds);
		}
		return new Mongo();
	}

	public void setMultiAddress(String[] serverAddresses) {
		replSeeds(serverAddresses);
	}

	private void replSeeds(String... serverAddresses) {
		try {
			replicaSetSeeds.clear();
			for (String addr : serverAddresses) {
				String[] a = addr.split(":");
				String host = a[0];
				if (a.length > 2) {
					throw new IllegalArgumentException("Invalid Server Address : " + addr);
				}else if(a.length == 2) {
					replicaSetSeeds.add(new ServerAddress(host, Integer.parseInt(a[1])));
				}else{
					replicaSetSeeds.add(new ServerAddress(host));
				}
			}
		} catch (Exception e) {
			throw new BeanCreationException("Error while creating replicaSetAddresses",e);
		}
	}

	public void setAddress(String serverAddress) {
		replSeeds(serverAddress);
	}

}
