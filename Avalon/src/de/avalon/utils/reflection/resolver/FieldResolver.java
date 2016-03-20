package de.avalon.utils.reflection.resolver;

import java.lang.reflect.Field;

import de.avalon.utils.reflection.util.AccessUtil;

public class FieldResolver extends MemberResolver<Field> {
	public FieldResolver(Class<?> clazz) {
		super(clazz);
	}

	public FieldResolver(String className) throws ClassNotFoundException {
		super(className);
	}

	public Field resolveSilent(String... names) {
		try {
			return resolve(names);
		} catch (Exception e) {
		}
		return null;
	}

	public Field resolve(String... names) throws NoSuchFieldException {
		ResolverQuery.Builder builder = ResolverQuery.builder();
		for (String name : names)
			builder.with(name);
		try {
			return (Field) super.resolve(builder.build());
		} catch (ReflectiveOperationException e) {
			throw ((NoSuchFieldException) e);
		}
	}

	public Field resolveSilent(ResolverQuery... queries) {
		try {
			return resolve(queries);
		} catch (Exception e) {
		}
		return null;
	}

	public Field resolve(ResolverQuery... queries) throws NoSuchFieldException {
		try {
			return (Field) super.resolve(queries);
		} catch (ReflectiveOperationException e) {
			throw ((NoSuchFieldException) e);
		}
	}

	protected Field resolveObject(ResolverQuery query) throws ReflectiveOperationException {
		if ((query.getTypes() == null) || (query.getTypes().length == 0)) {
			return AccessUtil.setAccessible(this.clazz.getDeclaredField(query.getName()));
		}
		for (Field field : this.clazz.getDeclaredFields()) {
			if (field.getName().equals(query.getName())) {
				for (Class<?> type : query.getTypes()) {
					if (field.getType().equals(type)) {
						return field;
					}
				}
			}
		}

		return null;
	}

	public Field resolveByFirstType(Class<?> type) throws ReflectiveOperationException {
		for (Field field : this.clazz.getDeclaredFields()) {
			if (field.getType().equals(type)) {
				return AccessUtil.setAccessible(field);
			}
		}
		throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
	}

	public Field resolveByFirstTypeSilent(Class<?> type) {
		try {
			return resolveByFirstType(type);
		} catch (Exception e) {
		}
		return null;
	}

	public Field resolveByLastType(Class<?> type) throws ReflectiveOperationException {
		Field field = null;
		for (Field field1 : this.clazz.getDeclaredFields()) {
			if (field1.getType().equals(type)) {
				field = field1;
			}
		}
		if (field == null)
			throw new NoSuchFieldException("Could not resolve field of type '" + type.toString() + "' in class " + this.clazz);
		return AccessUtil.setAccessible(field);
	}

	public Field resolveByLastTypeSilent(Class<?> type) {
		try {
			return resolveByLastType(type);
		} catch (Exception e) {
		}
		return null;
	}

	protected NoSuchFieldException notFoundException(String joinedNames) {
		return new NoSuchFieldException("Could not resolve field for " + joinedNames + " in class " + this.clazz);
	}
}