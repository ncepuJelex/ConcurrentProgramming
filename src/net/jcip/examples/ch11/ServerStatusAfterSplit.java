package net.jcip.examples.ch11;

import java.util.HashSet;
import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 这就是 lock split！
 * @author Jelex.xu
 * @date 2017年8月27日
 */
@ThreadSafe
public class ServerStatusAfterSplit {

	@GuardedBy("users")
	public final Set<String> users;
	@GuardedBy("queries")
	public final Set<String> queries;

	public ServerStatusAfterSplit() {
		users = new HashSet<String>();
		queries = new HashSet<String>();
	}

	public void addUser(String u) {
		synchronized (users) {
			users.add(u);
		}
	}

	public void addQuery(String q) {
		synchronized (queries) {
			queries.add(q);
		}
	}
	
	public void removeUser(String u) {
		synchronized(users) {
			users.remove(u);
		}
	}
	
	public void removeQuery(String query) {
		synchronized(queries) {
			queries.remove(query);
		}
	}
}
