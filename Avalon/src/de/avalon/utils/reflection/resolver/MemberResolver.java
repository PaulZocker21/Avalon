package de.avalon.utils.reflection.resolver;

import java.lang.reflect.Member;

public abstract class MemberResolver<T extends Member> extends ResolverAbstract<T> {
	protected Class<?> clazz;

	public MemberResolver(Class<?> clazz) {
		if (clazz == null)
			throw new IllegalArgumentException("class cannot be null");
		this.clazz = clazz;
	}

	public MemberResolver(String className) throws ClassNotFoundException {
		this(new ClassResolver().resolve(new String[] { className }));
	}
}