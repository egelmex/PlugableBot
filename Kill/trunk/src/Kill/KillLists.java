package Kill;

import java.util.ArrayList;
import java.util.List;

public class KillLists {
	private String owner;
	private List<String> kills;
	
	public KillLists(String owner) {
		this.owner = owner;
		kills = new ArrayList<String>();
	}
	
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public List<String> getKills() {
		return kills;
	}
	public void setKills(List<String> kills) {
		this.kills = kills;
	}
	
}
