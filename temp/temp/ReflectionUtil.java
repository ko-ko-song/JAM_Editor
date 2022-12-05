package kr.ac.uos.ai.editor.jam.temp;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import kr.ac.uos.ai.editor.jam.exception.AgentRuntimeException;
import kr.ac.uos.ai.editor.jam.expression.Binding;
import kr.ac.uos.ai.editor.jam.expression.Expression;
import kr.ac.uos.ai.editor.jam.expression.Value;


public class ReflectionUtil {
	private ReflectionUtil() {
		//
	}
	
	public static final Value newInstance(Class<?> clazz, List<Expression> expressionList, Binding binding) throws AgentRuntimeException {
		Object[] args = makeArgs(expressionList, binding);
		Object[] castArgs = null;
		Constructor<?> constructor = findConstructor(clazz, args);
		if (constructor != null) {
			castArgs = castArgs(constructor.getParameterTypes(), args);
		} else {
			constructor = findConstructor_varArgs(clazz, args);
			if (constructor != null) {
				castArgs = castArgs_varArgs(constructor.getParameterTypes(), args);
			} else {
				throw new AgentRuntimeException(IllegalArgumentException.class.getCanonicalName());
			}
		}
		Object instance = null;
		try {
			instance = constructor.newInstance(castArgs);
		} catch(Exception e) {
			throw new AgentRuntimeException(e.getClass().getCanonicalName());
		}
		return new Value(instance);
	}
	
	public static final Value getField(Object object, String fieldName) throws AgentRuntimeException {
		Field field = null;
		try {
			if (object instanceof Class<?>) {
				field = ((Class<?>)object).getField(fieldName);
			} else {
				field = object.getClass().getField(fieldName);
			}
		} catch(Exception e) {
			throw new AgentRuntimeException(e.getClass().getCanonicalName());
		}
		Object fieldValue = null;
		try {
			fieldValue = field.get(object);
		} catch(Exception e) {
			throw new AgentRuntimeException(e.getClass().getCanonicalName());
		}
		return makeResultValue(fieldValue);
	}
	
	public static final Value callMethod(Object object, String methodName, List<Expression> expressionList, Binding binding) throws AgentRuntimeException {
		Class<?> clazz = (object instanceof Class<?>) ? (Class<?>)object : object.getClass();
		Object[] args = makeArgs(expressionList, binding);
		Object[] castArgs = null;
		Method method = findMethod(clazz, methodName, args);
		if (method != null) {
			castArgs = castArgs(method.getParameterTypes(), args);
		} else {
			method = findMethod_varArgs(clazz, methodName, args);
			if (method != null) {
				castArgs = castArgs_varArgs(method.getParameterTypes(), args);
			} else {
				throw new AgentRuntimeException(NoSuchMethodException.class.getCanonicalName());
			}
		}
		Object result = null;
		try {
			result = method.invoke(object, castArgs);
		} catch(Exception e) {
			throw new AgentRuntimeException(e.getClass().getCanonicalName());
		}
		return makeResultValue(result);
	}
	
	private static final Value makeResultValue(Object result) {
		if (result == null) {
			return Value.UNDEFINED;
		}
		Class<?> clazz = result.getClass();
		if (clazz == Integer.class) {
			Integer iResult = (Integer)result;
			return new Value(iResult.intValue());
		} else if (clazz == Long.class) {
			Long lResult = (Long)result;
			return new Value(lResult.longValue());
		} else if (clazz == Float.class) {
			Float fResult = (Float)result;
			return new Value(fResult.floatValue());
		} else if (clazz == Double.class) {
			Double dResult = (Double)result;
			return new Value(dResult.doubleValue());
		} else if (clazz == String.class) {
			String sResult = (String)result;
			return new Value(sResult);
		} else {
			return new Value(result);
		}
	}
	
	private static final Constructor<?> findConstructor(Class<?> clazz, Object[] args) {
		// exactly match
		for (Constructor<?> constructor : clazz.getConstructors()) {
			if (isMatch(constructor.getParameterTypes(), args)) {
				return constructor;
			}
		}
		// shallow match
		for (Constructor<?> constructor : clazz.getConstructors()) {
			if (isMatch_shallow(constructor.getParameterTypes(), args)) {
				return constructor;
			}
		}
		return null;
	}
	
	private static final Constructor<?> findConstructor_varArgs(Class<?> clazz, Object[] args) {
		// exactly match
		for (Constructor<?> constructor : clazz.getConstructors()) {
			if (constructor.isVarArgs() && isMatch_varArgs(constructor.getParameterTypes(), args)) {
				return constructor;
			}
		}
		// shallow match
		for (Constructor<?> constructor : clazz.getConstructors()) {
			if (constructor.isVarArgs() && isMatch_varArgs_shallow(constructor.getParameterTypes(), args)) {
				return constructor;
			}
		}
		return null;
	}
	
	private static final Method findMethod(Class<?> clazz, String methodName, Object[] args) {
		// exactly match
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && isMatch(method.getParameterTypes(), args)) {
				return method;
			}
		}
		// shallow match
		for (Method method : clazz.getMethods()) {
			if (method.getName().equals(methodName) && isMatch_shallow(method.getParameterTypes(), args)) {
				return method;
			}
		}
		return null;
	}

	private static final Method findMethod_varArgs(Class<?> clazz, String methodName, Object[] args) {
		// exactly match
		for (Method method : clazz.getMethods()) {
			if (method.isVarArgs() && method.getName().equals(methodName) && isMatch_varArgs(method.getParameterTypes(), args)) {
				return method;
			}
		}
		// shallow match
		for (Method method : clazz.getMethods()) {
			if (method.isVarArgs() && method.getName().equals(methodName) && isMatch_varArgs_shallow(method.getParameterTypes(), args)) {
				return method;
			}
		}
		return null;
	}
	
	private static final boolean isMatch(Class<?>[] types, Object[] args) {
		if (types.length != args.length) {
			return false;
		}
		for (int i=0, n=types.length; i<n; i++) {
			if (!isInstance(types[i], args[i])) {
				return false;
			}
		}
		return true;
	}

	private static final boolean isMatch_shallow(Class<?>[] types, Object[] args) {
		if (types.length != args.length) {
			return false;
		}
		for (int i=0, n=types.length; i<n; i++) {
			if (!isInstance_shallow(types[i], args[i])) {
				return false;
			}
		}
		return true;
	}
	
	private static final boolean isMatch_varArgs(Class<?>[] types, Object[] args) {
		if (!types[types.length-1].isArray()) {
			return false;
		}
		for (int i=0, n=types.length-1; i<n; i++) {
			if (!isInstance(types[i], args[i])) {
				return false;
			}
		}
		Object[] lastArg = Arrays.copyOfRange(args, types.length-1, args.length);
		return isInstance(types[types.length-1], lastArg);
	}
	
	private static final boolean isMatch_varArgs_shallow(Class<?>[] types, Object[] args) {
		if (!types[types.length-1].isArray()) {
			return false;
		}
		for (int i=0, n=types.length-1; i<n; i++) {
			if (!isInstance_shallow(types[i], args[i])) {
				return false;
			}
		}
		Object[] lastArg = Arrays.copyOfRange(args, types.length-1, args.length);
		return isInstance_shallow(types[types.length-1], lastArg);
	}
	
	private static final boolean isInstance(Class<?> clazz, Object object) {
		if (clazz == int.class) {
			return Integer.class.isInstance(object);
		} else if (clazz == long.class) {
			return Long.class.isInstance(object);
		} else if (clazz == float.class) {
			return Float.class.isInstance(object);
		} else if (clazz == double.class) {
			return Double.class.isInstance(object);
		} else if (clazz == boolean.class) {
			return Boolean.class.isInstance(object);
		} else {
			if (clazz.isArray()) {
				if (!object.getClass().isArray()) {
					return false;
				}
				Class<?> componentType = clazz.getComponentType();
				for (Object anObject : (Object[])object) {
					if (!isInstance(componentType, anObject)) {
						return false;
					}
				}
				return true;
			} else {
				return clazz.isInstance(object);
			}
		}
	}
	
	private static final boolean isInstance_shallow(Class<?> clazz, Object object) {
		if (clazz == int.class) {
			return (Long.class.isInstance(object) || Integer.class.isInstance(object));
			// return Integer.class.isInstance(object);
		} else if (clazz == long.class) {
			return (Long.class.isInstance(object) || Integer.class.isInstance(object));
		} else if (clazz == float.class) {
			return (Double.class.isInstance(object) || Float.class.isInstance(object));
			// return Float.class.isInstance(object);
		} else if (clazz == double.class) {
			return (Double.class.isInstance(object) || Float.class.isInstance(object));
		} else if (clazz == boolean.class) {
			return Boolean.class.isInstance(object);
		} else {
			if (clazz.isArray()) {
				if (!object.getClass().isArray()) {
					return false;
				}
				Class<?> componentType = clazz.getComponentType();
				for (Object anObject : (Object[])object) {
					if (!isInstance(componentType, anObject)) {
						return false;
					}
				}
				return true;
			} else {
				return clazz.isInstance(object);
			}
		}
	}
	
	private static final Object[] castArgs(Class<?>[] types, Object[] args) {
		Object[] newArgs = new Object[args.length];
		for (int i=0, n=args.length; i<n; i++) {
			newArgs[i] = castArg(types[i], args[i]);
		}
		return newArgs;
	}
	
	private static final Object[] castArgs_varArgs(Class<?>[] types, Object[] args) {
		Object[] newArgs = new Object[types.length];
		for (int i=0, n=types.length-1; i<n; i++) {
			newArgs[i] = castArg(types[i], args[i]);
		}
		newArgs[types.length-1] = castArg(types[types.length-1], Arrays.copyOfRange(args, types.length-1, args.length));
		return newArgs;
	}
	
	private static final Object castArg(Class<?> type, Object arg) {
		if (!type.isArray()) {
			if (type == int.class && arg.getClass() == Long.class) {
				Long lArg = (Long)arg;
				return new Integer(lArg.intValue());
			} else if (type == float.class && arg.getClass() == Double.class) {
				Double dArg = (Double)arg;
				return new Float(dArg.floatValue());
			}			
			return arg;
		} else {
			Class<?> componentType = type.getComponentType();
			Object[] arrayArg = (Object[])arg;
			Object newArg = Array.newInstance(componentType, arrayArg.length);
			for (int i=0, n=arrayArg.length; i<n; i++) {
				Array.set(newArg, i, arrayArg[i]);
			}
			return newArg;
		}
	}
	
	private static final Object[] makeArgs(List<Expression> expressionList, Binding binding) throws AgentRuntimeException {
		Object[] args = new Object[expressionList.size()];
		for (int i=0, n=expressionList.size(); i<n; i++) {
			args[i] = expressionList.get(i).eval(binding).getObject();
		}
		return args;
	}	
}
