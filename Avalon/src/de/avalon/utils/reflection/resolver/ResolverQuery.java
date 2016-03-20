package de.avalon.utils.reflection.resolver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ResolverQuery {
	private String name;
	private Class<?>[] types;

	public ResolverQuery(String name, Class<?>... types) {
		this.name = name;
		this.types = types;
	}

	public ResolverQuery(String name) {
		/* 52 */ this.name = name;
		/* 53 */ this.types = new Class[0];
	}

	public ResolverQuery(Class<?>... types) {
		/* 57 */ this.types = types;
	}

	public String getName() {
		/* 61 */ return this.name;
	}

	public Class<?>[] getTypes() {
		/* 65 */ return this.types;
	}

	public boolean equals(Object o) {
		/* 70 */ if (this == o)
			return true;
		/* 71 */ if ((o == null) || (getClass() != o.getClass())) {
			return false;
		}
		/* 73 */ ResolverQuery that = (ResolverQuery) o;

		/* 75 */ if (this.name != null ? !this.name.equals(that.name) : that.name != null) {
			return false;
		}
		/* 77 */ return Arrays.equals(this.types, that.types);
	}

	public int hashCode() {
		int result = this.name != null ? this.name.hashCode() : 0;
		result = 31 * result + (this.types != null ? Arrays.hashCode(this.types) : 0);
		return result;
	}

	public String toString() {
		return "ResolverQuery{name='" + this.name + '\'' + ", types=" + Arrays.toString(this.types) + '}';
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder {
		private List<ResolverQuery> queryList = new ArrayList<>();

		public Builder with(String name, Class<?>[] types) {
			this.queryList.add(new ResolverQuery(name, types));
			return this;
		}

		public Builder with(String name) {
			this.queryList.add(new ResolverQuery(name));
			return this;
		}

		public Builder with(Class<?>[] types) {
			this.queryList.add(new ResolverQuery(types));
			return this;
		}

		public ResolverQuery[] build() {
			return (ResolverQuery[]) this.queryList.toArray(new ResolverQuery[this.queryList.size()]);
		}
	}
}
