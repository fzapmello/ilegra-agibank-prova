package br.com.ilegra.agibank.model;

import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe que armazena o objeto ResultDTO para
 * geração do relatório.
 *
 */
@Component
public class ResultDTOStore {
	
	private Map<String, ResultDTO> resultStore = new HashMap<String, ResultDTO>();

	public boolean containsKey(Object key) {
		return resultStore.containsKey(key);
	}

	public ResultDTO put(String key, ResultDTO value) {
		return resultStore.put(key, value);
	}

	public Collection<ResultDTO> values() {
		return resultStore.values();
	}

	public ResultDTO get(Object key) {
		return resultStore.get(key);
	}

}
