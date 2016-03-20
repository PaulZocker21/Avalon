package de.avalon.utils.reflection.resolver;

import java.lang.reflect.Constructor;

import de.avalon.utils.reflection.util.AccessUtil;

public class ConstructorResolver extends MemberResolver<Constructor<?>> {
	public ConstructorResolver(Class<?> clazz) {
		super(clazz);
	}

	public ConstructorResolver(String className) throws ClassNotFoundException {
		super(className);
	}

	public Constructor<?> resolveSilent(Class<?>[]... types) {
		try {
			return resolve(types);
		} catch (Exception e) {
		}
		return null;
	}

	public Constructor<?> resolve(Class<?>[]... types) throws NoSuchMethodException {
		ResolverQuery.Builder builder = ResolverQuery.builder();
		for (Class<?>[] type : types)
			builder.with(type);
		try {
			return (Constructor<?>) super.resolve(builder.build());
		} catch (ReflectiveOperationException e) {
			throw ((NoSuchMethodException) e);
		}
	}

	protected Constructor<?> resolveObject(ResolverQuery query) throws ReflectiveOperationException {
		return AccessUtil.setAccessible(this.clazz.getDeclaredConstructor(query.getTypes()));
	}

	public Constructor<?> resolveFirstConstructor() throws ReflectiveOperationException {
		Constructor<?>[] arrayOfConstructor = this.clazz.getDeclaredConstructors();
		int i = arrayOfConstructor.length;
		int j = 0;
		if (j < i) {
			Constructor<?> constructor = arrayOfConstructor[j];
			return AccessUtil.setAccessible(constructor);
		}
		return null;
	}

	public Constructor<?> resolveFirstConstructorSilent() {
		try {
			return resolveFirstConstructor();
		} catch (Exception e) {
		}
		return null;
	}

	public Constructor<?> resolveLastConstructor() throws ReflectiveOperationException {
		Constructor<?> constructor = null;
		for (Constructor<?> constructor1 : this.clazz.getDeclaredConstructors()) {
			constructor = constructor1;
		}
		if (constructor != null)
			return AccessUtil.setAccessible(constructor);
		return null;
	}

	public Constructor<?> resolveLastConstructorSilent() {
		try {
			return resolveLastConstructor();
		} catch (Exception e) {
		}
		return null;
	}

	protected NoSuchMethodException notFoundException(String joinedNames) {
		return new NoSuchMethodException("Could not resolve constructor for " + joinedNames + " in class " + this.clazz);
	}
}
