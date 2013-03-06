package com.genius.dao;

import java.util.List;

import com.genius.model.ExtractConfiguration;
/**
 * @author lins
 */
public interface IExtractConfigurationDAO {
	public List<ExtractConfiguration> loadExtCfgs(int begin, int end);
	public void saveExtCfg(ExtractConfiguration extCfg);
	public void updateExtCfg(ExtractConfiguration extCfg);
	public ExtractConfiguration loadExtCfg(String id);
	public void removeExtCfg(String id);
	public void changeExtCfgStatus(String id);
	public List<ExtractConfiguration> loadExtCfgBySeed(String name);
	public void removeExtCfgBySeed(String name);
	public long getCount();
	public long getCount(String type);
}
