package cn.sxw.android.base.utils.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

public class ReflectHelper {

	static HashMap<CacheKey, HashMap<String, Field>> sfiledCacheHashMap = new HashMap<CacheKey, HashMap<String, Field>>();

	static class CacheKey {
		Class<?> cls;

		public CacheKey(Class<?> cls) {
			this.cls = cls;

		}

		@Override
		public int hashCode() {
			return (cls.getClass().getName()).hashCode();
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof CacheKey) {
				CacheKey that = (CacheKey) o;
				if (this == o) {
					return true;
				}
				if (that.cls == null || this.cls == null) {
					return false;
				}
				if (this.cls.getName().equals(that.cls.getName())) {
					return true;
				}
			}
			return false;
		}
	}

	public static HashMap<String, Field> getFields(Object object) {
		Class<?> cls = object.getClass();
		CacheKey cacheKey = new CacheKey(cls);
		if (sfiledCacheHashMap.containsKey(cacheKey)) {
			return sfiledCacheHashMap.get(cacheKey);
		}

		HashMap<String, Field> retFields = new HashMap<String, Field>();
		while (cls != Object.class) {
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				try {
					if ((field.getModifiers() & Modifier.STATIC) != Modifier.STATIC) {
						field.setAccessible(true);
						retFields.put(field.getName(), field);
					}
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				}
			}
	        cls = cls.getSuperclass();
        }

		sfiledCacheHashMap.put(cacheKey, retFields);
		return retFields;
	}
	
    public static <T> T newInstance(Class<T> cls) {
        try {
            return cls.newInstance();
        }  catch (InstantiationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    public static Object invoke(Object instance, String methodName) {
        try {
            Method method = instance.getClass().getMethod(methodName);
            return method.invoke(instance);
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
