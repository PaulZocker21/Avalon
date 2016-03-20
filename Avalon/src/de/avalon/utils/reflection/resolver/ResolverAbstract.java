package de.avalon.utils.reflection.resolver;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ResolverAbstract<T> {
	protected final Map<ResolverQuery, T> resolvedObjects = new ConcurrentHashMap<ResolverQuery, T>();

	protected T resolveSilent(ResolverQuery... queries) {
		try {
			return (T) resolve(queries);
		} catch (Exception e) {
		}
		return null;
	}

	protected T resolve(ResolverQuery... queries) throws ReflectiveOperationException {
		if ((queries == null) || (queries.length <= 0))
			throw new IllegalArgumentException("Given possibilities are empty");
		for (ResolverQuery query : queries) {
			if (this.resolvedObjects.containsKey(query)) {
				return (T) this.resolvedObjects.get(query);
			}
			try {
				T resolved = resolveObject(query);

				this.resolvedObjects.put(query, resolved);
				return resolved;
			} catch (ReflectiveOperationException e) {
			}
		}

		throw notFoundException(Arrays.asList(queries).toString());
	}

	protected abstract T resolveObject(ResolverQuery paramResolverQuery) throws ReflectiveOperationException;

	protected ReflectiveOperationException notFoundException(String joinedNames) {
		return new ReflectiveOperationException("Objects could not be resolved: " + joinedNames);
	}
}