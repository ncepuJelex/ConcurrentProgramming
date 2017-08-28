package net.jcip.examples.ch11;

import java.util.Set;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

/**
 * 如类名所示，在lock split 之前效率好低！
 * user 登录退出 和 开始查询把query加进来，查询结束把
 * query拿掉，是2个独立的家伙，竟然用在一个类中
 * 的锁绑起来，何必呢！
 * @author Jelex.xu
 * @date 2017年8月27日
 */
@ThreadSafe
public class ServerStatusBeforeSplit {

	@GuardedBy("this")
	public final Set<String> users;
	
	@GuardedBy("this")
	public final Set<String> queries;

	public ServerStatusBeforeSplit(Set<String> users, Set<String> queries) {
		this.users = users;
		this.queries = queries;
	}
	
	public synchronized void addUser(String u) {
		users.add(u);
	}
	
	public synchronized void addQuery(String q) {
		queries.add(q);
	}
	
	public synchronized void removeUser(String u) {
		users.remove(u);
	}
	
	public synchronized void removeQuery(String q) {
		queries.remove(q);
	}
}
