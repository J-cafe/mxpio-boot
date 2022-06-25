package com.mxpioframework.jpa.provider.impl;

import java.util.Map;
import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import org.springframework.stereotype.Component;

import com.mxpioframework.jpa.provider.EntityManagerFactoryProvider;

@Component
public class EntityManagerFactoryProviderImpl implements EntityManagerFactoryProvider {
	
	@Resource
	private Map<String, EntityManagerFactory> entityManagerFactoryMap;
	
/*	private Map<String, List<String>> entityClassNameMap = new HashMap<>();
	
	@PostConstruct
	public void test(){
		for (Entry<String, EntityManagerFactory> entry : entityManagerFactoryMap.entrySet()) {
			System.out.println("EntityManagerFactory==>"+entry.getKey());
			List<EntityType<?>> entityTypes = new ArrayList<EntityType<?>>(entry.getValue().getMetamodel().getEntities());
			List<String> entityClassNames = new ArrayList<String>();
			for (EntityType<?> entityType : entityTypes) {
				if (entityType instanceof Type) {
					entityClassNames.add(entityType.getJavaType().getName());
				}
			}
			entityClassNameMap.put(entry.getKey(), entityClassNames);
		}
		for(Entry<String, List<String>> entry : entityClassNameMap.entrySet()){
			System.out.println("entry=>"+entry.getKey());
			for(String s : entry.getValue()){
				System.out.println("column=>"+s);
			}
		}
		
	}*/

}
