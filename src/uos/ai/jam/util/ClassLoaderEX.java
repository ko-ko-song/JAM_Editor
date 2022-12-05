package uos.ai.jam.util;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class ClassLoaderEX extends ClassLoader {
	private final List<String>			_importClasses;
	private final List<String>			_importPackages;

	public ClassLoaderEX() {
		_importClasses		= new LinkedList<String>();
		_importPackages		= new LinkedList<String>();
		_importPackages.add("java.lang");
	}
	
	public void importPackage(String packageName) {
		if (_importPackages.contains(packageName)) {
			throw new IllegalStateException("already imported: " + packageName);
		}
		_importPackages.add(packageName);
	}
	
	public void importClass(String className) {
		if (_importClasses.contains(className)) {
			throw new IllegalStateException("already imported: " + className);
		}
		_importClasses.add(className);
	}
	
	public Class<?> loadClass(String className) throws ClassNotFoundException {
		try {
			return super.loadClass(className);
		} catch(ClassNotFoundException e) {
			if (!className.contains(".")) {
				for (String importClass : _importClasses) {
					if (importClass.endsWith(className)) {
						try {
							return super.loadClass(importClass);
						} catch(ClassNotFoundException ignore) {}
					}
				}
				for (String packageName : _importPackages) {
					try {
						return super.loadClass(packageName + "." + className);
					} catch(ClassNotFoundException ignore) {}
				}
			} 
			throw e;
		}
	}
	
	public Class<?> loadClass(StringTokenizer tokenizer) throws ClassNotFoundException {
		String packageName = null;
		String token = tokenizer.nextToken();
		try {
			return loadClass(token);
		} catch(Exception e) {
			packageName = token;
		}
		while(tokenizer.hasMoreTokens()) {
			token = tokenizer.nextToken();
			try {
				return loadClass(packageName + "." + token);
			} catch(Exception e) {
				packageName = packageName + "." + token;
			}
		}
		throw new ClassNotFoundException();
	}
}
