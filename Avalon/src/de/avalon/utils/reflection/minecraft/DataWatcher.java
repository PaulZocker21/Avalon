package de.avalon.utils.reflection.minecraft;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

import javax.annotation.Nullable;

import de.avalon.utils.reflection.resolver.ClassResolver;
import de.avalon.utils.reflection.resolver.ConstructorResolver;
import de.avalon.utils.reflection.resolver.FieldResolver;
import de.avalon.utils.reflection.resolver.MethodResolver;
import de.avalon.utils.reflection.resolver.ResolverQuery;
import de.avalon.utils.reflection.resolver.minecraft.NMSClassResolver;

public class DataWatcher {
	static ClassResolver classResolver = new ClassResolver();
	static NMSClassResolver nmsClassResolver = new NMSClassResolver();

	static Class<?> ItemStack = nmsClassResolver.resolveSilent(new String[] { "ItemStack" });
	static Class<?> ChunkCoordinates = nmsClassResolver.resolveSilent(new String[] { "ChunkCoordinates" });
	static Class<?> BlockPosition = nmsClassResolver.resolveSilent(new String[] { "BlockPosition" });
	static Class<?> Vector3f = nmsClassResolver.resolveSilent(new String[] { "Vector3f" });
	static Class<?> DataWatcher = nmsClassResolver.resolveSilent(new String[] { "DataWatcher" });
	static Class<?> Entity = nmsClassResolver.resolveSilent(new String[] { "Entity" });
	static Class<?> TIntObjectMap = classResolver.resolveSilent(new String[] { "gnu.trove.map.TIntObjectMap", "net.minecraft.util.gnu.trove.map.TIntObjectMap" });

	static ConstructorResolver DataWacherConstructorResolver = new ConstructorResolver(DataWatcher);

	static FieldResolver DataWatcherFieldResolver = new FieldResolver(DataWatcher);

	static MethodResolver TIntObjectMapMethodResolver = new MethodResolver(TIntObjectMap);
	static MethodResolver DataWatcherMethodResolver = new MethodResolver(DataWatcher);

	public static Object newDataWatcher(@Nullable Object entity) throws ReflectiveOperationException {
		return DataWacherConstructorResolver.resolve(new Class[][] { { Entity } }).newInstance(new Object[] { entity });
	}

	public static Object setValue(Object dataWatcher, int index, Object dataWatcherObject, Object value) throws ReflectiveOperationException {
		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			return V1_8.setValue(dataWatcher, index, value);
		}
		return V1_9.setItem(dataWatcher, index, dataWatcherObject, value);
	}

	public static Object setValue(Object dataWatcher, int index, DataWatcher.V1_9.ValueType type, Object value) throws ReflectiveOperationException {
		return setValue(dataWatcher, index, type.getType(), value);
	}

	public static Object setValue(Object dataWatcher, int index, Object value, FieldResolver dataWatcherObjectFieldResolver, String... dataWatcherObjectFieldNames) throws ReflectiveOperationException {
		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			return V1_8.setValue(dataWatcher, index, value);
		}
		Object dataWatcherObject = dataWatcherObjectFieldResolver.resolve(dataWatcherObjectFieldNames).get(null);
		return V1_9.setItem(dataWatcher, index, dataWatcherObject, value);
	}

	public static Object getValue(DataWatcher dataWatcher, int index) throws ReflectiveOperationException {
		if (Minecraft.VERSION.olderThan(Minecraft.Version.v1_9_R1)) {
			return V1_8.getValue(dataWatcher, index);
		}
		return V1_9.getValue(dataWatcher, Integer.valueOf(index));
	}

	public static int getValueType(Object value) {
		int type = 0;
		if ((value instanceof Number)) {
			if ((value instanceof Byte)) {
				type = 0;
			} else if ((value instanceof Short)) {
				type = 1;
			} else if ((value instanceof Integer)) {
				type = 2;
			} else if ((value instanceof Float)) {
				type = 3;
			}
		} else if ((value instanceof String)) {
			type = 4;
		} else if ((value != null) && (value.getClass().equals(ItemStack))) {
			type = 5;
		} else if ((value != null) && ((value.getClass().equals(ChunkCoordinates)) || (value.getClass().equals(BlockPosition)))) {
			type = 6;
		} else if ((value != null) && (value.getClass().equals(Vector3f))) {
			type = 7;
		}

		return type;
	}

	public static class V1_9 {
		static Class<?> DataWatcherItem = nmsClassResolver.resolveSilent(new String[] { "DataWatcher$Item" });
		static Class<?> DataWatcherObject = nmsClassResolver.resolveSilent(new String[] { "DataWatcherObject" });
		static ConstructorResolver DataWatcherItemConstructorResolver;
		static FieldResolver DataWatcherItemFieldResolver;
		static FieldResolver DataWatcherObjectFieldResolver;

		public static Object newDataWatcherItem(Object dataWatcherObject, Object value) throws ReflectiveOperationException {
			if (DataWatcherItemConstructorResolver == null)
				DataWatcherItemConstructorResolver = new ConstructorResolver(DataWatcherItem);
			return DataWatcherItemConstructorResolver.resolveFirstConstructor().newInstance(new Object[] { dataWatcherObject, value });
		}

		public static Object setItem(Object dataWatcher, int index, Object dataWatcherObject, Object value) throws ReflectiveOperationException {
			return setItem(dataWatcher, index, newDataWatcherItem(dataWatcherObject, value));
		}

		public static Object setItem(Object dataWatcher, int index, Object dataWatcherItem) throws ReflectiveOperationException {
			@SuppressWarnings("unchecked")
			Map<Integer, Object> map = (Map<Integer, Object>) DataWatcherFieldResolver.resolve(new String[] { "c" }).get(dataWatcher);
			map.put(Integer.valueOf(index), dataWatcherItem);
			return dataWatcher;
		}

		public static Object getItem(Object dataWatcher, Object dataWatcherObject) throws ReflectiveOperationException {
			return DataWatcherMethodResolver.resolve(new ResolverQuery[] { new ResolverQuery("c", new Class[] { DataWatcherObject }) }).invoke(dataWatcher, new Object[] { dataWatcherObject });
		}

		public static Object getValue(Object dataWatcher, Object dataWatcherObject) throws ReflectiveOperationException {
			/* 156 */ return DataWatcherMethodResolver.resolve(new String[] { "get" }).invoke(dataWatcher, new Object[] { dataWatcherObject });
		}

		public static Object getValue(Object dataWatcher, ValueType type) throws ReflectiveOperationException {
			/* 160 */ return getValue(dataWatcher, type.getType());
		}

		public static Object getItemObject(Object item) throws ReflectiveOperationException {
			/* 164 */ if (DataWatcherItemFieldResolver == null)
				DataWatcherItemFieldResolver = new FieldResolver(DataWatcherItem);
			/* 165 */ return DataWatcherItemFieldResolver.resolve(new String[] { "a" }).get(item);
		}

		public static int getItemIndex(Object dataWatcher, Object item) throws ReflectiveOperationException {
			/* 169 */ int index = -1;
			/* 170 */ @SuppressWarnings("unchecked")
			Map<Integer, Object> map = (Map<Integer, Object>) DataWatcherFieldResolver.resolve(new String[] { "c" }).get(dataWatcher);
			/* 171 */ for (Map.Entry<Integer, Object> entry : map.entrySet()) {
				/* 172 */ if (entry.getValue().equals(item)) {
					/* 173 */ index = ((Integer) entry.getKey()).intValue();
					/* 174 */ break;
				}
			}
			/* 177 */ return index;
		}

		public static Type getItemType(Object item) throws ReflectiveOperationException {
			/* 181 */ if (DataWatcherObjectFieldResolver == null)
				DataWatcherObjectFieldResolver = new FieldResolver(DataWatcherObject);
			/* 182 */ Object object = getItemObject(item);
			/* 183 */ Object serializer = DataWatcherObjectFieldResolver.resolve(new String[] { "b" }).get(object);
			/* 184 */ Type[] genericInterfaces = serializer.getClass().getGenericInterfaces();
			/* 185 */ if (genericInterfaces.length > 0) {
				/* 186 */ Type type = genericInterfaces[0];
				/* 187 */ if ((type instanceof ParameterizedType)) {
					/* 188 */ Type[] actualTypes = ((ParameterizedType) type).getActualTypeArguments();
					/* 189 */ if (actualTypes.length > 0) {
						/* 190 */ return actualTypes[0];
					}
				}
			}
			/* 194 */ return null;
		}

		public static Object getItemValue(Object item) throws ReflectiveOperationException {
			/* 198 */ if (DataWatcherItemFieldResolver == null)
				DataWatcherItemFieldResolver = new FieldResolver(DataWatcherItem);
			/* 199 */ return DataWatcherItemFieldResolver.resolve(new String[] { "b" }).get(item);
		}

		public static void setItemValue(Object item, Object value) throws ReflectiveOperationException {
			/* 203 */ DataWatcherItemFieldResolver.resolve(new String[] { "b" }).set(item, value);
		}

		public static enum ValueType {
			/* 211 */ ENTITY_FLAG("Entity", new String[] { "ax" }),

			/* 215 */ ENTITY_NAME("Entity", new String[] { "az" }),

			/* 220 */ ENTITY_NAME_VISIBLE("Entity", new String[] { "aA" }),

			/* 224 */ ENTITY_AIR_TICKS("Entity", new String[] { "ay" }),

			/* 228 */ ENTITY_SILENT("Entity", new String[] { "aB" }),

			/* 231 */ ENTITY_as("Entity", new String[] { "as" }),

			/* 236 */ ENTITY_LIVING_HEALTH("EntityLiving", new String[] { "HEALTH" }),

			/* 239 */ ENTITY_LIVING_f("EntityLiving", new String[] { "f" }),

			/* 242 */ ENTITY_LIVING_g("EntityLiving", new String[] { "g" }),

			/* 245 */ ENTITY_LIVING_h("EntityLiving", new String[] { "h" }),

			/* 250 */ ENTITY_INSENTIENT_FLAG("EntityInsentient", new String[] { "a" }),

			/* 255 */ ENTITY_SLIME_SIZE("EntitySlime", new String[] { "bt" }),

			/* 258 */ ENTITY_WITHER_a("EntityWither", new String[] { "a" }),

			/* 261 */ ENTITY_WIHER_b("EntityWither", new String[] { "b" }),

			/* 264 */ ENTITY_WITHER_c("EntityWither", new String[] { "c" }),

			/* 267 */ ENTITY_WITHER_bv("EntityWither", new String[] { "bv" }),

			/* 270 */ ENTITY_WITHER_bw("EntityWither", new String[] { "bw" }),

			/* 275 */ ENTITY_HUMAN_ABSORPTION_HEARTS("EntityHuman", new String[] { "a" }),

			/* 280 */ ENTITY_HUMAN_SCORE("EntityHuman", new String[] { "b" }),

			/* 285 */ ENTITY_HUMAN_SKIN_LAYERS("EntityHuman", new String[] { "bp" }),

			/* 290 */ ENTITY_HUMAN_MAIN_HAND("EntityHuman", new String[] { "bq" });

			private Object type;

			private ValueType(String className, String... fieldNames) {
				try {
					/* 296 */ this.type = new FieldResolver(nmsClassResolver.resolve(new String[] { className })).resolve(fieldNames).get(null);
				} catch (Exception e) {
				}
			}

			public boolean hasType() {
				/* 302 */ return getType() != null;
			}

			public Object getType() {
				/* 306 */ return this.type;
			}
		}
	}

	public static class V1_8 {
		/* 316 */ static Class<?> WatchableObject = nmsClassResolver.resolveSilent(new String[] { "WatchableObject", "DataWatcher$WatchableObject" });
		static ConstructorResolver WatchableObjectConstructorResolver;
		static FieldResolver WatchableObjectFieldResolver;

		public static Object newWatchableObject(int index, Object value) throws ReflectiveOperationException {
			/* 323 */ return newWatchableObject(getValueType(value), index, value);
		}

		public static Object newWatchableObject(int type, int index, Object value) throws ReflectiveOperationException {
			/* 327 */ if (WatchableObjectConstructorResolver == null) {
				WatchableObjectConstructorResolver = new ConstructorResolver(WatchableObject);
			}

			/* 331 */ return WatchableObjectConstructorResolver.resolve(new Class[][] { { Integer.TYPE, Integer.TYPE, Object.class } }).newInstance(new Object[] { Integer.valueOf(type), Integer.valueOf(index), value });
		}

		public static Object setValue(Object dataWatcher, int index, Object value) throws ReflectiveOperationException {
			int type = de.avalon.utils.reflection.minecraft.DataWatcher.getValueType(value);

			Object map = DataWatcherFieldResolver.resolve(new String[] { "dataValues" }).get(dataWatcher);
			TIntObjectMapMethodResolver.resolve(new ResolverQuery[] { new ResolverQuery("put", new Class[] { Integer.TYPE, Object.class }) }).invoke(map, new Object[] { Integer.valueOf(index), newWatchableObject(type, index, value) });

			return dataWatcher;
		}

		public static Object getValue(Object dataWatcher, int index) throws ReflectiveOperationException {
			Object map = DataWatcherFieldResolver.resolve(new String[] { "dataValues" }).get(dataWatcher);

			return TIntObjectMapMethodResolver.resolve(new ResolverQuery[] { new ResolverQuery("get", new Class[] { Integer.TYPE }) }).invoke(map, new Object[] { Integer.valueOf(index) });
		}

		public static int getWatchableObjectIndex(Object object) throws ReflectiveOperationException {
			if (WatchableObjectFieldResolver == null)
				WatchableObjectFieldResolver = new FieldResolver(WatchableObject);
			return WatchableObjectFieldResolver.resolve(new String[] { "b" }).getInt(object);
		}

		public static int getWatchableObjectType(Object object) throws ReflectiveOperationException {
			/* 355 */ if (WatchableObjectFieldResolver == null)
				WatchableObjectFieldResolver = new FieldResolver(WatchableObject);
			/* 356 */ return WatchableObjectFieldResolver.resolve(new String[] { "a" }).getInt(object);
		}

		public static Object getWatchableObjectValue(Object object) throws ReflectiveOperationException {
			/* 360 */ if (WatchableObjectFieldResolver == null)
				WatchableObjectFieldResolver = new FieldResolver(WatchableObject);
			/* 361 */ return WatchableObjectFieldResolver.resolve(new String[] { "c" }).get(object);
		}
	}
}

/*
 * Location: C:\Users\Paul\Programmieren\Java\Jars\BossBarAPI_v2.3.7.jar!\org\
 * inventivetalent\reflection\minecraft\DataWatcher.class Java compiler version:
 * 7 (51.0) JD-Core Version: 0.7.1
 */