package uos.ai.jam.message;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import uos.ai.jam.expression.Relation;

public class MessageQueue {
	private final List<Relation>		_queue;
	private final Lock					_lock;
	
	public MessageQueue() {
		_queue 		= new LinkedList<Relation>();
		_lock 		= new ReentrantLock();
	}
	
	public void enqueue(Relation message) {
		_lock.lock();
		try {
			_queue.add(message);
		} finally {
			_lock.unlock();
		}
	}
	
	public Relation dequeue() {
		_lock.lock();
		try {
			return (_queue.size() > 0) ? _queue.remove(0) : null;
		} finally {
			_lock.unlock();
		}
	}
}
