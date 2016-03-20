package de.avalon.utils.reflection.resolver;

import java.lang.reflect.Method;

import de.avalon.utils.reflection.util.AccessUtil;

public class MethodResolver extends MemberResolver<Method> {
	public MethodResolver(Class<?> clazz) {
		super(clazz);
	}

	public MethodResolver(String className) throws ClassNotFoundException {
		super(className);
	}

	public Method resolveSilent(String... names) {
		try {
			return resolve(names);
		} catch (Exception e) {
		}
		return null;
	}

	public Method resolveSilent(ResolverQuery... queries) {
		return (Method) super.resolveSilent(queries);
	}

	public Method resolve(String... names) throws NoSuchMethodException {
		ResolverQuery.Builder builder = ResolverQuery.builder();
		for (String name : names) {
			builder.with(name);
		}
		return resolve(builder.build());
	}

	public Method resolve(ResolverQuery... queries) throws NoSuchMethodException {
		try {
			return (Method) super.resolve(queries);
		} catch (ReflectiveOperationException e) {
			throw ((NoSuchMethodException) e);
		}
	}

	protected Method resolveObject(ResolverQuery query) throws ReflectiveOperationException {
		for (Method method : this.clazz.getDeclaredMethods()) {
			if ((method.getName().equals(query.getName())) && ((query.getTypes().length == 0) || (ClassListEqual(query.getTypes(), method.getParameterTypes())))) {
				return AccessUtil.setAccessible(method);
			}
		}
		throw new NoSuchMethodException();
	}

	protected NoSuchMethodException notFoundException(String joinedNames) {
		return new NoSuchMethodException("Could not resolve method for " + joinedNames + " in class " + this.clazz);
	}

	static boolean ClassListEqual(Class<?>[] l1, Class<?>[] l2) {
		boolean equal = true;
		if (l1.length != l2.length)
			return false;
		for (int i = 0; i < l1.length; i++) {
			if (l1[i] != l2[i]) {
				equal = false;
				break;
			}
		}
		return equal;
	}
}